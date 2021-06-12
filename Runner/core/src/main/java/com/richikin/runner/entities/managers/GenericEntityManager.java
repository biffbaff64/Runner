package com.richikin.runner.entities.managers;

import com.badlogic.gdx.utils.Disposable;
import com.richikin.enumslib.GraphicID;
import com.richikin.runner.entities.components.IEntityManagerComponent;

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
