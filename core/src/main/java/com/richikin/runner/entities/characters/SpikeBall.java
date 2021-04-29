package com.richikin.runner.entities.characters;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.richikin.enumslib.ActionStates;
import com.richikin.enumslib.GraphicID;
import com.richikin.runner.core.App;
import com.richikin.runner.entities.objects.GdxSprite;
import com.richikin.runner.entities.objects.SpriteDescriptor;
import com.richikin.runner.graphics.Gfx;
import com.richikin.utilslib.logging.Trace;
import com.richikin.utilslib.maths.SimpleVec2F;

public class SpikeBall extends GdxSprite
{
    private SimpleVec2F distanceReset;

    public SpikeBall(GraphicID gid)
    {
        super(gid);
    }

    @Override
    public void initialise(SpriteDescriptor descriptor)
    {
        Trace.__FILE_FUNC();

        create(descriptor);

        direction.set(descriptor._DIR);
        distance.set(descriptor._DIST);
        distance.mul(Gfx.getTileWidth(), Gfx.getTileHeight());
        distanceReset = new SimpleVec2F(distance);
        speed.set(descriptor._SPEED);

        isDrawable  = true;
        isAnimating = true;
        isEnemy     = true;
        isRotating  = true;
        rotateSpeed = 4.0f;

        setActionState(ActionStates._RUNNING);
    }

    @Override
    public void update(int spriteNum)
    {
        moveBall();

        animate();

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

    private void moveBall()
    {
        if (distance.isEmpty())
        {
            direction.toggle();
            distance.set(distanceReset);
        }
        else
        {
            sprite.translate(speed.getX() * direction.getX(), speed.getY() * direction.getY());
            distance.sub(speed.getX(), speed.getY());
        }
    }
}
