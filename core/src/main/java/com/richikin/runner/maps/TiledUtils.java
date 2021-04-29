package com.richikin.runner.maps;

import com.badlogic.gdx.utils.Array;
import com.richikin.enumslib.GraphicID;
import com.richikin.runner.core.App;
import com.richikin.runner.entities.objects.SpriteDescriptor;
import com.richikin.utilslib.logging.Trace;

public class TiledUtils
{
    public Array<SpriteDescriptor> findMultiTiles(final GraphicID targetGID)
    {
        Trace.__FILE_FUNC();

        Array<SpriteDescriptor> tiles = new Array<>();

        for (SpriteDescriptor marker : App.mapData.placementTiles)
        {
            Trace.dbg("ID: " + marker._GID + "   Index: " + marker._INDEX);

            if (marker._GID == targetGID)
            {
                tiles.add(marker);
            }
        }

        return tiles;
    }
}
