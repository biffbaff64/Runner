
package com.richikin.runner.entities;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.richikin.enumslib.GraphicID;
import com.richikin.enumslib.TileID;
import com.richikin.runner.assets.GameAssets;
import com.richikin.runner.entities.hero.MainPlayer;
import com.richikin.runner.core.App;
import com.richikin.runner.entities.objects.GdxSprite;
import com.richikin.runner.entities.objects.SpriteDescriptor;
import com.richikin.utilslib.logging.Trace;

public class Entities
{
    public MainPlayer   mainPlayer;

    // TODO: 06/02/2021 - Transfer the information from GameAssets class into this class
    // TODO: 06/02/2021 - This would allow removal of GameAssets.

    public final SpriteDescriptor[] entityList =
        {
            // Main Characters
            new SpriteDescriptor
                (
                    "Player",
                    GraphicID.G_PLAYER, GraphicID._MAIN,
                    GameAssets._PLAYER_IDLE_ASSET, GameAssets._PLAYER_STAND_FRAMES,
                    Animation.PlayMode.LOOP,
                    TileID._PLAYER_TILE
                ),

            // Lasers, Bullets, Explosions, etc

            // Pickups

            // Decorations

            // Interactive

            // Stationary Enemies

            // Mobile Enemies

            // Miscellaneous Enemy Related

            // Background Sprites
        };

    public Entities()
    {
    }

    public int getDescriptorIndex(GraphicID gid)
    {
        int     index      = 0;
        int     defsIndex  = 0;
        boolean foundIndex = false;

        for (SpriteDescriptor descriptor : entityList)
        {
            if (descriptor._GID == gid)
            {
                defsIndex  = index;
                foundIndex = true;
            }

            index++;
        }

        if (!foundIndex)
        {
            Trace.megaDivider("INDEX FOR " + gid + " NOT FOUND!!!");
        }

        return defsIndex;
    }

    public SpriteDescriptor getDescriptor(GraphicID gid)
    {
        return entityList[getDescriptorIndex(gid)];
    }

    public void setAllEnemyStatuses()
    {
        boolean isEnemy;

        for (int i = 0; i < App.entityData.entityMap.size; i++)
        {
            switch (App.entityData.entityMap.get(i).gid)
            {
                case G_BACKGROUND_UFO:
                case G_TWINKLE_STAR:
                case G_PRIZE_BALLOON:
                case G_MESSAGE_BUBBLE:
                case G_ROVER_BULLET:
                case G_TRANSPORTER:
                case G_ROVER_GUN:
                case G_ROVER_GUN_BARREL:
                case G_ROVER:
                case G_ROVER_BOOT:
                case G_ROVER_WHEEL:
                case G_LASER:
                case G_BOMB:
                case G_PLAYER:
                case G_EXPLOSION12:
                case G_EXPLOSION64:
                case G_EXPLOSION128:
                case G_EXPLOSION256:
                {
                    isEnemy = false;
                }
                break;

                default:
                {
                    isEnemy = true;
                }
                break;
            }

            ((GdxSprite) App.entityData.entityMap.get(i)).isEnemy = isEnemy;
        }
    }
}
