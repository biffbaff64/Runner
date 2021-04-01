package com.richikin.runner.entities;

import com.badlogic.gdx.utils.Array;
import com.richikin.enumslib.ActionStates;
import com.richikin.enumslib.GraphicID;
import com.richikin.runner.config.AppConfig;
import com.richikin.runner.core.App;
import com.richikin.runner.entities.components.IEntityManagerComponent;
import com.richikin.runner.entities.managers.AlienManager;
import com.richikin.runner.entities.managers.PlayerManager;
import com.richikin.runner.entities.objects.GdxSprite;
import com.richikin.runner.entities.objects.IEntityManager;
import com.richikin.utilslib.logging.Trace;

public class  EntityManager implements IEntityManager
{
    // --------------------------------------------------
    //
    public static Array<GraphicID> enemies;

    static
    {
        enemies = new Array<>();
    }

    // --------------------------------------------------
    // Indexes into manager list
    public int _alienManagerIndex;

    // --------------------------------------------------
    // Indexes into entity list
    public int   _playerIndex;

    public PlayerManager playerManager;
    public RenderSystem  renderSystem;

    public boolean _playerReady;

    public EntityManager()
    {
        this.renderSystem = new RenderSystem();
    }

    @Override
    public void initialise()
    {
        Trace.__FILE_FUNC();

        _alienManagerIndex = App.entityData.addManager(new AlienManager());
    }

    public void initialiseForLevel()
    {
        Trace.__FILE_FUNC();

        AppConfig.entitiesExist = false;

        playerManager = new PlayerManager();
        playerManager.init();

        addBackgroundEntities();

        for (final IEntityManagerComponent system : App.entityData.managerList)
        {
            system.init();
        }

        App.entities.setAllEnemyStatuses();

        AppConfig.entitiesExist = true;
    }

    @Override
    public void updateSprites()
    {
        if (isEntityUpdateAllowed() && !AppConfig.gamePaused)
        {
            GdxSprite entity;

            //
            // Update all non-player entities.
            for (int i = 0; i < App.entityData.entityMap.size; i++)
            {
                entity = (GdxSprite) App.entityData.entityMap.get(i);

                if ((entity.getActionState() != ActionStates._DEAD) && (entity.gid != GraphicID.G_PLAYER))
                {
                    entity.preUpdate();
                    entity.update(entity.spriteNumber);
                }
            }

            //
            // Main Player, updated after all other entities.
            // Updated last to allow for possible reacting to
            // other entities actions.
            if (_playerReady && (App.getPlayer().getActionState() != ActionStates._DEAD))
            {
                App.getPlayer().preUpdate();
                App.getPlayer().update(_playerIndex);
            }

            //
            // Update the various entity managers. These updates will check
            // to see if any entities need re-spawning etc.
            if (!App.gameProgress.levelCompleted && !App.gameProgress.baseDestroyed)
            {
                for (final IEntityManagerComponent system : App.entityData.managerList)
                {
                    system.update();
                }
            }
        }
    }

    /**
     * Entity Tidy actions.
     * These are actions performed at the end
     * of each update.
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
                            case G_PLAYER, _CEILING, _GROUND -> {
                            }

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
        for (GraphicID gid : enemies)
        {
            if (gid == entity.gid)
            {
                App.entityData.managerList.get(_alienManagerIndex).free(gid);
            }
        }
    }

    /**
     * Update the indexes into the entity map
     * for the main entities
     */
    @Override
    public void updateIndexes()
    {
        _playerIndex = 0;

        for (int i = 0; i < App.entityData.entityMap.size; i++)
        {
            GdxSprite entity = (GdxSprite) App.entityData.entityMap.get(i);

            if (entity != null)
            {
                if (entity.gid == GraphicID.G_PLAYER)
                {
                    _playerIndex = i;
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
     * Background entities which are essentially just
     * decorations, such as ufos and twinkling stars.
     */
    private void addBackgroundEntities()
    {
        // --------------------------------------------------
        //
//        BackgroundObjectsManager backgroundObjectsManager = new BackgroundObjectsManager();
//        backgroundObjectsManager.addUFOs(2 + MathUtils.random(2));
//        backgroundObjectsManager.addTwinkleStars();
//
//        BarrierManager barrierManager = new BarrierManager();
//        barrierManager.init();
//        barrierManager.create();
    }

    @Override
    public void dispose()
    {
        enemies.clear();
        enemies = null;

        playerManager = null;
        renderSystem  = null;
    }
}
