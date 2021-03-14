package com.richikin.runner.graphics.parallax;

import com.richikin.runner.core.App;

public class ParallaxManager
{
    public ParallaxManager()
    {
    }

    public void scroll()
    {
        if ((App.mapData.mapPosition.getX() > App.mapData.minScrollX)
            && (App.mapData.mapPosition.getX() < App.mapData.maxScrollX))
        {
            if (App.mapData.previousMapPosition.getX() > App.mapData.mapPosition.getX())
            {
                App.baseRenderer.parallaxBackground.scrollLayersLeft();
            }
            else if (App.mapData.previousMapPosition.getX() < App.mapData.mapPosition.getX())
            {
                App.baseRenderer.parallaxBackground.scrollLayersRight();
            }
        }

        if ((App.mapData.mapPosition.getY() > App.mapData.minScrollY)
            && (App.mapData.mapPosition.getY() < App.mapData.maxScrollY))
        {
            if (App.mapData.previousMapPosition.getY() > App.mapData.mapPosition.getY())
            {
                App.baseRenderer.parallaxBackground.scrollLayersDown();
            }
            else if (App.mapData.previousMapPosition.getY() < App.mapData.mapPosition.getY())
            {
                App.baseRenderer.parallaxBackground.scrollLayersUp();
            }
        }
    }
}
