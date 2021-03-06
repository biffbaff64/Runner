package com.richikin.runner.scenes;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.richikin.enumslib.ScreenID;
import com.richikin.enumslib.StateID;
import com.richikin.runner.audio.AudioData;
import com.richikin.runner.audio.GameAudio;
import com.richikin.runner.config.AppConfig;
import com.richikin.runner.config.Version;
import com.richikin.runner.core.App;
import com.richikin.runner.graphics.Gfx;
import com.richikin.runner.graphics.camera.OrthoGameCamera;
import com.richikin.runner.ui.IUIPage;
import com.richikin.utilslib.logging.Trace;

import java.util.ArrayList;

/**
 * Main class for handling all actions on the front end screen.
 */
public class MainMenuScreen extends AbstractBaseScreen
{
    private static final int _MENU_PAGE    = 0;
    private static final int _OPTIONS_PAGE = 1;
    private static final int _CREDITS_PAGE = 2;

    private OptionsPage        optionsPage;
    private MenuPage           menuPage;
    private ArrayList<IUIPage> panels;
    private int                currentPage;

    public MainMenuScreen()
    {
        super();
    }

    @Override
    public void initialise()
    {
        Trace.__FILE_FUNC();

        App.mapData.mapPosition.set(0, 0);

        optionsPage = new OptionsPage();
        menuPage    = new MenuPage();
        panels      = new ArrayList<>();
        currentPage = _MENU_PAGE;

        panels.add(_MENU_PAGE, menuPage);
        panels.add(_OPTIONS_PAGE, optionsPage);
        panels.add(_CREDITS_PAGE, new CreditsPage());

        menuPage.initialise();
        menuPage.show();

        Trace.finishedMessage();
    }

    /**
     * Update and draw the screen.
     *
     * @param delta elapsed time since last update
     */
    @Override
    public void render(final float delta)
    {
        super.update();

        if (App.getAppState().peek() == StateID._STATE_MAIN_MENU)
        {
            update();

            super.render(delta);
        }
    }

    /**
     * Draw the currently active page.
     *
     * @param spriteBatch The spritebatch to use.
     * @param _camera     The camera to use.
     */
    public void draw(final SpriteBatch spriteBatch, final OrthoGameCamera _camera)
    {
        if (App.getAppState().peek() == StateID._STATE_MAIN_MENU)
        {
            AppConfig.hudOriginX = (_camera.camera.position.x - (float) (Gfx._HUD_WIDTH / 2));
            AppConfig.hudOriginY = (_camera.camera.position.y - (float) (Gfx._HUD_HEIGHT / 2));

            switch (currentPage)
            {
                case _MENU_PAGE:
                case _CREDITS_PAGE:
                case _OPTIONS_PAGE:
                {
                    panels.get(currentPage).draw(spriteBatch);

                    if (AppConfig.backButton.isVisible())
                    {
                        AppConfig.backButton.setPosition(AppConfig.hudOriginX + 20, AppConfig.hudOriginY + 20);
                    }
                }
                break;

                default:
                {
                    Trace.__FILE_FUNC("ERROR: Illegal panel: " + currentPage);
                }
                break;
            }
        }
    }

