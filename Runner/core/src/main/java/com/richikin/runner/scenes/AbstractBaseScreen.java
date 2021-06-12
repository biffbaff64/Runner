package com.richikin.runner.scenes;

import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.utils.Disposable;
import com.richikin.runner.config.AppConfig;
import com.richikin.runner.core.App;
import com.richikin.runner.graphics.effects.FadeEffect;

public abstract class AbstractBaseScreen extends ScreenAdapter implements IBaseScreen, Disposable
{
    public AbstractBaseScreen()
    {
        super();
    }

    @Override
    public void update()
    {
        if (FadeEffect.isActive)
        {
            if (FadeEffect.update())
            {
                FadeEffect.end();
            }
        }
        else
        {
            if (AppConfig.gameScreenActive())
            {
                App.mapData.update();
                App.gameProgress.update();
            }
        }
    }

    @Override
    public void triggerFadeIn()
    {
        FadeEffect.triggerFadeIn();
    }

    @Override
    public void triggerFadeOut()
    {
        FadeEffect.triggerFadeOut();
    }

    @Override
    public void resize(int _width, int _height)
    {
        App.baseRenderer.resizeCameras(_width, _height);
    }

    @Override
    public void pause()
    {
        App.settings.getPrefs().flush();
    }

    @Override
    public void resume()
    {
    }

    @Override
    public void show()
    {
        loadImages();
    }

    @Override
    public void hide()
    {
    }

    @Override
    public void render(float delta)
    {
        App.baseRenderer.render();
    }

    @Override
    public void loadImages()
    {
    }

    @Override
    public void dispose()
    {
    }
}
