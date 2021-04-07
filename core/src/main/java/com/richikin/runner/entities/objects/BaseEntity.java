package com.richikin.runner.entities.objects;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Disposable;
import com.richikin.enumslib.ActionStates;
import com.richikin.enumslib.GraphicID;
import com.richikin.runner.core.App;
import com.richikin.runner.entities.components.IEntityComponent;
import com.richikin.runner.physics.aabb.CollisionObject;
import com.richikin.utilslib.maths.SimpleVec3;
import com.richikin.utilslib.physics.aabb.AABBData;

public class BaseEntity implements IEntityComponent, Disposable
{
    public GraphicID       gid;                 // Entity ID
    public GraphicID       type;                // Entity Type - _Entity, _OBSTACLE, etc.
    public int             frameWidth;          // Width in pixels, or width of frame for animations
    public int             frameHeight;         // Width in pixels, or width of frame for animations
    public ActionStates    entityAction;        // Current action/state
    public SimpleVec3      position;            // Map position, only really needed for non-sprites
    public CollisionObject collisionObject;     // ...
    public float           rightEdge;           // The right edge of this entities collision box
    public float           topEdge;             // The top edge of this entities collision box
    public Body            b2dBody;             // Box2D Physics body
    public short           bodyCategory;        // Bit-mask entity collision type
    public short           collidesWith;        // Bit-mask of entity types that can be collided with

    public BaseEntity()
    {
        this.gid = GraphicID.G_NO_ID;
    }

    public BaseEntity(GraphicID _gid)
    {
        this.gid = _gid;
    }

    @Override
    public void setCollisionObject(int xPos, int yPos)
    {
        collisionObject = App.collisionUtils.newObject
            (
                new Rectangle
                    (
                        xPos,
                        yPos,
                        frameWidth,
                        frameHeight
                    )
            );

        collisionObject.gid          = this.gid;
        collisionObject.type         = GraphicID._ENTITY;
        collisionObject.isObstacle   = false;
        collisionObject.parentEntity = this;

        if (this.gid != GraphicID.G_NO_ID)
        {
            AABBData.add(collisionObject);
        }
    }

    @Override
    public void setCollisionObject(float xPos, float yPos)
    {
        setCollisionObject((int) xPos, (int) yPos);
    }

    @Override
    public Rectangle getCollisionRectangle()
    {
        return collisionObject.rectangle;
    }

    @Override
    public void setActionState(ActionStates action)
    {
        this.entityAction = action;
    }

    @Override
    public ActionStates getActionState()
    {
        return this.entityAction;
    }

    @Override
    public void tidy(int index)
    {
    }

    @Override
    public float getTopEdge()
    {
        return position.y + frameHeight;
    }

    @Override
    public float getRightEdge()
    {
        return position.x + frameWidth;
    }

    /**
     * Releases all resources of this object.
     */
    @Override
    public void dispose()
    {
        position = null;
    }
}
