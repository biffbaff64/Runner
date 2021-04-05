package com.richikin.runner.ui;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.richikin.enumslib.StateID;
import com.richikin.runner.assets.GameAssets;
import com.richikin.runner.config.AppConfig;
import com.richikin.runner.core.App;
import com.richikin.runner.graphics.Gfx;
import com.richikin.utilslib.graphics.text.FontUtils;
import com.richikin.utilslib.input.Switch;
import com.richikin.utilslib.logging.Trace;

public class HeadsUpDisplay implements IHud
{
    public Switch buttonUp;
    public Switch buttonDown;
    public Switch buttonLeft;
    public Switch buttonRight;

    // ###############################################################
    // TODO: 27/11/2020 - Are these following switches still needed ??
    public Switch buttonAction;
    public Switch buttonAttack;
    public Switch buttonX;
    public Switch buttonY;
    public Switch buttonPause;
    public Switch buttonDevOptions;
    // ###############################################################

    public ImageButton ActionButton;
    public ImageButton AttackButton;
    public ImageButton PauseButton;

    public Image          scorePanel;
    public BitmapFont     smallFont;
    public BitmapFont     midFont;
    public BitmapFont     bigFont;
    public MessageManager messageManager;
    public PausePanel     pausePanel;
    public StateID        hudStateID;

    private static final int _X1     = 0;
    private static final int _X2     = 1;
    private static final int _Y      = 2;
    private static final int _WIDTH  = 3;
    private static final int _HEIGHT = 4;

    //@formatter:off
    private final int[][] displayPos =
        {
            {0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0},
        };

    private final int[][] livesDisplay =
        {
            {0, 0},
            {0, 0},
            {0, 0},
            {0, 0},
            {0, 0},
        };
    //@formatter:on

    public HeadsUpDisplay()
    {
    }

    @Override
    public void createHud()
    {
        Trace.__FILE_FUNC();

        AppConfig.hudExists = false;

        Texture               texture  = App.assets.loadSingleAsset("hud_panel.png", Texture.class);
        TextureRegionDrawable drawable = new TextureRegionDrawable(texture);
        this.scorePanel = new Image(drawable);

        this.messageManager = new MessageManager();
        this.pausePanel     = new PausePanel();

        createHUDButtons();

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
    }

    private void drawItems()
    {
    }

    private void drawControls()
    {
    }

    private void drawMessages()
    {
    }

    private void drawHudDebug()
    {
    }

    /**
     * Creates the game screen buttons and then
     * registers them with the Scene2D Stage.
     */
    @Override
    public void createHUDButtons()
    {
    }

    private void addButtonListeners()
    {
    }

    @Override
    public void dispose()
    {
    }
}
