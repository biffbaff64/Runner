package com.richikin.runner.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.richikin.enumslib.StateID;
import com.richikin.runner.assets.GameAssets;
import com.richikin.runner.config.AppConfig;
import com.richikin.runner.config.Settings;
import com.richikin.runner.core.App;
import com.richikin.runner.core.GameConstants;
import com.richikin.runner.developer.Developer;
import com.richikin.runner.graphics.Gfx;
import com.richikin.runner.maps.Room;
import com.richikin.runner.ui.panels.IUserInterfacePanel;
import com.richikin.utilslib.graphics.GfxUtils;
import com.richikin.utilslib.graphics.text.FontUtils;
import com.richikin.utilslib.input.Switch;
import com.richikin.utilslib.logging.Trace;
import com.richikin.utilslib.ui.IUIProgressBar;
import com.richikin.utilslib.ui.ProgressBar;

import java.util.Locale;

public class HeadsUpDisplay implements IHud
{
    public static final int _X1     = 0;
    public static final int _X2     = 1;
    public static final int _Y      = 2;
    public static final int _WIDTH  = 3;
    public static final int _HEIGHT = 4;

    public static final int _JOYSTICK  = 0;
    public static final int _BUTTON_X  = 1;
    public static final int _BUTTON_Y  = 2;
    public static final int _BUTTON_B  = 3;
    public static final int _BUTTON_A  = 4;
    public static final int _PAUSE     = 5;
    public static final int _COINS     = 6;
    public static final int _GEMS      = 7;
    public static final int _LIVES     = 8;
    public static final int _HEALTH    = 9;
    public static final int _VILLAGERS = 10;
    public static final int _COMPASS   = 11;

    //@formatter:off
    public final int[][] displayPos =
        {
            {  40,  820,   50,  240,  240},             // Joystick

            {1069,   22,   85,   96,   96},             // X
            {1128,   22,  141,   96,   96},             // Y
            {1188,   22,   85,   96,   96},             // B (Attack)
            {1128,   79,   29,   96,   96},             // A (Action)

            {1179, 1179,  630,   66,   66},             // Pause Button

            //
            // Y is distance from the TOP of the screen
            { 990,  990,   28,    0,    0},             // Coins total
            { 990,  990,   70,    0,    0},             // Gems total
            {  75,   75,   47,    0,    0},             // Life bar
            {  75,   75,   89,    0,    0},             // Health bar

            //
            // Y is distance from the TOP of the screen
            { 835,  835,   53,    0,    0},             // Villagers
            {1172, 1172,  101,    0,    0},             // Compass
        };

    public final int[][] livesDisplay =
        {
            {0, 0},
            {0, 0},
            {0, 0},
            {0, 0},
            {0, 0},
        };
    //@formatter:on

    public Switch buttonUp;
    public Switch buttonDown;
    public Switch buttonLeft;
    public Switch buttonRight;
    public Switch buttonA;
    public Switch buttonB;
    public Switch buttonX;
    public Switch buttonY;
    public Switch buttonPause;

    public Image               scorePanel;
    public BitmapFont          smallFont;
    public BitmapFont          midFont;
    public BitmapFont          bigFont;
    public MessageManager      messageManager;
    public PausePanel          pausePanel;
    public IUserInterfacePanel conversationPanel;
    public StateID             hudStateID;

    public ItemBar itemBar;
    public int     itemPanelIndex;

    private IUIProgressBar  healthBar;
    private IUIProgressBar  livesBar;
    private TextureRegion[] compassTexture;

    public HeadsUpDisplay()
    {
    }

    @Override
    public void createHud()
    {
        Trace.__FILE_FUNC();

        AppConfig.hudExists = false;

        Texture               texture  = App.assets.loadSingleAsset(GameAssets._HUD_PANEL_ASSET, Texture.class);
        TextureRegionDrawable drawable = new TextureRegionDrawable(texture);
        this.scorePanel = new Image(drawable);

        this.messageManager = new MessageManager();
        this.pausePanel     = new PausePanel();
        this.livesBar       = new ProgressBar(1, 0, GameConstants._MAX_PROGRESSBAR_LENGTH, "bar9");
        this.healthBar      = new ProgressBar(1, 0, GameConstants._MAX_PROGRESSBAR_LENGTH, "bar9");
        this.livesBar.setHeightColorScale(19f, Color.GREEN, 3.0f);
        this.healthBar.setHeightColorScale(19f, Color.GREEN, 3.0f);

        createHUDButtons();

        GfxUtils gfxUtils = new GfxUtils();
        compassTexture = new TextureRegion[5];
        gfxUtils.splitRegion(App.assets.getObjectRegion("compass"), 5, compassTexture);

        itemBar = new ItemBar();
        itemPanelIndex = 0;

        bigFont   = FontUtils.inst().createFont(GameAssets._PRO_WINDOWS_FONT, 28);
        midFont   = FontUtils.inst().createFont(GameAssets._PRO_WINDOWS_FONT, 22);
        smallFont = FontUtils.inst().createFont(GameAssets._PRO_WINDOWS_FONT, 14);

        this.hudStateID = StateID._STATE_PANEL_START;

        AppConfig.hudExists = true;
    }

