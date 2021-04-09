
package com.richikin.runner.entities.hero;

import com.badlogic.gdx.utils.Disposable;
import com.richikin.enumslib.ActionStates;
import com.richikin.runner.config.AppConfig;
import com.richikin.runner.core.App;
import com.richikin.utilslib.input.DirectionMap;
import com.richikin.utilslib.input.controllers.ControllerType;
import com.richikin.utilslib.physics.Dir;
import com.richikin.utilslib.physics.Movement;

public class ButtonInputHandler implements Disposable
{
    AButtonActions aButtonActions;
    BButtonActions bButtonActions;
    XButtonActions xButtonActions;
    YButtonActions yButtonActions;

    public ButtonInputHandler()
    {
        aButtonActions = new AButtonActions();
        bButtonActions = new BButtonActions();
        xButtonActions = new XButtonActions();
        yButtonActions = new YButtonActions();
    }

    public void checkButtons()
    {
        //
        // A Button
        if (App.getHud().buttonA.isPressed())
        {
            aButtonActions.process();
            App.getHud().buttonA.release();
        }

        //
        // B Button
        if (App.getHud().buttonB.isPressed())
        {
            bButtonActions.process();
            App.getHud().buttonB.release();
        }

        //
        // X Button
        if (App.getHud().buttonX.isPressed())
        {
            xButtonActions.process();
            App.getHud().buttonX.release();
            App.getPlayer().canOpenMessagePanel = false;
        }

        //
        // Y Button
        if (App.getHud().buttonY.isPressed())
        {
            yButtonActions.process();
            App.getHud().buttonY.release();
        }

        if (AppConfig.availableInputs.contains(ControllerType._VIRTUAL, true))
        {
            if (App.inputManager.virtualJoystick != null)
            {
                //
                // Updates button presses depending
                // upon joystick knob position
                App.inputManager.virtualJoystick.update();

                setDirection(App.inputManager.lastRegisteredDirection);
            }
        }

        if (AppConfig.availableInputs.contains(ControllerType._EXTERNAL, true))
        {
            setDirection(App.inputManager.lastRegisteredDirection);
        }

        if (AppConfig.availableInputs.contains(ControllerType._KEYBOARD, true))
        {
            App.inputManager.keyboard.update();
        }

        boolean directionButtonPressed = false;

        //
        // UP button
        if (App.getPlayer().direction.getY() == Movement._DIRECTION_UP)
        {
            directionButtonPressed = true;

            if (App.getPlayer().collision.isBlockedTop())
            {
                App.getPlayer().isMovingY = false;
                App.getHud().buttonUp.release();
            }
            else
            {
                App.getPlayer().direction.setY(Movement._DIRECTION_UP);
                App.getPlayer().isMovingY = true;
                App.getPlayer().setActionState(ActionStates._RUNNING);
                App.getPlayer().isFlippedY = false;
            }
        }

        //
        // DOWN button
        if (App.getPlayer().direction.getY() == Movement._DIRECTION_DOWN)
        {
            directionButtonPressed = true;

            if (App.getPlayer().collision.isBlockedBottom())
            {
                App.getPlayer().isMovingY = false;
                App.getHud().buttonDown.release();
            }
            else
            {
                App.getPlayer().direction.setY(Movement._DIRECTION_DOWN);
                App.getPlayer().isMovingY = true;
                App.getPlayer().setActionState(ActionStates._RUNNING);
                App.getPlayer().isFlippedY = false;
            }
        }

        if (!App.getPlayer().isMovingY)
        {
            App.getPlayer().direction.setY(Movement._DIRECTION_STILL);
            App.getPlayer().speed.setY(0);
        }

        //
        // Check the RIGHT button
        if (App.getPlayer().direction.getX() == Movement._DIRECTION_RIGHT)
        {
            directionButtonPressed = true;

            if (App.getPlayer().collision.isBlockedRight())
            {
                App.getPlayer().isMovingX = false;
                App.getHud().buttonRight.release();
            }
            else
            {
                App.getPlayer().direction.setX(Movement._DIRECTION_RIGHT);
                App.getPlayer().isMovingX = true;
                App.getPlayer().setActionState(ActionStates._RUNNING);
                App.getPlayer().isFlippedX = false;
            }
        }
        //
        // Check the LEFT button
        else if (App.getPlayer().direction.getX() == Movement._DIRECTION_LEFT)
        {
            directionButtonPressed = true;

            if (App.getPlayer().collision.isBlockedLeft())
            {
                App.getPlayer().isMovingX = false;
                App.getHud().buttonLeft.release();
            }
            else
            {
                App.getPlayer().direction.setX(Movement._DIRECTION_LEFT);
                App.getPlayer().isMovingX = true;
                App.getPlayer().setActionState(ActionStates._RUNNING);
                App.getPlayer().isFlippedX = true;
            }
        }

        if (!App.getPlayer().isMovingX)
        {
            App.getPlayer().direction.setX(Movement._DIRECTION_STILL);
            App.getPlayer().speed.setX(0);
        }

        //
        // No direction buttons pressed
        if (!directionButtonPressed)
        {
            if ((App.getPlayer().getActionState() != ActionStates._HURT)
                && (App.getPlayer().getActionState() != ActionStates._FIGHTING))
            {
                App.getPlayer().isMovingX = false;
                App.getPlayer().isMovingY = false;

                App.getPlayer().setActionState(ActionStates._STANDING);
                App.getPlayer().speed.set(0, 0);

                App.getPlayer().direction.standStill();
            }
        }
    }

    private void setDirection(Dir _direction)
    {
        for (int i = 0; i< DirectionMap.map.length; i++)
        {
            if (DirectionMap.map[i].translated == _direction)
            {
                App.getPlayer().direction.set
                    (
                        DirectionMap.map[i].dirX,
                        DirectionMap.map[i].dirY
                    );
            }
        }
    }

    @Override
    public void dispose()
    {
    }
}
