package com.richikin.runner.maps;

import com.badlogic.gdx.utils.Array;
import com.richikin.enumslib.GraphicID;
import com.richikin.runner.core.App;
import com.richikin.runner.entities.objects.SpriteDescriptor;

public class TiledUtils
{
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
