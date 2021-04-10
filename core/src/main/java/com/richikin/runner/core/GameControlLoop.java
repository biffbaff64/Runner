package com.richikin.runner.core;

import com.richikin.enumslib.StateID;
import com.richikin.runner.assets.GameAssets;
import com.richikin.runner.audio.GameAudio;
import com.richikin.runner.config.AppConfig;
import com.richikin.runner.developer.Developer;
import com.richikin.runner.scenes.MainGameScreen;
import com.richikin.utilslib.logging.Trace;

import java.util.concurrent.TimeUnit;

public class GameControlLoop extends AbstractControlLoop
{
    public GameControlLoop()
    {
        super();
    }

    public void update()
    {
        switch (App.appState.peek())
        {
            //
            // Initialise the current level.
            // If the level is restarting, that will
            // also be handled here.
            case _STATE_SETUP -> {

                stateSetup();
            }

            //
            // Display and update the 'Get Ready' message.
            case _STATE_GET_READY -> {

                stateGetReady();
            }

            case _STATE_DEVELOPER_PANEL, _STATE_PAUSED, _STATE_GAME -> {

                stateGame();
            }

            //
            // Player lost a life.
            // Trying again.
            case _STATE_LEVEL_RETRY -> {

                stateSetForRetry();
            }

            case _STATE_LEVEL_FINISHED -> {

                stateSetForLevelFinished();
            }

            //
            // 'GAME OVER' Message, Player has lost all lives.
            case _STATE_GAME_OVER -> {

                stateSetForGameOverMessage();
            }

            case _STATE_GAME_FINISHED -> {

                stateSetForGameFinished();
            }

            //
            // Back to MainMenuScreen
            case _STATE_END_GAME -> {

                stateSetForEndGame();
            }

            case _STATE_CLOSING -> {
            }

            default -> {

                Trace.__FILE_FUNC("Unsupported gameState: " + App.appState.peek());
            }
        }
    }

    /**
     * Initialise the current level.
     * If the level is restarting, that will
     * also be handled here
     */
    private void stateSetup()
    {
        Trace.megaDivider("_STATE_SETUP");

        App.levelManager.prepareCurrentLevel(scr().firstTime);

        // All cameras ON
        App.cameraUtils.enableAllCameras();
        App.baseRenderer.parallaxCamera.isInUse = false;
        App.baseRenderer.tiledGameCamera.isLerpingEnabled    = false;
        App.baseRenderer.spriteGameCamera.isLerpingEnabled   = false;

        if (scr().firstTime)
        {
            GameAudio.inst().playGameTune(true);

            App.getHud().messageManager.addZoomMessage(GameAssets._GETREADY_MSG_ASSET, 1500);
        }

        App.appState.set(StateID._STATE_GET_READY);
        App.gameProgress.gameSetupDone = true;
    }

    /**
     * Display and update the 'Get Ready' message
     */
    private void stateGetReady()
    {
        App.getHud().update();

        //
        // If there is no 'Get Ready' message on screen then setup
        // flow control to play the game.
        if (!App.panelManager.doesPanelExist(GameAssets._GETREADY_MSG_ASSET))
        {
            Trace.__FILE_FUNC("----- START GAME (GET READY) -----");

            App.appState.set(StateID._STATE_GAME);
            App.getHud().setStateID(StateID._STATE_PANEL_UPDATE);

            App.getHud().showControls();
            App.getHud().showPauseButton(true);

            scr().firstTime = false;

            //
            // Re-setup the player after a death/restart
            if (App.getPlayer() != null)
            {
                App.getPlayer().setup(false);
            }
        }
    }

    /**
     * Update the game for states:-
     * _STATE_DEVELOPER_PANEL
     * _STATE_SETTINGS_PANEL
     * _STATE_PAUSED
     * _STATE_GAME
     */
    private void stateGame()
    {
        App.getHud().update();

        if (App.appState.peek() == StateID._STATE_DEVELOPER_PANEL)
        {
            if (!Developer.developerPanelActive)
            {
                App.appState.set(StateID._STATE_GAME);
                App.getHud().setStateID(StateID._STATE_PANEL_UPDATE);
            }
        }
        else
        {
            App.entityManager.updateSprites();
            App.entityManager.tidySprites();

            //
            // Check for game ending
            if (!scr().endGameManager.update())
            {
                //
                // Tasks to perform if the game has not ended
                if (App.appState.peek() == StateID._STATE_PAUSED)
                {
                    if (!AppConfig.gamePaused)
                    {
                        App.appState.set(StateID._STATE_GAME);
                    }
                }
            }
        }
    }

    /**
     * Handles the preparation for retrying the current
     * level, after Player loses a life.
     */
    private void stateSetForRetry()
    {
        App.getHud().update();

        if (AppConfig.quitToMainMenu || (scr().retryDelay.time(TimeUnit.MILLISECONDS) > 2000))
        {
            if (AppConfig.quitToMainMenu)
            {
                App.getHud().hideControls();
                App.getHud().messageManager.addZoomMessage(GameAssets._GAMEOVER_MSG_ASSET, 3000);

                App.appState.set(StateID._STATE_GAME_OVER);
            }
            else
            {
                App.appState.set(StateID._STATE_SETUP);
            }

            scr().retryDelay = null;
        }
    }

    /**
     * Handles finishing the current level and
     * moving on to the next one.
     * <p>
     * NOTE: "Level finished" for this game is actually "room exit".
     */
    private void stateSetForLevelFinished()
    {
        App.levelManager.closeCurrentLevel();

        App.getHud().update();

        scr().reset();
        App.appState.set(StateID._STATE_SETUP);
    }

    /**
     * Game Over, due to losing all lives.
     * (Waits for the 'Game Over' message to disappear.)
     */
    private void stateSetForGameOverMessage()
    {
        App.getHud().update();

        if (!App.getHud().messageManager.doesPanelExist(GameAssets._GAMEOVER_MSG_ASSET))
        {
            App.appState.set(StateID._STATE_END_GAME);
        }
    }

    /**
     * Game Over, due to all levels being completed.
     */
    private void stateSetForGameFinished()
    {
        App.getHud().update();

        if (scr().completedPanel.update())
        {
            App.appState.set(StateID._STATE_END_GAME);
        }
    }

    /**
     * Game Ended, hand control back to MainMenuScreen.
     */
    private void stateSetForEndGame()
    {
        Trace.megaDivider("***** GAME OVER *****");

        GameAudio.inst().playGameTune(false);

        App.gameProgress.closeLastGame();

        scr().dispose();

        App.mainGame.setScreen(App.mainMenuScreen);
    }

    private MainGameScreen scr()
    {
        return App.mainGameScreen;
    }
}