    @Override
    public void update()
    {
        if (!GameAudio.inst().isTunePlaying(AudioData.MUS_TITLE))
        {
            GameAudio.inst().playTitleTune(true);
        }

        if (App.getAppState().peek() == StateID._STATE_MAIN_MENU)
        {
            switch (currentPage)
            {
                case _CREDITS_PAGE:
                {
                    panels.get(currentPage).update();

                    if (AppConfig.backButton.isChecked())
                    {
                        AppConfig.backButton.setChecked(false);
                        changePageTo(_MENU_PAGE);
                    }
                }
                break;

                case _MENU_PAGE:
                {
                    panels.get(_MENU_PAGE).update();
                }
                break;

                case _OPTIONS_PAGE:
                {
                    if (panels.get(_OPTIONS_PAGE).update())
                    {
                        AppConfig.backButton.setChecked(false);
                        changePageTo(_MENU_PAGE);
                    }
                }
                break;

                default:
                {
                    Trace.__FILE_FUNC("ERROR:  illegal page - " + currentPage);
                }
                break;
            }

            //
            // Start button check
            if ((menuPage.buttonStart != null) && menuPage.buttonStart.isChecked())
            {
                Trace.divider('#', 100);
                Trace.dbg(" ***** START PRESSED ***** ");
                Trace.divider('#', 100);

                GameAudio.inst().playTitleTune(false);

                menuPage.buttonStart.setChecked(false);

                App.mainGameScreen.reset();
                App.mainGame.setScreen(App.mainGameScreen);
            }
            else
            {
                // If we're still on the title screen...
                if (App.getAppState().peek() == StateID._STATE_MAIN_MENU)
                {
                    //
                    // Check OPTIONS button, open settings page if pressed
                    if ((menuPage.buttonOptions != null) && menuPage.buttonOptions.isChecked())
                    {
                        menuPage.buttonOptions.setChecked(false);
                        changePageTo(_OPTIONS_PAGE);
                    }

                    //
                    // Check CREDITS button, open credits page if pressed
                    if ((menuPage.buttonCredits != null) && menuPage.buttonCredits.isChecked())
                    {
                        menuPage.buttonCredits.setChecked(false);
                        changePageTo(_CREDITS_PAGE);
                    }

                    //
                    // Check EXIT button, open exit panel if pressed
                    if ((menuPage.buttonExit != null) && menuPage.buttonExit.isChecked())
                    {
                        menuPage.buttonExit.setChecked(false);
                        panels.get(currentPage).hide();

//                        exitPanel = new ExitPanel();
//                        exitPanel.open();
//
//                        currentPage = _EXIT_PAGE;
                    }
                }
            }
        }
        else
        {
            Trace.__FILE_FUNC();
            Trace.dbg("Unsupported game state: " + App.getAppState().peek());
        }
    }

    @Override
    public void show()
    {
        Trace.__FILE_FUNC();

        super.show();

        initialise();

        App.cameraUtils.resetCameraZoom();
        App.cameraUtils.disableAllCameras();

        App.baseRenderer.parallaxCamera.isInUse = true;
        App.baseRenderer.hudGameCamera.isInUse  = true;
        App.baseRenderer.isDrawingStage         = true;

        Version.appDetails();

        AppConfig.currentScreenID = ScreenID._MAIN_MENU;
        App.getAppState().set(StateID._STATE_MAIN_MENU);

        SplashScreen.inst().isAvailable = false;
        SplashScreen.inst().dispose();

        Trace.finishedMessage();
    }

    @Override
    public void hide()
    {
        Trace.__FILE_FUNC();

        super.hide();

        dispose();
    }

    @Override
    public void loadImages()
    {
        App.baseRenderer.parallaxBackground.setupLayers(App.mapData.backgroundLayers);
    }

    public MenuPage getMenuPage()
    {
        return menuPage;
    }

    /**
     * Closes down the current page, and
     * switches to a new one.
     *
     * @param _nextPage The ID of the next page.
     */
    private void changePageTo(int _nextPage)
    {
        if (panels.get(currentPage) != null)
        {
            panels.get(currentPage).hide();
            panels.get(currentPage).dispose();
        }

        currentPage = _nextPage;

        if (panels.get(_nextPage) != null)
        {
            panels.get(currentPage).initialise();
            panels.get(currentPage).show();
        }
    }

    /**
     * Clear up all used resources
     */
    @Override
    public void dispose()
    {
        Trace.__FILE_FUNC();

        App.baseRenderer.parallaxBackground.dispose();

        menuPage.hide();
        menuPage.dispose();

        if (panels != null)
        {
            panels.clear();
            panels = null;
        }

        optionsPage = null;
        menuPage    = null;
    }
}
