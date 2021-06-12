package com.richikin.runner.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.richikin.enumslib.ScreenID;
import com.richikin.runner.assets.GameAssets;
import com.richikin.runner.config.AppConfig;
import com.richikin.runner.config.Settings;
import com.richikin.runner.core.App;
import com.richikin.runner.developer.Developer;
import com.richikin.runner.ui.IUIPage;
import com.richikin.utilslib.input.controllers.ControllerData;
import com.richikin.utilslib.logging.Trace;
import com.richikin.utilslib.ui.Scene2DUtils;

public class OptionsPage implements IUIPage
{
    private ImageButton buttonStats;
    private ImageButton buttonPrivacy;
    private ImageButton buttonStoryLine;
    private ImageButton buttonDevOptions;
    private ImageButton buttonGoogle;
    private CheckBox    musicCheckBox;
    private CheckBox    fxCheckBox;
    private CheckBox    vibrateCheckBox;
    private CheckBox    hintsCheckBox;
    private Texture     foreground;
    private Texture     controllerTestPanel;
    private Label       controllerLabel;
    private Label       buttonLabel;
    private Label       axisLabel;
    private Skin        skin;
    private ScreenID    activePanel;
    private boolean     isJustFinishedOptionsPanel;
    private boolean     enteredDeveloperPanel;

    /**
     *
     */
    public OptionsPage()
    {
    }

    /**
     *
     */
    @Override
    public void initialise()
    {
        if (AppConfig.currentScreenID == ScreenID._MAIN_MENU)
        {
            AppConfig.backButton.setVisible(true);
            AppConfig.backButton.setDisabled(false);
            AppConfig.backButton.setChecked(false);

            if (AppConfig.backButton.getClickListener() != null)
            {
                AppConfig.backButton.removeListener(AppConfig.backButton.getClickListener());
            }
        }

        foreground          = App.getAssets().loadSingleAsset(GameAssets._OPTIONS_PANEL_ASSET, Texture.class);
        controllerTestPanel = App.getAssets().loadSingleAsset(GameAssets._CONTROLLER_TEST_ASSET, Texture.class);

        skin = new Skin(Gdx.files.internal(GameAssets._UISKIN_ASSET));

        populateTable();
        createButtonListeners();
        createCheckboxListeners();
        updateSettingsOnEntry();

        activePanel = ScreenID._SETTINGS_SCREEN;

        Developer.developerPanelActive = false;
        enteredDeveloperPanel          = false;
        isJustFinishedOptionsPanel     = false;
    }

    /**
     *
     */
    @Override
    public boolean update()
    {
        if (AppConfig.backButton.isChecked())
        {
            switch (activePanel)
            {
                case _STATS_SCREEN:
                case _INSTRUCTIONS_SCREEN:
                case _PRIVACY_POLICY_SCREEN:
                {
                    // TODO: 22/02/2021 - Panel updates here
                }
                break;

                default:
                {
                    updateSettings();
                    isJustFinishedOptionsPanel = true;
                }
                break;
            }

            if (!isJustFinishedOptionsPanel)
            {
                showActors(true);
                activePanel = ScreenID._SETTINGS_SCREEN;
                AppConfig.backButton.setChecked(false);
            }
        }

        if (Developer.isDevMode())
        {
            if (AppConfig.isControllerFitted())
            {
                controllerLabel.setText(App.inputManager.gameController.controller.getName());
                buttonLabel.setText(ControllerData.controllerButtonCode);
                axisLabel.setText
                    (
                        "" + ControllerData.controllerAxisCode
                            + "    /    "
                            + ControllerData.controllerAxisValue
                    );
            }
        }

        if (enteredDeveloperPanel && !Developer.developerPanelActive)
        {
            enteredDeveloperPanel = false;
            showActors(true);

            if (AppConfig.currentScreenID == ScreenID._MAIN_MENU)
            {
                AppConfig.backButton.setVisible(true);
                AppConfig.backButton.setDisabled(false);
            }
        }

        return isJustFinishedOptionsPanel;
    }

