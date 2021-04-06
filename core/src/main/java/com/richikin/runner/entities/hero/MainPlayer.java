package com.richikin.runner.entities.hero;

import com.badlogic.gdx.Gdx;
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
import com.richikin.utilslib.maths.Box;
import com.richikin.utilslib.maths.SimpleVec2F;
import com.richikin.utilslib.physics.DirectionAnim;
import com.richikin.utilslib.physics.Movement;
import com.richikin.utilslib.physics.aabb.CollisionRect;

public class MainPlayer extends GdxSprite
{
    private static final float _VIEWBOX_WIDTH  = (Gfx._VIEW_WIDTH + Gfx._VIEW_HALF_WIDTH);
    private static final float _VIEWBOX_HEIGHT = (Gfx._VIEW_HEIGHT + Gfx._VIEW_HALF_HEIGHT);
    private static final float _PLAYER_X_SPEED = 6;
    private static final float _PLAYER_Y_SPEED = 6;

    public SimpleVec2F maxMoveSpeed;
    public Box         viewBox;
    public boolean     isOnPlatform;
    public boolean     isOnFloorButton;
    public boolean     isPossessed;
    public boolean     canOfferButton;
    public boolean     canOpenMessagePanel;
    public boolean     isHurting;
    public boolean     isGrabbing;
    public boolean     isCasting;
    public boolean     isShooting;
    public boolean     isMovingX;
    public boolean     isMovingY;

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

        isHurting           = false;
        isShooting          = false;
        isGrabbing          = false;
        isCasting           = false;
        isOnPlatform        = false;
        isOnFloorButton     = false;
        isPossessed         = false;
        canOfferButton      = false;
        canOpenMessagePanel = false;

        viewBox       = new Box();
        maxMoveSpeed  = new SimpleVec2F();
        tileRectangle = new CollisionRect(this.gid);

        TextureRegion abxyTexture = App.assets.getAnimationRegion(GameAssets._ABXY_ASSET);
        abxy = new TextureRegion[4];

        TextureRegion[] tmpFrames = abxyTexture.split
            (
                (abxyTexture.getRegionWidth() / 4),
                abxyTexture.getRegionHeight()
            )[0];

