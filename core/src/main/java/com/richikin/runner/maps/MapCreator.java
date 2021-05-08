package com.richikin.runner.maps;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.objects.TiledMapTileMapObject;
import com.badlogic.gdx.utils.Array;
import com.richikin.enumslib.GraphicID;
import com.richikin.enumslib.TileID;
import com.richikin.runner.config.Settings;
import com.richikin.runner.core.App;
import com.richikin.runner.entities.components.IEntityManagerComponent;
import com.richikin.runner.entities.objects.BaseEntity;
import com.richikin.runner.entities.objects.SpriteDescriptor;
import com.richikin.runner.graphics.Gfx;
import com.richikin.runner.physics.aabb.CollisionObject;
import com.richikin.utilslib.logging.Trace;
import com.richikin.utilslib.maths.Box;
import com.richikin.utilslib.maths.SimpleVec2F;
import com.richikin.utilslib.maths.SimpleVec3;
import com.richikin.utilslib.physics.Direction;
import com.richikin.utilslib.physics.Movement;
import com.richikin.utilslib.physics.aabb.AABBData;

public class MapCreator
{
    public MapCreator()
    {
    }

    /**
     * Create the map data for the current level, Load the TileMap data, then
     * create the game map from that data.
     */
    public void createMap()
    {
        Trace.__FILE_FUNC();

        for (IEntityManagerComponent component : App.entityData.managerList)
        {
            component.setPlaceable(false);
        }

        App.mapData.placementTiles = new Array<>();
        App.mapData.autoFloors     = new Array<>();

        parseMarkerTiles();
//        parseObjectTiles();

        if (AABBData.boxes().size == 0)
        {
            addDummyCollisionObject();
        }

        createCollisionBoxes();
    }

    /**
     * Parse marker tiles from the TILES marker tiles layer. This layer
     * is for basic marker tiles with no properties. Eventually MarkerTiles and
     * ObjectTiles layers should be combined.
     * <p>
     * The array {@link MapData#placementTiles} will be filled with all relevant
     * data needed to define and place game entities, except for the MainPlayer.
     * No entities will be created, this will be done later.
     */
    protected void parseMarkerTiles()
    {
        Trace.__FILE_FUNC();

        int     xOffset    = 0;
        int     yOffset    = 0;
        int     arrayIndex = 0;
        TileID  tileID;
        boolean isIgnoreTile;

        for (int y = 0; y < App.mapData.markerTilesLayer.getHeight(); y++)
        {
            for (int x = 0; x < App.mapData.markerTilesLayer.getWidth(); x++)
            {
                // getCell() returns null if the raw data at x,y is zero. This is Ok
                // because, here, that means there is no marker tile to process.
                TiledMapTileLayer.Cell cell = App.mapData.markerTilesLayer.getCell(x, y);

                if (cell != null)
                {
                    tileID = TileID.fromValue(cell.getTile().getId());

                    switch (tileID)
                    {
                        //
                        // Marker tiles that should not create entries
                        // in the placements array.
                        case _NORTH_TILE,
                            _EAST_TILE,
                            _SOUTH_TILE,
                            _WEST_TILE,
                            _PLAYER_TILE -> {

                            isIgnoreTile = true;
                        }

                        case _ARROW_TILE,
                            _GEM_TILE,
                            _KEY_TILE,
                            _COIN_TILE,
                            _SHIELD_TILE,
                            _CHEST_TILE,
                            _MYSTERY_COIN,
                            _MYSTERY_CHEST_TILE,
                            _HIDDEN_COIN_TILE -> {

                            setEntityPlaceable(GraphicID._PICKUP, true);
                            isIgnoreTile = false;
                        }

                        case _ALCOVE_TORCH_TILE,
                            _CRATE_TILE,
                            _BARREL_TILE,
                            _POT_TILE,
                            _PLANT_POT_TILE,
                            _SACKS_TILE,
                            _GLOW_EYES_TILE -> {

                            setEntityPlaceable(GraphicID._DECORATION, true);
                            isIgnoreTile = false;
                        }

                        case _VILLAGER_TILE -> {

                            setEntityPlaceable(GraphicID.G_VILLAGER, true);
                            isIgnoreTile = false;
                        }

                        case _SOLDIER_TILE -> {

                            setEntityPlaceable(GraphicID.G_SOLDIER, true);
                            isIgnoreTile = false;
                        }

                        case _TURRET_TILE -> {

                            setEntityPlaceable(GraphicID.G_TURRET, true);
                            isIgnoreTile = false;
                        }

                        case _STORM_DEMON_TILE,
                            _SCORPION_TILE,
                            _BOUNCER_TILE -> {

                            setEntityPlaceable(GraphicID._MONSTER, true);
                            isIgnoreTile = false;
                        }

                        case _LEVER_TILE,
                            _DOOR_TILE,
                            _ESCALATOR_UP_TILE,
                            _ESCALATOR_DOWN_TILE,
                            _ESCALATOR_LEFT_TILE,
                            _ESCALATOR_RIGHT_TILE -> {

                            setEntityPlaceable(GraphicID._INTERACTIVE, true);
                            isIgnoreTile = false;
                        }

                        default -> {

                            Trace.dbg
                                (
                                    " - Unknown tile: "
                                        + tileID
                                        + "(" + tileID.get() + ")"
                                        + " at " + xOffset + ", " + yOffset
                                );

                            isIgnoreTile = true;
                        }
                    }

                    if (!isIgnoreTile)
                    {
                        SpriteDescriptor descriptor = new SpriteDescriptor();
                        descriptor.set(App.entities.getDescriptor(tileID));

                        descriptor._POSITION.x = xOffset;
                        descriptor._POSITION.y = yOffset;
                        descriptor._INDEX      = arrayIndex++;

                        App.mapData.placementTiles.add(descriptor);
                    }
                }

                xOffset++;
            }

            xOffset = 0;
            yOffset++;
        }

        Trace.divider();
        Trace.dbg("Number of Entites created from tiles: " + App.mapData.placementTiles.size);
        Trace.divider();
    }

