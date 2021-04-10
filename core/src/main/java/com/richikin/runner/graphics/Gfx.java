package com.richikin.runner.graphics;

import com.badlogic.gdx.math.Vector2;
import com.richikin.runner.config.AppConfig;
import com.richikin.utilslib.maths.SimpleVec2F;

public class Gfx
{
    //
    // Entity collision types
    public static final short CAT_NOTHING        = 0x0000;   // - 00 (0     )
    public static final short CAT_PLAYER         = 0x0001;   // - 01 (1     )
    public static final short CAT_MOBILE_ENEMY   = 0x0002;   // - 02 (2     )
    public static final short CAT_FIXED_ENEMY    = 0x0004;   // - 03 (4     )
    public static final short CAT_PLAYER_WEAPON  = 0x0008;   // - 04 (8     )
    public static final short CAT_ENEMY_WEAPON   = 0x0010;   // - 05 (16    )
    public static final short CAT_WALL           = 0x0020;   // - 06 (32    )
    public static final short CAT_EXIT_BOX       = 0x0040;   // - 07 (64    )
    public static final short CAT_DOOR           = 0x0080;   // - 08 (128   )
    public static final short CAT_VILLAGER       = 0x0100;   // - 09 (256   )
    public static final short CAT_ENTITY_BARRIER = 0x0200;   // - 10 (512   )
    public static final short CAT_DECORATION     = 0x0400;   // - 11 (1024  )
    public static final short CAT_INTERACTIVE    = 0x0800;   // - 12 (2048  )
    public static final short CAT_COLLECTIBLE    = 0x1000;   // - 13 (4096  )
    public static final short CAT_PLATFORM       = 0x2000;   // - 14 (8192  )
    public static final short _UNDEFINED_15      = 0x4000;   // - 15 (16384 )

    //
    // Combined Categories
    public static final short CAT_ENEMY    = (CAT_MOBILE_ENEMY | CAT_FIXED_ENEMY);
    public static final short CAT_OBSTACLE = (CAT_WALL | CAT_DECORATION);
    public static final short CAT_WEAPON   = (CAT_PLAYER_WEAPON | CAT_ENEMY_WEAPON);

    //
    // Pixels Per Meter in the Box2D World
    // In this instance, a meter is the length of a single TiledMap tile.
    public static       float _PPM              = 64.0f;
    public static       float _PIXELS_TO_METERS = (1.0f / _PPM);
    public static final float _PPM_SETTING      = 64.0f;

    //
    // Maximum Z-sorting depth for sprites
    public static final int _MAXIMUM_Z_DEPTH = 20;

    //
    // The desired Frame Rate
    public static final float _FPS     = 60f;
    public static final float _MIN_FPS = 30f;

    //
    // Values for Box2D.step()
    public static final float   _STEP_TIME           = (1.0f / 60f);
    public static final int     _VELOCITY_ITERATIONS = 8;
    public static final int     _POSITION_ITERATIONS = 3;
    public static final Vector2 _WORLD_GRAVITY       = new Vector2(0, -9.8f);
    public static final int     _FALL_GRAVITY        = 10;
    public static final int     _TERMINAL_VELOCITY   = (int) (_PPM * _FALL_GRAVITY);

    public static final int _HUD_WIDTH      = 1280;
    public static final int _HUD_HEIGHT     = 720;
    public static final int _DESKTOP_WIDTH  = 1280;
    public static final int _DESKTOP_HEIGHT = 720;
    public static final int _VIEW_WIDTH     = 2400;
    public static final int _VIEW_HEIGHT    = 1536;

    public static final int _VIEW_HALF_WIDTH  = (_VIEW_WIDTH / 2);
    public static final int _VIEW_HALF_HEIGHT = (_VIEW_HEIGHT / 2);

    public static final float _DEFAULT_ZOOM = 1.0f;
    public static final float _LERP_SPEED   = 0.075f;

    public static final float _HUD_SCENE_WIDTH   = (_HUD_WIDTH / _PPM);
    public static final float _HUD_SCENE_HEIGHT  = (_HUD_HEIGHT / _PPM);
    public static       float _GAME_SCENE_WIDTH  = (_VIEW_WIDTH / _PPM);
    public static       float _GAME_SCENE_HEIGHT = (_VIEW_HEIGHT / _PPM);

    //
    // Initialised in MapData
    public static int mapWidth;
    public static int mapHeight;
    public static int tileWidth;
    public static int tileHeight;

    private static final SimpleVec2F pixelDimensions = new SimpleVec2F();
    private static final SimpleVec2F meterDimensions = new SimpleVec2F();

    public static SimpleVec2F getScreenSizeInMeters()
    {
        meterDimensions.set(_VIEW_WIDTH * _PIXELS_TO_METERS, _VIEW_HEIGHT * _PIXELS_TO_METERS);

        return meterDimensions;
    }

    public static SimpleVec2F getScreenSizeInPixels()
    {
        pixelDimensions.set(_VIEW_WIDTH, _VIEW_HEIGHT);

        return pixelDimensions;
    }

    public static float PixelsToMeters(float pixels)
    {
        return pixels * _PIXELS_TO_METERS;
    }

    public static int getTileWidth()
    {
        return tileWidth;
    }

    public static int getTileHeight()
    {
        return tileHeight;
    }

    public static int getMapWidth()
    {
        return mapWidth;
    }

    public static int getMapHeight()
    {
        return mapHeight;
    }

    public static void setPPM(final float newPPM)
    {
        if (!AppConfig.camerasReady)
        {
            if (newPPM != _PPM)
            {
                _PPM               = newPPM;
                _PIXELS_TO_METERS  = (1.0f / _PPM);
                _GAME_SCENE_WIDTH  = (_VIEW_WIDTH / _PPM);
                _GAME_SCENE_HEIGHT = (_VIEW_HEIGHT / _PPM);
            }
        }
    }

    public static int visibleMapWidth()
    {
        return mapWidth - (_VIEW_HALF_WIDTH * 2);
    }

    public static int visibleMapRight()
    {
        return mapWidth - _VIEW_HALF_WIDTH;
    }
}
