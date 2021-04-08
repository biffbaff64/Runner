package com.richikin.runner.graphics.renderers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.ScreenUtils;
import com.richikin.enumslib.StateID;
import com.richikin.runner.config.AppConfig;
import com.richikin.runner.core.App;
import com.richikin.runner.graphics.Gfx;
import com.richikin.runner.graphics.camera.OrthoGameCamera;
import com.richikin.runner.graphics.camera.ViewportType;
import com.richikin.runner.graphics.camera.Zoom;
import com.richikin.utilslib.logging.Trace;
import com.richikin.utilslib.maths.SimpleVec3F;

public class BaseRenderer implements Disposable
{
    public OrthoGameCamera hudGameCamera;
    public OrthoGameCamera spriteGameCamera;
    public OrthoGameCamera tiledGameCamera;

    public Zoom    gameZoom;
    public Zoom    hudZoom;
    public boolean isDrawingStage;

    private WorldRenderer worldRenderer;
    private HUDRenderer   hudRenderer;
    private SimpleVec3F   cameraPos;

    public BaseRenderer()
    {
        createCameras();
    }

    /**
     * Create all game cameras and
     * associated viewports.
     */
    private void createCameras()
    {
        Trace.__FILE_FUNC();

        AppConfig.camerasReady = false;

        // --------------------------------------
        // Camera for displaying TiledMap game maps.
        tiledGameCamera = new OrthoGameCamera
            (
                Gfx._GAME_SCENE_WIDTH, Gfx._GAME_SCENE_HEIGHT,
                ViewportType._STRETCH,
                "Tiled Cam"
            );

        // --------------------------------------
        // Camera for displaying game sprites
        spriteGameCamera = new OrthoGameCamera
            (
                Gfx._GAME_SCENE_WIDTH, Gfx._GAME_SCENE_HEIGHT,
                ViewportType._STRETCH,
                "Sprite Cam"
            );

        // --------------------------------------
        // Camera for displaying the HUD
        hudGameCamera = new OrthoGameCamera
            (
                Gfx._HUD_SCENE_WIDTH, Gfx._HUD_SCENE_HEIGHT,
                ViewportType._STRETCH,
                "Hud Cam"
            );

        gameZoom      = new Zoom();
        hudZoom       = new Zoom();
        worldRenderer = new WorldRenderer();
        hudRenderer   = new HUDRenderer();
        cameraPos     = new SimpleVec3F();

        isDrawingStage         = true;
        AppConfig.camerasReady = true;
    }

    /**
     * Process all cameras.
     */
    public void render()
    {
        ScreenUtils.clear(Color.BLACK, false);

        //
        // Set the positioning reference point for the cameras. Cameras
        // will centre on the main character.
        if (AppConfig.gameScreenActive())
        {
            if ((App.getPlayer() != null) && App.appState.after(StateID._STATE_SETUP))
            {
                App.mapUtils.positionAt
                    (
                        (int) (App.getPlayer().sprite.getX()),
                        (int) (App.getPlayer().sprite.getY())
                    );
            }
        }
        else
        {
            App.mapData.mapPosition.set(0, 0);
        }

        App.spriteBatch.enableBlending();

        drawTiledMap();
        drawSprites();
        drawHUD();

        // ----- Draw the Stage, if enabled -----
        if (isDrawingStage && (App.stage != null))
        {
            App.stage.act(Math.min(Gdx.graphics.getDeltaTime(), Gfx._STEP_TIME));
            App.stage.draw();
        }

        gameZoom.stop();
        hudZoom.stop();

        App.worldModel.drawDebugMatrix();
    }

    private void drawTiledMap()
    {
        if (tiledGameCamera.isInUse)
        {
            tiledGameCamera.viewport.apply();
            App.spriteBatch.setProjectionMatrix(tiledGameCamera.camera.combined);
            App.spriteBatch.begin();

            cameraPos.x = (App.mapData.mapPosition.getX() + (tiledGameCamera.camera.viewportWidth / 2));
            cameraPos.y = (App.mapData.mapPosition.getY() + (tiledGameCamera.camera.viewportHeight / 2));
            cameraPos.z = 0;

            if (tiledGameCamera.isLerpingEnabled)
            {
                tiledGameCamera.lerpTo(cameraPos, Gfx._LERP_SPEED, gameZoom.getZoomValue(), true);
            }
            else
            {
                tiledGameCamera.setPosition(cameraPos, gameZoom.getZoomValue(), true);
            }

            App.mapData.render(tiledGameCamera.camera);
            App.spriteBatch.end();
        }
    }

    private void drawSprites()
    {
        if (spriteGameCamera.isInUse)
        {
            spriteGameCamera.viewport.apply();
            App.spriteBatch.setProjectionMatrix(spriteGameCamera.camera.combined);
            App.spriteBatch.begin();

            cameraPos.x = (App.mapData.mapPosition.getX() + (spriteGameCamera.camera.viewportWidth / 2));
            cameraPos.y = (App.mapData.mapPosition.getY() + (spriteGameCamera.camera.viewportHeight / 2));
            cameraPos.z = 0;

            if (spriteGameCamera.isLerpingEnabled)
            {
                spriteGameCamera.lerpTo(cameraPos, Gfx._LERP_SPEED, gameZoom.getZoomValue(), true);
            }
            else
            {
                spriteGameCamera.setPosition(cameraPos, gameZoom.getZoomValue(), false);
            }

            worldRenderer.render(App.spriteBatch, spriteGameCamera);

            App.spriteBatch.end();
        }
    }

    /**
     * Draw the HUD and any related objects, if enabled.
     * The Front End should only be using this camera.
     */
    private void drawHUD()
    {
        if (hudGameCamera.isInUse)
        {
            hudGameCamera.viewport.apply();
            App.spriteBatch.setProjectionMatrix(hudGameCamera.camera.combined);
            App.spriteBatch.begin();

            cameraPos.x = (hudGameCamera.camera.viewportWidth / 2);
            cameraPos.y = (hudGameCamera.camera.viewportHeight / 2);
            cameraPos.z = 0;

            hudGameCamera.setPosition(cameraPos, hudZoom.getZoomValue(), false);

            hudRenderer.render(App.spriteBatch, hudGameCamera);

            App.spriteBatch.end();
        }
    }

    public void resizeCameras(int _width, int _height)
    {
        tiledGameCamera.resizeViewport(_width, _height, true);
        spriteGameCamera.resizeViewport(_width, _height, true);
        hudGameCamera.resizeViewport(_width, _height, true);
    }

    @Override
    public void dispose()
    {
        tiledGameCamera.dispose();
        spriteGameCamera.dispose();
        hudGameCamera.dispose();

        gameZoom = null;
        hudZoom  = null;

        worldRenderer = null;
        hudRenderer   = null;
    }
}
