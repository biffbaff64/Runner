package com.richikin.runner.maps;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.richikin.enumslib.TileID;
import com.richikin.utilslib.logging.Trace;
import com.richikin.utilslib.maths.SimpleVec2;
import org.jetbrains.annotations.NotNull;

public class RoomManager implements Disposable
{
    private static final String _MAZE_NW = "maze1_nw.tmx";         //

    private static final String _PASSAGE_ES  = "passage_es.tmx";       //
    private static final String _PASSAGE_ESW = "passage_esw.tmx";      //
    private static final String _PASSAGE_NS  = "passage_ns.tmx";       //
    private static final String _PASSAGE_NES = "passage_nes.tmx";      //
    private static final String _PASSAGE_NE  = "passage_ne.tmx";       //
    private static final String _PASSAGE_S   = "passage_s.tmx";        //

    private static final String _PRISON_N = "prison_room_n.tmx";    //
    private static final String _PRISON_S = "prison_room_s.tmx";    //

    private static final String _E     = "room_e.tmx";           //
    private static final String _E2    = "room_e2.tmx";          //
    private static final String _EW    = "room_ew.tmx";          //
    private static final String _N     = "room_n.tmx";           //
    private static final String _N2    = "room_n2.tmx";          //
    private static final String _S2    = "room_s2.tmx";          //
    private static final String _NES   = "room_nes.tmx";         //
    private static final String _NESW  = "room_nesw.tmx";        //
    private static final String _NESW2 = "room_nesw2.tmx";       //
    private static final String _N3    = "room_n3.tmx";          //
    private static final String _NW    = "room_nw2.tmx";         //
    private static final String _NWS   = "room_nws.tmx";         //
    private static final String _S     = "room_s.tmx";           //
    private static final String _SEW   = "room_sew.tmx";         //
    private static final String _SN    = "room_sn.tmx";          //
    private static final String _W     = "room_w.tmx";           //
    private static final String _WS    = "room_ws.tmx";          //

    private static final String _ROOM1_NS    = "room1_ns.tmx";         //
    private static final String _ROOM2_NES   = "room2_nes.tmx";        //
    private static final String _ROOM2_NS    = "room2_ns.tmx";         //
    private static final String _ROOM3_NEW   = "room3_new.tmx";        //
    private static final String _ROOM4_ES    = "room4_es.tmx";         //
    private static final String _ROOM5_NE    = "room5_ne.tmx";         //
    private static final String _ROOM5B_NE   = "room5b_ne.tmx";        //
    private static final String _ROOM6_ESW   = "room6_esw.tmx";        //
    private static final String _ROOM7_NS    = "room7_ns.tmx";         //
    private static final String _ROOM8_NW    = "room8_nw.tmx";         //
    private static final String _ROOM9_NESW  = "room9_nesw.tmx";       //
    private static final String _ROOM10_NSW  = "room10_nsw.tmx";       //
    private static final String _ROOM11_NESW = "room11_nesw.tmx";      //
    private static final String _ROOM12_NS   = "room12_ns.tmx";        //
    private static final String _ROOM13_ESW  = "room13_esw.tmx";       //
    private static final String _ROOM13B_EW  = "room13b_ew.tmx";       //
    private static final String _ROOM14_NS   = "room14_ns.tmx";        //
    private static final String _ROOM15_NE   = "room15_ne.tmx";        //
    private static final String _ROOM16_NW   = "room16_nw.tmx";        //
    private static final String _ROOM17_N    = "room17_n.tmx";         //
    private static final String _ROOM18_NS   = "room18_ns.tmx";        //

    private static final String _SECRET1 = "room_secret1.tmx";     //

    private static final int    _DEFAULT_START_ROW    = 1;
    private static final int    _DEFAULT_START_COLUMN = 1;
    private static final String _START_ROOM           = _MAZE_NW;
    private static final int    _START_POSITION       = Room._START;
    private static final String _MAPS_PATH            = "maps/";

    //@formatter:off
    private final Room[][] roomMap =
        {
            // DO NOT CHANGE THIS LINE
            // -----------------------------------------
            {null, null, null, null, null, null, null, null},
            // -----------------------------------------
            // 0   1                        2                       3                       4                       5                       6                       7
            {null, new Room(_S2), null, null, null, null, new Room(_PRISON_S), null},  // 1
            {null, new Room(_ROOM1_NS), new Room(_SECRET1), new Room(_ROOM4_ES), new Room(_W), new Room(_S), new Room(_SN), null},  // 2
            {null, new Room(_PASSAGE_NES), new Room(_NESW2), new Room(_NESW), new Room(_ROOM6_ESW), new Room(_ROOM11_NESW), new Room(_ROOM10_NSW), null},  // 3
            {null, new Room(_ROOM2_NES), new Room(_ROOM3_NEW), new Room(_ROOM9_NESW), new Room(_MAZE_NW), new Room(_ROOM7_NS), new Room(_PASSAGE_NS), null},  // 4
            {null, new Room(_N), new Room(_E), new Room(_ROOM5_NE), null, new Room(_NES), new Room(_NW), null},  // 5
            {null, new Room(_PASSAGE_ES), new Room(_WS), null, null, new Room(_ROOM12_NS), null, null},  // 6
            {null, new Room(_ROOM14_NS), new Room(_PASSAGE_NE), new Room(_PASSAGE_ESW), new Room(_ROOM13_ESW), new Room(_NWS), new Room(_PASSAGE_S), null},  // 7
            {null, new Room(_ROOM17_N), new Room(_N2), new Room(_ROOM18_NS), new Room(_ROOM2_NS), new Room(_ROOM15_NE), new Room(_ROOM16_NW), null},  // 8
            {null, null, null, new Room(_N3), new Room(_PRISON_N), null, null, null},  // 9

            // -----------------------------------------
            // DO NOT CHANGE THIS LINE
            // -----------------------------------------
            {null, null, null, null, null, null, null, null},
            // -----------------------------------------
        };
    //@formatter:on

