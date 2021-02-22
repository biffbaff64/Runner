package com.richikin.runner.desktop;

import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.tools.texturepacker.TexturePacker;
import com.richikin.runner.config.Version;
import com.richikin.runner.core.MainGame;
import com.richikin.runner.graphics.Gfx;

/**
 * Launches the desktop (LWJGL) application.
 */
public class DesktopLauncher
{
    // Set _rebuildAtlas to TRUE to rebuild all atlases. This is only really necessary
    // when new images are added to a folder, but can be left as true by default
    // if you prefer to rebuild every time.
    // NB: Bear in mind that, if you are testing builds other than the desktop
    // build and you add new images to a folder, YOU MUST build and run the desktop
    // version so that the atlases are rebuilt. If you don't do this then you will
    // experience errors when the program tries to access the new images.
    private static final boolean _rebuildAtlas          = false;
    private static final boolean _drawDebugLines        = false;
    private static final boolean _removeDuplicateImages = false;

    public static void main(String[] args)
    {
        updateTexturePacker();
        createApplication();
    }

    private static LwjglApplication createApplication()
    {
        return new LwjglApplication(new MainGame(), getDefaultConfiguration());
    }

    private static LwjglApplicationConfiguration getDefaultConfiguration()
    {
        LwjglApplicationConfiguration configuration = new LwjglApplicationConfiguration();
        //// This prevents a confusing error that would appear after exiting normally.
        configuration.forceExit = false;

        configuration.title         = Version.getProjectID();
        configuration.width         = Gfx._DESKTOP_WIDTH;
        configuration.height        = Gfx._DESKTOP_HEIGHT;
        configuration.backgroundFPS = (int) Gfx._FPS;
        configuration.foregroundFPS = (int) Gfx._FPS;
        configuration.resizable     = true;
        configuration.vSyncEnabled  = true;
        configuration.fullscreen    = false;
        configuration.useGL30       = false;

        for (int size : new int[]{128, 64, 32, 16})
        {
            configuration.addIcon("libgdx" + size + ".png", FileType.Internal);
        }

        return configuration;
    }

    private static void updateTexturePacker()
    {
        if (_rebuildAtlas)
        {
            TexturePacker.Settings settings = new TexturePacker.Settings();

            settings.maxWidth  = 2048;        // Maximum Width of final atlas image
            settings.maxHeight = 2048;        // Maximum Height of final atlas image
            settings.pot       = true;
            settings.debug     = _drawDebugLines;
            settings.alias     = _removeDuplicateImages;

            //
            // Build the Atlases from the specified parameters :-
            // - configuration settings
            // - source folder
            // - destination folder
            // - name of atlas, without extension (the extension '.atlas' will be added automatically)
            TexturePacker.process(settings, "packedimages/objects", "packedimages/output", "objects");
            TexturePacker.process(settings, "packedimages/animations", "packedimages/output", "animations");
            TexturePacker.process(settings, "packedimages/input", "packedimages/output", "buttons");
            TexturePacker.process(settings, "packedimages/text", "packedimages/output", "text");
        }
    }
}
