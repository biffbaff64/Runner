package com.richikin.runner.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.utils.ScreenUtils;
import com.richikin.enumslib.StateID;
import com.richikin.runner.config.AppConfig;
import com.richikin.runner.developer.Developer;
import com.richikin.runner.screens.SplashScreen;

public class MainGame extends com.badlogic.gdx.Game
{
    public SplashScreen splashScreen;

    public MainGame()
    {
    }

    @Override
    public void create()
    {
        App.mainGame = this;

        splashScreen = new SplashScreen();

        //
        // Initialise all essential objects required before
        // the main screen is initialised.

        // TODO: 21/02/2021 - This is crap, change it
        Startup startup = new Startup();
        startup.startApp();
        startup.closeStartup();
    }

    @Override
    public void render()
    {
        if (splashScreen.isAvailable)
        {
            splashScreen.update();
            splashScreen.render();
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
            && (App.appState != null)
            && (App.appState.equalTo(StateID._STATE_GAME)))
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
            && (App.appState != null)
            && (App.appState.equalTo(StateID._STATE_GAME)))
        {
            AppConfig.unPause();
        }
    }
}