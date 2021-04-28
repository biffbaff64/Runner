package com.richikin.runner.entities.managers;

import com.richikin.enumslib.GraphicID;
import com.richikin.runner.config.Settings;
import com.richikin.runner.core.App;
import com.richikin.runner.developer.Developer;
import com.richikin.utilslib.graphics.EntityCounts;
import com.richikin.utilslib.maths.SimpleVec2;

public class EnemyHandler extends GenericEntityManager
{
    private final EntityCounts[] enemies =
        {
            new EntityCounts(GraphicID.G_SOLDIER, 0, 0),
            new EntityCounts(GraphicID.G_BOUNCER, 0, 0),
            new EntityCounts(GraphicID.G_SCORPION, 0, 0),
        };

    public EnemyHandler()
    {
    }

    /**
     * Initialises all enemy tracking objects.
     */
    @Override
    public void init()
    {
        if (!Developer.isDevMode() || App.settings.isEnabled(Settings._ENABLE_ENEMIES))
        {
            for (EntityCounts item : enemies)
            {
                item.currentTotal = 0;
                item.maxTotal     = 0;
            }

            update();
        }
    }

    /**
     * Update all enemy trackers.
     * If the number of active enemies of a type is less than the max allowed,
     * a new enemy will be spawned.
     */
    @Override
    public void update()
    {
        if (!Developer.isDevMode() || App.settings.isEnabled(Settings._ENABLE_ENEMIES))
        {
            for (EntityCounts enemy : enemies)
            {
                if (enemy.currentTotal < enemy.maxTotal)
                {
                    create(enemy.graphicID);
                    enemy.currentTotal++;
                }
            }
        }
    }

    /**
     * Free an enemy of the specified type to allow for respawning.
     */
    @Override
    public void free(final GraphicID gid)
    {
        for (EntityCounts item : enemies)
        {
            if (item.graphicID == gid)
            {
                item.currentTotal = Math.max(0, item.currentTotal - 1);
            }
        }
    }

    /**
     * Spawn an enemy of the specified type. WIP
     */
    private void create(GraphicID graphicID)
    {
        if (App.entityUtils.canUpdate(graphicID))
        {
//            SpriteDescriptor descriptor = App.entities.getDescriptor(graphicID);

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

    /**
     *
     */
    @Override
    public GraphicID getGID()
    {
        return GraphicID._ENEMY_MANAGER;
    }
}
