package com.richikin.utilslib.physics;

import com.badlogic.gdx.math.Rectangle;
import com.richikin.runner.entities.objects.GdxSprite;
import com.richikin.runner.physics.aabb.CollisionObject;

public interface ICollideUtils
{
    void initialise();

    CollisionObject newObject();

    CollisionObject newObject(Rectangle rectangle);

    CollisionObject newObject(int x, int y, int width, int height, GraphicID graphicID);

    void tidy();

    boolean canCollide(GdxSprite entity, GdxSprite target);

    boolean filter(short theEntityFlag, short theCollisionBoxFlag);

    TileID getMarkerTileOn(int x, int y);

    int getXBelow(GdxSprite spriteObj);

    int getYBelow(GdxSprite spriteObj);

    CollisionObject getBoxHittingTop(GdxSprite spriteObject);

    CollisionObject getBoxHittingBottom(GdxSprite spriteObject);

    CollisionObject getBoxHittingLeft(GdxSprite spriteObject);

    CollisionObject getBoxHittingRight(GdxSprite spriteObject);
}
