package com.android.jrobbins.dungeonmaker.hauberkMapper;

import android.graphics.Color;
import android.util.Log;

import static android.graphics.Color.green;
import static com.android.jrobbins.dungeonmaker.hauberkMapper.Direction.s;

/**
 * Created by jim.robbins on 9/16/16.
 */

public class Tiles {
    static TileType floor = new TileType("floor", true, true, '.', "0#BDBDBD");
    static TileType room = new TileType("room", true, true, '.', "0#E0E0E0");
    static TileType wall = new TileType("wall", false, false, '#', "0#212121");
    static TileType lowWall = new TileType("low wall", false, true, '%', "0#232323");
    static TileType table = new TileType("table", false, true, 'π', "0#795548");
    static TileType openDoor = new TileType("open door", true, true, '\'', "0#BCAAA4");
    static TileType closedDoor = new TileType("closed door", false, false, 'X', "0#A1887F");
    static TileType stairs = new TileType("stairs", true, true,'≡', "0#A1887F");

    static TileType grass = new TileType("grass", true, true,'.', "0#9CCC65");
    static TileType tree = new TileType("tree", false, false, '▲', "0#33691E");
    static TileType treeAlt1 = new TileType("tree", false, false, '♠', "0#1B5E20");
    static TileType treeAlt2 = new TileType("tree", false, false, '♣', "0#1B5E20");

    public static void initialize() {
        Tiles.openDoor.closesTo = Tiles.closedDoor;
        Tiles.closedDoor.opensTo = Tiles.openDoor;
    }
}
