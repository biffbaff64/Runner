package com.richikin.runner.maps;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.objects.TiledMapTileMapObject;
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
     * ------------------------------------------------------------------------------
     * Create the map data for the current level, Load the TileMap data, then
     * create the game map from that data.
     * ------------------------------------------------------------------------------
     */
    public void createMap()
    {
        Trace.__FILE_FUNC();

        for (IEntityManagerComponent component : App.entityData.managerList)
        {
            component.setPlaceable(false);
        }

        App.mapData.placementTiles.clear();
        App.mapData.autoFloors.clear();

        parseMarkerTiles();
        parseObjectTiles();

        if (AABBData.boxes().size == 0)
        {
            addDummyCollisionObject();
        }

        createCollisionBoxes();
    }

    /**
     * ------------------------------------------------------------------------------
     * Parse marker tiles from the TILES marker tiles layer. This layer
     * is for basic marker tiles with no properties. Eventually MarkerTiles and
     * ObjectTiles layers should be combined.
     * NB: Does NOT create entities. This just extracts markers from
     * the Tile map (Object Layer) and creates the necessary information from them.
     * ------------------------------------------------------------------------------
     */
    protected void parseMarkerTiles()
    {
        int xOffset = 0;
        int yOffset = 0;

        TileID    tileID;
        GraphicID graphicID = GraphicID.G_NO_ID;

        for (int y = 0; y < App.mapData.markerTilesLayer.getHeight(); y++)
        {
            for (int x = 0; x < App.mapData.markerTilesLayer.getWidth(); x++)
            {
                // getCell() returns null if the raw data at x,y is zero.
                // This is Ok because, here, that means there is no
                // marker tile to process.
                TiledMapTileLayer.Cell cell = App.mapData.markerTilesLayer.getCell(x, y);

                if (cell != null)
                {
                    tileID = TileID.fromValue(cell.getTile().getId());

                    boolean isSpawnPoint = false;
                    boolean isIgnoreTile = false;

                    for (SpriteDescriptor descriptor : App.entities.entityList)
                    {
                        if (descriptor._TILE.equals(tileID))
                        {
                            graphicID       = descriptor._GID;
                            isSpawnPoint    = true;
                        }
                    }

                    if (!isSpawnPoint)
                    {
                        switch (tileID)
                        {
                            case _NORTH_TILE:
                            case _EAST_TILE:
                            case _SOUTH_TILE:
                            case _WEST_TILE:
                            {
                                //
                                // These aren't spawn point tiles, but they also
                                // should not write error messages.
                                isIgnoreTile = true;
                            }
                            break;

                            default:
                                break;
                        }
                    }

                    if (isSpawnPoint)
                    {
                        switch (tileID)
                        {
                            case _ARROW_TILE:
                            case _GEM_TILE:
                            case _KEY_TILE:
                            case _COIN_TILE:
                            case _SHIELD_TILE:
                            case _CHEST_TILE:
                            case _MYSTERY_COIN:
                            case _MYSTERY_CHEST_TILE:
                            case _HIDDEN_COIN_TILE:
                            {
                                setEntityPlaceable(GraphicID._PICKUP, true);
                            }
                            break;

                            case _TORCH2_TILE:
                            case _CRATE_TILE:
                            case _BARREL_TILE:
                            case _POT_TILE:
                            case _SACKS_TILE:
                            case _GLOW_EYES_TILE:
                            {
                                setEntityPlaceable(GraphicID._DECORATION, true);
                            }
                            break;

                            case _VILLAGER_TILE:
                            {
                                setEntityPlaceable(GraphicID.G_VILLAGER, true);
                            }
                            break;

                            case _SOLDIER_TILE:
                            {
                                setEntityPlaceable(GraphicID.G_SOLDIER, true);
                            }
                            break;

                            case _TURRET_TILE:
                            {
                                setEntityPlaceable(GraphicID.G_TURRET, true);
                            }
                            break;

                            case _STORM_DEMON_TILE:
                            case _SCORPION_TILE:
                            case _BOUNCER_TILE:
                            {
                                setEntityPlaceable(GraphicID._MONSTER, true);
                            }
                            break;

                            case _LEVER_TILE:
                            case _DOOR_TILE:
                            case _ESCALATOR_UP_TILE:
                            case _ESCALATOR_DOWN_TILE:
                            case _ESCALATOR_LEFT_TILE:
                            case _ESCALATOR_RIGHT_TILE:
                            {
                                setEntityPlaceable(GraphicID._INTERACTIVE, true);
                            }
                            break;

                            default:
                                break;
                        }

                        SpriteDescriptor descriptor = App.entities.getDescriptor(graphicID);
                        descriptor._POSITION.x = xOffset;
                        descriptor._POSITION.y = yOffset;
                        descriptor._INDEX = App.entityData.entityMap.size;

                        App.mapData.placementTiles.add(descriptor);
                    }
                    else
                    {
                        if (!isIgnoreTile)
                        {
                            Trace.dbg
                                (
                                    " - Unknown tile: "
                                        + tileID
                                        + "(" + tileID.get() + ")"
                                        + " at " + x + ", " + y
                                );
                        }
                    }
                }

                xOffset++;
            }

            xOffset = 0;
            yOffset++;
        }

        Trace.divider();
    }

    /**
     * ------------------------------------------------------------------------------
     * Parse marker tiles from the OBJECT marker tiles layer. This layer
     * is for complicated markers that have a set of properties.
     * NB: Does NOT create entities. This just extracts markers from
     * the Tile map (Object Layer) and creates the necessary information from them.
     * ------------------------------------------------------------------------------
     */
    protected void parseObjectTiles()
    {
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
                                ObjectTileProperties properties = setObjectTileProperties(descriptor);

                                createPlacementTile(mapObject, descriptor, properties);
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * ------------------------------------------------------------------------------
     *
     * ------------------------------------------------------------------------------
     */
    private void setEntityPlaceable(GraphicID _gid, boolean _placeable)
    {
        for (IEntityManagerComponent component : App.entityData.managerList)
        {
            if (component.getGID() == _gid)
            {
                component.setPlaceable(_placeable);
            }
        }
    }

    /**
     * ------------------------------------------------------------------------------
     *
     * ------------------------------------------------------------------------------
     */
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

    /**
     * ------------------------------------------------------------------------------
     *
     * ------------------------------------------------------------------------------
     */
    protected void createCollisionBoxes()
    {
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
                        case "Spawn Free Zone":
                        {
                        }
                        break;

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
                        baseEntity.position    = new SimpleVec3
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
     * ------------------------------------------------------------------------------
     *
     * ------------------------------------------------------------------------------
     */
    private void createPlacementTile(int x, int y)
    {
    }

    /**
     * ------------------------------------------------------------------------------
     *
     * ------------------------------------------------------------------------------
     */
    private void createPlacementTile(MapObject _mapObject, SpriteDescriptor _descriptor, ObjectTileProperties _properties)
    {
        SpriteDescriptor markerTile = new SpriteDescriptor();

        markerTile._POSITION.x = (int) (((TiledMapTileMapObject) _mapObject).getX() / Gfx.getTileWidth());
        markerTile._POSITION.y = (int) (((TiledMapTileMapObject) _mapObject).getY() / Gfx.getTileHeight());
        markerTile._GID        = _descriptor._GID;
        markerTile._TILE       = _descriptor._TILE;
        markerTile._ASSET      = _descriptor._ASSET;
        markerTile._INDEX      = _descriptor._INDEX;
        markerTile._DIST       = new SimpleVec2F();
        markerTile._DIR        = new Direction();
        markerTile._SPEED      = new SimpleVec2F();

        //
        // Create the bounding box for this placement tile.
        markerTile._BOX = new Box
            (
                (int) (((TiledMapTileMapObject) _mapObject).getX()),
                (int) (((TiledMapTileMapObject) _mapObject).getY()),
                Gfx.getTileWidth(),
                Gfx.getTileHeight()
            );

        if (_properties.hasDistance)
        {
            markerTile._DIST.set
                (
                    ((int) _mapObject.getProperties().get("xdistance")),
                    ((int) _mapObject.getProperties().get("ydistance"))
                );
        }

        if (_properties.hasDirection)
        {
            markerTile._DIR.set
                (
                    _mapObject.getProperties().get("xdirection")
                        .equals("right") ? Movement._DIRECTION_RIGHT :
                        _mapObject.getProperties().get("xdirection")
                            .equals("left") ? Movement._DIRECTION_LEFT : Movement._DIRECTION_STILL,

                    _mapObject.getProperties().get("ydirection")
                        .equals("up") ? Movement._DIRECTION_UP :
                        _mapObject.getProperties().get("ydirection")
                            .equals("down") ? Movement._DIRECTION_DOWN : Movement._DIRECTION_STILL
                );
        }

        if (_properties.hasSpeed)
        {
            markerTile._SPEED.set
                (
                    ((float) _mapObject.getProperties().get("xspeed")),
                    ((float) _mapObject.getProperties().get("yspeed"))
                );
        }

        if (_properties.isLinked)
        {
            //
            // Fetch the link ID of the attached entity
            if (_mapObject.getProperties().get("connection") != null)
            {
                markerTile._LINK = (int) _mapObject.getProperties().get("connection");
            }
        }

        App.mapData.placementTiles.add(markerTile);
    }

    /**
     * ------------------------------------------------------------------------------
     *
     * ------------------------------------------------------------------------------
     */
    private ObjectTileProperties setObjectTileProperties(SpriteDescriptor _descriptor)
    {
        // TODO: 13/08/2020 - set properties based on the type of entity passed.
        // eg: The entity might have an initial direction and speed...

        return new ObjectTileProperties();
    }

    /**
     * ------------------------------------------------------------------------------
     *
     * ------------------------------------------------------------------------------
     */
    private void debugPlacementsTiles()
    {
        for (SpriteDescriptor tile : App.mapData.placementTiles)
        {
            tile.debug();
        }
    }

    /**
     * ------------------------------------------------------------------------------
     *
     * ------------------------------------------------------------------------------
     */
    private void debugCollisionBoxes()
    {
        Trace.__FILE_FUNC();

        for (CollisionObject object : AABBData.boxes())
        {
            Trace.dbg("_GID  : " + object.gid + "  _INDEX: " + object.index);
        }
    }
}
