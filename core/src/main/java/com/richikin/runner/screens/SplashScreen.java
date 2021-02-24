
package com.richikin.runner.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Disposable;
import com.richikin.runner.assets.GameAssets;

public class SplashScreen implements Disposable
{
    public boolean isAvailable;

    private SpriteBatch batch;
    private Texture     background;
    private static final SplashScreen _INSTANCE;

    // Instance initialiser block
    static
    {
        try
        {
            _INSTANCE = new SplashScreen();
        }
        catch (Exception e)
        {
            throw new RuntimeException(e.getMessage());
        }
    }

    public static SplashScreen inst()
    {
        return _INSTANCE;
    }

    public SplashScreen()
    {
        Gdx.app.debug("SpS: ", "[Init:]");

        batch      = new SpriteBatch();
        background = new Texture(GameAssets._SPLASH_SCREEN_ASSET);

        isAvailable = true;
    }

    public void update()
    {
    }

    public void render()
    {
        if (isAvailable)
        {
            Gdx.app.debug("SpS: ", "[Render:]");

            Gdx.gl.glClearColor(1, 1, 1, 1);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

            batch.begin();
            batch.draw(background, 0, 0);
            batch.end();
        }
    }

    @Override
    public void dispose()
    {
        Gdx.app.debug("SpS: ", "[Dispose:]");

        batch.dispose();
        background.dispose();

        batch      = null;
        background = null;
    }
}
