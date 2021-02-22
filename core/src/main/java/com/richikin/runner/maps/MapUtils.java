
package com.richikin.runner.maps;

import com.badlogic.gdx.utils.Array;
import com.richikin.enumslib.GraphicID;
import com.richikin.runner.core.App;
import com.richikin.runner.entities.objects.SpriteDescriptor;
import com.richikin.runner.graphics.Gfx;

public class MapUtils
{
    public MapUtils()
    {
    }

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
            App.mapData.mapPosition.set(0, 0);
        }
    }

    public Array<SpriteDescriptor> findMultiTiles(final GraphicID targetGID)
    {
        Array<SpriteDescriptor> tiles = new Array<>();

        for (SpriteDescriptor marker : App.mapData.placementTiles)
        {
            if (marker._GID == targetGID)
            {
                tiles.add(marker);
            }
        }

        return tiles;
    }
}
