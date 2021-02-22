package com.richikin.runner.core;

import com.badlogic.gdx.utils.Disposable;
import com.richikin.utilslib.logging.Trace;
import com.richikin.utilslib.maths.Item;
import com.richikin.utilslib.maths.NumberUtils;

public class GameProgress implements Disposable
{
    public boolean isRestarting;
    public boolean levelCompleted;
    public boolean gameCompleted;
    public boolean gameSetupDone;

    public boolean baseDestroyed;
    public boolean roverDestroyed;
    public int     activeCraterCount;
    public boolean playerLifeOver;
    public int     playerLevel;
    public boolean playerGameOver;
    public float   gameDiffculty;

    public enum Stack
    {
        _SCORE,
        _TIME,
        _FUEL
    }

    private final Item score;
    private final Item lives;

    private int scoreStack;

    public GameProgress()
    {
        score = new Item(0, GameConstants._MAX_SCORE, 0);
        lives = new Item(0, GameConstants._MAX_LIVES, GameConstants._MAX_LIVES);

        this.scoreStack = 0;
    }

    public void resetProgress()
    {
        Trace.__FILE_FUNC();

        isRestarting   = false;
        levelCompleted = false;
        gameCompleted  = false;
        gameSetupDone  = false;
        gameDiffculty  = 1.0f;

        score.setRefillAmount(0);
        lives.setRefillAmount(GameConstants._MAX_LIVES);

        score.setToMinimum();
        lives.setToMaximum();

        baseDestroyed     = false;
        roverDestroyed    = false;
        activeCraterCount = 0;
        playerLifeOver    = false;
        playerLevel       = 1;
        playerGameOver    = false;
        gameDiffculty     = 1.0f;
    }

    public void update()
    {
        switch (App.appState.peek())
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

    public Item getScore()
    {
        return score;
    }

    public Item getLives()
    {
        return lives;
    }

    public void closeLastGame()
    {
    }

    public void toMinimum()
    {
        score.setToMinimum();
        lives.setToMinimum();

        playerLevel = 1;
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
}
