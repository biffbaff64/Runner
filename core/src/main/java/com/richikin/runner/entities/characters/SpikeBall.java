package com.richikin.runner.entities.characters;

import com.badlogic.gdx.Gdx;
import com.richikin.enumslib.ActionStates;
import com.richikin.enumslib.GraphicID;
import com.richikin.runner.core.App;
import com.richikin.runner.entities.objects.GdxSprite;
import com.richikin.runner.entities.objects.SpriteDescriptor;
import com.richikin.utilslib.logging.Trace;

public class SpikeBall extends GdxSprite
{
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
        animate();

        updateCommon();
    }

    @Override
    public void animate()
    {
        elapsedAnimTime += Gdx.graphics.getDeltaTime();
        sprite.setRegion(App.entityUtils.getKeyFrame(animation, elapsedAnimTime, true));
    }
}
