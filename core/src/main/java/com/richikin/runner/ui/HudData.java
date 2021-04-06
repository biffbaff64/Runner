package com.richikin.runner.ui;

//@formatter:off
public class HudData
{
    public static final int _X1     = 0;
    public static final int _X2     = 1;
    public static final int _Y      = 2;
    public static final int _WIDTH  = 3;
    public static final int _HEIGHT = 4;

    public static final int _JOYSTICK      = 0;
    public static final int _BUTTON_X      = 1;
    public static final int _BUTTON_Y      = 2;
    public static final int _BUTTON_B      = 3;
    public static final int _BUTTON_A      = 4;
    public static final int _PAUSE         = 5;
    public static final int _COINS         = 6;
    public static final int _GEMS          = 7;
    public static final int _LIVES         = 8;
    public static final int _HEALTH        = 9;
    public static final int _VILLAGERS     = 10;
    public static final int _COMPASS       = 11;

    public final int[][] displayPos =
        {
            {  40,  820,   50,  240,  240},             // Joystick

            {1069,   22,   85,   96,   96},             // X
            {1128,   22,  141,   96,   96},             // Y
            {1188,   22,   85,   96,   96},             // B (Attack)
            {1128,   79,   29,   96,   96},             // A (Action)

            {1179, 1179,  630,   66,   66},             // Pause Button

            //
            // Y is distance from the TOP of the screen
            { 990,  990,   28,    0,    0},             // Coins total
            { 990,  990,   70,    0,    0},             // Gems total
            {  75,   75,   47,    0,    0},             // Life bar
            {  75,   75,   89,    0,    0},             // Health bar

            //
            // Y is distance from the TOP of the screen
            { 835,  835,   53,    0,    0},             // Villagers
            {1172, 1172,  101,    0,    0},             // Compass
        };

    public final int[][] livesDisplay =
        {
            {0, 0},
            {0, 0},
            {0, 0},
            {0, 0},
            {0, 0},
        };
}
