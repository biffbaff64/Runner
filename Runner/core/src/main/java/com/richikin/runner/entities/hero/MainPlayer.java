package com.richikin.runner.entities.hero;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.richikin.enumslib.ActionStates;
import com.richikin.enumslib.GraphicID;
import com.richikin.enumslib.StateID;
import com.richikin.runner.assets.GameAssets;
import com.richikin.runner.config.Settings;
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

    private static final int _ABXY_A = 0;
    private static final int _ABXY_B = 1;
    private static final int _ABXY_X = 2;
    private static final int _ABXY_Y = 3;

    public ButtonInputHandler  buttons;
    public CollisionHandler    collision;
    public ActionButtonHandler actionButton;
    //    public PlayerBulletManager bulletManager;
    public SimpleVec2F         maxMoveSpeed;
    public GdxSprite           platformSprite;

    public Box     viewBox;
    public boolean isOnPlatform;
    public boolean isOnFloorButton;
    public boolean isPossessed;
    public boolean canOfferButton;
    public boolean canOpenMessagePanel;
    public boolean isHurting;
    public boolean isGrabbing;
    public boolean isCasting;
    public boolean isShooting;
    public boolean isMovingX;
    public boolean isMovingY;

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
        collidesWith = Gfx.CAT_MOBILE_ENEMY
            | Gfx.CAT_FIXED_ENEMY
            | Gfx.CAT_ENEMY_WEAPON
            | Gfx.CAT_COLLECTIBLE
            | Gfx.CAT_OBSTACLE
            | Gfx.CAT_EXIT_BOX
            | Gfx.CAT_INTERACTIVE
            | Gfx.CAT_PLATFORM
            | Gfx.CAT_VILLAGER
            | Gfx.CAT_DOOR;

        isHurting           = false;
        isShooting          = false;
        isGrabbing          = false;
        isCasting           = false;
        isOnPlatform        = false;
        isOnFloorButton     = false;
        isPossessed         = false;
        canOfferButton      = false;
        canOpenMessagePanel = false;

        buttons       = new ButtonInputHandler();
        collision     = new CollisionHandler();
        actionButton  = new ActionButtonHandler();
        viewBox       = new Box();
        maxMoveSpeed  = new SimpleVec2F();
        tileRectangle = new CollisionRect(this.gid);

        TextureRegion abxyTexture = App.getAssets().getAnimationRegion(GameAssets._ABXY_ASSET);
        abxy = new TextureRegion[4];

        TextureRegion[] tmpFrames = abxyTexture.split
            (
                (abxyTexture.getRegionWidth() / 4),
                abxyTexture.getRegionHeight()
            )[0];

        System.arraycopy(tmpFrames, 0, abxy, 0, 4);

        setup(false);
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
        canFlip             = false;
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
        if (App.getAppState().peek() == StateID._STATE_PAUSED)
        {
            setActionState(ActionStates._PAUSED);
        }

        updateMainPlayer();

        animate();

        updateCommon();
    }

    @Override
    public void postUpdate(int spriteNum)
    {
        super.postUpdate(spriteNum);

        isOnFloorButton = collision.isNextTo(GraphicID.G_FLOOR_BUTTON);
    }

    private void updateMainPlayer()
    {
        switch (getActionState())
        {
            case _DYING:
            case _EXPLODING:
            case _RESETTING:
            case _RESTARTING:
            case _WAITING:
            case _DEAD:
            case _PAUSED:
            case _KILLED:
            case _CHANGING_ROOM:
            {
            }
            break;

            case _STANDING:
            {
                if (direction.hasDirection())
                {
                    lookingAt.set(direction);
                }

                buttons.checkButtons();
            }
            break;

            case _HURT:
            {
                isHurting = false;
                setActionState(ActionStates._STANDING);
            }
            break;

            case _RIDING:
            case _RUNNING:
            {
                lookingAt.set(direction);

                buttons.checkButtons();

                movePlayer();
            }
            break;

            case _SPAWNING:
            case _FIGHTING:
            {
                if (animation.isAnimationFinished(elapsedAnimTime))
                {
                    setActionState(ActionStates._STANDING);
                }
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
    }

    private void movePlayer()
    {
        if (isMovingX)
        {
            speed.setX(_PLAYER_X_SPEED);
        }
        else
        {
            speed.setX(0);
        }

        if (isMovingY)
        {
            speed.setY(_PLAYER_Y_SPEED);
        }
        else
        {
            speed.setY(0);
        }

        if (isMovingX || isMovingY)
        {
            sprite.translate
                (
                    (speed.getX() * App.inputManager.getControllerXPercentage()),
                    (speed.getY() * App.inputManager.getControllerYPercentage())
                );

            if (getActionState() != ActionStates._CHANGING_ROOM)
            {
                handleRoomExit();
            }
        }
    }

    private void handleRoomExit()
    {
    }

    public void hurt()
    {
    }

    public void kill()
    {
    }

    public void handleDying()
    {
    }

    @Override
    public void animate()
    {
        final DirectionAnim[] runningAnims =
            {
                new DirectionAnim(Movement._DIRECTION_LEFT, Movement._DIRECTION_UP, GameAssets._RUN_UP_LEFT_ASSET),
                new DirectionAnim(Movement._DIRECTION_LEFT, Movement._DIRECTION_DOWN, GameAssets._RUN_DOWN_LEFT_ASSET),
                new DirectionAnim(Movement._DIRECTION_LEFT, Movement._DIRECTION_STILL, GameAssets._RUN_LEFT_ASSET),

                new DirectionAnim(Movement._DIRECTION_RIGHT, Movement._DIRECTION_UP, GameAssets._RUN_UP_RIGHT_ASSET),
                new DirectionAnim(Movement._DIRECTION_RIGHT, Movement._DIRECTION_DOWN, GameAssets._RUN_DOWN_RIGHT_ASSET),
                new DirectionAnim(Movement._DIRECTION_RIGHT, Movement._DIRECTION_STILL, GameAssets._RUN_RIGHT_ASSET),

                new DirectionAnim(Movement._DIRECTION_STILL, Movement._DIRECTION_UP, GameAssets._RUN_UP_ASSET),
                new DirectionAnim(Movement._DIRECTION_STILL, Movement._DIRECTION_DOWN, GameAssets._RUN_DOWN_ASSET),
            };

        final DirectionAnim[] idleAnims =
            {
                new DirectionAnim(Movement._DIRECTION_LEFT, Movement._DIRECTION_UP, GameAssets._IDLE_UP_LEFT_ASSET),
                new DirectionAnim(Movement._DIRECTION_LEFT, Movement._DIRECTION_DOWN, GameAssets._IDLE_DOWN_LEFT_ASSET),
                new DirectionAnim(Movement._DIRECTION_LEFT, Movement._DIRECTION_STILL, GameAssets._IDLE_LEFT_ASSET),

                new DirectionAnim(Movement._DIRECTION_RIGHT, Movement._DIRECTION_UP, GameAssets._IDLE_UP_RIGHT_ASSET),
                new DirectionAnim(Movement._DIRECTION_RIGHT, Movement._DIRECTION_DOWN, GameAssets._IDLE_DOWN_RIGHT_ASSET),
                new DirectionAnim(Movement._DIRECTION_RIGHT, Movement._DIRECTION_STILL, GameAssets._IDLE_RIGHT_ASSET),

                new DirectionAnim(Movement._DIRECTION_STILL, Movement._DIRECTION_UP, GameAssets._IDLE_UP_ASSET),
                new DirectionAnim(Movement._DIRECTION_STILL, Movement._DIRECTION_DOWN, GameAssets._IDLE_DOWN_ASSET),
            };

        final DirectionAnim[] fightAnims =
            {
                new DirectionAnim(Movement._DIRECTION_LEFT, Movement._DIRECTION_UP, GameAssets._FIGHT_UP_LEFT_ASSET),
                new DirectionAnim(Movement._DIRECTION_LEFT, Movement._DIRECTION_DOWN, GameAssets._FIGHT_DOWN_LEFT_ASSET),
                new DirectionAnim(Movement._DIRECTION_LEFT, Movement._DIRECTION_STILL, GameAssets._FIGHT_LEFT_ASSET),

                new DirectionAnim(Movement._DIRECTION_RIGHT, Movement._DIRECTION_UP, GameAssets._FIGHT_UP_RIGHT_ASSET),
                new DirectionAnim(Movement._DIRECTION_RIGHT, Movement._DIRECTION_DOWN, GameAssets._FIGHT_DOWN_RIGHT_ASSET),
                new DirectionAnim(Movement._DIRECTION_RIGHT, Movement._DIRECTION_STILL, GameAssets._FIGHT_RIGHT_ASSET),

                new DirectionAnim(Movement._DIRECTION_STILL, Movement._DIRECTION_UP, GameAssets._FIGHT_UP_ASSET),
                new DirectionAnim(Movement._DIRECTION_STILL, Movement._DIRECTION_DOWN, GameAssets._FIGHT_DOWN_ASSET),
            };

        final DirectionAnim[] dyingAnims =
            {
                new DirectionAnim(Movement._DIRECTION_LEFT, Movement._DIRECTION_UP, GameAssets._DYING_UP_LEFT_ASSET),
                new DirectionAnim(Movement._DIRECTION_LEFT, Movement._DIRECTION_DOWN, GameAssets._DYING_DOWN_LEFT_ASSET),
                new DirectionAnim(Movement._DIRECTION_LEFT, Movement._DIRECTION_STILL, GameAssets._DYING_LEFT_ASSET),

                new DirectionAnim(Movement._DIRECTION_RIGHT, Movement._DIRECTION_UP, GameAssets._DYING_UP_RIGHT_ASSET),
                new DirectionAnim(Movement._DIRECTION_RIGHT, Movement._DIRECTION_DOWN, GameAssets._DYING_DOWN_RIGHT_ASSET),
                new DirectionAnim(Movement._DIRECTION_RIGHT, Movement._DIRECTION_STILL, GameAssets._DYING_RIGHT_ASSET),

                new DirectionAnim(Movement._DIRECTION_STILL, Movement._DIRECTION_UP, GameAssets._DYING_UP_ASSET),
                new DirectionAnim(Movement._DIRECTION_STILL, Movement._DIRECTION_DOWN, GameAssets._DYING_DOWN_ASSET),
            };

        // TODO: 09/04/2021
        // The contents of each of these cases are quite similar to each other.
        // Try and refactor this to reduce the footprint of this method.
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
    public void updateCollisionBox()
    {
        collisionObject.rectangle.x      = sprite.getX() + (frameWidth / 3f);
        collisionObject.rectangle.y      = sprite.getY() + (frameHeight / 5f);
        collisionObject.rectangle.width  = frameWidth / 3f;
        collisionObject.rectangle.height = frameHeight / 2f;

        viewBox.x      = (int) (sprite.getX() - (_VIEWBOX_WIDTH / 2));
        viewBox.y      = (int) (sprite.getY() - (_VIEWBOX_HEIGHT / 2));
        viewBox.width  = (int) _VIEWBOX_WIDTH;
        viewBox.height = (int) _VIEWBOX_HEIGHT;

        if (App.settings.isEnabled((Settings._SPAWNPOINTS)))
        {
            tileRectangle.x      = (((collisionObject.rectangle.x + (frameWidth / 2f)) / Gfx.getTileWidth()));
            tileRectangle.y      = ((collisionObject.rectangle.y - Gfx.getTileHeight()) / Gfx.getTileHeight());
            tileRectangle.width  = Gfx.getTileWidth();
            tileRectangle.height = Gfx.getTileHeight();
        }

        rightEdge = collisionObject.rectangle.x + collisionObject.rectangle.width;
        topEdge   = collisionObject.rectangle.y + collisionObject.rectangle.height;
    }

    @Override
    public void preDraw()
    {
        super.preDraw();
    }

    @Override
    public void draw(SpriteBatch spriteBatch)
    {
        super.draw(spriteBatch);

        if (actionButton.getActionMode() == ActionStates._OFFER_ABXY_A)
        {
            spriteBatch.draw
                (
                    abxy[_ABXY_A],
                    this.sprite.getX() + (this.frameWidth / 3f),
                    this.sprite.getY() + this.frameHeight
                );
        }
        else if (actionButton.getActionMode() == ActionStates._OFFER_ABXY_B)
        {
            spriteBatch.draw
                (
                    abxy[_ABXY_B],
                    this.sprite.getX() + (this.frameWidth / 3f),
                    this.sprite.getY() + this.frameHeight
                );
        }
        else if (actionButton.getActionMode() == ActionStates._OFFER_ABXY_X)
        {
            spriteBatch.draw
                (
                    abxy[_ABXY_X],
                    this.sprite.getX() + (this.frameWidth / 3f),
                    this.sprite.getY() + this.frameHeight
                );
        }
        else if (actionButton.getActionMode() == ActionStates._OFFER_ABXY_Y)
        {
            spriteBatch.draw
                (
                    abxy[_ABXY_Y],
                    this.sprite.getX() + (this.frameWidth / 3f),
                    this.sprite.getY() + this.frameHeight
                );
        }
        else if (isOnFloorButton && App.gameProgress.keyCount.isEmpty() && !App.getHud().messageManager.isEnabled())
        {
            App.getHud().messageManager.addZoomMessage
                (
                    GameAssets._KEY_NEEDED_MSG_ASSET,
                    5000,
                    (Gfx._VIEW_WIDTH - GameAssets.getAssetSize(GraphicID._KEY_NEEDED).x) / 2,
                    300
                );
        }
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

    @Override
    public void dispose()
    {
        super.dispose();

        buttons.dispose();

        tileRectangle = null;
        buttons       = null;
        actionButton  = null;
        collision     = null;
    }
}