    /**
     * Parse marker tiles from the OBJECT marker tiles layer. This layer
     * is for complicated markers that have a set of properties.
     * NB: Does NOT create entities. This just extracts markers from
     * the Tile map (Object Layer) and creates the necessary information from them.
     */
    protected void parseObjectTiles()
    {
        Trace.__FILE_FUNC();

        for (MapObject mapObject : App.mapData.mapObjects)
        {
            if (mapObject instanceof TiledMapTileMapObject)
            {
                //
                // Find the objects details ready for parsing
                if (null != mapObject.getName())
                {
                    for (SpriteDescriptor descriptor : App.entities.entityList)
                    {
                        if (mapObject.getName().equals(descriptor._NAME))
                        {
                            if (descriptor._GID != GraphicID.G_NO_ID)
                            {
                                //
                                // 'mapObject' will contain positioning, distance,
                                // speed, and direction information.
                                // 'descriptor' will contain identity information.
                                createPlacementTile(mapObject, descriptor);
                            }
                        }
                    }
                }
            }
        }
    }

    protected void createCollisionBoxes()
    {
        Trace.__FILE_FUNC();

        BaseEntity baseEntity;

        for (MapObject mapObject : App.mapData.mapObjects)
        {
            if (mapObject instanceof RectangleMapObject)
            {
                if (null != mapObject.getName())
                {
                    baseEntity = new BaseEntity();

                    switch (mapObject.getName().toLowerCase())
                    {
                        case "wall":
                        {
                            baseEntity.gid          = GraphicID._WALL;
                            baseEntity.type         = GraphicID._OBSTACLE;
                            baseEntity.bodyCategory = Gfx.CAT_WALL;
                            baseEntity.collidesWith = Gfx.CAT_PLAYER | Gfx.CAT_PLAYER_WEAPON | Gfx.CAT_MOBILE_ENEMY | Gfx.CAT_FIXED_ENEMY;
                        }
                        break;

                        case "Entity Barrier":
                        {
                            baseEntity.gid          = GraphicID._ENTITY_BARRIER;
                            baseEntity.type         = GraphicID._OBSTACLE;
                            baseEntity.bodyCategory = Gfx.CAT_ENTITY_BARRIER;
                            baseEntity.collidesWith = Gfx.CAT_DECORATION | Gfx.CAT_VILLAGER | Gfx.CAT_INTERACTIVE;
                        }
                        break;

                        case "Auto Floor":
                        {
                            baseEntity.gid          = GraphicID._AUTO_FLOOR;
                            baseEntity.type         = GraphicID._INTERACTIVE;
                            baseEntity.bodyCategory = Gfx.CAT_INTERACTIVE;
                            baseEntity.collidesWith = Gfx.CAT_PLAYER;

                            App.mapData.autoFloors.add(baseEntity);
                        }
                        break;

                        default:
                            break;
                    }

                    if (baseEntity.gid != GraphicID.G_NO_ID)
                    {
                        baseEntity.position = new SimpleVec3
                            (
                                (int) ((float) mapObject.getProperties().get("x")),
                                (int) ((float) mapObject.getProperties().get("y")),
                                0
                            );
                        baseEntity.frameWidth  = (int) ((float) mapObject.getProperties().get("width"));
                        baseEntity.frameHeight = (int) ((float) mapObject.getProperties().get("height"));

                        baseEntity.setCollisionObject(baseEntity.position.x, baseEntity.position.y);

                        if (App.settings.isEnabled(Settings._BOX2D_PHYSICS))
                        {
                            baseEntity.b2dBody = App.worldModel.bodyBuilder.createStaticBody(baseEntity);
                        }
                    }
                }
            }
        }
    }

