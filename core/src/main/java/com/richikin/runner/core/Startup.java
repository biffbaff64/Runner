package com.richikin.runner.core;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.richikin.runner.assets.GameAssets;
import com.richikin.runner.audio.GameAudio;
import com.richikin.runner.config.AppConfig;
import com.richikin.runner.config.Settings;
import com.richikin.runner.developer.DebugRenderer;
import com.richikin.runner.developer.Developer;
import com.richikin.runner.graphics.Gfx;
import com.richikin.runner.graphics.camera.Shake;
import com.richikin.runner.scenes.MainGameScreen;
import com.richikin.runner.scenes.MainMenuScreen;
import com.richikin.utilslib.logging.Trace;

public class Startup
{
    public Startup()
    {
    }

    /**
     * App Startup process
     */
    public void startApp()
    {
        //
        // Initialise DEBUG classes
        //noinspection LibGDXLogLevel
        Gdx.app.setLogLevel(Application.LOG_DEBUG);
        Trace.__FILE_FUNC_WithDivider();

        App.initialiseObjects();

        App.settings.createPreferencesObject();
        AppConfig.setup();
        App.settings.freshInstallCheck();
        App.settings.debugReport();
        App.inputManager.setup();

        Gfx.setPPM(Gfx._PPM_SETTING);

        //
        // For Android apps...
        // Setting up google services goes here.

        DebugRenderer.setup(GameAssets._PRO_WINDOWS_FONT);
        App.worldModel.createB2DRenderer();

        GameAudio.inst().setup();
        Shake.setAllowed(false);

        AppConfig.addBackButton("back_arrow", "back_arrow_pressed");

        Trace.divider();
    }

    /**
     * Ends the startup process by handing control
     * to the {@link MainMenuScreen} or, if MainMenuScreen
     * is disabled, control is passed to {@link MainGameScreen}
     */
    public void closeStartup()
    {
        if (Developer.isDevMode() && App.settings.isEnabled(Settings._DISABLE_MENU_SCREEN))
        {
            App.mainGame.setScreen(App.mainGameScreen);
        }
        else
        {
            App.mainGame.setScreen(App.mainMenuScreen);
        }
    }
}