        setup(true);
    }

    public void setup(boolean isSpawning)
    {
        Trace.__FILE_FUNC();

        direction.set(Movement._DIRECTION_STILL, Movement._DIRECTION_STILL);
        lookingAt.set(direction);

        maxMoveSpeed.set(_PLAYER_X_SPEED, _PLAYER_Y_SPEED);
        strength = GameConstants._MAX_STRENGTH;

        isMovingX           = false;
        isMovingY           = false;
        isRotating          = false;
        isFlippedX          = false;
        isFlippedY          = false;
        isHurting           = false;
        isShooting          = false;
        isCasting           = false;
        isGrabbing          = false;
        isDrawable          = true;
        canOpenMessagePanel = true;

        this.sprite.setRotation(0);
        this.sprite.setPosition(initXYZ.getX(), initXYZ.getY());
        collisionObject.clearCollision();

        setActionState(isSpawning ? ActionStates._SPAWNING : ActionStates._STANDING);
    }

    @Override
    public void preUpdate()
    {
        if (getActionState() == ActionStates._RESTARTING)
        {
            this.sprite.setPosition(App.mapData.checkPoint.getX(), App.mapData.checkPoint.getY());
            setActionState(ActionStates._SPAWNING);
        }

        super.preUpdate();
    }

    @Override
    public void update(int spriteNum)
    {
        if (App.appState.peek() == StateID._STATE_PAUSED)
        {
            setActionState(ActionStates._PAUSED);
        }

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
        final DirectionAnim[] runningAnims =
            {
                new DirectionAnim(Movement._DIRECTION_LEFT, Movement._DIRECTION_UP,     GameAssets._RUN_UP_LEFT_ASSET),
                new DirectionAnim(Movement._DIRECTION_LEFT, Movement._DIRECTION_DOWN,   GameAssets._RUN_DOWN_LEFT_ASSET),
                new DirectionAnim(Movement._DIRECTION_LEFT, Movement._DIRECTION_STILL,  GameAssets._RUN_LEFT_ASSET),

                new DirectionAnim(Movement._DIRECTION_RIGHT, Movement._DIRECTION_UP,    GameAssets._RUN_UP_RIGHT_ASSET),
                new DirectionAnim(Movement._DIRECTION_RIGHT, Movement._DIRECTION_DOWN,  GameAssets._RUN_DOWN_RIGHT_ASSET),
                new DirectionAnim(Movement._DIRECTION_RIGHT, Movement._DIRECTION_STILL, GameAssets._RUN_RIGHT_ASSET),

                new DirectionAnim(Movement._DIRECTION_STILL, Movement._DIRECTION_UP,    GameAssets._RUN_UP_ASSET),
                new DirectionAnim(Movement._DIRECTION_STILL, Movement._DIRECTION_DOWN,  GameAssets._RUN_DOWN_ASSET),
            };

        final DirectionAnim[] idleAnims =
            {
                new DirectionAnim(Movement._DIRECTION_LEFT, Movement._DIRECTION_UP,     GameAssets._IDLE_UP_LEFT_ASSET),
                new DirectionAnim(Movement._DIRECTION_LEFT, Movement._DIRECTION_DOWN,   GameAssets._IDLE_DOWN_LEFT_ASSET),
                new DirectionAnim(Movement._DIRECTION_LEFT, Movement._DIRECTION_STILL,  GameAssets._IDLE_LEFT_ASSET),

                new DirectionAnim(Movement._DIRECTION_RIGHT, Movement._DIRECTION_UP,    GameAssets._IDLE_UP_RIGHT_ASSET),
                new DirectionAnim(Movement._DIRECTION_RIGHT, Movement._DIRECTION_DOWN,  GameAssets._IDLE_DOWN_RIGHT_ASSET),
                new DirectionAnim(Movement._DIRECTION_RIGHT, Movement._DIRECTION_STILL, GameAssets._IDLE_RIGHT_ASSET),

                new DirectionAnim(Movement._DIRECTION_STILL, Movement._DIRECTION_UP,    GameAssets._IDLE_UP_ASSET),
                new DirectionAnim(Movement._DIRECTION_STILL, Movement._DIRECTION_DOWN,  GameAssets._IDLE_DOWN_ASSET),
            };

        final DirectionAnim[] fightAnims =
            {
                new DirectionAnim(Movement._DIRECTION_LEFT, Movement._DIRECTION_UP,     GameAssets._FIGHT_UP_LEFT_ASSET),
                new DirectionAnim(Movement._DIRECTION_LEFT, Movement._DIRECTION_DOWN,   GameAssets._FIGHT_DOWN_LEFT_ASSET),
                new DirectionAnim(Movement._DIRECTION_LEFT, Movement._DIRECTION_STILL,  GameAssets._FIGHT_LEFT_ASSET),

                new DirectionAnim(Movement._DIRECTION_RIGHT, Movement._DIRECTION_UP,    GameAssets._FIGHT_UP_RIGHT_ASSET),
                new DirectionAnim(Movement._DIRECTION_RIGHT, Movement._DIRECTION_DOWN,  GameAssets._FIGHT_DOWN_RIGHT_ASSET),
                new DirectionAnim(Movement._DIRECTION_RIGHT, Movement._DIRECTION_STILL, GameAssets._FIGHT_RIGHT_ASSET),

                new DirectionAnim(Movement._DIRECTION_STILL, Movement._DIRECTION_UP,    GameAssets._FIGHT_UP_ASSET),
                new DirectionAnim(Movement._DIRECTION_STILL, Movement._DIRECTION_DOWN,  GameAssets._FIGHT_DOWN_ASSET),
            };

        final DirectionAnim[] dyingAnims =
            {
                new DirectionAnim(Movement._DIRECTION_LEFT, Movement._DIRECTION_UP,     GameAssets._DYING_UP_LEFT_ASSET),
                new DirectionAnim(Movement._DIRECTION_LEFT, Movement._DIRECTION_DOWN,   GameAssets._DYING_DOWN_LEFT_ASSET),
                new DirectionAnim(Movement._DIRECTION_LEFT, Movement._DIRECTION_STILL,  GameAssets._DYING_LEFT_ASSET),

                new DirectionAnim(Movement._DIRECTION_RIGHT, Movement._DIRECTION_UP,    GameAssets._DYING_UP_RIGHT_ASSET),
                new DirectionAnim(Movement._DIRECTION_RIGHT, Movement._DIRECTION_DOWN,  GameAssets._DYING_DOWN_RIGHT_ASSET),
                new DirectionAnim(Movement._DIRECTION_RIGHT, Movement._DIRECTION_STILL, GameAssets._DYING_RIGHT_ASSET),

                new DirectionAnim(Movement._DIRECTION_STILL, Movement._DIRECTION_UP,    GameAssets._DYING_UP_ASSET),
                new DirectionAnim(Movement._DIRECTION_STILL, Movement._DIRECTION_DOWN,  GameAssets._DYING_DOWN_ASSET),
            };

        switch (getActionState())
        {
            case _RUNNING:
            {
                SpriteDescriptor descriptor = new SpriteDescriptor();

                descriptor._FRAMES   = GameAssets._PLAYER_RUN_FRAMES;
                descriptor._PLAYMODE = Animation.PlayMode.LOOP;
                descriptor._SIZE     = GameAssets.getAssetSize(GraphicID.G_PLAYER);

                String asset = runningAnims[0].animation;

                for (DirectionAnim directionAnim : runningAnims)
                {
                    if ((direction.getX() == directionAnim.dirX)
                        && (direction.getY() == directionAnim.dirY))
                    {
                        asset = directionAnim.animation;
                    }
                }

                descriptor._ASSET = asset;

                setAnimation(descriptor, 0.5f);
            }
            break;

            case _PAUSED:
            case _WAITING:
            case _STANDING:
            case _RIDING:
            {
                SpriteDescriptor descriptor = new SpriteDescriptor();

                descriptor._FRAMES   = GameAssets._PLAYER_STAND_FRAMES;
                descriptor._PLAYMODE = Animation.PlayMode.LOOP;
                descriptor._SIZE     = GameAssets.getAssetSize(GraphicID.G_PLAYER);

                String asset = idleAnims[0].animation;

                for (DirectionAnim directionAnim : idleAnims)
                {
                    if ((lookingAt.getX() == directionAnim.dirX)
                        && (lookingAt.getY() == directionAnim.dirY))
                    {
                        asset = directionAnim.animation;
                    }
                }

                descriptor._ASSET = asset;

                setAnimation(descriptor, 1.0f);
            }
            break;

            case _FIGHTING:
            {
                SpriteDescriptor descriptor = new SpriteDescriptor();

                descriptor._FRAMES   = GameAssets._PLAYER_FIGHT_FRAMES;
                descriptor._PLAYMODE = Animation.PlayMode.LOOP_PINGPONG;
                descriptor._SIZE     = GameAssets.getAssetSize(GraphicID.G_PLAYER_FIGHT);

                String asset = fightAnims[0].animation;

                for (DirectionAnim directionAnim : fightAnims)
                {
                    if ((lookingAt.getX() == directionAnim.dirX)
                        && (lookingAt.getY() == directionAnim.dirY))
                    {
                        asset = directionAnim.animation;
                    }
                }

                descriptor._ASSET = asset;

                setAnimation(descriptor, 0.5f);
            }
            break;

            case _CHANGING_ROOM:
            case _SPAWNING:
            case _HURT:
            {
            }
            break;

            case _LAST_RITES:
            case _DYING:
            {
                SpriteDescriptor descriptor = new SpriteDescriptor();

                descriptor._FRAMES   = GameAssets._PLAYER_DYING_FRAMES;
                descriptor._PLAYMODE = Animation.PlayMode.NORMAL;
                descriptor._SIZE     = GameAssets.getAssetSize(GraphicID.G_PLAYER_FIGHT);

                String asset = dyingAnims[0].animation;

                for (DirectionAnim directionAnim : dyingAnims)
                {
                    if ((lookingAt.getX() == directionAnim.dirX)
                        && (lookingAt.getY() == directionAnim.dirY))
                    {
                        asset = directionAnim.animation;
                    }
                }

                descriptor._ASSET = asset;

                setAnimation(descriptor, 0.5f);
            }
            break;

            default:
            {
                Trace.__FILE_FUNC_WithDivider();
                Trace.dbg("Unsupported player action: " + getActionState());

                Stats.incMeter(Meters._BAD_PLAYER_ACTION.get());
            }
            break;
        }

        sprite.setRegion(App.entityUtils.getKeyFrame(animation, elapsedAnimTime, true));
        elapsedAnimTime += Gdx.graphics.getDeltaTime();
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
