package com.richikin.runner.scenes;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.richikin.enumslib.ScreenID;
import com.richikin.enumslib.StateID;
import com.richikin.runner.config.AppConfig;
import com.richikin.runner.config.Settings;
import com.richikin.runner.core.App;
import com.richikin.runner.core.EndgameManager;
import com.richikin.runner.core.GameControlLoop;
import com.richikin.runner.core.LevelManager;
import com.richikin.runner.developer.Developer;
import com.richikin.runner.graphics.Gfx;
import com.richikin.runner.graphics.camera.OrthoGameCamera;
import com.richikin.runner.graphics.camera.Shake;
import com.richikin.runner.ui.GameCompletedPanel;
import com.richikin.utilslib.input.controllers.ControllerType;
import com.richikin.utilslib.logging.StopWatch;
import com.richikin.utilslib.logging.Trace;

public class MainGameScreen extends AbstractBaseScreen
{
    public GameCompletedPanel completedPanel;
    public StopWatch          retryDelay;
    public EndgameManager     endGameManager;
    public GameControlLoop    gameControlLoop;

    /*
     * boolean firstTime - TRUE if MainGameSCreen has
     * just been entered, i.e. a NEW Game.
     *
     * Setting this to true allows initialise() to
     * be called from show(), one time only. If false, then
     * initialise() will be bypassed but the rest of show()
     * will be processed.
     */
    public boolean firstTime;
    public Texture background;

    /**
     * ------------------------------------------------------------------------------
     *
     * ------------------------------------------------------------------------------
     */
    public MainGameScreen()
    {
        super();

        this.firstTime = true;
    }

    /**
     * ------------------------------------------------------------------------------
     *
     * ------------------------------------------------------------------------------
     */
    @Override
    public void initialise()
    {
        if (firstTime)
        {
            Trace.divider();
            Trace.__FILE_FUNC("NEW GAME:");
            Trace.__FILE_FUNC("_DEVMODE: " + Developer.isDevMode());
            Trace.__FILE_FUNC("_GODMODE: " + Developer.isGodMode());
            Trace.__FILE_FUNC("prefs : " + App.settings.getPrefs());
            Trace.divider();

            endGameManager   = new EndgameManager();
            gameControlLoop  = new GameControlLoop();

            App.levelManager = new LevelManager();
            App.levelManager.prepareNewGame();

            App.appState.set(StateID._STATE_SETUP);
        }

        if (AppConfig.availableInputs.contains(ControllerType._VIRTUAL, true))
        {
            App.inputManager.virtualJoystick.show();
        }

        Shake.setAllowed(App.settings.isEnabled(Settings._VIBRATIONS));
    }

    /**
     * ------------------------------------------------------------------------------
     *
     * ------------------------------------------------------------------------------
     */
    @Override
    public void update()
    {
        switch (App.appState.peek())
        {
            case _STATE_MAIN_MENU,
                _STATE_CLOSING -> {
            }

            case _STATE_SETUP,
                 _STATE_GET_READY,
                 _STATE_DEVELOPER_PANEL,
                 _STATE_PAUSED,
                 _STATE_GAME,
                 _STATE_MESSAGE_PANEL,
                 _STATE_LEVEL_RETRY,
                 _STATE_LEVEL_FINISHED,
                 _STATE_GAME_OVER,
                 _STATE_GAME_FINISHED,
                 _STATE_ANNOUNCE_MISSILE,
                 _STATE_TELEPORTING,
                 _STATE_END_GAME -> {

                gameControlLoop.update();
            }

            default -> {

                Trace.__FILE_FUNC();
                Trace.dbg("Unsupported game state: " + App.appState.peek());
            }
        }
    }

    /**
     * ------------------------------------------------------------------------------
     * Update and Render the game, and step the physics world.
     * Called from {@link com.badlogic.gdx.Game}
     *
     * @param delta Time since the last update.
     * ------------------------------------------------------------------------------
     */
    @Override
    public void render(float delta)
    {
        super.update();

        if (AppConfig.gameScreenActive())
        {
            update();

            super.render(delta);

            App.worldModel.worldStep();
        }
    }

    /**
     * ------------------------------------------------------------------------------
     *
     * ------------------------------------------------------------------------------
     */
    public void draw(final SpriteBatch spriteBatch, OrthoGameCamera camera)
    {
        if (background != null)
        {
            spriteBatch.draw
                (
                    background,
                    camera.getPosition().x - Gfx._VIEW_HALF_WIDTH,
                    camera.getPosition().y - Gfx._VIEW_HALF_HEIGHT
                );
        }
    }

    /**
     * ------------------------------------------------------------------------------
     *
     * ------------------------------------------------------------------------------
     */
    public Texture getBackground()
    {
        return background;
    }

    /**
     * ------------------------------------------------------------------------------
     *
     * ------------------------------------------------------------------------------
     */
    public void reset()
    {
        firstTime = true;

        App.gameProgress.playerGameOver = false;
        AppConfig.gamePaused            = false;
    }

    /**
     * ------------------------------------------------------------------------------
     *
     * ------------------------------------------------------------------------------
     */
    @Override
    public void show()
    {
        super.show();

        AppConfig.currentScreenID = ScreenID._GAME_SCREEN;
        App.cameraUtils.disableAllCameras();

        initialise();

        App.appState.set(StateID._STATE_SETUP);
    }

    /**
     * ------------------------------------------------------------------------------
     *
     * ------------------------------------------------------------------------------
     */
    @Override
    public void loadImages()
    {
        background = App.assets.loadSingleAsset("water_background.png", Texture.class);
    }

    /**
     * ------------------------------------------------------------------------------
     *
     * ------------------------------------------------------------------------------
     */
    @Override
    public void dispose()
    {
        Trace.__FILE_FUNC();

        App.entityManager.dispose();
        App.getHud().dispose();
        App.gameProgress.dispose();

        App.baseRenderer.gameZoom.setZoomValue(0.0f);
        App.baseRenderer.hudZoom.setZoomValue(0.0f);

        App.assets.unloadAsset("water_background.png");

        App.hud          = null;
        App.levelManager = null;
        endGameManager   = null;
        retryDelay       = null;
        gameControlLoop  = null;

        Trace.finishedMessage();
    }
}
