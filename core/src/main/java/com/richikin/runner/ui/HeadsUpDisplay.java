package com.richikin.runner.ui;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.utils.Disposable;
import com.richikin.enumslib.StateID;
import com.richikin.runner.config.AppConfig;
import com.richikin.runner.core.App;
import com.richikin.runner.input.VirtualJoystick;
import com.richikin.utilslib.input.Switch;

public class HeadsUpDisplay implements Disposable
{
    private static final int _X1     = 0;
    private static final int _X2     = 1;
    private static final int _Y      = 2;
    private static final int _WIDTH  = 3;
    private static final int _HEIGHT = 4;

    public Switch buttonUp;
    public Switch buttonDown;
    public Switch buttonLeft;
    public Switch buttonRight;

    // TODO: 27/11/2020 - Are these following switches still needed ??
    public Switch buttonAction;
    public Switch buttonAttack;
    public Switch buttonX;
    public Switch buttonY;
    public Switch buttonPause;
    public Switch buttonDevOptions;

    public ImageButton ActionButton;
    public ImageButton AttackButton;
    public ImageButton PauseButton;

    public MessageManager messageManager;
    public StateID        hudStateID;

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

    public void releaseDirectionButtons()
    {
        buttonRight.release();
        buttonLeft.release();
        buttonUp.release();
        buttonDown.release();
    }

    public void setStateID(StateID newState)
    {
        hudStateID = newState;
    }

    public VirtualJoystick getJoystick()
    {
        return App.inputManager.virtualJoystick;
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
