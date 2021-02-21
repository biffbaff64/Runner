package com.richikin.utilslib.assets;

import com.richikin.enumslib.GraphicID;
import com.richikin.enumslib.TileID;
import com.richikin.utilslib.maths.SimpleVec2;

/**
 * Asset description class.
 */
public class AssetDef
{
    public final String    asset;        // The asset filename
    public final GraphicID graphicID;    // Asset Identity
    public final GraphicID  type;         // Asset Type - _ENTITY, _OBSTACLE, etc
    public final TileID     tileID;       // Associated marker tile
    public final SimpleVec2 size;         // Asset size, or Frame size for multiple frame assets
    public final int        frames;       // Number of animation frames

    public AssetDef()
    {
        this.graphicID  = GraphicID.G_NO_ID;
        this.type       = GraphicID.G_NO_ID;
        this.tileID     = TileID._DEFAULT_TILE;
        this.asset      = "";
        this.size       = new SimpleVec2();
        this.frames     = 0;
    }

    public AssetDef(String _asset,
                    GraphicID _graphicID,
                    GraphicID _type,
                    TileID _tileID,
                    int _width, int _height,
                    int _frames)
    {
        this.asset      = _asset;
        this.graphicID  = _graphicID;
        this.type       = _type;
        this.tileID     = _tileID;
        this.size       = new SimpleVec2(_width, _height);
        this.frames     = _frames;
    }
}
