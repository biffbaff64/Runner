package com.richikin.runner.entities.managers;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.richikin.enumslib.GraphicID;
import com.richikin.runner.core.App;
import com.richikin.runner.entities.components.IEntityManagerComponent;
import com.richikin.runner.entities.objects.SpriteDescriptor;
import com.richikin.utilslib.maths.SimpleVec2;
import com.richikin.utilslib.maths.Vec2;
import com.richikin.utilslib.maths.Vec3;

public class GenericEntityManager implements IEntityManagerComponent, Disposable
{
    private final GraphicID        managerID;
    public        boolean          canPlace;
    public        int              activeCount;
    public        GraphicID        graphicID;

    public GenericEntityManager()
    {
        this.graphicID = GraphicID.G_NO_ID;
        this.managerID = GraphicID.G_NO_ID;
    }

    public GenericEntityManager(final GraphicID _graphicID)
    {
        this.graphicID = _graphicID;
        this.managerID = _graphicID;
    }

    @Override
    public void init()
    {
        activeCount = 0;
    }

    @Override
    public void update()
    {
    }

    @Override
    public void create()
    {
    }

    @Override
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

    @Override
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

    @Override
    public void free()
    {
        activeCount = Math.max(0, activeCount - 1);
    }

    @Override
    public void free(final GraphicID gid)
    {
    }

    @Override
    public void reset()
    {
        activeCount = 0;
    }

    @Override
    public int getActiveCount()
    {
        return activeCount;
    }

    @Override
    public void setActiveCount(final int numActive)
    {
        activeCount = numActive;
    }

    @Override
    public GraphicID getGID()
    {
        return managerID;
    }

    @Override
    public void setPlaceable(final boolean placeable)
    {
        canPlace = placeable;
    }

    @Override
    public void dispose()
    {
    }
}
