package com.richikin.runner.entities.characters;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.richikin.enumslib.ActionStates;
import com.richikin.enumslib.GraphicID;
import com.richikin.runner.assets.GameAssets;
import com.richikin.runner.core.App;
import com.richikin.runner.entities.objects.BoundedEnemy;
import com.richikin.runner.entities.objects.GdxSprite;
import com.richikin.runner.entities.objects.SpriteDescriptor;
import com.richikin.runner.graphics.Gfx;
import com.richikin.utilslib.logging.StopWatch;
import com.richikin.utilslib.logging.Trace;
import com.richikin.utilslib.maths.SimpleVec2F;
import com.richikin.utilslib.physics.Movement;

import java.util.concurrent.TimeUnit;

public class BigBlock extends BoundedEnemy
{
    private static final float _SPEED = 4.0f;

    private TextureRegion coverImage;
    private SimpleVec2F   coverPos;
    private StopWatch     stopWatch;
    private int           restingTime;

    public BigBlock(GraphicID gid)
    {
        super(gid);
    }

    @Override
    public void initialise(SpriteDescriptor descriptor)
    {
        create(descriptor);

        bodyCategory = Gfx.CAT_MOBILE_ENEMY;
        collidesWith = Gfx.CAT_PLAYER | Gfx.CAT_COLLECTIBLE | Gfx.CAT_MOBILE_ENEMY | Gfx.CAT_WEAPON;

        setActionState(ActionStates._STANDING);

        direction.set(descriptor._DIR);
        distance.set
            (
                descriptor._DIST.getX() * Gfx.getTileWidth(),
                descriptor._DIST.getY() * Gfx.getTileHeight()
            );

        speed.setEmpty();

        if (direction.hasXDirection())
        {
            speed.setX(_SPEED);
            setHorizontalMovementBounds();
            coverPos = new SimpleVec2F(initXYZ.getX(), initXYZ.getY());

            if (direction.getX() == Movement._DIRECTION_LEFT)
            {
                isFlippedX = false;
                coverImage = App.getAssets().getObjectRegion(GameAssets._BLOCK_COVER_LEFT);
            }
            else if (direction.getX() == Movement._DIRECTION_RIGHT)
            {
                isFlippedX = true;
                coverImage = App.getAssets().getObjectRegion(GameAssets._BLOCK_COVER_RIGHT);
            }
        }

        if (direction.hasYDirection())
        {
            speed.setY(_SPEED);
            setVerticalMovementBounds();

            coverPos   = new SimpleVec2F(initXYZ.getX(), initXYZ.getY());
            coverImage = App.getAssets().getObjectRegion(GameAssets._BLOCK_COVER_BOTTOM);
            isFlippedY = (direction.getY() == Movement._DIRECTION_DOWN);
        }

        stopWatch = StopWatch.start();
        restingTime = (100 + MathUtils.random(100)) * 10;
    }

    @Override
    public void update(int spriteNum)
    {
        switch (getActionState())
        {
            case _STANDING:
            {
                if (stopWatch.time(TimeUnit.MILLISECONDS) > restingTime)
                {
                    setActionState(ActionStates._MOVING_OUT);
                }
            }
            break;

            case _MOVING_IN:
            case _MOVING_OUT:
            {
                checkMovementBounds();

                sprite.translate(speed.getX() * direction.getX(), speed.getY() * direction.getY());

                distance.subXMinZero(speed.getX());
                distance.subYMinZero(speed.getY());
            }
            break;

            default:
            {
                Trace.__FILE_FUNC("Unsupported spriteAction: " + getActionState());
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
    public void onMovementBoundsTurn(int edgeSide)
    {
        setActionState(ActionStates._STANDING);
        stopWatch.reset();
    }

    @Override
    public void draw(SpriteBatch spriteBatch)
    {
        super.draw(spriteBatch);

        if (coverImage != null)
        {
            spriteBatch.draw(coverImage, coverPos.getX(), coverPos.getY());
        }
    }
}
