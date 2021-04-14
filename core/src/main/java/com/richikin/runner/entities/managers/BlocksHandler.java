package com.richikin.runner.entities.managers;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.richikin.enumslib.GraphicID;
import com.richikin.runner.assets.GameAssets;
import com.richikin.runner.config.Settings;
import com.richikin.runner.core.App;
import com.richikin.runner.entities.objects.SpriteDescriptor;
import com.richikin.runner.maps.TiledUtils;
import com.richikin.utilslib.assets.GfxAsset;

public class BlocksHandler extends GenericEntityManager
{
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

    public BlocksHandler()
    {
    }

    @Override
    public void create()
    {
        TiledUtils tiledUtils = new TiledUtils();

        for (GraphicID gid : blockTypes)
        {
            Array<SpriteDescriptor> tiles = tiledUtils.findMultiTiles(gid);

            if (tiles.size > 0)
            {
                for (SpriteDescriptor descriptor : tiles)
                {
                    create
                        (
                            checkAssetName(gid).asset,
                            descriptor._FRAMES,
                            descriptor._PLAYMODE,
                            descriptor._POSITION.x,
                            descriptor._POSITION.y
                        );

                    super.descriptor._SIZE 
                }
            }
        }
    }

    private GfxAsset checkAssetName(GfxAsset gfxAsset)
    {
        final String[] spikeBallAssets =
            {
                GameAssets._SPIKE_BALL_1_ASSET,
                GameAssets._SPIKE_BALL_2_ASSET,
                GameAssets._SPIKE_BALL_3_ASSET,
            };

        if (gfxAsset.graphicID == GraphicID.G_SPIKE_BALL)
        {
            gfxAsset.asset = spikeBallAssets[MathUtils.random(spikeBallAssets.length - 1)];
        }

        return gfxAsset;
    }
}
