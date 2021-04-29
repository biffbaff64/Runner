package com.richikin.runner.entities.characters;

import com.badlogic.gdx.Gdx;
import com.richikin.enumslib.GraphicID;
import com.richikin.runner.core.App;
import com.richikin.runner.entities.objects.GdxSprite;
import com.richikin.runner.entities.objects.SpriteDescriptor;
import com.richikin.runner.graphics.Gfx;

public class Decoration extends GdxSprite
{
    private boolean isMoveable;

    public Decoration(GraphicID gid)
    {
        super(gid);
    }

    @Override
    public void initialise(SpriteDescriptor descriptor)
    {
        create(descriptor);

        if (collisionObject != null)
        {
            bodyCategory = Gfx.CAT_DECORATION;
            collidesWith = Gfx.CAT_PLAYER
                | Gfx.CAT_ENEMY
                | Gfx.CAT_WEAPON
                | Gfx.CAT_OBSTACLE
                | Gfx.CAT_ENTITY_BARRIER
                | Gfx.CAT_DOOR;
        }

        if (isAnimating = (descriptor._FRAMES > 1))
        {
            animation.setFrameDuration(descriptor._ANIM_RATE);
        }

        isDrawable = true;
        isMoveable = ((gid == GraphicID.G_BARREL) || (gid == GraphicID.G_CRATE) || (gid == GraphicID.G_POT));
    }

    @Override
    public void update(int spriteNum)
    {
        animate();

        if (this.isMoveable)
        {
            if (!distance.isEmpty())
            {
                sprite.translate(speed.getX() * direction.getX(), speed.getY() * direction.getY());
                distance.sub(speed.getX(), speed.getY());
            }
        }

        updateCommon();
    }

    @Override
    public void animate()
    {
        if (isAnimating)
        {
            elapsedAnimTime += Gdx.graphics.getDeltaTime();
            sprite.setRegion(App.entityUtils.getKeyFrame(animation, elapsedAnimTime, true));
        }
    }
}
