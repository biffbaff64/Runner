package com.richikin.runner.core;

import com.badlogic.gdx.utils.Disposable;
import com.richikin.runner.assets.GameAssets;
import com.richikin.utilslib.logging.Trace;
import com.richikin.utilslib.maths.Item;
import com.richikin.utilslib.maths.NumberUtils;

import java.util.Arrays;

public class GameProgress implements Disposable
{
    public boolean isRestarting;
    public boolean levelCompleted;
    public boolean gameCompleted;
    public boolean gameSetupDone;

    public boolean playerLifeOver;
    public int     playerLevel;
    public boolean playerGameOver;
    public float   gameDiffculty;

    public  boolean     cloudDemonInView;
    public  boolean[][] collectItems;
    public  Item        score;
    public  Item        lives;
    public  Item        gemCount;
    public  Item        coinCount;
    public  Item        keyCount;
    public  Item        rescuedVillagers;
    private int         scoreStack;

    public GameProgress()
    {
    }

    public void resetProgress()
    {
        Trace.__FILE_FUNC();

        isRestarting   = false;
        levelCompleted = false;
        gameCompleted  = false;
        gameSetupDone  = false;
        gameDiffculty  = 1.0f;

        score            = new Item(0, GameConstants._MAX_SCORE, 0);
        lives            = new Item(0, GameConstants._MAX_LIVES, GameConstants._MAX_LIVES);
        gemCount         = new Item(0, GameConstants._MAX_GEMS);
        coinCount        = new Item(0, GameConstants._MAX_COINS);
        keyCount         = new Item(0, GameConstants._MAX_KEYS);
        rescuedVillagers = new Item(0, 0, GameConstants._MAX_PRISONERS);

        score.setRefillAmount(0);
        lives.setRefillAmount(GameConstants._MAX_LIVES);

        score.setToMinimum();
        lives.setToMaximum();

        collectItems    = new boolean[3][8];
        collectItems[0] = new boolean[GameAssets._RUNES_FRAMES];
        collectItems[1] = new boolean[GameAssets._BOOKS_FRAMES];
        collectItems[2] = new boolean[GameAssets._POTIONS_FRAMES];

        Arrays.fill(collectItems[0], false);
        Arrays.fill(collectItems[1], false);
        Arrays.fill(collectItems[2], false);

        playerLifeOver = false;
        playerLevel    = 1;
        playerGameOver = false;
        gameDiffculty  = 1.0f;
    }

    public void update()
    {
        switch (App.getAppState().peek())
        {
            case _STATE_PAUSED:
            case _STATE_GAME:
            case _STATE_MESSAGE_PANEL:
            {
                updateStacks();
            }
            break;

            default:
                break;
        }
    }

    public void stackPush(Stack stack, int amount)
    {
        if (stack == Stack._SCORE)
        {
            scoreStack += amount;
        }
    }

    private void updateStacks()
    {
        int amount;

        if (scoreStack > 0)
        {
            amount = NumberUtils.getCount(scoreStack);

            score.add(amount);
            scoreStack -= amount;
        }
    }

    public void closeLastGame()
    {
    }

    public void toMinimum()
    {
        score.setToMinimum();
        lives.setToMinimum();
        gemCount.setToMinimum();
        coinCount.setToMinimum();
        keyCount.setToMinimum();
        rescuedVillagers.setToMinimum();

        Arrays.fill(collectItems[0], false);
        Arrays.fill(collectItems[1], false);
        Arrays.fill(collectItems[2], false);

        playerLifeOver = false;
        playerLevel    = 1;
    }

    public float getGameDifficulty()
    {
        return gameDiffculty;
    }

    @Override
    public void dispose()
    {
        Trace.__FILE_FUNC();

        // DO NOT dispose of score and lives unless they have
        // been processed with regard to hiscores first.
    }

    public enum Stack
    {
        _SCORE,
        _GEM,
        _COIN,
        _KEY,
        _VILLAGER,
    }
}
