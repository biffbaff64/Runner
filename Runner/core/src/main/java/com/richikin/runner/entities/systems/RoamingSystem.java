package com.richikin.runner.entities.systems;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.richikin.enumslib.ActionStates;
import com.richikin.runner.core.App;
import com.richikin.runner.entities.objects.GdxSprite;
import com.richikin.runner.graphics.Gfx;
import com.richikin.utilslib.logging.StopWatch;
import com.richikin.utilslib.physics.Movement;

import java.util.concurrent.TimeUnit;

public class RoamingSystem implements DecisionSystem
{
    public float   speedX;
    public float   speedY;
    public boolean isAdjustingTarget;
    public boolean rotationAllowed;
    public boolean checkMoveAllowed;

    private Vector2 destination;

    public RoamingSystem()
    {
    }

    @Override
    public void update(final GdxSprite parentSprite, final GdxSprite target)
    {
        Vector2 vector2 = getTargetPosition(parentSprite, target);

        faceTarget(vector2.x, vector2.y, parentSprite);
    }

    @Override
    public Vector2 getTargetPosition(final GdxSprite parentSprite, final GdxSprite target)
    {
        float targetX;
        float targetY;

        if (isAdjustingTarget)
        {
            targetX = destination.x;
            targetY = destination.y;

            if (parentSprite.collisionObject.invisibilityTimer.time(TimeUnit.MILLISECONDS) > 3500)
            {
                isAdjustingTarget = false;
            }
        }
        else
        {
            targetX = (target.sprite.getX() + (target.frameWidth / 2f));
            targetY = (target.sprite.getY() + (target.frameHeight / 2f));
        }

        return new Vector2(targetX, targetY);
    }

    @Override
    public void setAdjustedTarget(final GdxSprite parentSprite)
    {
        int trys = 0;

        do
        {
            destination.set
                (
                    MathUtils.random(Gfx.getMapWidth()),
                    MathUtils.random(Gfx.getMapHeight())
                );

            trys++;
        }
        while ((trys < 1000) && (App.getPlayer().viewBox.contains(destination)));

        parentSprite.collisionObject.action            = ActionStates._INVISIBLE;
        parentSprite.collisionObject.invisibilityTimer = StopWatch.start();
    }

    @Override
    public void faceTarget(final float targetX, final float targetY, final GdxSprite parentSprite)
    {
        Vector2 vector2 = getTargetVector(targetX, targetY, parentSprite);

        if (rotationAllowed)
        {
            parentSprite.sprite.setRotation(vector2.angle() - 90);
        }

        if (parentSprite.getActionState() == ActionStates._RUNNING)
        {
            calculateMove(vector2.nor(), parentSprite);
        }
    }

    @Override
    public Vector2 getTargetVector(final float targetX, final float targetY, final GdxSprite parentSprite)
    {
        Vector2 targetPos = new Vector2(targetX, targetY);
        Vector2 parentPos = new Vector2(parentSprite.sprite.getX(), parentSprite.sprite.getY());

        return new Vector2(targetPos.sub(parentPos));
    }

    @Override
    public void calculateMove(final Vector2 vector2, final GdxSprite parentSprite)
    {
        float xMove = (speedX * vector2.x);
        float yMove = (speedY * vector2.y);

        if (checkMoveAllowed)
        {
            xMove = checkXMovement(xMove, parentSprite);
            yMove = checkYMovement(yMove, parentSprite);
        }

        parentSprite.speed.set(xMove, yMove);

        if (parentSprite.speed.isEmpty())
        {
            parentSprite.setActionState(ActionStates._STANDING);
        }
    }

    @Override
    public float checkXMovement(float xMove, GdxSprite parentSprite)
    {
        if (xMove > 0)
        {
            if (parentSprite.collisionObject.hasContactRight() && parentSprite.collisionObject.isContactObstacle)
            {
                xMove = 0;
                parentSprite.direction.setX(Movement._DIRECTION_STILL);
            }
            else
            {
                parentSprite.direction.setX(Movement._DIRECTION_RIGHT);
            }
        }
        else if (xMove < 0)
        {
            if (parentSprite.collisionObject.hasContactLeft() && parentSprite.collisionObject.isContactObstacle)
            {
                xMove = 0;
                parentSprite.direction.setX(Movement._DIRECTION_STILL);
            }
            else
            {
                parentSprite.direction.setX(Movement._DIRECTION_LEFT);
            }
        }
        else
        {
            xMove = 0;
            parentSprite.direction.setX(Movement._DIRECTION_STILL);
        }

        return xMove;
    }

    @Override
    public float checkYMovement(float yMove, GdxSprite parentSprite)
    {
        if (yMove > 0)
        {
            if (parentSprite.collisionObject.hasContactUp() && parentSprite.collisionObject.isContactObstacle)
            {
                yMove = 0;
                parentSprite.direction.setX(Movement._DIRECTION_STILL);
            }
            else
            {
                parentSprite.direction.setX(Movement._DIRECTION_UP);
            }
        }
        else if (yMove < 0)
        {
            if (parentSprite.collisionObject.hasContactDown() && parentSprite.collisionObject.isContactObstacle)
            {
                yMove = 0;
                parentSprite.direction.setX(Movement._DIRECTION_STILL);
            }
            else
            {
                parentSprite.direction.setX(Movement._DIRECTION_DOWN);
            }
        }
        else
        {
            yMove = 0;
            parentSprite.direction.setX(Movement._DIRECTION_STILL);
        }

        return yMove;
    }

    @Override
    public float distanceRemaining(final GdxSprite parentSprite, final Vector2 destination)
    {
        return new Vector2(parentSprite.sprite.getX(), parentSprite.sprite.getY()).dst(destination);
    }
}
