package com.android.jrobbins.dungeonmaker.hauberkMapper;

import android.app.ProgressDialog;
import android.graphics.Rect;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jim.robbins on 9/16/16.
 */

/// The alogoritms were converted from work done here: https://github.com/munificent/hauberk
//   The random dungeon generator.
///
/// Starting with a stage of solid walls, it works like so:
///
/// 1. Place a number of randomly sized and positioned rooms. If a room
///    overlaps an existing room, it is discarded. Any remaining rooms are
///    carved out.
/// 2. Any remaining solid areas are filled in with mazes. The maze generator
///    will grow and fill in even odd-shaped areas, but will not touch any
///    rooms.
/// 3. The result of the previous two steps is a series of unconnected rooms
///    and mazes. We walk the stage and find every tile that can be a
///    "connector". This is a solid tile that is adjacent to two unconnected
///    regions.
/// 4. We randomly choose connectors and open them or place a door there until
///    all of the unconnected regions have been joined. There is also a slight
///    chance to carve a connector between two already-joined regions, so that
///    the dungeon isn't single connected.
/// 5. The mazes will have a lot of dead ends. Finally, we remove those by
///    repeatedly filling in any open tile that's closed on three sides. When
///    this is done, every corridor in a maze actually leads somewhere.
///
/// The end result of this is a multiply-connected dungeon with rooms and lots
/// of winding corridors.

public class Dungeon extends StageBuilder {
    private static final String LOG_TAG = "Dungeon";
    public int numRoomTries = 200;

    /// Increasing this allows rooms to be larger.
    public int roomExtraSize = 5;

    public int windingPercent = 10;

    List<Rect> _rooms = new ArrayList<>();

    /// For each open position in the dungeon, the index of the connected region
    /// that that position is a part of.
    int[][] _regions;

    /// The index of the current region being carved.
    int _currentRegion = -1;
    public int dropSeed = 2;
    public int dungeonLevel = 1;

    @Override
    public void generate(Stage stage, ProgressDialog mProgress) {
        super.generate(stage, mProgress);

        fillTile(Tiles.wall);

        _regions = new int[bounds.width()][bounds.height()];

        _addRooms();

       // Fill in all of the empty space with mazes.

        Log.d(LOG_TAG,"Generate Maze");
        for (int y = 2; y < bounds.height()-1; y += 2) {
            for (int x = 2; x < bounds.width()-1; x += 2) {
                Vector2D pos = new Vector2D(x, y);
                if (getTile(pos) != Tiles.wall)
                    continue;

                _growMaze(pos);
            }
        }

        Log.d(LOG_TAG,"Remove deadends");
         _removeDeadEnds();

        for(Rect room : _rooms )
        {
            Log.d(LOG_TAG,"Add items and monsters to room:" + room.flattenToString());
            onDecorateRoom(room);
        }
    }

    public String displayMap()
    {
        String mapString = "";
        Log.d(LOG_TAG,"Display Map");
        for (int y = 0; y < bounds.height(); y++) {
            Log.d("Map","Writing row:" + y);
            for (int x = 0; x < bounds.width(); x++) {
                TileType tile = getTile(new Vector2D(x, y));
                if (tile != null) {
                    mapString += tile.appearance;
                }
            }
            mapString += "\n";
        }
        Log.d("Map",mapString);
        return mapString;
    }

    public String writeMap()
    {
        String htmlMapStr = "";
        TileType prevChar = Tiles.room;
        //String debugString = "";
        Log.d(LOG_TAG,"Write HTML Map");
        for (int y = 0; y < bounds.height(); y++) {
            Log.d("Map","Writing row:" + y);
            for (int x = 0; x < bounds.width(); x++) {
                TileType tile = getTile(new Vector2D(x, y));
                if (tile != null) {
                    if (prevChar.equals(tile) && x > 0) {
                        htmlMapStr += tile.appearance;
                    }
                    else
                    {
                        if (x>0) htmlMapStr += "</>";
                        htmlMapStr += "<font color=\""+tile.color+"\">"+tile.appearance;
                    }
                }
                prevChar = tile;
            }
            htmlMapStr += "</>";
            htmlMapStr += "\n";
        }
        htmlMapStr += "</>";
        return htmlMapStr;
    }

    public void onDecorateRoom(Rect room) {
        dropItems(room);
        breedMonsters(room);
    }

    private void breedMonsters(Rect room) {
        int numDrops = Utils.range(1,(2*dropSeed)*dungeonLevel);
        for (int i = 0; i < numDrops; i++) {
            setTile(findOpenTile(room), Monsters.randomMonster());
        }
    }

    private void dropItems(Rect room) {
        int numDrops = Utils.range(1,dropSeed*dungeonLevel);
        for (int i = 0; i < numDrops; i++) {
            setTile(findOpenTile(room), Items.randomItem());
        }
    }

