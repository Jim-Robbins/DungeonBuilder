package com.android.jrobbins.dungeonmaker.hauberkMapper;

import android.graphics.Color;

/**
 * Created by jim.robbins on 9/16/16.
 */

public class TileType {
    public String name;
    public String color;
    public boolean isPassable;
    public boolean isTransparent;
    public Character appearance;
    public TileType opensTo;
    public TileType closesTo;

    public TileType(String name, boolean isPassable, boolean isTransparent, Character appearance, String color){
        this.name = name;
        this.isPassable = isPassable;
        this.isTransparent = isTransparent;
        this.appearance = appearance;
        this.color = color;
    }


}
