package com.richikin.runner.core;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms.
 */
public class MainGame extends ApplicationAdapter
{
    private SpriteBatch batch;
    private Texture     image;

    @Override
    public void create()
    {
        batch = new SpriteBatch();
        image = new Texture("badlogic.png");
    }

    @Override
    public void render()
    {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        batch.draw(image, 165, 180);
        batch.end();
    }

    @Override
    public void dispose()
    {
        batch.dispose();
        image.dispose();
    }
}