    /**
     * Identifies the properties associated with this ObjectTile.
     */
    private ObjectTileProperties setObjectTileProperties(SpriteDescriptor descriptor)
    {
        ObjectTileProperties properties = new ObjectTileProperties();

        switch (descriptor._TILE)
        {
            case _FLOOR_BUTTON_TILE,
                _LEVER_TILE -> {

                properties.isLinked = true;
            }

            case _BIG_BLOCK_TILE,
                _LOOP_BLOCK_HORIZONTAL_TILE,
                _LOOP_BLOCK_VERTICAL_TILE,
                _SPIKE_BALL_TILE,
                _SPIKE_BLOCK_DOWN_TILE,
                _SPIKE_BLOCK_UP_TILE,
                _SPIKE_BLOCK_LEFT_TILE,
                _SPIKE_BLOCK_RIGHT_TILE -> {

                properties.hasDirection = true;
                properties.hasDistance  = true;
                properties.hasSpeed     = true;
            }

            default -> {
            }
        }

        return properties;
    }

    /**
     * Creates a new PlacementTile from a TiledMap MapObject.
     */
    private void createPlacementTile(MapObject mapObject, SpriteDescriptor descriptor)
    {
        descriptor._POSITION.x = (int) (((TiledMapTileMapObject) mapObject).getX() / Gfx.getTileWidth());
        descriptor._POSITION.y = (int) (((TiledMapTileMapObject) mapObject).getY() / Gfx.getTileHeight());
        descriptor._DIST       = new SimpleVec2F();
        descriptor._DIR        = new Direction();
        descriptor._SPEED      = new SimpleVec2F();

        //
        // Create the bounding box for this placement tile.
        descriptor._BOX = new Box
            (
                (int) (((TiledMapTileMapObject) mapObject).getX()),
                (int) (((TiledMapTileMapObject) mapObject).getY()),
                Gfx.getTileWidth(),
                Gfx.getTileHeight()
            );

        ObjectTileProperties properties = setObjectTileProperties(descriptor);

        if (properties.hasDistance)
        {
            descriptor._DIST.set
                (
                    ((int) mapObject.getProperties().get("xdistance")),
                    ((int) mapObject.getProperties().get("ydistance"))
                );
        }

        if (properties.hasDirection)
        {
            descriptor._DIR.set
                (
                    mapObject.getProperties().get("xdirection")
                        .equals("right") ? Movement._DIRECTION_RIGHT :
                        mapObject.getProperties().get("xdirection")
                            .equals("left") ? Movement._DIRECTION_LEFT : Movement._DIRECTION_STILL,

                    mapObject.getProperties().get("ydirection")
                        .equals("up") ? Movement._DIRECTION_UP :
                        mapObject.getProperties().get("ydirection")
                            .equals("down") ? Movement._DIRECTION_DOWN : Movement._DIRECTION_STILL
                );
        }

        if (properties.hasSpeed)
        {
            descriptor._SPEED.set
                (
                    ((float) mapObject.getProperties().get("xspeed")),
                    ((float) mapObject.getProperties().get("yspeed"))
                );
        }

        if (properties.isLinked)
        {
            //
            // Fetch the link ID of the attached entity
            if (mapObject.getProperties().get("connection") != null)
            {
                descriptor._LINK = (int) mapObject.getProperties().get("connection");
            }
        }

        descriptor._INDEX = App.mapData.placementTiles.size;
    }

    @SuppressWarnings("SameParameterValue")
    private void setEntityPlaceable(GraphicID gid, boolean placeable)
    {
        for (IEntityManagerComponent component : App.entityData.managerList)
        {
            if (component.getGID() == gid)
            {
                component.setPlaceable(placeable);
            }
        }
    }

    private void addDummyCollisionObject()
    {
        BaseEntity baseEntity;

        baseEntity                 = new BaseEntity();
        baseEntity.gid             = GraphicID.G_NO_ID;
        baseEntity.type            = GraphicID.G_NO_ID;
        baseEntity.position        = new SimpleVec3();
        baseEntity.collisionObject = App.collisionUtils.newObject();
        AABBData.add(baseEntity.collisionObject);
    }

    private void debugPlacementsTiles()
    {
        Trace.__FILE_FUNC();

        for (int i = 0; i < App.mapData.placementTiles.size; i++)
        {
            App.mapData.placementTiles.get(i).debug();
        }
    }

    private void debugCollisionBoxes()
    {
        Trace.__FILE_FUNC();

        for (CollisionObject object : AABBData.boxes())
        {
            Trace.dbg("_GID  : " + object.gid + "  _INDEX: " + object.index);
        }
    }
}
