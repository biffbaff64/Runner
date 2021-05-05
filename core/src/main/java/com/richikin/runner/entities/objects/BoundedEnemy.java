package com.richikin.runner.entities.objects;

import com.richikin.enumslib.GraphicID;
import com.richikin.runner.graphics.Gfx;
import com.richikin.utilslib.maths.BoundsBoxF;
import com.richikin.utilslib.physics.Movement;

public class BoundedEnemy extends GdxSprite
{
    public BoundsBoxF movementBounds;
    public boolean    isVertical;
    public boolean    justTurnedAroundX;
    public boolean    justTurnedAroundY;

    public BoundedEnemy(GraphicID graphicID)
    {
        super(graphicID);
    }

    public void init()
    {
        justTurnedAroundX = false;
        justTurnedAroundY = false;
    }

    public void checkMovementBounds()
    {
        if (movementBounds != null)
        {
            if (direction.getX() != Movement._DIRECTION_STILL)
            {
                justTurnedAroundX = false;

                if (direction.getX() == Movement._DIRECTION_LEFT)
                {
                    if ((sprite.getX() < 0)
                        || (sprite.getX() <= movementBounds.left))
                    {
                        justTurnedAroundX = true;
                    }
                }
                else if (direction.getX() == Movement._DIRECTION_RIGHT)
                {
                    if ((sprite.getX() > Gfx.getMapWidth())
                        || (sprite.getX() >= movementBounds.right))
                    {
                        justTurnedAroundX = true;
                    }
                }

                if (justTurnedAroundX)
                {
                    onMovementBoundsTurn(direction.getX());

                    direction.toggleX();
                }
            }

            if (direction.getY() != Movement._DIRECTION_STILL)
            {
                justTurnedAroundY = false;

                if (direction.getY() == Movement._DIRECTION_UP)
                {
                    if ((sprite.getY() >= Gfx.getMapHeight())
                        || (sprite.getY() >= movementBounds.top))
                    {
                        justTurnedAroundY = true;
                    }
                }
                else if (direction.getY() == Movement._DIRECTION_DOWN)
                {
                    if ((sprite.getY() < 0)
                        || (sprite.getY() <= movementBounds.bottom))
                    {
                        justTurnedAroundY = true;
                    }
                }

                if (justTurnedAroundY)
                {
                    onMovementBoundsTurn(direction.getY());

                    direction.toggleY();
                }
            }
        }
    }

    public void onMovementBoundsTurn(int edgeSide)
    {
    }

    public void setHorizontalMovementBounds()
    {
        if (direction.getX() == Movement._DIRECTION_LEFT)
        {
            movementBounds = new BoundsBoxF
                (
                    // left
                    (sprite.getX() - distance.getX()),
                    // top
                    (sprite.getY() + frameHeight),
                    // right
                    sprite.getX(),
                    // bottom
                    sprite.getY()
                );
        }
        else    // Movement._DIRECTION_RIGHT
        {
            movementBounds = new BoundsBoxF
                (
                    // left
                    sprite.getX(),
                    // top
                    (sprite.getY() + frameHeight),
                    // right
                    (sprite.getX() + distance.getX()),
                    // bottom
                    sprite.getY()
                );
        }

        isVertical = (direction.getX() != Movement._DIRECTION_STILL);
    }

    public void setVerticalMovementBounds()
    {
        if (direction.getY() == Movement._DIRECTION_UP)
        {
            movementBounds = new BoundsBoxF
                (
                    // left
                    sprite.getX(),
                    // top
                    (sprite.getY() + distance.getY()),
                    // right
                    (sprite.getX() + frameWidth),
                    // bottom
                    sprite.getY()
                );
        }
        else if (direction.getY() == Movement._DIRECTION_DOWN)
        {
            movementBounds = new BoundsBoxF
                (
                    // left
                    sprite.getX(),
                    // top
                    sprite.getY(),
                    // right
                    (sprite.getX() + frameWidth),
                    // bottom
                    (sprite.getY() - distance.getY())
                );
        }

        isVertical = (direction.getY() != Movement._DIRECTION_STILL);
    }
}
