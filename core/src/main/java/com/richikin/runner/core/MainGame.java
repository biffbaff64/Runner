package com.richikin.runner.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.utils.ScreenUtils;
import com.richikin.enumslib.StateID;
import com.richikin.runner.config.AppConfig;
import com.richikin.runner.developer.Developer;

public class MainGame extends com.badlogic.gdx.Game
{
    @Override
    public void create()
    {
        App.mainGame = this;

        //
        // Initialise all essential objects required before
        // the main screen is initialised.
        //

        // TODO: 21/02/2021 - This is crap, change it
        Startup startup = new Startup();
        startup.startApp();
        startup.closeStartup();
    }

    @Override
    public void render()
    {
        ScreenUtils.clear(1, 1, 1, 1);

        super.render();

        AppConfig.configListener.update();

        App.baseRenderer.getSplashScreen().update();
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