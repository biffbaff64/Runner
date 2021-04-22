package com.richikin.runner.entities.managers;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.richikin.enumslib.GraphicID;
import com.richikin.runner.assets.GameAssets;
import com.richikin.runner.core.App;
import com.richikin.runner.entities.EntityStats;
import com.richikin.runner.entities.characters.*;
import com.richikin.runner.entities.objects.SpriteDescriptor;
import com.richikin.runner.maps.TiledUtils;
import com.richikin.utilslib.graphics.EntityCounts;
import com.richikin.utilslib.logging.Trace;

public class BlocksHandler extends GenericEntityManager
{
    private final EntityCounts[] blockTypes =
        {
            new EntityCounts(GraphicID.G_SPIKE_BALL, 0, 0),
            new EntityCounts(GraphicID.G_SPIKE_BLOCK_HORIZONTAL, 0, 0),
            new EntityCounts(GraphicID.G_SPIKE_BLOCK_VERTICAL, 0, 0),
            new EntityCounts(GraphicID.G_LOOP_BLOCK_HORIZONTAL, 0, 0),
            new EntityCounts(GraphicID.G_LOOP_BLOCK_VERTICAL, 0, 0),
            new EntityCounts(GraphicID.G_FLOATING_PLATFORM, 0, 0),
            new EntityCounts(GraphicID.G_BIG_BLOCK_HORIZONTAL, 0, 0),
            new EntityCounts(GraphicID.G_BIG_BLOCK_VERTICAL, 0, 0),
        };

    /**
     *
     */
    public BlocksHandler()
    {
    }

    /**
     *
     */
    @Override
    public void init()
    {
        Trace.__FILE_FUNC();

        for (EntityCounts item : blockTypes)
        {
            for (SpriteDescriptor descriptor : App.mapData.placementTiles)
            {
                if (descriptor._GID.equals(item.graphicID))
                {
                    item.currentTotal = 0;
                    item.maxTotal++;
                }
            }

            Trace.dbg("Entity " + item.graphicID + " : maxTotal = " + item.maxTotal);
        }
    }

    /**
     *
     */
    @Override
    public void create()
    {
        TiledUtils tiledUtils = new TiledUtils();

        for (EntityCounts item : blockTypes)
        {
            Array<SpriteDescriptor> tiles = tiledUtils.findMultiTiles(item.graphicID);

            if (tiles.size > 0)
            {
                for (SpriteDescriptor descriptor : tiles)
                {
                    descriptor._ASSET = checkAssetName(descriptor)._ASSET;

                    switch (item.graphicID)
                    {
                        case G_SPIKE_BALL -> {

                            SpikeBall spikeBall = new SpikeBall(item.graphicID);
                            spikeBall.initialise(descriptor);

                            App.entityData.addEntity(spikeBall);
                        }

                        case G_SPIKE_BLOCK_HORIZONTAL,
                            G_SPIKE_BLOCK_VERTICAL -> {

                            SpikeBlock spikeBlock = new SpikeBlock(item.graphicID);
                            spikeBlock.initialise(descriptor);

                            App.entityData.addEntity(spikeBlock);
                        }

                        case G_LOOP_BLOCK_HORIZONTAL,
                            G_LOOP_BLOCK_VERTICAL -> {

                            LoopBlock loopBlock = new LoopBlock(item.graphicID);
                            loopBlock.initialise(descriptor);

                            App.entityData.addEntity(loopBlock);
                        }

                        case G_FLOATING_PLATFORM -> {

                            FloatingPlatform platform = new FloatingPlatform(item.graphicID);
                            platform.initialise(descriptor);

                            App.entityData.addEntity(platform);
                        }

                        case G_BIG_BLOCK_HORIZONTAL,
                            G_BIG_BLOCK_VERTICAL -> {

                            BigBlock bigBlock = new BigBlock(item.graphicID);
                            bigBlock.initialise(descriptor);

                            App.entityData.addEntity(bigBlock);
                        }

                        default -> {
                        }
                    }

                    EntityStats.log(item.graphicID);
                }
            }
        }
    }

    /**
     *
     */
    private SpriteDescriptor checkAssetName(SpriteDescriptor descriptor)
    {
        final String[] spikeBallAssets =
            {
                GameAssets._SPIKE_BALL_1_ASSET,
                GameAssets._SPIKE_BALL_2_ASSET,
                GameAssets._SPIKE_BALL_3_ASSET,
            };

        if (graphicID == GraphicID.G_SPIKE_BALL)
        {
            descriptor._ASSET = spikeBallAssets[MathUtils.random(spikeBallAssets.length - 1)];
        }

        return descriptor;
    }

    /**
     *
     */
    public EntityCounts[] getBlockTypes()
    {
        return blockTypes;
    }
}