    /**
     *
     */
    public void draw(SpriteBatch spriteBatch)
    {
        switch (activePanel)
        {
            case _STATS_SCREEN:
            case _INSTRUCTIONS_SCREEN:
            case _PRIVACY_POLICY_SCREEN:
            {
            }
            break;

            default:
            {
                if (Developer.developerPanelActive)
                {
                    // TODO: 22/02/2021 - Draw developer panel here
                }
                else
                {
                    if (foreground != null)
                    {
                        spriteBatch.draw(foreground, AppConfig.hudOriginX, AppConfig.hudOriginY);

                        if (Developer.isDevMode())
                        {
                            spriteBatch.draw(controllerTestPanel, AppConfig.hudOriginX, AppConfig.hudOriginY);
                        }
                    }
                }
            }
            break;
        }
    }

    /**
     *
     */
    public void show()
    {
        showActors(true);
    }

    /**
     *
     */
    public void hide()
    {
        showActors(false);
    }

    /**
     *
     */
    private void populateTable()
    {
        Trace.__FILE_FUNC();

        Scene2DUtils scene2DUtils = new Scene2DUtils();

        // ----------
        musicCheckBox   = scene2DUtils.addCheckBox("toggle_on", "toggle_off", (int) AppConfig.hudOriginX + 800, (int) AppConfig.hudOriginY + (720 - 279), Color.WHITE, skin);
        fxCheckBox      = scene2DUtils.addCheckBox("toggle_on", "toggle_off", (int) AppConfig.hudOriginX + 800, (int) AppConfig.hudOriginY + (720 - 330), Color.WHITE, skin);
        vibrateCheckBox = scene2DUtils.addCheckBox("toggle_on", "toggle_off", (int) AppConfig.hudOriginX + 800, (int) AppConfig.hudOriginY + (720 - 382), Color.WHITE, skin);
        hintsCheckBox   = scene2DUtils.addCheckBox("toggle_on", "toggle_off", (int) AppConfig.hudOriginX + 800, (int) AppConfig.hudOriginY + (720 - 435), Color.WHITE, skin);

        // ----------
        if (AppConfig.currentScreenID == ScreenID._MAIN_MENU)
        {
            buttonPrivacy = scene2DUtils.addButton
                (
                    "new_privacy_policy_button",
                    "new_privacy_policy_button_pressed",
                    (int) AppConfig.hudOriginX + 401,
                    (int) AppConfig.hudOriginY + (720 - 511)
                );

            buttonStoryLine = scene2DUtils.addButton
                (
                    "new_objectives_button",
                    "new_objectives_button_pressed",
                    (int) AppConfig.hudOriginX + 401,
                    (int) AppConfig.hudOriginY + (720 - 558)
                );

            // ----------
            buttonGoogle = scene2DUtils.addButton
                (
                    "signedOutButton",
                    "signedInButton",
                    (int) AppConfig.hudOriginX + 479,
                    (int) AppConfig.hudOriginY + (720 - 241)
                );
        }

        // ----------
        if (Developer.isDevMode())
        {
            buttonDevOptions = scene2DUtils.addButton
                (
                    "new_developer_options_button",
                    "new_developer_options_button_pressed",
                    (int) AppConfig.hudOriginX + 648,
                    (int) AppConfig.hudOriginY + (720 - 511)
                );

            buttonStats = scene2DUtils.addButton
                (
                    "new_stats_button",
                    "new_stats_button_pressed",
                    (int) AppConfig.hudOriginX + 648,
                    (int) AppConfig.hudOriginY + (720 - 558)
                );

            if (AppConfig.isControllerFitted())
            {
                controllerLabel = scene2DUtils.addLabel
                    (
                        "",
                        (int) AppConfig.hudOriginX + 180,
                        (int) AppConfig.hudOriginY + 50,
                        Color.WHITE,
                        new Skin(Gdx.files.internal(GameAssets._UISKIN_ASSET))
                    );

                buttonLabel = scene2DUtils.addLabel
                    (
                        "",
                        (int) AppConfig.hudOriginX + 680,
                        (int) AppConfig.hudOriginY + 50,
                        Color.WHITE,
                        new Skin(Gdx.files.internal(GameAssets._UISKIN_ASSET))
                    );

                axisLabel = scene2DUtils.addLabel
                    (
                        "",
                        (int) AppConfig.hudOriginX + 980,
                        (int) AppConfig.hudOriginY + 50,
                        Color.WHITE,
                        new Skin(Gdx.files.internal(GameAssets._UISKIN_ASSET))
                    );

                controllerLabel.setAlignment(Align.left);
                buttonLabel.setAlignment(Align.left);
                axisLabel.setAlignment(Align.left);
            }
        }

        showActors(true);
    }

