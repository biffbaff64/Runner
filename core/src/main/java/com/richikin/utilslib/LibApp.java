package com.richikin.utilslib;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.richikin.utilslib.assets.IAssets;
import com.richikin.utilslib.logging.StateManager;

@SuppressWarnings({"UtilityClassCanBeEnum", "NonFinalUtilityClass"})
public class LibApp
{
    public static SpriteBatch      spriteBatch;
    public static Stage            stage;
    public static StateManager     appState;
    public static IAssets          assets;
}
