package com.richikin.runner.assets;

import com.richikin.enumslib.GraphicID;
import com.richikin.utilslib.assets.AssetSize;
import com.richikin.utilslib.logging.Trace;
import com.richikin.utilslib.maths.SimpleVec2;

public class GameAssets
{
    public static final String _PLAYER_IDLE_ASSET = "";
    public static final int _PLAYER_STAND_FRAMES  = 1;

    public static int hudPanelWidth;      // Set when object is loaded
    public static int hudPanelHeight;     //

    private static final AssetSize[] assetSizes =
        {
            new AssetSize(GraphicID.G_NO_ID,           80, 80),
        };

    public static SimpleVec2 getAssetSize(GraphicID _gid)
    {
        SimpleVec2 size = new SimpleVec2();

        for (final AssetSize assetSize : assetSizes)
        {
            if (assetSize.graphicID == _gid)
            {
                size = assetSize.size;
            }
        }

        if (size.isEmpty())
        {
            Trace.__FILE_FUNC("***** Size for " + _gid + " not found! *****");
        }

        return size;
    }
}
