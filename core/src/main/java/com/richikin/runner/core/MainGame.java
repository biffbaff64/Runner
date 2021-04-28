package com.richikin.runner.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.utils.ScreenUtils;
import com.richikin.enumslib.StateID;
import com.richikin.runner.config.AppConfig;
import com.richikin.runner.developer.Developer;
import com.richikin.runner.scenes.SplashScreen;
import com.richikin.utilslib.LibApp;
import com.richikin.utilslib.assets.AssetLoader;

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

        LibApp.setAssets(new AssetLoader());

        SplashScreen.inst().setup();

        startup = new Startup();
    }

    @Override
    public void render()
    {
        if (!startup.startupDone)
        {
            if ((App.getAssets().getAssetManager() != null)
                && (App.getAssets().getAssetManager().update())
                && (!SplashScreen.inst().isAvailable))
            {
                startup.startApp();
                startup.closeStartup();
            }
            else
            {
                SplashScreen.inst().update();
                SplashScreen.inst().render();
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