package com.richikin.runner.core;

import com.richikin.enumslib.ActionStates;
import com.richikin.enumslib.StateID;
import com.richikin.runner.config.AppConfig;
import com.richikin.runner.ui.GameCompletedPanel;
import com.richikin.utilslib.logging.StopWatch;
import com.richikin.utilslib.logging.Trace;

public class EndgameManager
{
    public EndgameManager()
    {
    }

    public boolean update()
    {
        boolean returnFlag = false;

        if ((App.getPlayer() != null)
            && (App.getPlayer().getActionState() == ActionStates._DEAD))
        {
            App.getHud().hideControls();

            //
            // All lives lost...
            //
            // Setting appState to Level Retry, but setting quitToMainMenu to true
            // will redirect flow to Game Over state after a short delay followed
            // by a 'Game Over' message.
            App.getAppState().set(StateID._STATE_LEVEL_RETRY);
            App.mainGameScreen.retryDelay = StopWatch.start();

            AppConfig.quitToMainMenu = true;

            returnFlag = true;
        }
        else
        {
            if (App.gameProgress.gameCompleted)
            {
                Trace.__FILE_FUNC_WithDivider("GAME COMPLETED");
                Trace.divider();

                App.getHud().hideControls();

                App.mainGameScreen.completedPanel = new GameCompletedPanel();
                App.mainGameScreen.completedPanel.setup();

                App.getHud().setStateID(StateID._STATE_GAME_FINISHED);
                App.getAppState().set(StateID._STATE_GAME_FINISHED);

                returnFlag = true;
            }
            else if (App.gameProgress.levelCompleted)
            {
                //
                // For this game, 'levelCompleted' is actually 'roomExited'...
                Trace.__FILE_FUNC_WithDivider("LEVEL COMPLETED");
                Trace.divider();

                App.getHud().setStateID(StateID._STATE_LEVEL_FINISHED);
                App.getAppState().set(StateID._STATE_LEVEL_FINISHED);

                returnFlag = true;
            }
            //
            // Restarting due to life lost and
            // player is resetting...
            else if (App.gameProgress.isRestarting)
            {
                if (App.getPlayer().getActionState() == ActionStates._RESETTING)
                {
                    Trace.__FILE_FUNC_WithDivider();
                    Trace.__FILE_FUNC("LIFE LOST - TRY AGAIN");
                    Trace.divider();

                    App.mainGameScreen.retryDelay = StopWatch.start();
                    App.getAppState().set(StateID._STATE_LEVEL_RETRY);
                }

                returnFlag = true;
            }
            //
            // Forcing quit to main menu screen.
            // For example, from pause menu...
            else if (AppConfig.forceQuitToMenu)
            {
                App.getAppState().set(StateID._STATE_END_GAME);
                returnFlag = true;
            }
        }

        return returnFlag;
    }
}
