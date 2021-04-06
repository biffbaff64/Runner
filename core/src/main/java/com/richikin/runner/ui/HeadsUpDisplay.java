package com.richikin.runner.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.richikin.enumslib.StateID;
import com.richikin.runner.assets.GameAssets;
import com.richikin.runner.config.AppConfig;
import com.richikin.runner.core.App;
import com.richikin.runner.core.GameConstants;
import com.richikin.runner.graphics.Gfx;
import com.richikin.runner.ui.panels.IUserInterfacePanel;
import com.richikin.utilslib.graphics.text.FontUtils;
import com.richikin.utilslib.input.Switch;
import com.richikin.utilslib.logging.Trace;
import com.richikin.utilslib.ui.IUIProgressBar;
import com.richikin.utilslib.ui.ProgressBar;

public class HeadsUpDisplay implements IHud
{
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

    private IUIProgressBar healthBar;
    private IUIProgressBar livesBar;

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
