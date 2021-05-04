package com.richikin.runner.entities.characters;

import com.badlogic.gdx.math.MathUtils;
import com.richikin.enumslib.ActionStates;
import com.richikin.enumslib.GraphicID;
import com.richikin.runner.entities.objects.GdxSprite;
import com.richikin.runner.entities.objects.SpriteDescriptor;
import com.richikin.runner.graphics.Gfx;
import com.richikin.utilslib.logging.StopWatch;
import com.richikin.utilslib.logging.Trace;
import com.richikin.utilslib.maths.SimpleVec2F;
import com.richikin.utilslib.physics.Movement;

import java.util.concurrent.TimeUnit;

public class SpikeBlock extends GdxSprite
{
    private SimpleVec2F distanceReset;
    private StopWatch   stopWatch;
    private int         restingTime;
    private SimpleVec2F fastSpeed;
    private SimpleVec2F slowSpeed;

    public SpikeBlock(GraphicID gid)
    {
        super(gid);
    }

    @Override
    public void initialise(SpriteDescriptor descriptor)
    {
        create(descriptor);

        bodyCategory = Gfx.CAT_ENEMY;
        collidesWith = Gfx.CAT_WALL | Gfx.CAT_ENEMY | Gfx.CAT_DOOR | Gfx.CAT_PLAYER | Gfx.CAT_PLAYER_WEAPON;

        isDrawable  = true;
        isAnimating = true;
        isEnemy     = true;

        direction.set(descriptor._DIR);
        distance.set(descriptor._DIST);
        distance.mul(Gfx.getTileWidth(), Gfx.getTileHeight());
        distanceReset = new SimpleVec2F(distance);
        speed.setEmpty();

        if (direction.getX() == Movement._DIRECTION_LEFT)
        {
            isFlippedX = true;
            speed.setX(descriptor._SPEED.getX());
        }
        else if (direction.getX() == Movement._DIRECTION_RIGHT)
        {
            isFlippedX = false;
            speed.setX(descriptor._SPEED.getX());
        }

        if (direction.getY() == Movement._DIRECTION_UP)
        {
            isFlippedY = false;
            speed.setY(descriptor._SPEED.getY());
        }
        else if (direction.getY() == Movement._DIRECTION_DOWN)
        {
            isFlippedY = true;
            speed.setY(descriptor._SPEED.getY());
        }

        animation.setFrameDuration(1.0f / 6f);

        slowSpeed = new SimpleVec2F(speed);
        fastSpeed = new SimpleVec2F(speed.getX() * 2, speed.getY() * 2);

        setActionState(ActionStates._STANDING);
        stopWatch   = StopWatch.start();
        restingTime = (100 + MathUtils.random(100)) * 10;
    }

    @Override
    public void update(int spriteNum)
    {
        switch (getActionState())
        {
            case _STANDING -> {

                if (stopWatch.time(TimeUnit.MILLISECONDS) > restingTime)
                {
                    setActionState(ActionStates._MOVING_OUT);
                    speed.set(fastSpeed);
                }
            }

            case _MOVING_IN,
                _MOVING_OUT -> {

                sprite.translate(speed.getX() * direction.getX(), speed.getY() * direction.getY());
            }

            default -> {

                Trace.__FILE_FUNC("Unsupported actionState: " + getActionState());
            }
        }

        animate();

        updateCommon();
    }
}
