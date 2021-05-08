package com.richikin.runner.entities;

import com.badlogic.gdx.utils.Array;
import com.richikin.enumslib.ActionStates;
import com.richikin.enumslib.GraphicID;
import com.richikin.runner.config.AppConfig;
import com.richikin.runner.core.App;
import com.richikin.runner.entities.components.IEntityManagerComponent;
import com.richikin.runner.entities.managers.BlocksHandler;
import com.richikin.runner.entities.managers.DecorationsHandler;
import com.richikin.runner.entities.managers.EnemyHandler;
import com.richikin.runner.entities.managers.PlayerManager;
import com.richikin.runner.entities.objects.GdxSprite;
import com.richikin.runner.entities.systems.RenderSystem;
import com.richikin.utilslib.entities.IEntityManager;
import com.richikin.utilslib.logging.Trace;

public class EntityManager implements IEntityManager
{
    public Array<GraphicID> enemies;
    public int              alienManagerIndex;
    public int              blocksManagerIndex;
    public int              decorationsManagerIndex;
    public int              playerIndex;
    public PlayerManager    playerManager;
    public RenderSystem     renderSystem;
    public boolean          playerReady;

    public EntityManager()
    {
        this.enemies      = new Array<>();
        this.renderSystem = new RenderSystem();
    }

    @Override
    public void initialise()
    {
        Trace.__FILE_FUNC();

        this.enemies.add(GraphicID.G_BOUNCER);
        this.enemies.add(GraphicID.G_SOLDIER);
        this.enemies.add(GraphicID.G_SCORPION);

        this.playerManager = new PlayerManager();

//        this.alienManagerIndex       = App.entityData.addManager(new EnemyHandler());
//        this.blocksManagerIndex      = App.entityData.addManager(new BlocksHandler());
        this.decorationsManagerIndex = App.entityData.addManager(new DecorationsHandler());
    }

    /**
     * Level-specific entity initialisation.
     */
    public void initialiseForLevel()
    {
        Trace.__FILE_FUNC();

        AppConfig.entitiesExist = false;

        playerManager.init();

        for (final IEntityManagerComponent component : App.entityData.managerList)
        {
            component.init();
        }

        App.entities.setAllEnemyStatuses();

        AppConfig.entitiesExist = true;
    }

    @Override
    public void updateSprites()
    {
        if (isEntityUpdateAllowed() && !AppConfig.gamePaused)
        {
            //
            // Update all non-player entities.
            for (int i = 0; i < App.entityData.entityMap.size; i++)
            {
                GdxSprite entity = (GdxSprite) App.entityData.entityMap.get(i);

                if ((entity.getActionState() != ActionStates._DEAD)
                    && (entity.gid != GraphicID.G_PLAYER))
                {
                    entity.preUpdate();
                    entity.update(entity.spriteNumber);
                }
            }

            //
            // Main Player, updated after all other entities.
            // Updated last to allow for possible reacting to
            // other entities actions.
            if (playerReady && (App.getPlayer().getActionState() != ActionStates._DEAD))
            {
                App.getPlayer().preUpdate();
                App.getPlayer().update(playerIndex);
            }

            //
            // Update the various entity managers. These updates will check
            // to see if any entities need re-spawning etc.
            if (!App.gameProgress.levelCompleted)
            {
                for (final IEntityManagerComponent component : App.entityData.managerList)
                {
                    component.update();
                }
            }
        }
    }

    /**
     * Entity Tidy actions.
     * These are actions performed at the end of each update.
     */
    @Override
    public void tidySprites()
    {
        if (App.entityData.entityMap != null)
        {
            for (int i = 0; i < App.entityData.entityMap.size; i++)
            {
                GdxSprite entity = (GdxSprite) App.entityData.entityMap.get(i);

                if (entity != null)
                {
                    if (entity.getActionState() != ActionStates._DEAD)
                    {
                        entity.postUpdate(i);
                    }

                    //
                    // NB: entity might have died in postUpdate, which is
                    // why this next if() is not an else.
                    if (entity.getActionState() == ActionStates._DEAD)
                    {
                        switch (entity.gid)
                        {
                            case G_PLAYER, _WALL, _GROUND -> {
                            }

                            //
                            // Explosions release and remove themselves
                            case G_EXPLOSION12, G_EXPLOSION64, G_EXPLOSION128, G_EXPLOSION256 -> {

                                entity.tidy(i);
                            }

                            default -> {

                                if (entity.gid != GraphicID.G_NO_ID)
                                {
                                    releaseEntity(entity);

                                    entity.collisionObject.kill();
                                    App.entityData.removeEntity(i);
                                }
                            }
                        }

                        updateIndexes();
                    }
                }
            }

            App.collisionUtils.tidy();
        }
    }

    /**
     * Draw all game entities
     */
    @Override
    public void drawSprites()
    {
        if (renderSystem != null)
        {
            renderSystem.drawSprites();
        }
    }

    @Override
    public void releaseEntity(GdxSprite entity)
    {
        if (!App.entityData.managerList.isEmpty())
        {
            for (GraphicID gid : enemies)
            {
                if (gid == entity.gid)
                {
                    App.entityData.managerList.get(alienManagerIndex).free(gid);
                }
            }
        }
    }

    /**
     * Update the indexes into the entity map for the main entities
     */
    @Override
    public void updateIndexes()
    {
        playerIndex = 0;

        for (int i = 0; i < App.entityData.entityMap.size; i++)
        {
            GdxSprite entity = (GdxSprite) App.entityData.entityMap.get(i);

            if (entity != null)
            {
                if (entity.gid == GraphicID.G_PLAYER)
                {
                    playerIndex = i;
                }
            }
        }
    }

    @Override
    public boolean isEntityUpdateAllowed()
    {
        return (AppConfig.entitiesExist && !AppConfig.quitToMainMenu);
    }

    /**
     * <p>
     */
    @Override
    public void dispose()
    {
        enemies.clear();
        enemies = null;

        playerManager = null;
        renderSystem  = null;
    }
}
