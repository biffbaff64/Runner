
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

    public SplashScreen()
    {
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
        batch.dispose();
        background.dispose();

        batch      = null;
        background = null;
    }
}