    public int           roomIndex;
    public Array<String> roomList;
    public int           playerStart;
    public Room          activeRoom;
    public boolean       taskOffered;

    private final int worldWidth;
    private final int worldHeight;

    /**
     * <p>
     */
    public RoomManager()
    {
        Trace.__FILE_FUNC();

        worldWidth  = roomMap[0].length;
        worldHeight = roomMap.length;

        Trace.dbg("_WORLD_WIDTH : " + worldWidth);
        Trace.dbg("_WORLD_HEIGHT: " + worldHeight);
    }

    /**
     * <p>
     */
    public void initialise()
    {
        Trace.__FILE_FUNC();

        activeRoom = new Room();

        SimpleVec2 roomPos = createRoomList();
        storeEntryPoints();

        int startRow    = (roomPos == null) ? _DEFAULT_START_ROW : roomPos.getX();
        int startColumn = (roomPos == null) ? _DEFAULT_START_COLUMN : roomPos.getY();

        Trace.dbg("startRow   : ", startRow);
        Trace.dbg("startColumn: ", startColumn);
        Trace.dbg("roomIndex  : ", roomIndex);

        setRoom(startRow, startColumn, _START_POSITION);
    }

    /**
     * <p>
     */
    private SimpleVec2 findRoom(String roomName)
    {
        SimpleVec2 roomPosition = null;

        for (int row = 0; row < worldHeight && (roomPosition == null); row++)
        {
            for (int column = 0; column < worldWidth && (roomPosition == null); column++)
            {
                if (roomMap[row][column] != null)
                {
                    if (roomName.equals(roomMap[row][column].roomName))
                    {
                        roomPosition = new SimpleVec2(row, column);
                    }
                }
            }
        }

        return roomPosition;
    }

    /**
     * <p>
     */
    private void setRoom(int row, int column, int position)
    {
        if (roomMap[row][column] != null)
        {
            activeRoom.set(roomMap[row][column]);

            activeRoom.row    = row;
            activeRoom.column = column;
        }

        playerStart = position;
    }

    /**
     * <p>
     */
    public Room[][] getRoomMap()
    {
        return roomMap;
    }

    /**
     * <p>
     */
    public int getWorldWidth()
    {
        return worldWidth;
    }

    /**
     * <p>
     */
    public int getWorldHeight()
    {
        return worldHeight;
    }

    /**
     * <p>
     */
    public String getActiveRoomName()
    {
        String name = "null";

        if (roomMap[activeRoom.row][activeRoom.column] != null)
        {
            name = roomMap[activeRoom.row][activeRoom.column].roomName;
        }

        return name;
    }

    /**
     * <p>
     */
    public SimpleVec2 getStartPosition()
    {
        String     currentMap = roomMap[activeRoom.row][activeRoom.column].roomName;
        SimpleVec2 positions  = new SimpleVec2();

        for (int row = 0; row < worldHeight; row++)
        {
            for (int column = 0; column < worldWidth; column++)
            {
                if (roomMap[row][column] != null)
                {
                    if (currentMap.equals(roomMap[row][column].roomName))
                    {
                        positions.set(roomMap[row][column].compassPoints[playerStart]);
                    }
                }
            }
        }

        return positions;
    }

    /**
     * <p>
     */
    public String getCurrentMapNameWithPath()
    {
        return _MAPS_PATH + roomMap[activeRoom.row][activeRoom.column].roomName;
    }

    /**
     * <p>
     */
    @NotNull
    public String getMapNameWithPath(String roomName)
    {
        return _MAPS_PATH + roomName;
    }

    /**
     * <p>
     * `
     */
    private SimpleVec2 createRoomList()
    {
        SimpleVec2 startRoomPos = null;
        roomList = new Array<>();

        for (int roomRow = 0; roomRow < worldHeight; roomRow++)
        {
            for (int roomColumn = 0; roomColumn < worldWidth; roomColumn++)
            {
                Room room = roomMap[roomRow][roomColumn];

                if (room != null)
                {
                    roomList.add(room.roomName.toUpperCase());

                    if (room.roomName.equals(_START_ROOM))
                    {
                        startRoomPos = new SimpleVec2(roomRow, roomColumn);
                    }
                }
            }
        }

        roomList.sort();

        return startRoomPos;
    }

