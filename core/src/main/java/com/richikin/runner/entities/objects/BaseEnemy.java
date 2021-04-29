package com.richikin.runner.entities.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.Vector2;
import com.richikin.enumslib.ActionStates;
import com.richikin.enumslib.GraphicID;
import com.richikin.runner.core.App;
import com.richikin.runner.entities.components.AbilityComponent;
import com.richikin.runner.entities.systems.EnemyAttackSystem;
import com.richikin.runner.graphics.Gfx;
import com.richikin.runner.physics.aabb.CollisionObject;
import com.richikin.utilslib.logging.StopWatch;
import com.richikin.utilslib.maths.BoundsBoxF;
import com.richikin.utilslib.physics.Movement;
import com.richikin.utilslib.physics.aabb.ICollisionListener;

// TODO: 03/12/2020 - Eventually all enemy entities must extend from this class
// TODO: 03/12/2020 - This class should handle all common enemy actions

public class BaseEnemy extends GdxSprite
{
    public BoundsBoxF        movementBounds;
    public AbilityComponent  abilityComponent;
    public Color             originalColor;
    public EnemyAttackSystem attackSystem;
    public Vector2           destination;
    public StopWatch         stopWatch;

    public boolean isVertical;
    public boolean justTurnedAroundX;
    public boolean justTurnedAroundY;
    public boolean justBegunStanding;

    public BaseEnemy(GraphicID graphicID)
    {
        super(graphicID);
    }

    @Override
    public void initialise(SpriteDescriptor descriptor)
    {
        create(descriptor);

        setActionState(ActionStates._INIT_SPAWN);

        justBegunStanding = false;
        justTurnedAroundX = false;
        justTurnedAroundY = false;

        setCollisionListener();
    }

    @Override
    public void animate()
    {
        elapsedAnimTime += Gdx.graphics.getDeltaTime();
        sprite.setRegion(App.entityUtils.getKeyFrame(animation, elapsedAnimTime, true));
    }

    protected void initSpawning()
    {
        animation = App.entityUtils.createAnimation
            (
                "spawm64x64",
                animFrames,
                20,
                Animation.PlayMode.NORMAL
            );

        animation.setFrameDuration(0.3f / 6f);

        elapsedAnimTime = 0;
        isAnimating = true;

        sprite.setAlpha(1.0f);

        setActionState(ActionStates._SPAWNING);
    }

    public void setCollisionListener()
    {
        addCollisionListener(new ICollisionListener()
        {
            @Override
            public void onPositiveCollision(CollisionObject cobjHitting)
            {

            }

            @Override
            public void onNegativeCollision()
            {

            }
        });
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
