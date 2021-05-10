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
    //
    // This only needs to be a list of the GraphicID types,
    // unlike pickups, for instance, as these entities
    // cannot be destroyed and respawned.
    private final GraphicID[] blockTypes =
        {
            GraphicID.G_SPIKE_BALL,
            GraphicID.G_SPIKE_BLOCK_HORIZONTAL,
            GraphicID.G_SPIKE_BLOCK_VERTICAL,
            GraphicID.G_LOOP_BLOCK_HORIZONTAL,
            GraphicID.G_LOOP_BLOCK_VERTICAL,
            GraphicID.G_FLOATING_PLATFORM,
            GraphicID.G_BIG_BLOCK_HORIZONTAL,
            GraphicID.G_BIG_BLOCK_VERTICAL,
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

        create();
    }

    /**
     *
     */
    @Override
    public void create()
    {
        TiledUtils tiledUtils = new TiledUtils();

        for (GraphicID item : blockTypes)
        {
            Array<SpriteDescriptor> tiles = tiledUtils.findMultiTiles(item);

            if (tiles.size > 0)
            {
                for (int i=0; i< tiles.size; i++)
                {
                    SpriteDescriptor descriptor = App.entities.getDescriptor(item);

                    descriptor._ASSET = checkAssetName(descriptor)._ASSET;

                    switch (item)
                    {
                        case G_SPIKE_BALL -> {

                            SpikeBall spikeBall = new SpikeBall(item);
                            spikeBall.initialise(descriptor);

                            App.entityData.addEntity(spikeBall);
                        }

                        case G_SPIKE_BLOCK_HORIZONTAL,
                            G_SPIKE_BLOCK_VERTICAL -> {

                            SpikeBlock spikeBlock = new SpikeBlock(item);
                            spikeBlock.initialise(descriptor);

                            App.entityData.addEntity(spikeBlock);
                        }

                        case G_LOOP_BLOCK_HORIZONTAL,
                            G_LOOP_BLOCK_VERTICAL -> {

                            LoopBlock loopBlock = new LoopBlock(item);
                            loopBlock.initialise(descriptor);

                            App.entityData.addEntity(loopBlock);
                        }

                        case G_FLOATING_PLATFORM -> {

                            FloatingPlatform platform = new FloatingPlatform(item);
                            platform.initialise(descriptor);

                            App.entityData.addEntity(platform);
                        }

                        case G_BIG_BLOCK_HORIZONTAL,
                            G_BIG_BLOCK_VERTICAL -> {

                            BigBlock bigBlock = new BigBlock(item);
                            bigBlock.initialise(descriptor);

                            App.entityData.addEntity(bigBlock);
                        }

                        default -> {
                        }
                    }

                    EntityStats.log(item);
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
}
