package com.richikin.runner.entities.characters;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.richikin.enumslib.ActionStates;
import com.richikin.enumslib.GraphicID;
import com.richikin.runner.core.App;
import com.richikin.runner.entities.objects.BoundedEnemy;
import com.richikin.runner.entities.objects.GdxSprite;
import com.richikin.runner.entities.objects.GenericCollisionListener;
import com.richikin.runner.entities.objects.SpriteDescriptor;
import com.richikin.runner.graphics.Gfx;
import com.richikin.utilslib.logging.StopWatch;
import com.richikin.utilslib.logging.Trace;
import com.richikin.utilslib.maths.SimpleVec2F;
import com.richikin.utilslib.physics.Movement;
import com.richikin.utilslib.physics.aabb.ICollisionListener;

import java.util.concurrent.TimeUnit;

public class SpikeBall extends BoundedEnemy
{
    private SimpleVec2F distanceReset;
    private StopWatch stopWatch;
    private int restingTime;

    public SpikeBall(GraphicID gid)
    {
        super(gid);
    }

    @Override
    public void initialise(SpriteDescriptor descriptor)
    {
        Trace.__FILE_FUNC();

        create(descriptor);

        bodyCategory = Gfx.CAT_ENEMY;
        collidesWith = Gfx.CAT_WALL | Gfx.CAT_ENEMY | Gfx.CAT_DOOR | Gfx.CAT_PLAYER | Gfx.CAT_PLAYER_WEAPON;

        isDrawable  = true;
        isAnimating = true;
        isEnemy     = true;
        isRotating  = true;
        rotateSpeed = 6.0f;

        direction.set(descriptor._DIR);
        distance.set(descriptor._DIST);
        distance.mul(Gfx.getTileWidth(), Gfx.getTileHeight());
        distanceReset = new SimpleVec2F(distance);
        speed.set(descriptor._SPEED);

        setActionState(ActionStates._RUNNING);
        stopWatch = StopWatch.start();
        restingTime = 1000;
    }

    @Override
    public void update(int spriteNum)
    {
        switch (getActionState())
        {
            case _STANDING:
            {
                setActionState(ActionStates._RUNNING);
            }
            break;

            case _PAUSED:
            {
                if (stopWatch.time(TimeUnit.MILLISECONDS) > restingTime)
                {
                    setActionState(ActionStates._STANDING);

                    speed.setX(direction.getX() == Movement._DIRECTION_STILL ? 0 : 4f + MathUtils.random(2f));
                    speed.setY(direction.getY() == Movement._DIRECTION_STILL ? 0 : 4f + MathUtils.random(2f));
                }
            }
            break;

            case _RUNNING:
            {
                moveBall();
            }
            break;
        }

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

    @Override
    public void addCollisionListener(ICollisionListener listener)
    {
        super.addCollisionListener(new GenericCollisionListener(this));
    }

    private void moveBall()
    {
        if (distance.isEmpty())
        {
            direction.toggle();
            distance.set(distanceReset);

            stopWatch.reset();
            restingTime = 1000 + (MathUtils.random(5) * 100);

            setActionState(ActionStates._PAUSED);
        }
        else
        {
            sprite.translate(speed.getX() * direction.getX(), speed.getY() * direction.getY());
            distance.sub(speed.getX(), speed.getY());
        }
    }
}
