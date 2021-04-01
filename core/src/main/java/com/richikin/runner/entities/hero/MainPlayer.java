package com.richikin.runner.entities.hero;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.richikin.enumslib.ActionStates;
import com.richikin.enumslib.GraphicID;
import com.richikin.enumslib.StateID;
import com.richikin.runner.assets.GameAssets;
import com.richikin.runner.core.App;
import com.richikin.runner.core.GameConstants;
import com.richikin.runner.entities.objects.GdxSprite;
import com.richikin.runner.entities.objects.SpriteDescriptor;
import com.richikin.runner.graphics.Gfx;
import com.richikin.utilslib.logging.Meters;
import com.richikin.utilslib.logging.Stats;
import com.richikin.utilslib.logging.Trace;
import com.richikin.utilslib.physics.Movement;
import com.richikin.utilslib.physics.aabb.CollisionRect;

public class MainPlayer extends GdxSprite
{
    private static final float _VIEWBOX_WIDTH  = (Gfx._VIEW_WIDTH + Gfx._VIEW_HALF_WIDTH);
    private static final float _VIEWBOX_HEIGHT = (Gfx._VIEW_HEIGHT + Gfx._VIEW_HALF_HEIGHT);
    private static final float _PLAYER_X_SPEED = 6;
    private static final float _PLAYER_Y_SPEED = 6;

    private TextureRegion[] abxy;
    private CollisionRect   tileRectangle;

    public MainPlayer()
    {
        super(GraphicID.G_PLAYER);
    }

    @Override
    public void initialise(SpriteDescriptor descriptor)
    {
        Trace.__FILE_FUNC();

        create(descriptor);

        bodyCategory = Gfx.CAT_PLAYER;
        collidesWith = Gfx.CAT_WALL | Gfx.CAT_COLLECTIBLE;

        setup(true);
    }

    public void setup(boolean isSpawning)
    {
        Trace.__FILE_FUNC();

        direction.set(Movement._DIRECTION_STILL, Movement._DIRECTION_STILL);
        lookingAt.set(direction);

        strength = GameConstants._MAX_STRENGTH;
    }

    @Override
    public void preUpdate()
    {
        if (App.appState.peek() == StateID._STATE_PAUSED)
        {
            setActionState(ActionStates._PAUSED);
        }

        super.preUpdate();
    }

    @Override
    public void update(int spriteNum)
    {
        updateMainPlayer();

        animate();

        updateCommon();
    }

    private void updateMainPlayer()
    {
    }

    @Override
    public void postUpdate(int spriteNum)
    {
    }

    @Override
    public void animate()
    {
    }

    @Override
    public void draw(SpriteBatch spriteBatch)
    {
        super.draw(spriteBatch);
    }

    @Override
    public void setActionState(ActionStates newAction)
    {
        if (newAction != getActionState())
        {
            switch (newAction)
            {
                case _SPAWNING:
                {
                    SpriteDescriptor descriptor = App.entities.getDescriptor(this.gid);

                    descriptor._ASSET    = GameAssets._PLAYER_SPAWN_ASSET;
                    descriptor._FRAMES   = GameAssets._PLAYER_SPAWN_FRAMES;
                    descriptor._PLAYMODE = Animation.PlayMode.NORMAL;

                    setAnimation(descriptor, 0.3f);

                    elapsedAnimTime = 0;
                }
                break;

                case _STANDING:
                {
                    SpriteDescriptor descriptor = App.entities.getDescriptor(this.gid);

                    descriptor._ASSET    = GameAssets._IDLE_DOWN_ASSET;
                    descriptor._FRAMES   = GameAssets._PLAYER_STAND_FRAMES;
                    descriptor._PLAYMODE = Animation.PlayMode.LOOP;

                    setAnimation(descriptor, 1.0f);

                    elapsedAnimTime = 0;
                }
                break;

                case _PAUSED:
                case _WAITING:
                case _TELEPORTING:
                case _CHANGING_ROOM:
                case _DEAD:
                case _NO_ACTION:
                case _RUNNING:
                case _FIGHTING:
                case _LAST_RITES:
                case _HURT:
                case _DYING:
                case _RIDING:
                {
                }
                break;

                default:
                {
                    Trace.__FILE_FUNC("Unsupported player action: " + newAction);

                    Stats.incMeter(Meters._BAD_PLAYER_ACTION.get());
                }
                break;
            }
        }

        super.setActionState(newAction);
    }
}
