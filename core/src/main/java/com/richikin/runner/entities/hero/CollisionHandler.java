package com.richikin.runner.entities.hero;

import com.badlogic.gdx.utils.Disposable;
import com.richikin.enumslib.ActionStates;
import com.richikin.enumslib.GraphicID;
import com.richikin.runner.core.App;
import com.richikin.runner.entities.objects.BaseEntity;
import com.richikin.runner.entities.objects.GdxSprite;
import com.richikin.runner.physics.aabb.CollisionObject;
import com.richikin.utilslib.maths.Point;
import com.richikin.utilslib.maths.SimpleVec2;
import com.richikin.utilslib.physics.Movement;
import com.richikin.utilslib.physics.aabb.ICollisionListener;

public class CollisionHandler implements ICollisionListener, Disposable
{
    public boolean isImpeded;

    public CollisionHandler()
    {
    }

    @Override
    public void onPositiveCollision(CollisionObject cobjHitting)
    {
        switch (cobjHitting.gid)
        {
            case _WALL:
            case G_DOOR:
            case G_OPEN_DOOR:
            case G_LOCKED_DOOR:
            case G_VILLAGER:
            {
                App.getPlayer().isOnPlatform   = false;
                App.getPlayer().platformSprite = null;

                isImpeded = true;
            }
            break;

            case G_FLOATING_PLATFORM:
            {
                if (App.getPlayer().collisionObject.contactEntity.getCollisionRectangle().contains
                    (
                        (App.getPlayer().getPosition().x + (App.getPlayer().frameWidth / 2)),
                        App.getPlayer().getPosition().y
                    ))
                {
                    App.getPlayer().isOnPlatform   = true;
                    App.getPlayer().platformSprite = (GdxSprite) App.getPlayer().collisionObject.contactEntity;
                }
            }
            break;

            // Objects that can be collided with, and
            // which WILL hurt LJM.
            case G_SPIKE_BLOCK_HORIZONTAL:
            case G_SPIKE_BLOCK_VERTICAL:
            case G_LOOP_BLOCK_HORIZONTAL:
            case G_LOOP_BLOCK_VERTICAL:
            case G_BIG_BLOCK_VERTICAL:
            case G_BIG_BLOCK_HORIZONTAL:
            case G_LASER_BEAM_HORIZONTAL:
            case G_LASER_BEAM_VERTICAL:
            case G_SPIKE_BALL:
            case G_STORM_DEMON:
            case G_BOUNCER:
            case G_DOUBLE_SPIKE_BLOCK:
            case G_ENEMY_BULLET:
            case G_FLAME_THROWER:
            case G_FLAME_THROWER_VERTICAL:
            case G_SOLDIER:
            {
                if ((App.getPlayer().getActionState() != ActionStates._HURT)
                    && (App.getPlayer().getActionState() != ActionStates._DYING))
                {
                    App.getPlayer().hurt();
                    rebound();
                }

                App.getPlayer().isOnPlatform   = false;
                App.getPlayer().platformSprite = null;
            }
            break;

            case G_NO_ID:
            default:
            {
                App.getPlayer().isOnPlatform   = false;
                App.getPlayer().platformSprite = null;
            }
            break;
        }

        App.getPlayer().collisionObject.action = ActionStates._INVISIBLE;
    }

    /**
     * Called when there is no collision occurring.
     */
    @Override
    public void onNegativeCollision()
    {
        isImpeded = false;

        if (App.getPlayer().getActionState() == ActionStates._HURT)
        {
            App.getPlayer().isHurting = false;
        }

        App.getPlayer().collisionObject.action = ActionStates._COLLIDABLE;
    }

    public boolean isNextTo(GraphicID graphicID)
    {
        boolean isNextTo = false;

        for (BaseEntity sprite : App.entityData.entityMap)
        {
            if (sprite.gid == graphicID)
            {
                isNextTo = (sprite.collisionObject.rectangle.overlaps(App.getPlayer().sprite.getBoundingRectangle()));
            }
        }

        return isNextTo;
    }

