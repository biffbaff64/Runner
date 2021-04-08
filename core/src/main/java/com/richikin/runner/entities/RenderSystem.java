package com.richikin.runner.entities;

import com.richikin.runner.config.Settings;
import com.richikin.runner.core.App;
import com.richikin.runner.entities.objects.GdxSprite;
import com.richikin.runner.graphics.Gfx;
import com.richikin.utilslib.logging.Trace;
import com.richikin.utilslib.physics.Movement;
import org.jetbrains.annotations.NotNull;

public class RenderSystem
{
    public RenderSystem()
    {
    }

    /**
     * Draw all sprites to the scene.
     * Uses a Z-coord system: 0 at front, MAX at rear.
     * Any sprites with a Z value > MAX will not be drawn.
     */
    public void drawSprites()
    {
        if (App.entityManager.isEntityUpdateAllowed())
        {
            GdxSprite entity;

            for (int z = Gfx._MAXIMUM_Z_DEPTH - 1; z >= 0; z--)
            {
                for (int i = 0; i < App.entityData.entityMap.size; i++)
                {
                    entity = (GdxSprite) App.entityData.entityMap.get(i);

                    if (entity != null)
                    {
                        if (entity.position.z == z)
                        {
                            entity.preDraw();

                            Trace.__FILE_FUNC("isDrawable: " + entity.isDrawable + ", isInViewWindow: " + isInViewWindow(entity));

                            if (isInViewWindow(entity) && entity.isDrawable)
                            {
                                entity.draw(App.spriteBatch);
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Draw non-game sprites which exist in the background layers.
     * These can be the twinkling stars, ufos, or anything else
     * which is animating.
     */
    public void drawBackgroundSprites()
    {
    }

    /**
     * Checks for the supplied sprite being inside
     * the scene window.
     *
     * @param sprObj The sprite to check.
     * @return TRUE if inside the window.
     */
    public boolean isInViewWindow(@NotNull GdxSprite sprObj)
    {
        if (App.settings.isEnabled(Settings._CULL_SPRITES))
        {
            return App.mapData.viewportBox.overlaps(sprObj.sprite.getBoundingRectangle());
        }

        return true;
    }

    /**
     * Relocate enemy entities when crossing the map loop boundary.
     */
    public void relocateSprites()
    {
        GdxSprite entity;

        for (int i = 0; i < App.entityData.entityMap.size; i++)
        {
            entity = (GdxSprite) App.entityData.entityMap.get(i);

            if (EntityManager.enemies.contains(entity.gid, true))
            {
                final int _WRAP_POINT_LEFT = 1600;
                final int _WRAP_POINT_RIGHT = 14400;

                if (entity.sprite.getX() < _WRAP_POINT_LEFT)
                {
                    entity.sprite.translateX((Gfx.getMapWidth() - Gfx._VIEW_WIDTH) * Movement._DIRECTION_RIGHT);
                }
                else
                {
                    if (entity.sprite.getX() > _WRAP_POINT_RIGHT)
                    {
                        entity.sprite.translateX((Gfx.getMapWidth() - Gfx._VIEW_WIDTH) * Movement._DIRECTION_LEFT);
                    }
                }
            }
        }
    }
}
