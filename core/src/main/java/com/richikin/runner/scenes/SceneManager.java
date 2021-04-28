package com.richikin.runner.scenes;

import com.richikin.enumslib.ScreenID;

public class SceneManager
{
    private static final SceneManager _INSTANCE;

    // Instance initialiser block
    static
    {
        try
        {
            _INSTANCE = new SceneManager();
        }
        catch (Exception e)
        {
            throw new RuntimeException(e.getMessage());
        }
    }

    private ScreenID currentScene;

    private SceneManager()
    {
    }

    public static SceneManager inst()
    {
        return _INSTANCE;
    }

    public ScreenID getCurrentScene()
    {
        return currentScene;
    }

    public void setCurrentScene(ScreenID currentScene)
    {
        this.currentScene = currentScene;
    }
}
