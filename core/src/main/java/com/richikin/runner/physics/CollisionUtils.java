package com.richikin.runner.physics;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Disposable;
import com.richikin.enumslib.ActionStates;
import com.richikin.enumslib.GraphicID;
import com.richikin.enumslib.TileID;
import com.richikin.runner.core.App;
import com.richikin.runner.entities.objects.GdxSprite;
import com.richikin.runner.entities.objects.SpriteDescriptor;
import com.richikin.runner.graphics.Gfx;
import com.richikin.utilslib.physics.aabb.AABBData;
import com.richikin.runner.physics.aabb.CollisionObject;
import com.richikin.utilslib.physics.ICollideUtils;

public class CollisionUtils implements ICollideUtils, Disposable
{
    /**
     * Instantiates a new CollisionUtils object.
     */
    public CollisionUtils()
    {
    }

    /**
     * Initialise these utils.
     */
    @Override
    public void initialise()
    {
        AABBData.initialise();
    }

    /**
     * Get a new collision object.
     * @return the collision object
     */
    @Override
    public CollisionObject newObject()
    {
        return new CollisionObject();
    }

    /**
     * Get a new collision object.
     * @param rectangle the rectangle
     * @return the collision object
     */
    @Override
    public CollisionObject newObject(Rectangle rectangle)
    {
        return new CollisionObject(rectangle);
    }

    @Override
    public CollisionObject newObject(int x, int y, int width, int height, GraphicID graphicID)
    {
        return new CollisionObject(x, y, width, height, graphicID);
    }

    public boolean isTouchingAnother(int parentIndex)
    {
        boolean isTouching = false;

        for (CollisionObject object : AABBData.boxes())
        {
            if (object.index != parentIndex)
            {
                if (Intersector.overlaps(AABBData.boxes().get(parentIndex).rectangle, object.rectangle))
                {
                    isTouching = true;
                }
            }
        }

        return isTouching;
    }

    public boolean isTouchingAnEntity(int parentIndex)
    {
        boolean isTouching = false;

        for (CollisionObject object : AABBData.boxes())
        {
            if ((object.index != parentIndex) && !object.isObstacle)
            {
                if (Intersector.overlaps(AABBData.boxes().get(parentIndex).rectangle, object.rectangle))
                {
                    isTouching = true;
                }
            }
        }

        return isTouching;
    }

    /**
     * Tidy ALL currently active collision objects.
     */
    @Override
    public void tidy()
    {
        for (int i = 0; i < AABBData.boxes().size; i++)
        {
            if (AABBData.boxes().get(i).action == ActionStates._DEAD)
            {
                AABBData.remove(i);
            }
        }
    }

    /**
     * Tidy (Kill) ALL currently active collision objects.
     */
    private void tidyAll()
    {
        for (int i = 0; i < AABBData.boxes().size; i++)
        {
            AABBData.boxes().get(i).action = ActionStates._DEAD;
        }

        tidy();
    }

    @Override
    public boolean canCollide(GdxSprite entity, GdxSprite target)
    {
        return ((entity.collidesWith & target.bodyCategory) != 0)
            && ((target.collidesWith & entity.bodyCategory) != 0)
            && (entity.spriteNumber != target.spriteNumber);
//            && (entity.collisionObject.collisionArrayIndex != target.collisionObject.collisionArrayIndex);
    }

    @Override
    public boolean filter(short theEntityFlag, short theCollisionBoxFlag)
    {
        return ((theEntityFlag & theCollisionBoxFlag) != 0);
    }

    @Override
    public TileID getMarkerTileOn(int x, int y)
    {
        TileID tileID = TileID._UNKNOWN;

        for (SpriteDescriptor placementTile : App.mapData.placementTiles)
        {
            if (placementTile._BOX.contains(x, y))
            {
                tileID = placementTile._TILE;
            }
        }

        return tileID;
    }

    @Override
    public int getXBelow(GdxSprite spriteObj)
    {
        return (int) ((spriteObj.getCollisionRectangle().getX() + (Gfx.getTileWidth() / 2)) / Gfx.getTileWidth());
    }

    @Override
    public int getYBelow(GdxSprite spriteObj)
    {
        int y;

        y = (int) ((spriteObj.sprite.getY() - (spriteObj.sprite.getY() % Gfx.getTileHeight())) / Gfx.getTileHeight());

        if ((spriteObj.sprite.getY() % Gfx.getTileHeight()) == 0)
        {
            y--;
        }

        return y;
    }

    @Override
    public CollisionObject getBoxHittingTop(GdxSprite spriteObject)
    {
        return AABBData.boxes().get(spriteObject.collisionObject.boxHittingTop);
    }

    @Override
    public CollisionObject getBoxHittingBottom(GdxSprite spriteObject)
    {
        return AABBData.boxes().get(spriteObject.collisionObject.boxHittingBottom);
    }

    @Override
    public CollisionObject getBoxHittingLeft(GdxSprite spriteObject)
    {
        return AABBData.boxes().get(spriteObject.collisionObject.boxHittingLeft);
    }

    @Override
    public CollisionObject getBoxHittingRight(GdxSprite spriteObject)
    {
        return AABBData.boxes().get(spriteObject.collisionObject.boxHittingRight);
    }

    @Override
    public void dispose()
    {
        tidyAll();
    }
}
