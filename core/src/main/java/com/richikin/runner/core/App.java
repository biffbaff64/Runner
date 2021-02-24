package com.richikin.runner.core;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.richikin.enumslib.StateID;
import com.richikin.runner.config.Settings;
import com.richikin.runner.entities.Entities;
import com.richikin.runner.entities.EntityData;
import com.richikin.runner.entities.EntityManager;
import com.richikin.runner.entities.EntityUtils;
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
import com.richikin.utilslib.maths.SimpleVec2;

public final class App extends LibApp
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

    private App()
    {
    }

    public static void initialiseObjects()
    {
        appState = new StateManager(StateID._STATE_POWER_UP);

        settings     = new Settings();
        assets       = new AssetLoader();
        spriteBatch  = new SpriteBatch();
        cameraUtils  = new CameraUtils();
        worldModel   = new WorldModel();
        baseRenderer = new BaseRenderer();

        //
        // This needs setting here as InputManager needs access to it.
        stage = new Stage(baseRenderer.hudGameCamera.viewport, spriteBatch);

        inputManager   = new InputManager();
        panelManager   = new PanelManager();
        highScoreUtils = new HighScoreUtils();

        //
        // TODO: 19/08/2020
        // These objects should really be initialised when moving
        // from MainMenuScreen to MainGameScreen.
        mapCreator     = new MapCreator();
        entityData     = new EntityData();
        entities       = new Entities();
        mapData        = new MapData();
        mapUtils       = new MapUtils();
        gameProgress   = new GameProgress();
        mainMenuScreen = new MainMenuScreen();
        mainGameScreen = new MainGameScreen();
    }

    public static MainPlayer getPlayer()
    {
        return entities.mainPlayer;
    }

    private static final SimpleVec2 position = new SimpleVec2();

    public static SimpleVec2 getPlayerPos()
    {
//        position.set(entities.mainPlayer.sprite.getX(), entities.mainPlayer.sprite.getY());

        return position;
    }

    public static int getLevel()
    {
        return gameProgress.playerLevel;
    }

    public static HeadsUpDisplay getHud()
    {
        return hud;
    }
}
