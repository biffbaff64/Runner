package com.richikin.runner.ui;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.Disposable;
import com.richikin.runner.config.AppConfig;
import com.richikin.runner.core.App;
import com.richikin.utilslib.input.controllers.ControllerType;

public class HeadsUpDisplay implements Disposable
{
    private static final int _X1     = 0;
    private static final int _X2     = 1;
    private static final int _Y      = 2;
    private static final int _WIDTH  = 3;
    private static final int _HEIGHT = 4;

    public HeadsUpDisplay()
    {
    }

    public void createHud()
    {
        AppConfig.hudExists = false;
    }

    public void update()
    {
    }

    public void render(OrthographicCamera camera, boolean canDrawControls)
    {
    }

    public void showControls()
    {
    }

    public void hideControls()
    {
    }

    public void showPauseButton(boolean state)
    {
    }

    /**
     * Creates the game screen buttons and then
     * registers them with the Scene2D Stage.
     */
    private void createHUDButtons()
    {
    }

    @Override
    public void dispose()
    {
    }
}
