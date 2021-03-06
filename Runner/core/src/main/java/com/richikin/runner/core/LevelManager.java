package com.richikin.runner.core;

import com.richikin.enumslib.GraphicID;
import com.richikin.runner.config.AppConfig;
import com.richikin.runner.entities.EntityManager;
import com.richikin.runner.entities.EntityUtils;
import com.richikin.runner.maps.RoomManager;
import com.richikin.runner.physics.CollisionUtils;
import com.richikin.runner.ui.HeadsUpDisplay;
import com.richikin.utilslib.logging.Trace;
import com.richikin.utilslib.physics.aabb.AABBData;

public class LevelManager
{
    private boolean isFirstTime;

    public LevelManager()
    {
        isFirstTime = true;
    }

    // TODO: 23/04/2021 - EVERYTHING in this class needs correcting
    // TODO: 23/04/2021 - for this game, as this is from Jetman.

    /**
     * Gets the current level ready for playing.
     *
     * @param _firstTime TRUE if first call from power-up.
     */
    public void prepareCurrentLevel(boolean _firstTime)
    {
        if (App.gameProgress.isRestarting)
        {
            restartCurrentLevel();
        }
        else if (_firstTime || App.gameProgress.levelCompleted)
        {
            setupForNewLevel(_firstTime);

            App.gameProgress.resetProgress();
        }

        AppConfig.gamePaused      = false;
        AppConfig.quitToMainMenu  = false;
        AppConfig.forceQuitToMenu = false;

        App.gameProgress.isRestarting   = false;
        App.gameProgress.levelCompleted = false;
        App.gameProgress.playerGameOver = false;

        //
        // Centre the camera on the player
        if (App.getPlayer() != null)
        {
            App.mapUtils.positionAt((int) App.getPlayer().sprite.getX(), (int) App.getPlayer().sprite.getY());
        }

        if (_firstTime)
        {
            App.getHud().refillItems();
            App.getHud().update();
        }
    }

    /**
     * Set up the current level ready for starting.
     */
    public void setupForNewLevel(boolean firstTime)
    {
        Trace.__FILE_FUNC();

        App.collisionUtils.initialise();
        App.mapData.initialiseRoom();               // Load tiled map and create renderer
        App.mapCreator.createMap();                 // Process the tiled map data

        App.entityManager.initialiseForLevel();

        //
        // Create entity paths if any relevant data
        // exists in the tilemap data.
        App.pathUtils.setup();

        Trace.finishedMessage();
    }

    /**
     * Restart the current level (room), for instance after
     * the player loses a life.
     */
    public void restartCurrentLevel()
    {
        App.entityUtils.resetAllPositions();

        App.getPlayer().setup(false);

        if (App.gameProgress.levelCompleted)
        {
            App.entityManager.updateIndexes();
        }
    }

    /**
     * Actions to perform when a level has been completed.
     * Remove all entities/pickups/etc from the level, but
     * make sure that the main player is untouched.
     */
    public void closeCurrentLevel()
    {
        for (int i = 0; i < App.entityData.entityMap.size; i++)
        {
            if (App.entityData.entityMap.get(i).gid != GraphicID.G_PLAYER)
            {
                App.entityData.entityMap.removeIndex(i);
            }
        }

        App.mapData.placementTiles.clear();
        App.mapData.autoFloors.clear();
        App.mapData.placementTiles = null;
        App.mapData.autoFloors = null;

        App.mapData.currentMap.dispose();
    }

    /**
     * Set up everything for a NEW game only.
     */
    public void prepareNewGame()
    {
        if (isFirstTime)
        {
            //
            // Make sure all progress counters are initialised.
            App.gameProgress.resetProgress();

            //
            // Initialise the room that the game will start in.
            App.roomManager = new RoomManager();
            App.roomManager.initialise();

            //
            // Create collision and entity controllers.
            App.collisionUtils = new CollisionUtils();
            App.entityUtils    = new EntityUtils();
            App.entityManager  = new EntityManager();
            App.hud            = new HeadsUpDisplay();

            App.cameraUtils.disableAllCameras();
            App.baseRenderer.hudGameCamera.isInUse = true;
            App.baseRenderer.isDrawingStage        = true;

            App.entityData.createData();
            AABBData.createData();
            App.entityManager.initialise();
            App.mapData.update();
            App.getHud().createHud();
        }

        isFirstTime = false;
    }
}