    public boolean isBlockedTop()
    {
        return validate(App.collisionUtils.getBoxHittingTop(App.getPlayer()).gid);
    }

    public boolean isBlockedBottom()
    {
        return validate(App.collisionUtils.getBoxHittingBottom(App.getPlayer()).gid);
    }

    public boolean isBlockedLeft()
    {
        return validate(App.collisionUtils.getBoxHittingLeft(App.getPlayer()).gid);
    }

    public boolean isBlockedRight()
    {
        return validate(App.collisionUtils.getBoxHittingRight(App.getPlayer()).gid);
    }

    private boolean validate(final GraphicID graphicID)
    {
        boolean isContact;

        switch (graphicID)
        {
            case _WALL:
            case G_LOCKED_DOOR:

            case G_VILLAGER:

            case G_POT:
            case G_BARREL:
            case G_TREASURE_CHEST:
            case G_MYSTERY_CHEST:
            case G_CRATE:

            case G_SPIKE_BLOCK_HORIZONTAL:
            case G_SPIKE_BLOCK_VERTICAL:
            case G_LOOP_BLOCK_HORIZONTAL:
            case G_LOOP_BLOCK_VERTICAL:
            case G_BIG_BLOCK_VERTICAL:
            case G_BIG_BLOCK_HORIZONTAL:
            case G_LASER_BEAM:
            case G_SPIKE_BALL:
            case G_STORM_DEMON:
            case G_BOUNCER:
            case G_DOUBLE_SPIKE_BLOCK:
            case G_ENEMY_BULLET:
            case G_SOLDIER:
            {
                isContact = true;
            }
            break;

            case G_QUESTION_MARK:
            default:
            {
                isContact = false;
            }
            break;
        }

        return isContact;
    }

    private void rebound()
    {
        if (App.getPlayer().collisionObject.hasContactUp()
            && !App.getPlayer().collisionObject.hasContactDown())
        {
            App.getPlayer().sprite.translateY(4 * Movement._DIRECTION_DOWN);
        }
        else if (App.getPlayer().collisionObject.hasContactDown()
            && !App.getPlayer().collisionObject.hasContactUp())
        {
            App.getPlayer().sprite.translateY(4 * Movement._DIRECTION_UP);
        }

        if (App.getPlayer().collisionObject.hasContactLeft()
            && !App.getPlayer().collisionObject.hasContactRight())
        {
            App.getPlayer().sprite.translateX(4 * Movement._DIRECTION_RIGHT);
        }
        else if (App.getPlayer().collisionObject.hasContactRight()
            && !App.getPlayer().collisionObject.hasContactLeft())
        {
            App.getPlayer().sprite.translateX(4 * Movement._DIRECTION_LEFT);
        }
    }

    public SimpleVec2 getBottomPoint(Point point)
    {
        SimpleVec2 simpleVec2;
        SimpleVec2 currentPos = new SimpleVec2
            (
                (int) App.getPlayer().getCollisionRectangle().x,
                (int) App.getPlayer().getCollisionRectangle().y
            );

        switch (point)
        {
            case _LEFT -> {

                simpleVec2 = new SimpleVec2((int) currentPos.getX(), (int) currentPos.getY());
            }

            case _RIGHT -> {

                simpleVec2 = new SimpleVec2
                    (
                        (int) (currentPos.getX() + App.getPlayer().collisionObject.rectangle.width),
                        (int) currentPos.getY()
                    );
            }

            default -> {

                simpleVec2 = new SimpleVec2
                    (
                        (int) (currentPos.getX() + (App.getPlayer().collisionObject.rectangle.width / 2)),
                        (int) currentPos.getY()
                    );
            }
        }

        return simpleVec2;
    }

    @Override
    public void dispose()
    {
    }
}
