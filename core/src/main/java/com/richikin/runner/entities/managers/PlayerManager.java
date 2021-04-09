package com.richikin.runner.entities.managers;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.richikin.enumslib.GraphicID;
import com.richikin.runner.assets.GameAssets;
import com.richikin.runner.core.App;
import com.richikin.runner.entities.hero.MainPlayer;
import com.richikin.runner.entities.objects.SpriteDescriptor;
import com.richikin.utilslib.logging.Trace;

public class PlayerManager extends GenericEntityManager
{
    public int playerTileX;
    public int playerTileY;

    private SpriteDescriptor descriptor;

    public PlayerManager()
    {
    }

    public void init()
    {
        setSpawnPoint();
        createPlayer();
    }

    public void createPlayer()
    {
        Trace.__FILE_FUNC();

        App.entityManager._playerIndex = 0;
        App.entityManager._playerReady = false;

        App.entities.mainPlayer = new MainPlayer();
        App.entities.mainPlayer.initialise(descriptor);
        App.entityData.addEntity(App.entities.mainPlayer);

        App.entityManager.updateIndexes();
        App.entityManager._playerReady = true;
        App.entityManager._playerIndex = descriptor._INDEX;

        App.entities.mainPlayer.addCollisionListener(App.entities.mainPlayer.collision);
    }

    public void setSpawnPoint()
    {
        Trace.__FILE_FUNC();

        playerTileX = App.roomManager.getStartPosition().getX();
        playerTileY = App.roomManager.getStartPosition().getY();

        descriptor             = App.entities.getDescriptor(GraphicID.G_PLAYER);
        descriptor._PLAYMODE   = Animation.PlayMode.LOOP;
        descriptor._POSITION.x = playerTileX;
        descriptor._POSITION.y = playerTileY;
        descriptor._POSITION.z = App.entityUtils.getInitialZPosition(GraphicID.G_PLAYER);
        descriptor._INDEX      = App.entityData.entityMap.size;
        descriptor._SIZE       = GameAssets.getAssetSize(GraphicID.G_PLAYER);
    }
}
