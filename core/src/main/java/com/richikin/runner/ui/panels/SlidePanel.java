
package com.richikin.runner.ui.panels;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.richikin.enumslib.ActionStates;
import com.richikin.enumslib.StateID;
import com.richikin.utilslib.maths.SimpleVec2F;
import com.richikin.utilslib.maths.XYSetF;
import com.richikin.utilslib.physics.Direction;
import com.richikin.utilslib.ui.DefaultPanel;

public class SlidePanel extends DefaultPanel implements IUserInterfacePanel
{
    public  SimpleVec2F  speed;
    public  Direction    direction;
    public  XYSetF       distance;
    public  XYSetF       distanceReset;
    public  ActionStates action;
    private boolean      isInPlace;

    public SlidePanel()
    {
        super();
    }

    @Override
    public void initialise(final TextureRegion _region, final String _nameID, final Object... args)
    {
        this.textureRegion = _region;
        this.nameID        = _nameID;

        deactivate();

        this.isInPlace = false;
        this.action    = ActionStates._NO_ACTION;

        setWidth(textureRegion.getRegionWidth());
        setHeight(textureRegion.getRegionHeight());

        setState(StateID._STATE_OPENING);
    }

    @Override
    public void set(SimpleVec2F xy, SimpleVec2F distance, Direction direction, SimpleVec2F speed)
    {
        setPosition(xy.getX(), xy.getY());

        this.distance.set(distance);
        this.distanceReset.set(distance);
        this.direction.set(direction);
        this.speed.set(speed);
    }

    @Override
    public boolean update()
    {
        if (getActiveState())
        {
            switch (getState())
            {
                case _STATE_OPENING:
                {
                    if (move())
                    {
                        setState(StateID._UPDATE);
                    }
                }
                break;

                case _STATE_CLOSING:
                {
                    if (move())
                    {
                        deactivate();

                        setState(StateID._STATE_CLOSED);
                    }
                }
                break;

                case _UPDATE:
                case _STATE_CLOSED:
                default:
                {
                }
                break;
            }
        }

        return !getActiveState();
    }

    private boolean updateReveal()
    {
        if (move())
        {
            isInPlace = true;
            deactivate();

            return true;
        }

        return false;
    }

    private boolean updateHide()
    {
        if (move())
        {
            isInPlace = false;
            deactivate();

            return true;
        }

        return false;
    }

    private boolean move()
    {
        if (distance.getX() > 0)
        {
            setPosition((int) (getPosition().getX() + (speed.getX() * direction.getX())), getPosition().getY());
            distance.subX((int) Math.min(distance.getX(), speed.getX()));
        }

        if (distance.getY() > 0)
        {
            setPosition(getPosition().getX(), (int) (getPosition().getY() + (speed.getY() * direction.getY())));
            distance.subY((int) Math.min(distance.getY(), speed.getY()));
        }

        return distance.isEmpty();
    }
}
