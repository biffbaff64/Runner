package com.richikin.runner.graphics.renderers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.utils.Disposable;
import com.richikin.enumslib.StateID;
import com.richikin.runner.config.AppConfig;
import com.richikin.runner.core.App;
import com.richikin.runner.graphics.Gfx;
import com.richikin.runner.graphics.camera.OrthoGameCamera;
import com.richikin.runner.graphics.camera.ViewportType;
import com.richikin.runner.graphics.camera.Zoom;
import com.richikin.runner.graphics.parallax.ParallaxBackground;
import com.richikin.runner.graphics.parallax.ParallaxManager;
import com.richikin.utilslib.logging.Trace;
import com.richikin.utilslib.maths.SimpleVec3F;

public class BaseRenderer implements Disposable
{
    public OrthoGameCamera hudGameCamera;
    public OrthoGameCamera spriteGameCamera;
    public OrthoGameCamera tiledGameCamera;
    public OrthoGameCamera parallaxGameCamera;

    public ParallaxBackground parallaxBackground;
    public ParallaxBackground parallaxForeground;
    public Zoom               gameZoom;
    public Zoom               hudZoom;
    public boolean            isDrawingStage;

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
        // Camera for parallax scrolling backgrounds
        parallaxGameCamera = new OrthoGameCamera
            (
                Gfx._GAME_SCENE_WIDTH, Gfx._GAME_SCENE_HEIGHT,
                ViewportType._STRETCH,
                "Parallax Cam"
            );

        parallaxBackground  = new ParallaxBackground();
        parallaxForeground  = new ParallaxBackground();
        App.parallaxManager = new ParallaxManager();

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
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

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

        // ----- Draw the first set of Parallax Layers, if enabled -----
        if (parallaxGameCamera.isInUse)
        {
            parallaxGameCamera.viewport.apply();
            App.spriteBatch.setProjectionMatrix(parallaxGameCamera.camera.combined);
            App.spriteBatch.begin();

            cameraPos.x = parallaxGameCamera.camera.viewportWidth / 2;
            cameraPos.y = parallaxGameCamera.camera.viewportHeight / 2;
            cameraPos.z = 0;

            parallaxGameCamera.setPosition(cameraPos, gameZoom.getZoomValue(), false);
            parallaxBackground.render();

            App.entityManager.renderSystem.drawBackgroundSprites();
            App.spriteBatch.end();
        }

        // ----- Draw the TiledMap, if enabled -----
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

        // ----- Draw the game sprites, if enabled -----
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

        // ----- Draw the HUD and any related objects, if enabled -----
        // ----- The Front End should only be using this camera -------
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

    public void resizeCameras(int _width, int _height)
    {
        parallaxGameCamera.resizeViewport(_width, _height, true);
        tiledGameCamera.resizeViewport(_width, _height, true);
        spriteGameCamera.resizeViewport(_width, _height, true);
        hudGameCamera.resizeViewport(_width, _height, true);
    }

    @Override
    public void dispose()
    {
        parallaxGameCamera.dispose();
        tiledGameCamera.dispose();
        spriteGameCamera.dispose();
        hudGameCamera.dispose();

        parallaxBackground.dispose();
        parallaxForeground.dispose();

        parallaxBackground = null;
        parallaxForeground = null;

        gameZoom = null;
        hudZoom  = null;

        worldRenderer = null;
        hudRenderer   = null;
    }
}
