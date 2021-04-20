
package com.richikin.runner.entities.systems;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.richikin.enumslib.ActionStates;
import com.richikin.runner.core.App;
import com.richikin.runner.entities.objects.GdxSprite;
import com.richikin.runner.graphics.Gfx;
import com.richikin.utilslib.logging.StopWatch;
import com.richikin.utilslib.physics.Movement;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

public class TargettingSystem implements DecisionSystem
{
    public float   speedX;
    public float   speedY;
    public boolean isAdjustingTarget;
    public boolean rotationAllowed;
    public boolean checkMoveAllowed;

    private Vector2 destination;

    public TargettingSystem()
    {
        speedX = 0;
        speedY = 0;

        destination      = new Vector2();
        rotationAllowed  = false;
        checkMoveAllowed = true;
    }

    @Override
    public void update(GdxSprite parentSprite, GdxSprite target)
    {
        Vector2 vector2 = getTargetPosition(parentSprite, target);

        faceTarget(vector2.x, vector2.y, parentSprite);
    }

    @Override
    public Vector2 getTargetPosition(GdxSprite parentSprite, GdxSprite target)
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
            targetX = (target.sprite.getX() + (target.frameWidth / 2));
            targetY = (target.sprite.getY() + (target.frameHeight / 2));
        }

        return new Vector2(targetX, targetY);
    }

    @Override
    public void setAdjustedTarget(@NotNull GdxSprite parentSprite)
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
    public void faceTarget(float targetX, float targetY, GdxSprite parentSprite)
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
    public Vector2 getTargetVector(float targetX, float targetY, GdxSprite parentSprite)
    {
        Vector2 targetPos = new Vector2(targetX, targetY);
        Vector2 parentPos = new Vector2(parentSprite.sprite.getX(), parentSprite.sprite.getY());

        return new Vector2(targetPos.sub(parentPos));
    }

    @Override
    public void calculateMove(@NotNull Vector2 vector2, GdxSprite parentSprite)
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
    public float checkXMovement(float _xMove, GdxSprite parentSprite)
    {
        if (_xMove > 0)
        {
            if (parentSprite.collisionObject.hasContactRight() && parentSprite.collisionObject.isContactObstacle)
            {
                _xMove = 0;
                parentSprite.direction.setX(Movement._DIRECTION_STILL);
            }
            else
            {
                parentSprite.direction.setX(Movement._DIRECTION_RIGHT);
            }
        }
        else if (_xMove < 0)
        {
            if (parentSprite.collisionObject.hasContactLeft() && parentSprite.collisionObject.isContactObstacle)
            {
                _xMove = 0;
                parentSprite.direction.setX(Movement._DIRECTION_STILL);
            }
            else
            {
                parentSprite.direction.setX(Movement._DIRECTION_LEFT);
            }
        }
        else
        {
            _xMove = 0;
            parentSprite.direction.setX(Movement._DIRECTION_STILL);
        }

        return _xMove;
    }

    @Override
    public float checkYMovement(float _yMove, GdxSprite parentSprite)
    {
        if (_yMove > 0)
        {
            if (parentSprite.collisionObject.hasContactUp() && parentSprite.collisionObject.isContactObstacle)
            {
                _yMove = 0;
                parentSprite.direction.setX(Movement._DIRECTION_STILL);
            }
            else
            {
                parentSprite.direction.setX(Movement._DIRECTION_UP);
            }
        }
        else if (_yMove < 0)
        {
            if (parentSprite.collisionObject.hasContactDown() && parentSprite.collisionObject.isContactObstacle)
            {
                _yMove = 0;
                parentSprite.direction.setX(Movement._DIRECTION_STILL);
            }
            else
            {
                parentSprite.direction.setX(Movement._DIRECTION_DOWN);
            }
        }
        else
        {
            _yMove = 0;
            parentSprite.direction.setX(Movement._DIRECTION_STILL);
        }

        return _yMove;
    }

    @Override
    public float distanceRemaining(GdxSprite parentSprite, Vector2 destination)
    {
        return new Vector2(parentSprite.sprite.getX(), parentSprite.sprite.getY()).dst(destination);
    }
}
