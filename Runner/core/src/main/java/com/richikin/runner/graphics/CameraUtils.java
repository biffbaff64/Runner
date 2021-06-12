package com.richikin.runner.graphics;

import com.richikin.runner.core.App;

public class CameraUtils
{
    public CameraUtils()
    {
    }

    public void resetCameraZoom()
    {
        App.baseRenderer.parallaxCamera.camera.update();
        App.baseRenderer.tiledGameCamera.camera.update();
        App.baseRenderer.spriteGameCamera.camera.update();
        App.baseRenderer.hudGameCamera.camera.update();

        App.baseRenderer.gameZoom.stop();
        App.baseRenderer.hudZoom.stop();

        App.baseRenderer.parallaxCamera.camera.zoom   = 1.0f;
        App.baseRenderer.tiledGameCamera.camera.zoom  = 1.0f;
        App.baseRenderer.spriteGameCamera.camera.zoom = 1.0f;
        App.baseRenderer.hudGameCamera.camera.zoom    = 1.0f;
    }

    public void enableAllCameras()
    {
        App.baseRenderer.parallaxCamera.isInUse   = true;
        App.baseRenderer.tiledGameCamera.isInUse  = true;
        App.baseRenderer.spriteGameCamera.isInUse = true;
        App.baseRenderer.hudGameCamera.isInUse    = true;

        App.baseRenderer.isDrawingStage = true;
    }

    public void disableAllCameras()
    {
        App.baseRenderer.parallaxCamera.isInUse   = false;
        App.baseRenderer.tiledGameCamera.isInUse  = false;
        App.baseRenderer.spriteGameCamera.isInUse = false;
        App.baseRenderer.hudGameCamera.isInUse    = false;

        App.baseRenderer.isDrawingStage = false;
    }
}
