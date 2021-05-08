package com.richikin.runner.maps;

import com.badlogic.gdx.utils.Array;
import com.richikin.enumslib.GraphicID;
import com.richikin.enumslib.TileID;
import com.richikin.runner.core.App;
import com.richikin.runner.entities.objects.SpriteDescriptor;
import com.richikin.runner.entities.Entities;

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

    /**
     * Scans through {@link Entities#entityList} to find the
     * {@link GraphicID} which matches the supplied {@link TileID}.
     */
    public GraphicID tileToGID(TileID tileID)
    {
        GraphicID gid = GraphicID.G_NO_ID;
        int index = 0;

        while((index < App.entities.entityList.length) && (gid == GraphicID.G_NO_ID))
        {
            if (App.entities.entityList[index]._TILE == tileID)
            {
                gid = App.entities.entityList[index]._GID;
            }

            index++;
        }

        return gid;
    }
}
