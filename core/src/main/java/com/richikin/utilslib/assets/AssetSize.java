package com.richikin.utilslib.assets;

import com.richikin.enumslib.GraphicID;
import com.richikin.utilslib.maths.Vec2;

/**
 * Matches graphics asset width & height
 * to a Graphic ID.
 */
public class AssetSize
{
    public final GraphicID graphicID;
    public final Vec2      size;

    public AssetSize(GraphicID gid, int width, int height)
    {
        this.graphicID = gid;
        this.size      = new Vec2(width, height);
    }
}