    /**
     *
     */
    private void updateSettings()
    {
        App.settings.getPrefs().putBoolean(Settings._MUSIC_ENABLED, musicCheckBox.isChecked());
        App.settings.getPrefs().putBoolean(Settings._SOUNDS_ENABLED, fxCheckBox.isChecked());
        App.settings.getPrefs().putBoolean(Settings._VIBRATIONS, vibrateCheckBox.isChecked());
        App.settings.getPrefs().putBoolean(Settings._SHOW_HINTS, hintsCheckBox.isChecked());
        App.settings.getPrefs().flush();
    }

    /**
     *
     */
    private void updateSettingsOnEntry()
    {
        vibrateCheckBox.setChecked(App.settings.isEnabled(Settings._VIBRATIONS));
        hintsCheckBox.setChecked(App.settings.isEnabled(Settings._SHOW_HINTS));
        musicCheckBox.setChecked(App.settings.isEnabled(Settings._MUSIC_ENABLED));
        fxCheckBox.setChecked(App.settings.isEnabled(Settings._SOUNDS_ENABLED));
    }

    /**
     *
     */
    private void showActors(boolean _visibilty)
    {
        if (Developer.isDevMode())
        {
            buttonDevOptions.setVisible(_visibilty);
            buttonStats.setVisible(_visibilty);

            if (AppConfig.isControllerFitted())
            {
                controllerLabel.setVisible(_visibilty);
                buttonLabel.setVisible(_visibilty);
                axisLabel.setVisible(_visibilty);
            }
        }

        if (buttonGoogle != null)
        {
            buttonGoogle.setVisible(_visibilty);
        }

        if (buttonPrivacy != null)
        {
            buttonPrivacy.setVisible(_visibilty);
        }

        if (buttonStoryLine != null)
        {
            buttonStoryLine.setVisible(_visibilty);
        }

        musicCheckBox.setVisible(_visibilty);
        fxCheckBox.setVisible(_visibilty);
        vibrateCheckBox.setVisible(_visibilty);
        hintsCheckBox.setVisible(_visibilty);
    }

    /**
     *
     */
    private void createButtonListeners()
    {
        /*
         * Privacy policy button.
         * Displays the privacy policy on screen, for
         * the players reference.
         */
        if (buttonPrivacy != null)
        {
            buttonPrivacy.addListener(new ClickListener()
            {
                public void clicked(InputEvent event, float x, float y)
                {
                }
            });
        }

        /*
         * Instructions button.
         * Displays the Instructions / Game objectives on
         * screen, for the players reference.
         */
        if (buttonStoryLine != null)
        {
            buttonStoryLine.addListener(new ClickListener()
            {
                public void clicked(InputEvent event, float x, float y)
                {
                }
            });
        }

        if (buttonGoogle != null)
        {
            buttonGoogle.addListener(new ClickListener()
            {
                public void clicked(InputEvent event, float x, float y)
                {
                }
            });
        }

        /*
         * Statistics button.
         * Displays the in-game statistics.
         */
        if (buttonStats != null)
        {
            buttonStats.addListener(new ClickListener()
            {
                public void clicked(InputEvent event, float x, float y)
                {
                }
            });
        }

        /*
         * Developer Options Button.
         * Provides a button for accessing DEV MODE ONLY game option settings
         */
        if (buttonDevOptions != null)
        {
            buttonDevOptions.addListener(new ClickListener()
            {
                public void clicked(InputEvent event, float x, float y)
                {
                    if (!Developer.developerPanelActive)
                    {
                        Developer.developerPanelActive = true;
                        enteredDeveloperPanel          = true;

                        showActors(false);

                        AppConfig.backButton.setVisible(false);
                        AppConfig.backButton.setDisabled(true);

//                        DeveloperPanel.inst().setup();
                    }
                }
            });
        }
    }

