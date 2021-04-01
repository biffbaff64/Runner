
package com.richikin.runner.entities;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.richikin.enumslib.ActionStates;
import com.richikin.enumslib.GraphicID;
import com.richikin.runner.core.App;
import com.richikin.runner.entities.objects.BaseEntity;
import com.richikin.runner.entities.objects.GdxSprite;
import com.richikin.runner.graphics.Gfx;
import com.richikin.utilslib.logging.Trace;
import com.richikin.utilslib.maths.SimpleVec3;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
public class EntityUtils
{
    public EntityUtils()
    {
    }

    public Animation<TextureRegion> createAnimation(String filename, TextureRegion[] destinationFrames, int frameCount, Animation.PlayMode playmode)
    {
        Animation<TextureRegion> animation;

        try
        {
            TextureRegion textureRegion = App.assets.getAnimationRegion(filename);

            TextureRegion[] tmpFrames = textureRegion.split
                (
                    (textureRegion.getRegionWidth() / frameCount),
                    textureRegion.getRegionHeight()
                )[0];

            System.arraycopy(tmpFrames, 0, destinationFrames, 0, frameCount);

            //noinspection MagicNumber
            animation = new Animation<>(0.75f / 6f, tmpFrames);
            animation.setPlayMode(playmode);
        }
        catch (NullPointerException npe)
        {
            Trace.__FILE_FUNC("Craeting animation from " + filename + " failed!");

            animation = null;
        }

        return animation;
    }

    public TextureRegion getKeyFrame(final Animation<? extends TextureRegion> animation, final float elapsedTime, final boolean looping)
    {
        return animation.getKeyFrame(elapsedTime, looping);
    }

    /**
     * Gets a random sprite from {@link EntityData#entityMap}, making
     * sure to not return the specified sprite.
     */
    public GdxSprite getRandomSprite(GdxSprite oneToAvoid)
    {
        GdxSprite randomSprite;

        do
        {
            randomSprite = (GdxSprite) App.entityData.entityMap.get(MathUtils.random(App.entityData.entityMap.size - 1));
        }
        while ((randomSprite.gid == oneToAvoid.gid)
                || (randomSprite.sprite == null)
                || (randomSprite.spriteNumber == oneToAvoid.spriteNumber));

        return randomSprite;
    }

    /**
     * Finds the nearest sprite of type gid to the player.
     */
    public GdxSprite findNearest(GraphicID gid)
    {
        GdxSprite distantSprite = findFirstOf(gid);

        if (distantSprite != null)
        {
            float distance = App.getPlayer().getPosition().dst(distantSprite.getPosition());

            for (BaseEntity entity : App.entityData.entityMap)
            {
                if (entity.gid == gid)
                {
                    GdxSprite gdxSprite = (GdxSprite) entity;

                    float tempDistance = App.getPlayer().getPosition().dst(gdxSprite.getPosition());

                    if (Math.abs(tempDistance) < Math.abs(distance))
                    {
                        distance      = tempDistance;
                        distantSprite = gdxSprite;
                    }
                }
            }
        }

        return distantSprite;
    }

    /**
     * Finds the furthest sprite of type gid to the player.
     */
    public GdxSprite getDistantSprite(@NotNull GdxSprite checkSprite)
    {
        GdxSprite distantSprite = App.getPlayer();

        float distance = checkSprite.getPosition().dst(distantSprite.getPosition());

        for (BaseEntity entity : App.entityData.entityMap)
        {
            GdxSprite gdxSprite = (GdxSprite) entity;

            float tempDistance = checkSprite.getPosition().dst(gdxSprite.getPosition());

            if (Math.abs(tempDistance) > Math.abs(distance))
            {
                distance = tempDistance;
                distantSprite = gdxSprite;
            }
        }

        return distantSprite;
    }

    /**
     * Resets positions for all entities back to
     * their initialisation positions.
     */
    public void resetAllPositions()
    {
        if (App.entityData.entityMap != null)
        {
            GdxSprite entity;

            for (int i = 0; i < App.entityData.entityMap.size; i++)
            {
                entity = (GdxSprite) App.entityData.entityMap.get(i);

                entity.sprite.setPosition(entity.initXYZ.getX(), entity.initXYZ.getY());
            }
        }
    }

    /**
     * Fetch an initial Z position for the specified ID.
     *
     * @param graphicID The GraphicID.
     * @return Z position range is between 0 and Gfx._MAXIMUM_Z_DEPTH.
     */
    public int getInitialZPosition(@NotNull GraphicID graphicID)
    {
        int zed;

        switch (graphicID)
        {
            case G_PRIZE_BALLOON:
            case G_MESSAGE_BUBBLE:
            {
                zed = 10;
            }
            break;

            case G_LASER:
            case G_PLAYER:
            {
                zed = 5;
            }
            break;

            case G_EXPLOSION12:
            case G_EXPLOSION64:
            case G_EXPLOSION128:
            case G_EXPLOSION256:
            {
                zed = 0;
            }
            break;

            default:
            {
                zed = Gfx._MAXIMUM_Z_DEPTH + 1;
            }
            break;
        }

        return zed;
    }

    public boolean isOnScreen(@NotNull GdxSprite spriteObject)
    {
        return App.mapData.viewportBox.overlaps(spriteObject.sprite.getBoundingRectangle());
    }

    public void tidy()
    {
        for (int i = 0; i < App.entityData.entityMap.size; i++)
        {
            if (App.entityData.entityMap.get(i).entityAction == ActionStates._DEAD)
            {
                App.entityData.entityMap.removeIndex(i);
            }
        }
    }

    public GdxSprite findFirstOf(final GraphicID gid)
    {
        GdxSprite gdxSprite = null;

        for (BaseEntity entity : App.entityData.entityMap)
        {
            if (entity.gid == gid)
            {
                gdxSprite = (GdxSprite) entity;
                break;
            }
        }

        return gdxSprite;
    }

    public GdxSprite findLastOf(final GraphicID gid)
    {
        GdxSprite gdxSprite = null;

        for (BaseEntity entity : App.entityData.entityMap)
        {
            if (entity.gid == gid)
            {
                gdxSprite = (GdxSprite) entity;
                break;
            }
        }

        return gdxSprite;
    }

    public boolean canUpdate(@NotNull GraphicID graphicID)
    {
        boolean isUpdateable;

        if (graphicID == GraphicID.G_ARROW)
        {
            isUpdateable = true;
        }
        else
        {
            isUpdateable = isEntityEnabled(graphicID);
        }

        return isUpdateable;
    }

    // TODO: 07/12/2020 - This method is too long, shorten it.
    private boolean isEntityEnabled(@NotNull GraphicID graphicID)
    {
        boolean isEnabled;

        switch (graphicID)
        {
            case G_PLAYER:
            case G_LASER:
            case G_EXPLOSION12:
            case G_EXPLOSION64:
            case G_EXPLOSION128:
            case G_EXPLOSION256:
            case G_MESSAGE_BUBBLE:
            case G_PRIZE_BALLOON:
            {
                isEnabled = true;
            }
            break;

            default:
            {
                isEnabled = false;
            }
            break;
        }

        return isEnabled;
    }

    public SimpleVec3 translateMapPosToEntityWindow(GdxSprite sprite)
    {
        // TODO: 07/12/2020

        return new SimpleVec3();
    }
}