    private Vector2D findOpenTile(Rect room)
    {
        Vector2D pos = null;
        while(true) {
            pos = new Vector2D(Utils.range(room.left, room.right), Utils.range(room.top, room.bottom));
            if(getTile(pos) == Tiles.room)
            {
                break;
            }
        }

        return pos;
    }

    /// Implementation of the "growing tree" algorithm from here:
    /// http://www.astrolog.org/labyrnth/algrithm.htm.
    private void _growMaze(Vector2D start) {
        List<Vector2D> cells = new ArrayList<>();
        Direction lastDir = null;

        _startRegion();
        _carve(start);

        cells.add(start);

        while (!cells.isEmpty()) {
            Vector2D cell = cells.get(cells.size()-1);

            // See which adjacent cells are open.
            List<Direction> unmadeCells = new ArrayList<>();

            for (Direction dir : Direction.cardinal) {
                if (_canCarve(cell, dir)) {
                    unmadeCells.add(dir);
                }
            }

            if (!unmadeCells.isEmpty()) {
                // Based on how "windy" passages are, try to prefer carving in the
                // same direction.
                Direction dir;
                if (unmadeCells.contains(lastDir) && Utils.range(100) > windingPercent) {
                    dir = lastDir;
                } else {
                    dir = Utils.rngDirInList(unmadeCells);
                }

                _carve(cell.plus(dir));
                _carve(cell.plus(dir.scale(2)));

                cells.add(cell.plus(dir.scale(2)));

                lastDir = dir;
            } else {
                // No adjacent uncarved cells.
                cells.remove(cell);

                // This path has ended.
                lastDir = null;
            }
        }
    }

    /// Places rooms ignoring the existing maze corridors.
    void _addRooms() {
        Log.d(LOG_TAG,"addRooms");
        for (int i = 0; i < numRoomTries; i++) {
            String logStr = "";
            // Pick a random room size. The funny math here does two things:
            // - It makes sure rooms are odd-sized to line up with maze.
            // - It avoids creating rooms that are too rectangular: too tall and
            //   narrow or too wide and flat.
            // TODO: This isn't very flexible or tunable. Do something better here.
            int size = Utils.range(1, 3 + roomExtraSize) * 2 + 1;
            int rectangularity = Utils.range(0, 1 + size / 2) * 2;
            int width = size;
            int height = size;
            if (Utils.rngOneIn(2)) {
                width += rectangularity;
            } else {
                height += rectangularity;
            }

            int xPos = Utils.range((bounds.width()-1 - width) / 2) * 2 + 1;
            int yPos = Utils.range((bounds.height()-1 - height) / 2) * 2 + 1;

            Rect room = new Rect(xPos, yPos, width+xPos, height+yPos);

            boolean overlaps = false;
            for (Rect other : _rooms) {
                if (Utils.distanceTo(room, other) <= 0) {
                    overlaps = true;
                    break;
                }
            }

            if (overlaps) continue;

            _rooms.add(room);
            _startRegion();


            for (int x = room.left; x < room.right; x++) {
                for (int y = room.top; y < room.bottom; y++) {
                    _carve(new Vector2D(x,y), Tiles.room);
                }
            }
        }
    }

    void _addJunction(Vector2D pos) {
        if (Utils.rngOneIn(4)) {
            setTile(pos, Utils.rngOneIn(3) ? Tiles.openDoor : Tiles.floor);
        } else {
            setTile(pos, Tiles.closedDoor);
        }
    }

    private void _removeDeadEnds() {
        boolean done = false;

        while (!done) {
            done = true;

            for (int x = 0; x < bounds.width()-1; x++) {
                for (int y = 0; y < bounds.height()-1; y++) {
                    Vector2D pos = new Vector2D(x,y);

                    if (getTile(pos) == Tiles.wall) continue;

                    // If it only has one exit, it's a dead end.
                    int exits = 0;
                    for (Direction dir : Direction.cardinal) {
                        if (getTile(pos.plus(dir)) != Tiles.wall) exits++;
                    }

                    if (exits != 1) continue;

                    done = false;
                    setTile(pos, Tiles.wall);
                }
            }
        }
    }

    /// Gets whether or not an opening can be carved from the given starting
    /// [Cell] at [pos] to the adjacent Cell facing [direction]. Returns `true`
    /// if the starting Cell is in bounds and the destination Cell is filled
    /// (or out of bounds).</returns>
    private boolean _canCarve(Vector2D pos, Direction direction) {
        // Must end in bounds.
        Vector2D newPos = pos.plus(direction.scale(3));
        if (!bounds.contains(newPos.x(), newPos.y())) {
            return false;
        }

        // Destination must not be open.
        return getTile(pos.plus(direction.scale(2))) == Tiles.wall;
    }

    private void _startRegion() {
        _currentRegion++;
    }

    private void _carve(Vector2D pos)
    {
        _carve(pos, Tiles.floor);
    }

    private void _carve(Vector2D pos, TileType type) {
        setTile(pos, type);
        _regions[pos.x()][pos.y()] = _currentRegion;
    }
}
