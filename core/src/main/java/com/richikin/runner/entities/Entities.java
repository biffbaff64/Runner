
package com.richikin.runner.entities;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.richikin.enumslib.GraphicID;
import com.richikin.enumslib.TileID;
import com.richikin.runner.assets.GameAssets;
import com.richikin.runner.core.App;
import com.richikin.runner.entities.hero.MainPlayer;
import com.richikin.runner.entities.objects.GdxSprite;
import com.richikin.runner.entities.objects.SpriteDescriptor;
import com.richikin.utilslib.logging.Trace;
import com.richikin.utilslib.maths.Vec2;

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
                    GraphicID.G_PLAYER, GraphicID._MAIN, TileID._PLAYER_TILE,
                    GameAssets._IDLE_DOWN_ASSET, GameAssets._PLAYER_STAND_FRAMES,
                    new Vec2(192, 192),
                    Animation.PlayMode.LOOP
                ),

            // Other Characters
            new SpriteDescriptor
                (
                    "Prisoner",
                    GraphicID.G_PRISONER, GraphicID._MAIN, TileID._PRISONER_TILE,
                    GameAssets._PRISONER_IDLE_DOWN_ASSET, GameAssets._PRISONER_IDLE_FRAMES,
                    new Vec2(128, 128),
                    Animation.PlayMode.LOOP

                ),

            new SpriteDescriptor
                (
                    "Villager",
                    GraphicID.G_VILLAGER, GraphicID._MAIN, TileID._VILLAGER_TILE,
                    GameAssets._VILLAGER_IDLE_DOWN_ASSET, GameAssets._VILLAGER_IDLE_FRAMES,
                    new Vec2(128, 128),
                    Animation.PlayMode.LOOP

                ),

            // Lasers, Bullets, Explosions, etc

            // Pickups

            // Decorations
            new SpriteDescriptor
                (
                    "Alcove Torch",
                    GraphicID.G_ALCOVE_TORCH, GraphicID._DECORATION, TileID._ALCOVE_TORCH_TILE,
                    GameAssets._ALCOVE_TORCH_ASSET, GameAssets._ALCOVE_TORCH_FRAMES,
                    new Vec2(64, 192),
                    Animation.PlayMode.LOOP
                ),

            new SpriteDescriptor
                (
                    "Barrel",
                    GraphicID.G_BARREL, GraphicID._DECORATION, TileID._BARREL_TILE,
                    GameAssets._BARREL_1_ASSET, GameAssets._BARREL_FRAMES,
                    new Vec2(90, 152),
                    Animation.PlayMode.NORMAL

                ),

            // Interactive

            // Stationary Enemies

            // Mobile Enemies
            new SpriteDescriptor
                (
                    "Soldier",
                    GraphicID.G_SOLDIER, GraphicID._ENEMY, TileID._SOLDIER_TILE,
                    GameAssets._SOLDIER_IDLE_DOWN_ASSET, GameAssets._SOLDIER_IDLE_FRAMES,
                    new Vec2(148, 148),
                    Animation.PlayMode.LOOP

                ),

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
                case G_PRIZE_BALLOON, G_MESSAGE_BUBBLE, G_LASER, G_PLAYER,
                    G_EXPLOSION12, G_EXPLOSION64, G_EXPLOSION128, G_EXPLOSION256 -> {

                    isEnemy = false;
                }

                default -> {

                    isEnemy = true;
                }
            }

            ((GdxSprite) App.entityData.entityMap.get(i)).isEnemy = isEnemy;
        }
    }
}
