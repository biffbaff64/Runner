
package com.richikin.runner.graphics.effects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.richikin.runner.core.App;
import com.richikin.utilslib.logging.StopWatch;
import com.richikin.utilslib.physics.Movement;

import java.util.concurrent.TimeUnit;

public abstract class FadeEffect
{
    public static int     direction;
    public static float   speed;
    public static boolean isActive;

    private static StopWatch timer;
    private static float     width;
    private static float     height;
    private static Color     colour;
    private static NinePatch image;

    public static void createEffect(float _width, float _height)
    {
        image = new NinePatch(App.assets.getObjectRegion("bar9patch"), 1, 1, 1, 1);

        timer     = StopWatch.start();
        direction = Movement._DIRECTION_STILL;
        colour    = Color.BLACK;
        speed     = 0;
        width     = _width;
        height    = _height;
        isActive  = false;
    }

    public static boolean update()
    {
        if (isActive)
        {
            int _FADE_SPEED = 15;

            if (timer.time(TimeUnit.MILLISECONDS) >= _FADE_SPEED)
            {
                colour.a += (speed * direction);

                timer.reset();
            }

            return ((direction == Movement._DIRECTION_DOWN) && (colour.a < 0.0f))
                || ((direction == Movement._DIRECTION_UP) && (colour.a > 1.0f));
        }

        return true;
    }

    public static void render()
    {
        if (isActive)
        {
            if (colour.a >= 0)
            {
                image.setColor(colour);
                image.draw(App.spriteBatch, 0, 0, width, height);
            }
        }
    }

    public static void end()
    {
        direction = Movement._DIRECTION_STILL;
        isActive  = false;
    }

    /**
     * Fades from clear to Black.
     */
    public static void triggerFadeOut()
    {
        direction = Movement._DIRECTION_UP;
        colour.a  = 0.0f;
        speed     = 0.1f;
        isActive  = true;
    }

    /**
     * Fades from Black to clear.
     */
    public static void triggerFadeIn()
    {
        direction = Movement._DIRECTION_DOWN;
        colour.a  = 1.0f;
        speed     = 0.1f;
        isActive  = true;
    }
}
