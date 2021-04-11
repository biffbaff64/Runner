package com.richikin.runner.entities.managers;

import com.richikin.enumslib.GraphicID;
import com.richikin.runner.config.Settings;
import com.richikin.runner.core.App;
import com.richikin.runner.developer.Developer;
import com.richikin.utilslib.graphics.GraphicIndex;
import com.richikin.utilslib.logging.Trace;
import com.richikin.utilslib.maths.SimpleVec2;

public class EnemyManager extends GenericEntityManager
{
    private final GraphicIndex[] enemies =
        {
            new GraphicIndex(GraphicID.G_SOLDIER, 0),
            new GraphicIndex(GraphicID.G_BIG_BLOCK_HORIZONTAL, 0),
            new GraphicIndex(GraphicID.G_BIG_BLOCK_VERTICAL, 0),
            new GraphicIndex(GraphicID.G_BOUNCER, 0),
            new GraphicIndex(GraphicID.G_DOUBLE_SPIKE_BLOCK, 0),
            new GraphicIndex(GraphicID.G_FLAME_THROWER, 0),
            new GraphicIndex(GraphicID.G_FLAME_THROWER_VERTICAL, 0),
            new GraphicIndex(GraphicID.G_SCORPION, 0),
            new GraphicIndex(GraphicID.G_SPIKE_BALL, 0),
            new GraphicIndex(GraphicID.G_SPIKE_BLOCK_HORIZONTAL, 0),
            new GraphicIndex(GraphicID.G_SPIKE_BLOCK_VERTICAL, 0),
        };

    public EnemyManager()
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
                item.value = 0;
            }

            update();
        }
    }

    @Override
    public void free(final GraphicID gid)
    {
        for (GraphicIndex item : enemies)
        {
            if (item.graphicID == gid)
            {
                item.value = Math.max(0, item.value - 1);
            }
        }
    }

    @Override
    public void update()
    {
//        if (!Developer.isDevMode() || App.settings.isEnabled(Settings._ENABLE_ENEMIES))
//        {
//            for (GraphicIndex enemy : enemies)
//            {
//                if (enemy.value < App.roomManager.getMaxAllowed(enemy.graphicID))
//                {
//                    create(enemy.graphicID);
//                    enemy.value++;
//                }
//            }
//        }
    }

    private void create(GraphicID graphicID)
    {
        if (App.entityUtils.canUpdate(graphicID))
        {
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
    }

    @Override
    public GraphicID getGID()
    {
        return GraphicID._ENEMY_MANAGER;
    }
}
