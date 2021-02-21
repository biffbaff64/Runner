
package com.richikin.runner.entities.paths;


import com.richikin.runner.entities.objects.GdxSprite;

public class Circular implements IPathMover
{
    private float timeInterval;
    private float xOffset;
    private float yOffset;

    public Circular()
    {
        reset();
    }

    public void reset()
    {
        timeInterval = 0;
        xOffset = 0;
        yOffset = 0;
    }

    public float getXOffset()
    {
        return xOffset;
    }

    public void setXOffset(float offset)
    {
        this.xOffset = offset;
    }

    public float getYOffset()
    {
        return yOffset;
    }

    public void setYOffset(float offset)
    {
        this.yOffset = offset;
    }

    /**
     * based on the current time interval, calculate
     * where the sphere is at on its orbit
     */
    public void setNextMove(GdxSprite gdxSprite)
    {
        double radian = (Math.PI / 75) * timeInterval;

        gdxSprite.sprite.setX((float) ((gdxSprite.sprite.getOriginX() + xOffset) + ((gdxSprite.frameWidth * 2) * Math.cos(radian))));

        gdxSprite.sprite.setY((float) ((gdxSprite.sprite.getOriginY() + yOffset) + ((gdxSprite.frameHeight * 2) * Math.sin(radian))));

        timeInterval++;
    }
}