    /**
     *
     */
    private void createCheckboxListeners()
    {
        if (musicCheckBox != null)
        {
            musicCheckBox.addListener(new ChangeListener()
            {
                /**
                 * @param event the {@link ChangeEvent}
                 * @param actor The event target, which is the actor that emitted the change event.
                 */
                @Override
                public void changed(ChangeEvent event, Actor actor)
                {
                    App.settings.getPrefs().putBoolean(Settings._MUSIC_ENABLED, musicCheckBox.isChecked());
                    App.settings.getPrefs().flush();
                }
            });
        }

        if (fxCheckBox != null)
        {
            fxCheckBox.addListener(new ChangeListener()
            {
                /**
                 * @param event the {@link ChangeEvent}
                 * @param actor The event target, which is the actor that emitted the change event.
                 */
                @Override
                public void changed(ChangeEvent event, Actor actor)
                {
                    App.settings.getPrefs().putBoolean(Settings._SOUNDS_ENABLED, fxCheckBox.isChecked());
                    App.settings.getPrefs().flush();
                }
            });
        }

        if (vibrateCheckBox != null)
        {
            vibrateCheckBox.addListener(new ChangeListener()
            {
                /**
                 * @param event the {@link ChangeEvent}
                 * @param actor The event target, which is the actor that emitted the change event.
                 */
                @Override
                public void changed(ChangeEvent event, Actor actor)
                {
                    App.settings.getPrefs().putBoolean(Settings._VIBRATIONS, vibrateCheckBox.isChecked());
                    App.settings.getPrefs().flush();
                }
            });
        }

        if (hintsCheckBox != null)
        {
            hintsCheckBox.addListener(new ChangeListener()
            {
                /**
                 * @param event the {@link ChangeEvent}
                 * @param actor The event target, which is the actor that emitted the change event.
                 */
                @Override
                public void changed(ChangeEvent event, Actor actor)
                {
                    App.settings.getPrefs().putBoolean(Settings._SHOW_HINTS, hintsCheckBox.isChecked());
                    App.settings.getPrefs().flush();
                }
            });
        }
    }

    /**
     *
     */
    @Override
    public void dispose()
    {
        Trace.__FILE_FUNC();

        AppConfig.backButton.setVisible(false);
        AppConfig.backButton.setDisabled(true);

        if (buttonStats != null)
        {
            buttonStats.addAction(Actions.removeActor());
            buttonStats = null;
        }

        if (buttonPrivacy != null)
        {
            buttonPrivacy.addAction(Actions.removeActor());
            buttonPrivacy = null;
        }

        if (buttonStoryLine != null)
        {
            buttonStoryLine.addAction(Actions.removeActor());
            buttonStoryLine = null;
        }

        if (buttonDevOptions != null)
        {
            buttonDevOptions.addAction(Actions.removeActor());
            buttonDevOptions = null;
        }

        musicCheckBox.addAction(Actions.removeActor());
        musicCheckBox = null;
        fxCheckBox.addAction(Actions.removeActor());
        fxCheckBox = null;

        if (vibrateCheckBox != null)
        {
            vibrateCheckBox.addAction(Actions.removeActor());
            vibrateCheckBox = null;
        }

        if (hintsCheckBox != null)
        {
            hintsCheckBox.addAction(Actions.removeActor());
            hintsCheckBox = null;
        }

        if (buttonGoogle != null)
        {
            buttonGoogle.addAction(Actions.removeActor());
            buttonGoogle = null;
        }

        App.getAssets().unloadAsset(GameAssets._OPTIONS_PANEL_ASSET);
        App.getAssets().unloadAsset(GameAssets._CONTROLLER_TEST_ASSET);

        if (controllerLabel != null)
        {
            controllerLabel.addAction(Actions.removeActor());
            controllerLabel = null;
        }

        if (buttonLabel != null)
        {
            buttonLabel.addAction(Actions.removeActor());
            buttonLabel = null;
        }

        if (axisLabel != null)
        {
            axisLabel.addAction(Actions.removeActor());
            axisLabel = null;
        }

        foreground = null;
        skin       = null;
    }

    /**
     *
     */
    public ScreenID getActivePanel()
    {
        return activePanel;
    }

    /**
     *
     */
    public void setActivePanel(ScreenID screenID)
    {
        activePanel = screenID;
    }
}
