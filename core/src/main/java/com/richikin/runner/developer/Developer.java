package com.richikin.runner.developer;

import com.richikin.runner.config.AppConfig;

public abstract class Developer
{
    public static boolean developerPanelActive = false;

    private static boolean isAndroidOnDesktop = false;
    private static boolean isGodMode          = false;
    private static boolean isDevMode          = false;

    /**
     * Set _DEVMODE from the _DEV_MODE Environment variable.
     */
    public static void setMode()
    {
        if (AppConfig.isDesktopApp())
        {
            isDevMode = "TRUE".equalsIgnoreCase(System.getenv("_DEV_MODE"));
        }
    }

    public static boolean isDevMode()
    {
        return !AppConfig.isAndroidApp() && isDevMode;
    }

    public static void setDevMode(boolean _state)
    {
        isDevMode = _state;
    }

    public static boolean isGodMode()
    {
        return !AppConfig.isAndroidApp() && isGodMode;
    }

    public static void setGodMode(boolean _state)
    {
        isGodMode = _state;
    }

    public static boolean isAndroidOnDesktop()
    {
        return isAndroidOnDesktop;
    }

    public static void setAndroidOnDesktop(boolean _state)
    {
        isAndroidOnDesktop = _state;
    }
}
