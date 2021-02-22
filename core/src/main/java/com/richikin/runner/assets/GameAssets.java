package com.richikin.runner.assets;

import com.richikin.enumslib.GraphicID;
import com.richikin.utilslib.assets.AssetSize;
import com.richikin.utilslib.logging.Trace;
import com.richikin.utilslib.maths.SimpleVec2;

public class GameAssets
{
    public static final String _PLAYER_IDLE_ASSET   = "player_idle";
    public static final int    _PLAYER_STAND_FRAMES = 1;

    //
    // Fonts and HUD assets
    public static final String _BENZOIC_FONT        = "fonts/paraaminobenzoic.ttf";
    public static final String _GALAXY_FONT         = "fonts/galaxy.ttf";
    public static final String _PRO_WINDOWS_FONT    = "fonts/ProFontWindows.ttf";
    public static final String _VIDEO_PHREAK_FONT   = "fonts/ProFontWindows.ttf";
    public static final String _HUD_PANEL_ASSET     = "hud_panel.png";
    public static final String _SPLASH_SCREEN_ASSET = "splash_screen.png";

    public static final String _GETREADY_MSG_ASSET  = "getready";
    public static final String _GAMEOVER_MSG_ASSET  = "gameover";
    public static final String _CREDITS_PANEL_ASSET = "credits_panel.png";
    public static final String _OPTIONS_PANEL_ASSET = "options_panel.png";
    public static final String _PAUSE_PANEL_ASSET   = "pause_panel.png";
    public static final String _UISKIN_ASSET        = "uiskin.json";

    public static int hudPanelWidth;      // Set when object is loaded
    public static int hudPanelHeight;     //

    private static final AssetSize[] assetSizes =
        {
            new AssetSize(GraphicID.G_NO_ID, 80, 80),
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
