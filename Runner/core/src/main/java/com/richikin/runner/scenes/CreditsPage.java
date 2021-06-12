package com.richikin.runner.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Disposable;
import com.richikin.runner.assets.GameAssets;
import com.richikin.runner.config.AppConfig;
import com.richikin.runner.config.Version;
import com.richikin.runner.core.App;
import com.richikin.runner.ui.IUIPage;
import com.richikin.utilslib.logging.StopWatch;
import com.richikin.utilslib.logging.Trace;
import com.richikin.utilslib.ui.Scene2DUtils;

public class CreditsPage implements IUIPage, Disposable
{
    private Texture   foreground;
    private StopWatch stopWatch;
    private Label     versionLabel;

    public CreditsPage()
    {
    }

    @Override
    public void initialise()
    {
        foreground = App.getAssets().loadSingleAsset(GameAssets._CREDITS_PANEL_ASSET, Texture.class);

        Scene2DUtils scene2DUtils = new Scene2DUtils();

        versionLabel = scene2DUtils.addLabel
            (
                Version.getDisplayVersion(),
                (int) AppConfig.hudOriginX + 364,
                (int) AppConfig.hudOriginY + (720 - 608),
                Color.WHITE,
                new Skin(Gdx.files.internal(GameAssets._UISKIN_ASSET))
            );

        this.stopWatch = StopWatch.start();
    }

    @Override
    public boolean update()
    {
        return false;
    }

    @Override
    public void show()
    {
        AppConfig.backButton.setVisible(true);
        AppConfig.backButton.setDisabled(false);
        AppConfig.backButton.setChecked(false);

        versionLabel.setVisible(true);

        stopWatch.reset();
    }

    @Override
    public void hide()
    {
        AppConfig.backButton.setVisible(false);
        AppConfig.backButton.setDisabled(true);

        versionLabel.setVisible(false);
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
    public void dispose()
    {
        Trace.__FILE_FUNC();

        App.getAssets().unloadAsset(GameAssets._CREDITS_PANEL_ASSET);

        versionLabel.addAction(Actions.removeActor());
        versionLabel = null;

        stopWatch  = null;
        foreground = null;
    }
}
