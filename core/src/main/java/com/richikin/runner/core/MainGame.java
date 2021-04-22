package com.richikin.runner.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.utils.ScreenUtils;
import com.richikin.enumslib.StateID;
import com.richikin.runner.config.AppConfig;
import com.richikin.runner.developer.Developer;
import com.richikin.runner.scenes.SplashScreen;

public class MainGame extends com.badlogic.gdx.Game
{
    private Startup startup;

    public MainGame()
    {
    }

    @Override
    public void create()
    {
        App.mainGame = this;

        SplashScreen.inst().setup();

        //
        // Initialise all essential objects required before
        // the main screen is initialised.
        startup = new Startup();
    }

    @Override
    public void render()
    {
        if (SplashScreen.inst().isAvailable)
        {
            if (!startup.startupDone)
            {
                startup.startApp();
            }

            SplashScreen.inst().update();
            SplashScreen.inst().render();

            if (!SplashScreen.inst().isAvailable)
            {
                startup.closeStartup();
            }
        }
        else
        {
            ScreenUtils.clear(1, 1, 1, 1);

            super.render();

            AppConfig.configListener.update();
        }
    }

    @Override
    public void setScreen(Screen screen)
    {
        Gdx.app.debug("MG: ", ("" + screen.getClass()));

        super.setScreen(screen);
    }

    /**
     * Pause the app
     */
    @Override
    public void pause()
    {
        super.pause();

        if (!Developer.isDevMode()
            && (App.getAppState() != null)
            && (App.getAppState().equalTo(StateID._STATE_GAME)))
        {
            AppConfig.pause();
        }
    }

    /**
     * Actions to perform on leaving Pause
     */
    @Override
    public void resume()
    {
        super.resume();

        if (!Developer.isDevMode()
            && (App.getAppState() != null)
            && (App.getAppState().equalTo(StateID._STATE_GAME)))
        {
            AppConfig.unPause();
        }
    }
}