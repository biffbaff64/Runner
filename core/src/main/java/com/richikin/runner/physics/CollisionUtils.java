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
    public CollisionUtils()
    {
    }

    @Override
    public void initialise()
    {
        // Reset the AABB box array, ready for refilling
        // with fresh data.
        AABBData.initialise();
    }

    /**
     * Creates a new {@link CollisionObject} with default settings.
     * @return The CollisionObject.
     */
    @Override
    public CollisionObject newObject()
    {
        return new CollisionObject();
    }

    /**
     * Creates a new {@link CollisionObject} with position and size
     * data obtained from the supplied rectangle.
     * @param rectangle The rectangle to use.
     * @return The CollisionObject.
     */
    @Override
    public CollisionObject newObject(Rectangle rectangle)
    {
        return new CollisionObject(rectangle);
    }

    /**
     * Creates a new {@link CollisionObject} with the supplied
     * position and size data.
     * @param x The X coordinate.
     * @param y The Y coordinatee.
     * @param width Width of the rectangle in pixels.
     * @param height Height of the rectangle in pixels.
     * @param graphicID The {@link GraphicID} of this CollisionObject.
     * @return The Collision Object.
     */
    @Override
    public CollisionObject newObject(int x, int y, int width, int height, GraphicID graphicID)
    {
        return new CollisionObject(x, y, width, height, graphicID);
    }

    /**
     * Checks for contact between the {@link CollisionObject} at the
     * specified array position and any other CollisionObject. This
     * check is done for ALL objects, entities and otherwise.
     * @param parentIndex The array position.
     * @return True if contact is made.
     */
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

    /**
     * Checks for contact between the {@link CollisionObject} at the
     * specified array position and any Entity object.
     * CollisionObjects that are not associated with Entities, such as
     * walls, floors, ceilings etc are ignored.
     * @param parentIndex The array position.
     * @return True if contact is made.
     */
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
