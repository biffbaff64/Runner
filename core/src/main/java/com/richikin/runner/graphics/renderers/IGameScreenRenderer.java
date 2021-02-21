
package com.richikin.runner.graphics.renderers;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.richikin.runner.graphics.camera.OrthoGameCamera;

public interface IGameScreenRenderer
{
    void render(SpriteBatch spriteBatch, OrthoGameCamera guiCamera);
}
