package com.richikin.runner.entities.managers;

import com.badlogic.gdx.utils.Array;
import com.richikin.enumslib.GraphicID;
import com.richikin.runner.core.App;
import com.richikin.runner.entities.objects.SpriteDescriptor;
import com.richikin.utilslib.maths.Vec3;

public class EntityManagerUtils
{
    public Array<SpriteDescriptor> getDescriptorList(final GraphicID targetGID)
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

    public Array<Vec3> findMultiCoordinates(final GraphicID targetGID)
    {
        Array<Vec3> coords = new Array<>();

        for (SpriteDescriptor marker : App.mapData.placementTiles)
        {
            if (marker._GID == targetGID)
            {
                coords.add(new Vec3(marker._POSITION.x, marker._POSITION.y, 0));
            }
        }

        return coords;
    }

    public Vec3 findCoordinates(final GraphicID targetGID)
    {
        Vec3 coords = new Vec3();

        for (SpriteDescriptor marker : App.mapData.placementTiles)
        {
            if (marker._GID == targetGID)
            {
                coords.x = marker._POSITION.x;
                coords.y = marker._POSITION.y;
                coords.z = 0;
            }
        }

        return coords;
    }
}
