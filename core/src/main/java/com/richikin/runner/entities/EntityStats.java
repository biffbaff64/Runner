package com.richikin.runner.entities;

import com.richikin.enumslib.GraphicID;
import com.richikin.utilslib.logging.Trace;

public class EntityStats
{
    public static final int MIN = 0;
    public static final int MAX = 1;
    public static final int NUM = 2;

    public static int[] mtrStormDemons = new int[]{0, 0, 0};
    public static int[] mtrBouncers    = new int[]{0, 0, 0};
    public static int[] mtrScorpions   = new int[]{0, 0, 0};
    public static int[] mtrSoldiers    = new int[]{0, 0, 0};

    public static void log(GraphicID graphicID)
    {
        switch (graphicID)
        {
            case G_STORM_DEMON -> mtrStormDemons[NUM]++;
            case G_BOUNCER -> mtrBouncers[NUM]++;
            case G_SCORPION -> mtrScorpions[NUM]++;
            case G_SOLDIER -> mtrSoldiers[NUM]++;

            case G_COIN,
                G_GEM,
                G_KEY,
                G_SHIELD,
                G_TREASURE_CHEST,
                G_SPIKE_BALL,
                G_SPIKE_BLOCK_VERTICAL,
                G_SPIKE_BLOCK_HORIZONTAL,
                G_LOOP_BLOCK_HORIZONTAL,
                G_LOOP_BLOCK_VERTICAL,
                G_PRISONER,
                G_VILLAGER,
                G_POT,
                G_PLANT_POT,
                G_CRATE,
                G_BARREL,
                G_ALCOVE_TORCH,
                G_FLAME_THROWER,
                G_DOOR,
                G_LOCKED_DOOR,
                G_FLOOR_BUTTON,
                G_LEVER_SWITCH,
                G_MYSTERY_CHEST,
                G_FLOATING_PLATFORM,
                G_TELEPORTER,
                G_TURRET,
                G_ESCALATOR,
                G_ESCALATOR_LEFT,
                G_ESCALATOR_RIGHT,
                G_ESCALATOR_UP,
                G_ESCALATOR_DOWN,
                G_GLOW_EYES,
                G_BOOK,
                G_LITTER,
                G_RUNE,
                G_SACKS -> {
            }

            default -> Trace.__FILE_FUNC("Unable to log " + graphicID);
        }
    }

    public void clearMaxCounts()
    {
        mtrBouncers[MIN]    = 0;
        mtrScorpions[MIN]   = 0;
        mtrSoldiers[MIN]    = 0;
        mtrStormDemons[MIN] = 0;
    }
}
