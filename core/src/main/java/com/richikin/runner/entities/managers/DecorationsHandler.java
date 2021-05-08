package com.richikin.runner.entities.managers;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.richikin.enumslib.GraphicID;
import com.richikin.runner.assets.GameAssets;
import com.richikin.runner.core.App;
import com.richikin.runner.entities.EntityStats;
import com.richikin.runner.entities.characters.Decoration;
import com.richikin.runner.entities.objects.SpriteDescriptor;
import com.richikin.utilslib.graphics.EntityCounts;
import com.richikin.utilslib.logging.Trace;

public class DecorationsHandler extends GenericEntityManager
{
    private final EntityCounts[] decorationsList =
        {
            new EntityCounts(GraphicID.G_ALCOVE_TORCH, 0, 0),
            new EntityCounts(GraphicID.G_BARREL, 0, 0),
            new EntityCounts(GraphicID.G_CRATE, 0, 0),
            new EntityCounts(GraphicID.G_GLOW_EYES, 0, 0),
            new EntityCounts(GraphicID.G_PLANT_POT, 0, 0),
            new EntityCounts(GraphicID.G_POT, 0, 0),
            new EntityCounts(GraphicID.G_WAGON_WHEEL, 0, 0),
            new EntityCounts(GraphicID.G_POT_STAND, 0, 0),
            new EntityCounts(GraphicID.G_ANVIL, 0, 0),
            new EntityCounts(GraphicID.G_HAMMERS, 0, 0),
        };

    public DecorationsHandler()
    {
    }

    @Override
    public void init()
    {
        Trace.__FILE_FUNC();

        for (EntityCounts item : decorationsList)
        {
            for (SpriteDescriptor descriptor : App.mapData.placementTiles)
            {
                if (descriptor._GID.equals(item.graphicID))
                {
                    item.currentTotal = 0;
                    item.maxTotal++;
                }
            }
        }

        create();
    }

    @Override
    public void create()
    {
        Trace.__FILE_FUNC();

        EntityManagerUtils managerUtils = new EntityManagerUtils();

        for (EntityCounts item : decorationsList)
        {
            Array<SpriteDescriptor> tiles = managerUtils.getDescriptorList(item.graphicID);

            if (tiles.size > 0)
            {
                for (SpriteDescriptor descriptor : tiles)
                {
                    descriptor._ASSET = checkAssetName(descriptor);

                    Decoration decoration = new Decoration(descriptor._GID);
                    decoration.initialise(descriptor);

                    App.entityData.addEntity(decoration);

                    EntityStats.log(descriptor._GID);

                    item.currentTotal++;
                }
            }
        }
    }

    @Override
    public void update()
    {
    }

    public EntityCounts[] getDecorationsList()
    {
        return decorationsList;
    }

    private String checkAssetName(SpriteDescriptor descriptor)
    {
        final String[] barrels =
            {
                GameAssets._BARREL_1_ASSET,
                GameAssets._BARREL_2_ASSET,
                GameAssets._BARREL_3_ASSET,
                GameAssets._BARREL_4_ASSET
            };

        final String[] pots =
            {
                GameAssets._POT_1_ASSET,
                GameAssets._POT_2_ASSET,
                GameAssets._POT_3_ASSET,
                GameAssets._POT_4_ASSET,
            };

        if (descriptor._GID == GraphicID.G_BARREL)
        {
            descriptor._ASSET = barrels[MathUtils.random(barrels.length - 1)];
        }
        else if (descriptor._GID == GraphicID.G_POT)
        {
            descriptor._ASSET = pots[MathUtils.random(pots.length - 1)];
        }

        return descriptor._ASSET;
    }
}