    /**
     * <p>
     */
    private int countRoomMarkers(TileID _marker, Room _room)
    {
        int markerCount = 0;

        if ((_room != null) && (_room.roomName != null))
        {
            TmxMapLoader      tmxMapLoader = new TmxMapLoader();
            TiledMap          map          = tmxMapLoader.load(getMapNameWithPath(_room.roomName));
            TiledMapTileLayer layer        = (TiledMapTileLayer) map.getLayers().get(MapData._MARKER_TILES);

            for (int row = 0; row < layer.getHeight(); row++)
            {
                for (int column = 0; column < layer.getWidth(); column++)
                {
                    TiledMapTileLayer.Cell cell = layer.getCell(column, row);

                    if (cell != null)
                    {
                        if (_marker.equals(TileID.fromValue(cell.getTile().getId())))
                        {
                            markerCount++;
                        }
                    }
                }
            }
        }

        return markerCount;
    }

    /**
     * Check all rooms for entry points, and store the coordinates in the roomList array
     */
    // TODO: 16/04/2021 - This is not a good idea and is far too slow. 
    // TODO: 16/04/2021 - Maybe it would be better to do this once during development 
    // TODO: 16/04/2021 - and store these values in a final array?
    private void storeEntryPoints()
    {
        for (int roomRow = 0; roomRow < worldHeight; roomRow++)
        {
            for (int roomColumn = 0; roomColumn < worldWidth; roomColumn++)
            {
                Room room = roomMap[roomRow][roomColumn];

                if (room != null)
                {
                    TmxMapLoader      tmxMapLoader = new TmxMapLoader();
                    TiledMap          map          = tmxMapLoader.load(getMapNameWithPath(room.roomName));
                    TiledMapTileLayer layer        = (TiledMapTileLayer) map.getLayers().get(MapData._MARKER_TILES);

                    room.mysteryChestsAvailable = 0;

                    for (int row = 0; row < layer.getHeight(); row++)
                    {
                        for (int column = 0; column < layer.getWidth(); column++)
                        {
                            TiledMapTileLayer.Cell cell = layer.getCell(column, row);

                            if (cell != null)
                            {
                                TileID tileID = TileID.fromValue(cell.getTile().getId());

                                switch (tileID)
                                {
                                    case _NORTH_TILE -> room.compassPoints[Room._NORTH].set(column, row);

                                    case _EAST_TILE -> room.compassPoints[Room._EAST].set(column, row);

                                    case _SOUTH_TILE -> room.compassPoints[Room._SOUTH].set(column, row);

                                    case _WEST_TILE -> room.compassPoints[Room._WEST].set(column, row);

                                    case _PLAYER_TILE -> room.compassPoints[Room._START].set(column, row);

                                    case _MYSTERY_CHEST_TILE -> room.mysteryChestsAvailable++;

                                    default -> {
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Leave the current room and enter the room to the North.
     */
    public void moveUp()
    {
        if (activeRoom.row > 0)
        {
            if (roomMap[activeRoom.row - 1][activeRoom.column] != null)
            {
                activeRoom.row--;

                //
                // Room._SOUTH is passed here because that is the
                // position in the room where the player will
                // appear upon entering.
                setRoom(activeRoom.row, activeRoom.column, Room._SOUTH);
            }
        }
    }

    /**
     * Leave the current room and enter the room to the South.
     */
    public void moveDown()
    {
        if (activeRoom.row < roomMap.length)
        {
            if (roomMap[activeRoom.row + 1][activeRoom.column] != null)
            {
                activeRoom.row++;

                // see moveUp()
                setRoom(activeRoom.row, activeRoom.column, Room._NORTH);
            }
        }
    }

    /**
     * Leave the current room and enter the room to the left.
     */
    public void moveLeft()
    {
        if (activeRoom.column > 0)
        {
            if (roomMap[activeRoom.row][activeRoom.column - 1] != null)
            {
                activeRoom.column--;

                // see moveUp()
                setRoom(activeRoom.row, activeRoom.column, Room._EAST);
            }
        }
    }

    /**
     * Leave the current room and enter the room to the right.
     */
    public void moveRight()
    {
        if (activeRoom.column < roomMap[activeRoom.column].length)
        {
            if (roomMap[activeRoom.row][activeRoom.column + 1] != null)
            {
                activeRoom.column++;

                // see moveUp()
                setRoom(activeRoom.row, activeRoom.column, Room._WEST);
            }
        }
    }

    /**
     * <p>
     */
    private void debugRoomMap()
    {
        for (int _row = 0; _row < worldHeight; _row++)
        {
            Trace.divider();

            for (int _column = 0; _column < worldWidth; _column++)
            {
                if (roomMap[_row][_column] == null)
                {
                    Trace.dbg("null room");
                }
                else
                {
                    Trace.dbg(roomMap[_row][_column].roomName);
                }
            }
        }
    }

    /**
     * <p>
     */
    @Override
    public void dispose()
    {
        Trace.__FILE_FUNC();

        activeRoom = null;
    }
}
