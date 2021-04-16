package com.richikin.runner.entities.managers;

import com.richikin.enumslib.GraphicID;
import com.richikin.runner.core.App;
import com.richikin.runner.entities.objects.SpriteDescriptor;
import com.richikin.utilslib.graphics.GraphicIndex;
import com.richikin.utilslib.logging.Trace;

public class InteractiveHandler extends GenericEntityManager
{
    private GraphicIndex[] interactives =
        {
            new GraphicIndex(GraphicID.G_DOOR, 0, 0),
            new GraphicIndex(GraphicID.G_LEVER_SWITCH, 0, 0),
            new GraphicIndex(GraphicID.G_ESCALATOR, 0, 0),
            new GraphicIndex(GraphicID.G_VILLAGER, 0, 0),
            new GraphicIndex(GraphicID.G_TREASURE_CHEST, 0, 0),
            new GraphicIndex(GraphicID.G_TELEPORTER, 0, 0),
            new GraphicIndex(GraphicID.G_PRISONER, 0, 0),
            new GraphicIndex(GraphicID.G_MYSTERY_CHEST, 0, 0),
            new GraphicIndex(GraphicID.G_FLOOR_BUTTON, 0, 0),
            new GraphicIndex(GraphicID.G_FLOATING_PLATFORM, 0, 0),
        };

    /**
     * ------------------------------------------------------------------------------
     *
     * ------------------------------------------------------------------------------
     */
    public InteractiveHandler()
    {
    }

    /**
     * ------------------------------------------------------------------------------
     *
     * ------------------------------------------------------------------------------
     */
    @Override
    public void init()
    {
        Trace.__FILE_FUNC();

        for (GraphicIndex item : interactives)
        {
            for (SpriteDescriptor descriptor : App.mapData.placementTiles)
            {
                if (descriptor._GID.equals(item.graphicID))
                {
                    item.currentTotal = 0;
                    item.maxTotal++;
                }
            }

            Trace.dbg("Entity " + item.graphicID + " : maxTotal = " + item.maxTotal);
        }
    }

    /**
     * ------------------------------------------------------------------------------
     *
     * ------------------------------------------------------------------------------
     */
    @Override
    public void create()
    {
    }

    /**
     * ------------------------------------------------------------------------------
     *
     * ------------------------------------------------------------------------------
     */
    @Override
    public void update()
    {
    }

    /**
     * ------------------------------------------------------------------------------
     *
     * ------------------------------------------------------------------------------
     */
    public GraphicIndex[] getInteractives()
    {
        return interactives;
    }
}
