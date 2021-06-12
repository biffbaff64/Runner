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
            case G_STORM_DEMON:
                mtrStormDemons[NUM]++;
                break;

            case G_BOUNCER:
                mtrBouncers[NUM]++;
                break;

            case G_SCORPION:
                mtrScorpions[NUM]++;
                break;

            case G_SOLDIER:
                mtrSoldiers[NUM]++;
                break;

            case G_COIN:
            case G_GEM:
            case G_KEY:
            case G_SHIELD:
            case G_TREASURE_CHEST:
            case G_SPIKE_BALL:
            case G_SPIKE_BLOCK_VERTICAL:
            case G_SPIKE_BLOCK_HORIZONTAL:
            case G_LOOP_BLOCK_HORIZONTAL:
            case G_LOOP_BLOCK_VERTICAL:
            case G_PRISONER:
            case G_VILLAGER:
            case G_POT:
            case G_PLANT_POT:
            case G_CRATE:
            case G_BARREL:
            case G_ALCOVE_TORCH:
            case G_FLAME_THROWER:
            case G_DOOR:
            case G_LOCKED_DOOR:
            case G_FLOOR_BUTTON:
            case G_LEVER_SWITCH:
            case G_MYSTERY_CHEST:
            case G_FLOATING_PLATFORM:
            case G_TELEPORTER:
            case G_TURRET:
            case G_ESCALATOR:
            case G_ESCALATOR_LEFT:
            case G_ESCALATOR_RIGHT:
            case G_ESCALATOR_UP:
            case G_ESCALATOR_DOWN:
            case G_GLOW_EYES:
            case G_BOOK:
            case G_LITTER:
            case G_RUNE:
            case G_SACKS:
            {
            }
            break;

            default:
            {
                Trace.__FILE_FUNC("Unable to log " + graphicID);
            }
            break;
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
