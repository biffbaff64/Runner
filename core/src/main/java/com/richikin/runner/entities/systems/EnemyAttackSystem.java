
package com.richikin.runner.entities.systems;

import com.richikin.enumslib.GraphicID;
import com.richikin.runner.assets.GameAssets;
import com.richikin.runner.core.App;
import com.richikin.runner.entities.objects.GdxSprite;
import com.richikin.runner.entities.objects.SpriteDescriptor;
import com.richikin.runner.graphics.Gfx;

public class EnemyAttackSystem
{
    private final GdxSprite parent;

    public EnemyAttackSystem(GdxSprite _parent)
    {
        this.parent = _parent;
    }

    public void shoot()
    {
        SpriteDescriptor descriptor = new SpriteDescriptor();

        if (parent.gid == GraphicID.G_TURRET)
        {
            descriptor._ASSET       = GameAssets._FIREBALL_BULLET_ASSET;
            descriptor._FRAMES      = GameAssets._FIREBALL_BULLET_FRAMES;
            descriptor._SIZE        = GameAssets.getAssetSize(GraphicID.G_ENEMY_FIREBALL);
        }
        else
        {
            descriptor._ASSET   = GameAssets._PHASER_BULLET_ASSET;
            descriptor._FRAMES  = GameAssets._PHASER_BULLET_FRAMES;
            descriptor._SIZE    = GameAssets.getAssetSize(GraphicID.G_ENEMY_BULLET);
        }

        descriptor._PARENT = parent;
        descriptor._POSITION.x = (int) (parent.sprite.getX() / Gfx.getTileWidth());
        descriptor._POSITION.y = (int) (parent.sprite.getY() / Gfx.getTileHeight());
        descriptor._INDEX = App.entityData.entityMap.size;

//        EnemyBullet enemyBullet = new EnemyBullet(GraphicID.G_ENEMY_BULLET);
//        enemyBullet.initialise(descriptor);
//
//        App.entityData.addEntity(enemyBullet);
    }
}
