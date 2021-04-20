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
import com.richikin.runner.input.buttons.GameButton;
import com.richikin.runner.maps.Room;
import com.richikin.runner.ui.panels.IUserInterfacePanel;
import com.richikin.utilslib.graphics.GfxUtils;
import com.richikin.utilslib.graphics.text.FontUtils;
import com.richikin.utilslib.input.IGDXButton;
import com.richikin.utilslib.input.Switch;
import com.richikin.utilslib.input.controllers.ControllerType;
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
    //@formatter:on

    public Switch     buttonUp;
    public Switch     buttonDown;
    public Switch     buttonLeft;
    public Switch     buttonRight;
    public Switch     buttonPause;
    public IGDXButton buttonA;
    public IGDXButton buttonB;
    public IGDXButton buttonX;
    public IGDXButton buttonY;

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

        itemBar        = new ItemBar();
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
            drawItems();
            drawCompass();
            drawMessages();

            if (canDrawControls && App.gameProgress.gameSetupDone)
            {
                drawControls();
            }

            if (hudStateID == StateID._STATE_PAUSED)
            {
                pausePanel.draw();
            }

            drawHudDebug();
        }
    }

    @Override
    public void showControls()
    {
        if (AppConfig.availableInputs.contains(ControllerType._VIRTUAL, true))
        {
            buttonB.setDrawable(true);
            buttonA.setDrawable(true);
            buttonX.setDrawable(true);
            buttonY.setDrawable(true);

            if (App.inputManager.virtualJoystick != null)
            {
                App.inputManager.virtualJoystick.show();
            }
        }
    }

    @Override
    public void hideControls()
    {
        if (AppConfig.availableInputs.contains(ControllerType._VIRTUAL, true))
        {
            buttonB.setDrawable(false);
            buttonA.setDrawable(false);
            buttonX.setDrawable(false);
            buttonY.setDrawable(false);

            if (App.inputManager.virtualJoystick != null)
            {
                App.inputManager.virtualJoystick.hide();
            }
        }
    }

    @Override
    public void showPauseButton(boolean state)
    {
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
        if (!App.roomManager.activeRoom.compassPoints[Room._NORTH].isEmpty())
        {
            App.spriteBatch.draw
                (
                    compassTexture[Room._NORTH + 1],
                    AppConfig.hudOriginX + displayPos[_COMPASS][_X1],
                    AppConfig.hudOriginY + (Gfx._HUD_HEIGHT - displayPos[_COMPASS][_Y])
                );
        }

        if (!App.roomManager.activeRoom.compassPoints[Room._EAST].isEmpty())
        {
            App.spriteBatch.draw
                (
                    compassTexture[Room._EAST + 1],
                    AppConfig.hudOriginX + displayPos[_COMPASS][_X1],
                    AppConfig.hudOriginY + (Gfx._HUD_HEIGHT - displayPos[_COMPASS][_Y])
                );
        }

        if (!App.roomManager.activeRoom.compassPoints[Room._SOUTH].isEmpty())
        {
            App.spriteBatch.draw
                (
                    compassTexture[Room._SOUTH + 1],
                    AppConfig.hudOriginX + displayPos[_COMPASS][_X1],
                    AppConfig.hudOriginY + (Gfx._HUD_HEIGHT - displayPos[_COMPASS][_Y])
                );
        }

        if (!App.roomManager.activeRoom.compassPoints[Room._WEST].isEmpty())
        {
            App.spriteBatch.draw
                (
                    compassTexture[Room._WEST + 1],
                    AppConfig.hudOriginX + displayPos[_COMPASS][_X1],
                    AppConfig.hudOriginY + (Gfx._HUD_HEIGHT - displayPos[_COMPASS][_Y])
                );
        }
    }

    private void drawControls()
    {
        if (AppConfig.availableInputs.contains(ControllerType._VIRTUAL, true))
        {
            if (!AppConfig.gamePaused && (App.appState.peek() != StateID._STATE_MESSAGE_PANEL))
            {
                if (App.inputManager.virtualJoystick != null)
                {
                    App.inputManager.virtualJoystick.getTouchpad().setPosition
                        (
                            AppConfig.hudOriginX + displayPos[_JOYSTICK][_X1],
                            AppConfig.hudOriginY + displayPos[_JOYSTICK][_Y]
                        );

                    App.inputManager.virtualJoystick.getTouchpad().setBounds
                        (
                            AppConfig.hudOriginX + displayPos[_JOYSTICK][_X1],
                            AppConfig.hudOriginY + displayPos[_JOYSTICK][_Y],
                            App.inputManager.virtualJoystick.getTouchpad().getWidth(),
                            App.inputManager.virtualJoystick.getTouchpad().getHeight()
                        );
                }

                ((GameButton) buttonA).draw();
                ((GameButton) buttonB).draw();
                ((GameButton) buttonX).draw();
                ((GameButton) buttonY).draw();
            }
        }
    }

    private void drawMessages()
    {
        if (!AppConfig.gamePaused)
        {
            if (messageManager.isEnabled())
            {
                messageManager.draw();
            }
        }
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

    public int getItemPanelIndex()
    {
        return itemPanelIndex;
    }

    public void setItemPanelIndex(int index)
    {
        itemPanelIndex = index;
    }

    public IUIProgressBar getHealthBar()
    {
        return healthBar;
    }

    public IUIProgressBar getLivesBar()
    {
        return livesBar;
    }

    public void releaseDirectionButtons()
    {
        buttonRight.release();
        buttonLeft.release();
        buttonUp.release();
        buttonDown.release();
    }

    @Override
    public void createHUDButtons()
    {
        buttonUp    = new Switch();
        buttonDown  = new Switch();
        buttonLeft  = new Switch();
        buttonRight = new Switch();
        buttonPause = new Switch();

        if (AppConfig.availableInputs.contains(ControllerType._VIRTUAL, true))
        {
            buttonA = new GameButton
                (
                    App.assets.getButtonRegion("button_a"),
                    App.assets.getButtonRegion("button_a_pressed"),
                    (int) AppConfig.hudOriginX + displayPos[_BUTTON_A][_X1],
                    (int) AppConfig.hudOriginY + displayPos[_BUTTON_A][_Y],
                    displayPos[_BUTTON_A][_WIDTH], displayPos[_BUTTON_A][_HEIGHT]
                );

            buttonB = new GameButton
                (
                    App.assets.getButtonRegion("button_b"),
                    App.assets.getButtonRegion("button_b_pressed"),
                    (int) AppConfig.hudOriginX + displayPos[_BUTTON_B][_X1],
                    (int) AppConfig.hudOriginY + displayPos[_BUTTON_B][_Y],
                    displayPos[_BUTTON_B][_WIDTH], displayPos[_BUTTON_B][_HEIGHT]
                );

            buttonX = new GameButton
                (
                    App.assets.getButtonRegion("button_x"),
                    App.assets.getButtonRegion("button_x_pressed"),
                    (int) AppConfig.hudOriginX + displayPos[_BUTTON_X][_X1],
                    (int) AppConfig.hudOriginY + displayPos[_BUTTON_X][_Y],
                    displayPos[_BUTTON_X][_WIDTH], displayPos[_BUTTON_X][_HEIGHT]
                );

            buttonY = new GameButton
                (
                    App.assets.getButtonRegion("button_y"),
                    App.assets.getButtonRegion("button_y_pressed"),
                    (int) AppConfig.hudOriginX + displayPos[_BUTTON_Y][_X1],
                    (int) AppConfig.hudOriginY + displayPos[_BUTTON_Y][_Y],
                    displayPos[_BUTTON_Y][_WIDTH], displayPos[_BUTTON_Y][_HEIGHT]
                );
        }
        else
        {
            buttonA = new Switch();
            buttonB = new Switch();
            buttonX = new Switch();
            buttonY = new Switch();
        }

        hideControls();

        AppConfig.gameButtonsReady = true;
    }

    @Override
    public void dispose()
    {
        if (AppConfig.availableInputs.contains(ControllerType._VIRTUAL, true))
        {
            buttonUp    = null;
            buttonDown  = null;
            buttonLeft  = null;
            buttonRight = null;

            //
            // These buttons will be on-screen if a _VIRTUAL controller
            // is defined, otherwise they will be switches and do not
            // need disposing.
            ((GameButton) buttonA).dispose();
            ((GameButton) buttonB).dispose();
            ((GameButton) buttonX).dispose();
            ((GameButton) buttonY).dispose();
        }

        buttonA = null;
        buttonB = null;
        buttonX = null;
        buttonY = null;

        if (App.inputManager.virtualJoystick != null)
        {
            App.inputManager.virtualJoystick.removeTouchpad();
        }

        App.assets.unloadAsset(GameAssets._HUD_PANEL_ASSET);

        bigFont.dispose();
        midFont.dispose();
        smallFont.dispose();

        // TODO: 20/04/2021
//        messageManager.dispose();
//        pausePanel.dispose();
//        healthBar.dispose();
//        livesBar.dispose();

        healthBar = null;
        livesBar  = null;

        messageManager = null;
        pausePanel     = null;
        scorePanel     = null;
    }
}
