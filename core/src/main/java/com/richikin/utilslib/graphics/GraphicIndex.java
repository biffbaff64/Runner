package com.richikin.utilslib.graphics;

import com.richikin.enumslib.GraphicID;

public class GraphicIndex
{
    public final GraphicID graphicID;
    public       int       currentTotal;
    public       int       maxTotal;

    public GraphicIndex(GraphicID gid, int currentTotal, int maxTotal)
    {
        this.graphicID    = gid;
        this.currentTotal = currentTotal;
        this.maxTotal     = maxTotal;
    }
}
