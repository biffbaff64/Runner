package com.richikin.utilslib.graphics;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Disposable;
import com.richikin.runner.core.App;
import com.richikin.utilslib.maths.Vec2;
import com.richikin.utilslib.maths.Vec2F;

public class SimpleDrawable implements Disposable
{
    public TextureRegion image;
    public Vec2F         position;
    public Vec2          size;

    public SimpleDrawable()
    {
        this.image    = new TextureRegion();
        this.position = new Vec2F();
        this.size     = new Vec2(16, 16);
    }

    public SimpleDrawable(TextureRegion image, float x, float y)
    {
        this.image    = image;
        this.position = new Vec2F(x, y);
        this.size     = new Vec2(image.getRegionWidth(), image.getRegionHeight());
    }

    public void draw()
    {
        App.getSpriteBatch().draw(image, position.x, position.y, size.x, size.y);
    }

    @Override
    public void dispose()
    {
        image    = null;
        position = null;
    }
}
