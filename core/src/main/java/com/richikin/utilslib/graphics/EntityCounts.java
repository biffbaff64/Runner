package com.richikin.utilslib.graphics;

import com.richikin.enumslib.GraphicID;

/**
 * ------------------------------------------------------------------------------
 * This class is badly named, and needs renaming.
 * Its purpose is to store a count of currently active entities of the
 * specified type, and also a maximum allowed number of that entity.
 * ------------------------------------------------------------------------------
 */
public class EntityCounts
{
    public final GraphicID graphicID;
    public       int       currentTotal;
    public       int       maxTotal;

    /**
     * Constructor.
     * @param gid           The {@link GraphicID} of the entity to track.
     * @param currentTotal  Initial value for currently active entities.
     * @param maxTotal      Initializer for maximum allowed. This may initially be
     *                      set to zero, and then set correctly later.
     */
    public EntityCounts(GraphicID gid, int currentTotal, int maxTotal)
    {
        this.graphicID    = gid;
        this.currentTotal = currentTotal;
        this.maxTotal     = maxTotal;
    }
}
