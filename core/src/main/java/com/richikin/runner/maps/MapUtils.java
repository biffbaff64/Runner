
package com.richikin.runner.maps;

import com.richikin.runner.core.App;
import com.richikin.runner.graphics.Gfx;
import com.richikin.utilslib.logging.Trace;

public class MapUtils
{
    public MapUtils()
    {
    }

    /**
     * Updates the map position with the supplied coordinates.
     */
    public void positionAt(int x, int y)
    {
        App.mapData.previousMapPosition.set(App.mapData.mapPosition);

        try
        {
            App.mapData.mapPosition.setX
                (
                    (int) Math.max(App.mapData.minScrollX,
                    (float) ((x + (App.getPlayer().frameWidth / 2)) - (Gfx._VIEW_WIDTH / 2)))
                );

            App.mapData.mapPosition.setX(Math.min(App.mapData.mapPosition.getX(), App.mapData.maxScrollX));

            App.mapData.mapPosition.setY
                (
                    (int) Math.max(App.mapData.minScrollY,
                    (float) ((y + (App.getPlayer().frameHeight / 2)) - (Gfx._VIEW_HEIGHT / 2)))
                );

            App.mapData.mapPosition.setY(Math.min(App.mapData.mapPosition.getY(), App.mapData.maxScrollY));

            App.parallaxManager.scroll();
        }
        catch (NullPointerException npe)
        {
            Trace.__FILE_FUNC(npe.getMessage());

            App.mapData.mapPosition.set(0, 0);
        }
    }
}
