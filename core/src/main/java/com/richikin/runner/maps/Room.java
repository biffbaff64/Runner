
package com.richikin.runner.maps;

import com.richikin.runner.entities.characters.JailKey;
import com.richikin.utilslib.logging.Trace;
import com.richikin.utilslib.maths.SimpleVec2;

public class Room
{
    public static final int _N         = 0;
    public static final int _E         = 1;
    public static final int _S         = 2;
    public static final int _W         = 3;
    public static final int _START     = 4;
    public static final int _UNDEFINED = 5;

    public String       roomName;
    public SimpleVec2[] compassPoints;
    public int          row;
    public int          column;
    public JailKey      key;
    public int          mysteryChestsAvailable;

    /**
     * ------------------------------------------------------------------------------
     *
     * ------------------------------------------------------------------------------
     */
    public Room()
    {
        this("");
    }

    /**
     * ------------------------------------------------------------------------------
     *
     * ------------------------------------------------------------------------------
     */
    public Room(final String roomName)
    {
        this.roomName               = roomName;
        this.key                    = new JailKey();
        this.mysteryChestsAvailable = 0;

        this.row    = 0;
        this.column = 0;

        this.compassPoints         = new SimpleVec2[5];
        this.compassPoints[_N]     = new SimpleVec2();
        this.compassPoints[_E]     = new SimpleVec2();
        this.compassPoints[_S]     = new SimpleVec2();
        this.compassPoints[_W]     = new SimpleVec2();
        this.compassPoints[_START] = new SimpleVec2();
    }

    /**
     * ------------------------------------------------------------------------------
     *
     * ------------------------------------------------------------------------------
     */
    public void set(Room _reference)
    {
        this.roomName        = _reference.roomName;
        this.key.isCollected = false;
        this.key.isUsed      = false;

        this.row    = _reference.row;
        this.column = _reference.column;

        this.compassPoints[_N]     = _reference.compassPoints[_N];
        this.compassPoints[_E]     = _reference.compassPoints[_E];
        this.compassPoints[_S]     = _reference.compassPoints[_S];
        this.compassPoints[_W]     = _reference.compassPoints[_W];
        this.compassPoints[_START] = _reference.compassPoints[_START];
    }

    /**
     * ------------------------------------------------------------------------------
     *
     * ------------------------------------------------------------------------------
     */
    public void debug()
    {
        Trace.__FILE_FUNC_WithDivider();
        Trace.dbg("roomName: " + roomName);
        Trace.dbg("row: " + row);
        Trace.dbg("column: " + column);
        Trace.dbg("compassPoints[_N]: " + compassPoints[_N]);
        Trace.dbg("compassPoints[_E]: " + compassPoints[_E]);
        Trace.dbg("compassPoints[_S]: " + compassPoints[_S]);
        Trace.dbg("compassPoints[_W]: " + compassPoints[_W]);
        Trace.dbg("compassPoints[_START]: " + compassPoints[_START]);
    }
}
