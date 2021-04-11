
package com.richikin.utilslib.assets;

import com.richikin.enumslib.GraphicID;
import com.richikin.utilslib.maths.SimpleVec2;
import com.richikin.utilslib.maths.Vec2;

/**
 * Matches graphics asset width & height
 * to a Graphic ID.
 */
public class AssetSize
{
    public final GraphicID graphicID;
    public final Vec2      size;

    public AssetSize(GraphicID _gid, int _width, int _height)
    {
        this.graphicID  = _gid;
        this.size       = new Vec2(_width, _height);
    }
}
