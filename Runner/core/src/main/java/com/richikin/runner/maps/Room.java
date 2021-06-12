package com.richikin.runner.maps;

import com.richikin.runner.entities.characters.JailKey;
import com.richikin.utilslib.logging.Trace;
import com.richikin.utilslib.maths.SimpleVec2;

public class Room
{
    public static final int _NORTH     = 0;
    public static final int _EAST      = 1;
    public static final int _SOUTH     = 2;
    public static final int _WEST      = 3;
    public static final int _START     = 4;
    public static final int _UNDEFINED = 5;

    public String       roomName;
    public SimpleVec2[] compassPoints;
    public int          row;
    public int          column;
    public JailKey      key;
    public int          mysteryChestsAvailable;

    /**
     * <p>
     */
    public Room()
    {
        this("");
    }

    /**
     * <p>
     */
    public Room(final String roomName)
    {
        this.roomName               = roomName;
        this.key                    = new JailKey();
        this.mysteryChestsAvailable = 0;

        this.row    = 0;
        this.column = 0;

        this.compassPoints         = new SimpleVec2[5];
        this.compassPoints[_NORTH] = new SimpleVec2();
        this.compassPoints[_EAST]  = new SimpleVec2();
        this.compassPoints[_SOUTH] = new SimpleVec2();
        this.compassPoints[_WEST]  = new SimpleVec2();
        this.compassPoints[_START] = new SimpleVec2();
    }

    /**
     * <p>
     */
    public void set(Room _reference)
    {
        this.roomName        = _reference.roomName;
        this.key.isCollected = false;
        this.key.isUsed      = false;

        this.row    = _reference.row;
        this.column = _reference.column;

        this.compassPoints[_NORTH] = _reference.compassPoints[_NORTH];
        this.compassPoints[_EAST]  = _reference.compassPoints[_EAST];
        this.compassPoints[_SOUTH] = _reference.compassPoints[_SOUTH];
        this.compassPoints[_WEST]  = _reference.compassPoints[_WEST];
        this.compassPoints[_START] = _reference.compassPoints[_START];
    }

    /**
     * <p>
     */
    public void debug()
    {
        Trace.__FILE_FUNC_WithDivider();
        Trace.dbg("roomName: " + roomName);
        Trace.dbg("row: " + row);
        Trace.dbg("column: " + column);
        Trace.dbg("compassPoints[_N]: " + compassPoints[_NORTH]);
        Trace.dbg("compassPoints[_E]: " + compassPoints[_EAST]);
        Trace.dbg("compassPoints[_S]: " + compassPoints[_SOUTH]);
        Trace.dbg("compassPoints[_W]: " + compassPoints[_WEST]);
        Trace.dbg("compassPoints[_START]: " + compassPoints[_START]);
    }
}
