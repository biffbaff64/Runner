package com.richikin.utilslib;

import com.badlogic.gdx.Gdx;
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

    /**
     * Getter method which returns the global instance
     * of the {@link SpriteBatch}.
     * @return The SpriteBatch.
     */
    public static SpriteBatch getSpriteBatch()
    {
        return spriteBatch;
    }

    /**
     * Setter method which initialises the global instance
     * of the {@link SpriteBatch}.
     * @param _spriteBatch The SpriteBatch.
     */
    public static void setSpriteBatch(SpriteBatch _spriteBatch)
    {
        spriteBatch = _spriteBatch;
    }

    /**
     * Getter method which returns the global instance
     * of the {@link Stage}.
     * @return The Stage.
     */
    public static Stage getStage()
    {
        return stage;
    }

    /**
     * Setter method which initialises the global instance
     * of the {@link Stage}.
     * @param _stage The Stage
     */
    public static void setStage(Stage _stage)
    {
        stage = _stage;
    }

    /**
     * Getter method which returns the global instance
     * of the {@link StateManager}.
     * @return The StateManager.
     */
    public static StateManager getAppState()
    {
        return appState;
    }

    /**
     * Setter method which initialises the global instance
     * of the {@link StateManager}.
     * @param _appState The StateManager
     */
    public static void setAppState(StateManager _appState)
    {
        appState = _appState;
    }

    /**
     * Getter method which returns the global instance
     * of the {@link IAssets} interface. Game code Asset Loader
     * classes must implement this interface.
     * @return The IAssets interface.
     */
    public static IAssets getAssets()
    {
        return assets;
    }

    /**
     * Setter method which initialises the global instance
     * of the games Asset Loader class, which MUST implement
     * the {@link IAssets} interface.
     * @param _assets The asset loader class.
     */
    public static void setAssets(IAssets _assets)
    {
        assets = _assets;
    }
}
