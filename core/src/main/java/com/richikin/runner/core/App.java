package com.richikin.runner.core;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.richikin.enumslib.StateID;
import com.richikin.runner.config.Settings;
import com.richikin.runner.entities.*;
import com.richikin.runner.entities.hero.MainPlayer;
import com.richikin.runner.entities.paths.PathUtils;
import com.richikin.runner.graphics.CameraUtils;
import com.richikin.runner.graphics.parallax.ParallaxManager;
import com.richikin.runner.graphics.renderers.BaseRenderer;
import com.richikin.runner.input.InputManager;
import com.richikin.runner.maps.MapCreator;
import com.richikin.runner.maps.MapData;
import com.richikin.runner.maps.MapUtils;
import com.richikin.runner.maps.RoomManager;
import com.richikin.runner.physics.CollisionUtils;
import com.richikin.runner.scenes.MainGameScreen;
import com.richikin.runner.scenes.MainMenuScreen;
import com.richikin.runner.ui.HeadsUpDisplay;
import com.richikin.runner.ui.panels.PanelManager;
import com.richikin.utilslib.LibApp;
import com.richikin.utilslib.assets.AssetLoader;
import com.richikin.utilslib.core.ISettings;
import com.richikin.utilslib.logging.StateManager;

public abstract class App extends LibApp
{
    // =======================================================
    // Global access references
    //
    public static MainGame        mainGame;
    public static BaseRenderer    baseRenderer;
    public static CameraUtils     cameraUtils;
    public static WorldModel      worldModel;
    public static InputManager    inputManager;
    public static MainMenuScreen  mainMenuScreen;
    public static MainGameScreen  mainGameScreen;
    public static ISettings       settings;
    public static HighScoreUtils  highScoreUtils;
    public static ParallaxManager parallaxManager;
    public static PanelManager    panelManager;
    public static LevelManager    levelManager;
    public static RoomManager     roomManager;

    //
    // Globals to be made available when MainGameScreen is active.
    // These must be released when MainGameScreen is destroyed.
    public static CollisionUtils collisionUtils;
    public static EntityUtils    entityUtils;
    public static Entities       entities;
    public static MapUtils       mapUtils;
    public static PathUtils      pathUtils;
    public static EntityData     entityData;
    public static MapData        mapData;
    public static HeadsUpDisplay hud;
    public static GameProgress   gameProgress;
    public static MapCreator     mapCreator;
    public static EntityManager  entityManager;

    public static void initialiseObjects()
    {
        LibApp.setAppState(new StateManager(StateID._STATE_POWER_UP));
        LibApp.setSpriteBatch(new SpriteBatch());

        settings       = new Settings();
        cameraUtils    = new CameraUtils();
        worldModel     = new WorldModel();
        baseRenderer   = new BaseRenderer();
        mainMenuScreen = new MainMenuScreen();
        mainGameScreen = new MainGameScreen();

        //
        // This needs setting here as InputManager needs access to it, and cannot be done
        // until baseRenderer has been initialised.
        LibApp.setStage(new Stage(baseRenderer.hudGameCamera.viewport, getSpriteBatch()));

        // TODO: 23/04/2021 - Look into seeing which of the following can be initialised
        // TODO: 23/04/2021 - on entering MainGameScreen instead of here.
        inputManager   = new InputManager();
        panelManager   = new PanelManager();
        highScoreUtils = new HighScoreUtils();
        mapCreator     = new MapCreator();
        entityData     = new EntityData();
        entities       = new Entities();
        mapData        = new MapData();
        mapUtils       = new MapUtils();
        gameProgress   = new GameProgress();
        pathUtils      = new PathUtils();
    }

    /**
     * Convenience method which gets the global instance
     * of the {@link MainPlayer}.
     *
     * @return The player object.
     */
    public static MainPlayer getPlayer()
    {
        return entities.mainPlayer;
    }

    /**
     * Convenience method which returns the current game level.
     *
     * @return The game level.
     */
    public static int getLevel()
    {
        return gameProgress.playerLevel;
    }

    /**
     * Convenience method which returns the global instance
     * of the {@link HeadsUpDisplay}, aka The HUD.
     *
     * @return The HUD.
     */
    public static HeadsUpDisplay getHud()
    {
        return hud;
    }
}
