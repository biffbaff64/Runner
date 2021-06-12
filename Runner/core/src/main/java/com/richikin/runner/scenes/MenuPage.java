package com.richikin.runner.scenes;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.TimeUtils;
import com.richikin.runner.audio.AudioData;
import com.richikin.runner.audio.GameAudio;
import com.richikin.runner.config.AppConfig;
import com.richikin.runner.core.App;
import com.richikin.runner.ui.IUIPage;
import com.richikin.utilslib.logging.Trace;
import com.richikin.utilslib.ui.Scene2DUtils;

import java.util.Calendar;
import java.util.Date;

public class MenuPage implements IUIPage, Disposable
{
    public ImageButton buttonStart;
    public ImageButton buttonOptions;
    public ImageButton buttonCredits;
    public ImageButton buttonExit;

    private Texture foreground;
    private Image   decoration;

    public MenuPage()
    {
    }

    @Override
    public void initialise()
    {
        Trace.__FILE_FUNC();

        foreground = App.getAssets().loadSingleAsset("title_background.png", Texture.class);

        populateMenuScreen();
        addClickListeners();
    }

    @Override
    public boolean update()
    {
        return false;
    }

    @Override
    public void draw(SpriteBatch spriteBatch)
    {
        if (foreground != null)
        {
            spriteBatch.draw(foreground, AppConfig.hudOriginX, AppConfig.hudOriginY);
        }
    }

    @Override
    public void show()
    {
        Trace.__FILE_FUNC();

        showItems(true);
    }

    @Override
    public void hide()
    {
        Trace.__FILE_FUNC();

        showItems(false);
    }

    @Override
    public void dispose()
    {
        Trace.__FILE_FUNC();

        if (buttonStart != null)
        {
            buttonStart.addAction(Actions.removeActor());
        }
        if (buttonOptions != null)
        {
            buttonOptions.addAction(Actions.removeActor());
        }
        if (buttonCredits != null)
        {
            buttonCredits.addAction(Actions.removeActor());
        }
        if (buttonExit != null)
        {
            buttonExit.addAction(Actions.removeActor());
        }

        buttonStart   = null;
        buttonOptions = null;
        buttonCredits = null;
        buttonExit    = null;

        if (decoration != null)
        {
            decoration.addAction(Actions.removeActor());
            decoration = null;
        }

        App.getAssets().unloadAsset("title_background.png");

        foreground = null;
    }

    private void populateMenuScreen()
    {
        Trace.__FILE_FUNC();

        Scene2DUtils scene2DUtils = new Scene2DUtils();

        buttonStart   = scene2DUtils.addButton
            (
                "buttonStart",
                "buttonStart_pressed",
                (int) AppConfig.hudOriginX + 515,
                (int) AppConfig.hudOriginY + (720 - 379)
            );
        buttonOptions = scene2DUtils.addButton
            (
                "buttonOptions",
                "buttonOptions_pressed",
                (int) AppConfig.hudOriginX + 558,
                (int) AppConfig.hudOriginY + (720 - 437)
            );
        buttonExit    = scene2DUtils.addButton
            (
                "buttonExit",
                "buttonExit_pressed",
                (int) AppConfig.hudOriginX + 596,
                (int) AppConfig.hudOriginY + (720 - 614)
            );
        buttonCredits = scene2DUtils.addButton
            (
                "button_credits",
                "button_credits_pressed",
                (int) AppConfig.hudOriginX + 558,
                (int) AppConfig.hudOriginY + (720 - 496)
            );

        addDateSpecificItems();
    }

    private void addDateSpecificItems()
    {
        // TODO: 29/12/2020 - Add New Years Day, Mother Goddess Day

        Date     date     = new Date(TimeUtils.millis());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        Scene2DUtils scene2DUtils = new Scene2DUtils();

        if (calendar.get(Calendar.MONTH) == Calendar.NOVEMBER)
        {
            if (calendar.get(Calendar.DAY_OF_MONTH) == 11)
            {
                decoration = scene2DUtils.makeObjectsImage("poppy");
                decoration.setPosition(AppConfig.hudOriginX + 1160, AppConfig.hudOriginY + (720 - 90));
                App.getStage().addActor(decoration);
            }
        }
        else
        {
            if (calendar.get(Calendar.MONTH) == Calendar.DECEMBER)
            {
                if (calendar.get(Calendar.DAY_OF_MONTH) == 25)
                {
                    decoration = scene2DUtils.makeObjectsImage("xmas_tree");
                    decoration.setPosition(AppConfig.hudOriginX + 1075, AppConfig.hudOriginY + (720 - 342));
                    App.getStage().addActor(decoration);
                }
            }
        }
    }

    private void addClickListeners()
    {
        Trace.__FILE_FUNC();

        if (buttonStart != null)
        {
            buttonStart.addListener(new ClickListener()
            {
                public void clicked(InputEvent event, float x, float y)
                {
                    GameAudio.inst().startSound(AudioData.SFX_BEEP);

                    buttonStart.setChecked(true);
                }
            });
        }

        if (buttonOptions != null)
        {
            buttonOptions.addListener(new ClickListener()
            {
                public void clicked(InputEvent event, float x, float y)
                {
                    GameAudio.inst().startSound(AudioData.SFX_BEEP);

                    buttonOptions.setChecked(true);
                }
            });
        }

        if (buttonCredits != null)
        {
            buttonCredits.addListener(new ClickListener()
            {
                public void clicked(InputEvent event, float x, float y)
                {
                    GameAudio.inst().startSound(AudioData.SFX_BEEP);

                    buttonCredits.setChecked(true);
                }
            });
        }

        if (buttonExit != null)
        {
            buttonExit.addListener(new ClickListener()
            {
                public void clicked(InputEvent event, float x, float y)
                {
                    GameAudio.inst().startSound(AudioData.SFX_BEEP);

                    buttonExit.setChecked(true);
                }
            });
        }
    }

    /**
     * Sets visibility of all rlevant actors.
     *
     * @param _visible boolean visibility setting.
     */
    private void showItems(boolean _visible)
    {
        if (buttonStart != null)
        {
            buttonStart.setVisible(_visible);
        }
        if (buttonOptions != null)
        {
            buttonOptions.setVisible(_visible);
        }
        if (buttonCredits != null)
        {
            buttonCredits.setVisible(_visible);
        }
        if (buttonExit != null)
        {
            buttonExit.setVisible(_visible);
        }

        if (decoration != null)
        {
            decoration.setVisible(_visible);
        }
    }
}
