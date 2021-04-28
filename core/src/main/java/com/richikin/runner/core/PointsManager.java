package com.richikin.runner.core;

import com.richikin.enumslib.GraphicID;

public class PointsManager
{
    private static final Points[] pointsTable =
        {
            new Points(GraphicID.G_SOLDIER, 90),
        };

    public static int getPoints(GraphicID gid)
    {
        int score = 0;

        for (final Points aPointsTable : pointsTable)
        {
            if (gid.equals(aPointsTable.gid))
            {
                score = aPointsTable.points;

                break;
            }
        }

        return score;
    }

    static class Points
    {
        final GraphicID gid;
        final int       points;

        Points(GraphicID _gid, int _points)
        {
            this.gid    = _gid;
            this.points = _points;
        }
    }
}
