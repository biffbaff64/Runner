package com.richikin.runner.entities.managers;

import com.richikin.enumslib.GraphicID;
import com.richikin.runner.config.Settings;
import com.richikin.runner.core.App;
import com.richikin.runner.developer.Developer;
import com.richikin.runner.entities.objects.SpriteDescriptor;
import com.richikin.utilslib.graphics.GraphicIndex;
import com.richikin.utilslib.logging.Trace;
import com.richikin.utilslib.maths.SimpleVec2;

public class EnemyHandler extends GenericEntityManager
{
    private final GraphicIndex[] enemies =
        {
            new GraphicIndex(GraphicID.G_SOLDIER, 0, 0),
            new GraphicIndex(GraphicID.G_BOUNCER, 0, 0),
            new GraphicIndex(GraphicID.G_SCORPION, 0, 0),
        };

    public EnemyHandler()
    {
    }

    @Override
    public void init()
    {
        if (!Developer.isDevMode() || App.settings.isEnabled(Settings._ENABLE_ENEMIES))
        {
            Trace.__FILE_FUNC();

            for (GraphicIndex item : enemies)
            {
                item.currentTotal = 0;
            }

            update();
        }
    }

    @Override
    public void update()
    {
        if (!Developer.isDevMode() || App.settings.isEnabled(Settings._ENABLE_ENEMIES))
        {
            for (GraphicIndex enemy : enemies)
            {
                if (enemy.currentTotal < enemy.maxTotal)
                {
                    create(enemy.graphicID);
                    enemy.currentTotal++;
                }
            }
        }
    }

    @Override
    public void free(final GraphicID gid)
    {
        for (GraphicIndex item : enemies)
        {
            if (item.graphicID == gid)
            {
                item.currentTotal = Math.max(0, item.currentTotal - 1);
            }
        }
    }

    private void create(GraphicID graphicID)
    {
        if (App.entityUtils.canUpdate(graphicID))
        {
            SpriteDescriptor descriptor = App.entities.getDescriptor(graphicID);

        }
    }

    /**
     * Set up the initial position for the enemy specified
     * by graphicID.
     *
     * @param graphicID The GraphicID of the enemy.
     * @return SimpleVec2 holding X and Y.
     */
    private SimpleVec2 setInitialPosition(GraphicID graphicID)
    {
        return new SimpleVec2();
    }

    @Override
    public GraphicID getGID()
    {
        return GraphicID._ENEMY_MANAGER;
    }
}
