package com.richikin.runner.ui;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.richikin.runner.assets.GameAssets;
import com.richikin.runner.core.App;
import com.richikin.utilslib.graphics.GfxUtils;

public class ItemBar
{
    private static final int _X1     = 0;
    private static final int _X2     = 1;
    private static final int _Y      = 2;
    private static final int _WIDTH  = 3;
    private static final int _HEIGHT = 4;

    private static final int _RUNES_PANEL   = 0;
    private static final int _BOOKS_PANEL   = 1;
    private static final int _POTIONS_PANEL = 2;

    private static final int _COLLECT_PANEL = 0;
    private static final int _ITEMS_INDEX   = 1;
    private static final int _ITEM1         = 1;
    private static final int _ITEM2         = 2;
    private static final int _ITEM3         = 3;
    private static final int _ITEM4         = 4;
    private static final int _ITEM5         = 5;
    private static final int _ITEM6         = 6;
    private static final int _ITEM7         = 7;
    private static final int _ITEM8         = 8;

    private static final int[][] displayPos = new int[][]
        {
            //
            {374, 374, 17, 0, 0},  // Collection Panel

            //
            //
            {386, 386, 38, 48, 48},
            {452, 452, 38, 48, 48},
            {518, 518, 38, 48, 48},
            {582, 582, 38, 48, 48},
            {649, 649, 38, 48, 48},
            {715, 715, 38, 48, 48},
            {781, 781, 38, 48, 48},
            {847, 847, 38, 48, 48},
        };

    private TextureRegion[]   objectivesPanel;
    private TextureRegion[][] itemTextures;
    private TextureRegion[][] itemGreyTextures;

    public ItemBar()
    {
        create();
    }

    public void draw(float originX, float originY)
    {
        if (App.getHud().conversationPanel == null)
        {
            App.getSpriteBatch().draw
                (
                    objectivesPanel[App.getHud().itemPanelIndex],
                    originX + displayPos[_COLLECT_PANEL][_X1],
                    originY + displayPos[_COLLECT_PANEL][_Y]
                );

            drawItems(originX, originY);
        }
    }

    private void drawItems(float originX, float originY)
    {
        TextureRegion textureRegion;

        for (int i = 0; i < 8; i++)
        {
            if (App.gameProgress.collectItems[App.getHud().itemPanelIndex][i])
            {
                textureRegion = itemTextures[App.getHud().itemPanelIndex][i];
            }
            else
            {
                textureRegion = itemGreyTextures[App.getHud().itemPanelIndex][i];
            }

            App.getSpriteBatch().draw
                (
                    textureRegion,
                    originX + displayPos[_ITEMS_INDEX + i][_X1],
                    originY + displayPos[_ITEMS_INDEX + i][_Y],
                    textureRegion.getRegionWidth() * 1.5f,
                    textureRegion.getRegionHeight() * 1.5f
                );
        }
    }

    private void create()
    {
        itemTextures    = new TextureRegion[3][8];
        itemTextures[0] = new TextureRegion[8];
        itemTextures[1] = new TextureRegion[8];
        itemTextures[2] = new TextureRegion[8];

        GfxUtils gfxUtils = new GfxUtils();

        gfxUtils.splitRegion
            (
                App.getAssets().getAnimationRegion(GameAssets._RUNES_ASSET),
                GameAssets._RUNES_FRAMES,
                itemTextures[_RUNES_PANEL]
            );

        gfxUtils.splitRegion
            (
                App.getAssets().getAnimationRegion(GameAssets._BOOKS_ASSET),
                GameAssets._BOOKS_FRAMES,
                itemTextures[_BOOKS_PANEL]
            );

        gfxUtils.splitRegion
            (
                App.getAssets().getAnimationRegion(GameAssets._POTIONS_ASSET),
                GameAssets._POTIONS_FRAMES,
                itemTextures[_POTIONS_PANEL]
            );

        itemGreyTextures    = new TextureRegion[3][8];
        itemGreyTextures[0] = new TextureRegion[8];
        itemGreyTextures[1] = new TextureRegion[8];
        itemGreyTextures[2] = new TextureRegion[8];

        gfxUtils.splitRegion
            (
                App.getAssets().getAnimationRegion(GameAssets._GREY_RUNES_ASSET),
                GameAssets._RUNES_FRAMES,
                itemGreyTextures[_RUNES_PANEL]
            );

        gfxUtils.splitRegion
            (
                App.getAssets().getAnimationRegion(GameAssets._GREY_BOOKS_ASSET),
                GameAssets._BOOKS_FRAMES,
                itemGreyTextures[_BOOKS_PANEL]
            );

        gfxUtils.splitRegion
            (
                App.getAssets().getAnimationRegion(GameAssets._GREY_POTIONS_ASSET),
                GameAssets._POTIONS_FRAMES,
                itemGreyTextures[_POTIONS_PANEL]
            );

        objectivesPanel    = new TextureRegion[3];
        objectivesPanel[0] = App.getAssets().getObjectRegion("runes_panel");
        objectivesPanel[1] = App.getAssets().getObjectRegion("books_panel");
        objectivesPanel[2] = App.getAssets().getObjectRegion("potions_panel");
    }
}
