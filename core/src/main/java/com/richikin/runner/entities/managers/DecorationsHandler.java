package com.richikin.runner.entities.managers;

import com.richikin.enumslib.GraphicID;
import com.richikin.runner.core.App;
import com.richikin.runner.entities.objects.SpriteDescriptor;
import com.richikin.utilslib.graphics.GraphicIndex;
import com.richikin.utilslib.logging.Trace;

public class DecorationsHandler extends GenericEntityManager
{
    private GraphicIndex[] decorations =
        {
            new GraphicIndex(GraphicID.G_ALCOVE_TORCH, 0, 0),
            new GraphicIndex(GraphicID.G_BARREL, 0, 0),
            new GraphicIndex(GraphicID.G_CRATE, 0, 0),
            new GraphicIndex(GraphicID.G_GLOW_EYES, 0, 0),
            new GraphicIndex(GraphicID.G_PLANT_POT, 0, 0),
            new GraphicIndex(GraphicID.G_POT, 0, 0),
            new GraphicIndex(GraphicID.G_SACKS, 0, 0),
        };

    /**
     * ------------------------------------------------------------------------------
     *
     * ------------------------------------------------------------------------------
     */
    public DecorationsHandler()
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

        for (GraphicIndex item : decorations)
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
    public GraphicIndex[] getDecorations()
    {
        return decorations;
    }
}