    @Override
    public void update()
    {
        switch (hudStateID)
        {
            case _STATE_PANEL_START:
            {
                hudStateID = StateID._STATE_PANEL_UPDATE;
            }
            break;

            case _STATE_PANEL_UPDATE:
            {
                if (buttonPause.isPressed())
                {
                    AppConfig.pause();
                    buttonPause.release();
                    pausePanel.setup();
                    hideControls();
                }
                else
                {
                    messageManager.update();
                }
            }
            break;

            case _STATE_PAUSED:
            {
            }
            break;

            default:
                break;
        }
    }

    @Override
    public void render(OrthographicCamera camera, boolean canDrawControls)
    {
        if (AppConfig.hudExists)
        {
            drawPanels();

            drawHudDebug();
        }
    }

    @Override
    public void showControls()
    {
    }

    @Override
    public void hideControls()
    {
    }

    @Override
    public void showPauseButton(boolean state)
    {
    }

    public void releaseDirectionButtons()
    {
        buttonRight.release();
        buttonLeft.release();
        buttonUp.release();
        buttonDown.release();
    }

    @Override
    public void setStateID(StateID newState)
    {
        hudStateID = newState;
    }

    @Override
    public void refillItems()
    {
    }

    private void drawPanels()
    {
        scorePanel.setPosition(AppConfig.hudOriginX, AppConfig.hudOriginY + (Gfx._HUD_HEIGHT - scorePanel.getHeight()));
        scorePanel.draw(App.spriteBatch, 1.0f);

        itemBar.draw(AppConfig.hudOriginX, AppConfig.hudOriginY);
    }

    private void drawItems()
    {
    }

    private void drawCompass()
    {
        if (!App.roomManager.activeRoom.compassPoints[Room._N].isEmpty())
        {
            App.spriteBatch.draw
                (
                    compassTexture[Room._N + 1],
                    AppConfig.hudOriginX + displayPos[_COMPASS][_X1],
                    AppConfig.hudOriginY + (Gfx._HUD_HEIGHT - displayPos[_COMPASS][_Y])
                );
        }

        if (!App.roomManager.activeRoom.compassPoints[Room._E].isEmpty())
        {
            App.spriteBatch.draw
                (
                    compassTexture[Room._E + 1],
                    AppConfig.hudOriginX + displayPos[_COMPASS][_X1],
                    AppConfig.hudOriginY + (Gfx._HUD_HEIGHT - displayPos[_COMPASS][_Y])
                );
        }

        if (!App.roomManager.activeRoom.compassPoints[Room._S].isEmpty())
        {
            App.spriteBatch.draw
                (
                    compassTexture[Room._S + 1],
                    AppConfig.hudOriginX + displayPos[_COMPASS][_X1],
                    AppConfig.hudOriginY + (Gfx._HUD_HEIGHT - displayPos[_COMPASS][_Y])
                );
        }

        if (!App.roomManager.activeRoom.compassPoints[Room._W].isEmpty())
        {
            App.spriteBatch.draw
                (
                    compassTexture[Room._W + 1],
                    AppConfig.hudOriginX + displayPos[_COMPASS][_X1],
                    AppConfig.hudOriginY + (Gfx._HUD_HEIGHT - displayPos[_COMPASS][_Y])
                );
        }
    }

    private void drawControls()
    {
    }

    private void drawMessages()
    {
    }

    private void drawHudDebug()
    {
        if (Developer.isDevMode())
        {
            smallFont.setColor(Color.WHITE);
            smallFont.draw(App.spriteBatch, "DEV MODE", AppConfig.hudOriginX + 470, AppConfig.hudOriginY + (720 - 6));

            if (Developer.isGodMode())
            {
                smallFont.draw(App.spriteBatch, "GOD MODE", AppConfig.hudOriginX + 790, AppConfig.hudOriginY + (720 - 6));
            }

            int yPosition = 600;

            if (App.settings.isEnabled(Settings._SHOW_FPS))
            {
                smallFont.draw
                    (
                        App.spriteBatch,
                        "FPS  : " + Gdx.graphics.getFramesPerSecond(),
                        AppConfig.hudOriginX + 20,
                        AppConfig.hudOriginY + yPosition
                    );

                yPosition -= 30;
            }

//            if (App.settings.isEnabled(Settings._SHOW_DEBUG))
//            {
//            }

            if (App.settings.isEnabled(Settings._MENU_HEAPS))
            {
                smallFont.draw
                    (
                        App.spriteBatch,
                        String.format
                            (
                                Locale.UK,
                                "JAVA HEAP: %3.2fMB",
                                ((((float) Gdx.app.getJavaHeap()) / 1024) / 1024)
                            ),
                        AppConfig.hudOriginX + 20,
                        AppConfig.hudOriginY + yPosition
                    );

                yPosition -= 30;

                smallFont.draw
                    (
                        App.spriteBatch,
                        String.format
                            (
                                Locale.UK,
                                "NATIVE HEAP: %3.2fMB",
                                ((((float) Gdx.app.getNativeHeap()) / 1024) / 1024)
                            ),
                        AppConfig.hudOriginX + 20,
                        AppConfig.hudOriginY + yPosition
                    );
            }
        }
    }

    /**
     * Creates the game screen buttons and then
     * registers them with the Scene2D Stage.
     */
    @Override
    public void createHUDButtons()
    {
        buttonUp    = new Switch();
        buttonDown  = new Switch();
        buttonLeft  = new Switch();
        buttonRight = new Switch();
        buttonA     = new Switch();
        buttonB     = new Switch();
        buttonX     = new Switch();
        buttonY     = new Switch();
        buttonPause = new Switch();
    }

    private void addButtonListeners()
    {
    }

    @Override
    public void dispose()
    {
    }
}
