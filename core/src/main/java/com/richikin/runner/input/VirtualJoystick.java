
package com.richikin.runner.input;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.richikin.runner.core.App;
import com.richikin.utilslib.input.DirectionMap;
import com.richikin.utilslib.input.Touchpad;
import com.richikin.utilslib.physics.Dir;

public class VirtualJoystick
{
    private static final float PAD_X      = 25;
    private static final float PAD_Y      = 25;
    private static final float PAD_WIDTH  = 240;
    private static final float PAD_HEIGHT = 240;

    private Touchpad touchpad;

    public VirtualJoystick()
    {
    }

    public void create()
    {
        Skin touchpadSkin = new Skin();
        touchpadSkin.add("background", new Texture("packedimages/input/touch_background.png"));
        touchpadSkin.add("ball", new Texture("packedimages/input/joystick_ball.png"));

        Touchpad.TouchpadStyle touchpadStyle = new Touchpad.TouchpadStyle();

        Drawable touchBackground = touchpadSkin.getDrawable("background");
        Drawable touchKnob       = touchpadSkin.getDrawable("ball");

        touchpadStyle.background = touchBackground;
        touchpadStyle.knob       = touchKnob;

        touchpad = new Touchpad(1, touchpadStyle);
        touchpad.setBounds(PAD_X, PAD_Y, PAD_WIDTH, PAD_HEIGHT);
        touchpad.setResetOnTouchUp(true);

        App.getStage().addActor(touchpad);

        hide();
    }

    public void update()
    {
        App.getHud().releaseDirectionButtons();

        switch (evaluateJoypadDirection())
        {
            case _UP -> {
                App.getHud().buttonUp.press();
            }
            case _DOWN -> {
                App.getHud().buttonDown.press();
            }
            case _LEFT -> {
                App.getHud().buttonLeft.press();
            }
            case _RIGHT -> {
                App.getHud().buttonRight.press();
            }
            case _UP_LEFT -> {
                App.getHud().buttonUp.press();
                App.getHud().buttonLeft.press();
            }
            case _UP_RIGHT -> {
                App.getHud().buttonUp.press();
                App.getHud().buttonRight.press();
            }
            case _DOWN_LEFT -> {
                App.getHud().buttonDown.press();
                App.getHud().buttonLeft.press();
            }
            case _DOWN_RIGHT -> {
                App.getHud().buttonDown.press();
                App.getHud().buttonRight.press();
            }
            default -> {
            }
        }
    }

    public void show()
    {
        if (touchpad != null)
        {
            touchpad.addAction(Actions.show());
        }
    }

    public void hide()
    {
        if (touchpad != null)
        {
            touchpad.addAction(Actions.hide());
        }
    }

    public float getXPercent()
    {
        return touchpad.getKnobPercentX();
    }

    public float getYPercent()
    {
        return touchpad.getKnobPercentY();
    }

    public Touchpad getTouchpad()
    {
        return touchpad;
    }

    private Dir evaluateJoypadDirection()
    {
        Dir joyDir;

        //
        // The default angle for joystick goes round anti-clockwise,
        // so modify so that the result is now clockwise.
        int angle = Math.abs((int) (InputUtils.getJoystickAngle() - 360));

        joyDir = DirectionMap.map[angle / 10].translated;

        App.inputManager.lastRegisteredDirection = joyDir;

        return joyDir;
    }

    public void removeTouchpad()
    {
        if (touchpad != null)
        {
            touchpad.addAction(Actions.removeActor());
            touchpad = null;
        }
    }
}
