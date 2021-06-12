package com.richikin.runner.graphics.renderers;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.richikin.runner.config.Settings;
import com.richikin.runner.core.App;
import com.richikin.runner.developer.DebugRenderer;
import com.richikin.runner.graphics.camera.OrthoGameCamera;

public class WorldRenderer implements IGameScreenRenderer
{
    public WorldRenderer()
    {
    }

    @Override
    public void render(SpriteBatch spriteBatch, OrthoGameCamera gameCamera)
    {
        switch (App.getAppState().peek())
        {
            case _STATE_SETUP:
            case _STATE_GET_READY:
            case _STATE_PAUSED:
            case _STATE_LEVEL_RETRY:
            case _STATE_LEVEL_FINISHED:
            case _STATE_GAME:
            case _STATE_ANNOUNCE_MISSILE:
            case _STATE_SETTINGS_PANEL:
            case _STATE_TELEPORTING:
            case _STATE_DEBUG_HANG:
            {
                if (!App.settings.isEnabled(Settings._USING_ASHLEY_ECS))
                {
                    App.entityManager.drawSprites();
                }

                DebugRenderer.drawBoxes();
            }
            break;

            case _STATE_MAIN_MENU:
            case _STATE_CLOSING:
            case _STATE_GAME_OVER:
            case _STATE_END_GAME:
            default:
                break;
        }
    }
}
