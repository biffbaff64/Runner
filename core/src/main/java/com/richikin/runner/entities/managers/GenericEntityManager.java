package com.richikin.runner.entities.managers;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.richikin.enumslib.GraphicID;
import com.richikin.runner.core.App;
import com.richikin.runner.entities.components.IEntityManagerComponent;
import com.richikin.runner.entities.objects.SpriteDescriptor;
import com.richikin.utilslib.maths.SimpleVec2;

public class GenericEntityManager implements IEntityManagerComponent, Disposable
{
    public       boolean          canPlace;
    public       SpriteDescriptor descriptor;
    public       int              activeCount;
    public       GraphicID        graphicID;

    private final GraphicID managerID;

    public GenericEntityManager()
    {
        this.graphicID    = GraphicID.G_NO_ID;
        this.managerID    = GraphicID.G_NO_ID;
    }

    public GenericEntityManager(final GraphicID _graphicID)
    {
        this.graphicID    = _graphicID;
        this.managerID    = _graphicID;
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
    public SimpleVec2 findCoordinates(final GraphicID targetGID)
    {
        SimpleVec2 coords = new SimpleVec2();

        for (SpriteDescriptor marker : App.mapData.placementTiles)
        {
            if (marker._GID == targetGID)
            {
                coords.set(marker._POSITION.x, marker._POSITION.y);
            }
        }

        return coords;
    }

    @Override
    public Array<SimpleVec2> findMultiCoordinates(final GraphicID targetGID)
    {
        Array<SimpleVec2> coords = new Array<>();

        for (SpriteDescriptor marker : App.mapData.placementTiles)
        {
            if (marker._GID == targetGID)
            {
                coords.add(new SimpleVec2(marker._POSITION.x, marker._POSITION.y));
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
        descriptor = null;
    }
}
