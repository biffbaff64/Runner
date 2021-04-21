package com.richikin.utilslib;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.richikin.utilslib.assets.IAssets;
import com.richikin.utilslib.logging.StateManager;

public class LibApp
{
    private static SpriteBatch      spriteBatch;
    private static Stage            stage;
    private static StateManager     appState;
    private static IAssets          assets;

    public static SpriteBatch getSpriteBatch()
    {
        return spriteBatch;
    }

    public static void setSpriteBatch(SpriteBatch _spriteBatch)
    {
        spriteBatch = _spriteBatch;
    }

    public static Stage getStage()
    {
        return stage;
    }

    public static void setStage(Stage _stage)
    {
        stage = _stage;
    }

    public static StateManager getAppState()
    {
        return appState;
    }

    public static void setAppState(StateManager _appState)
    {
        appState = _appState;
    }

    public static IAssets getAssets()
    {
        return assets;
    }

    public static void setAssets(IAssets _assets)
    {
        assets = _assets;
    }
}
