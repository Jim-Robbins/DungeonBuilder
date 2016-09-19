package com.android.jrobbins.dungeonmaker.hauberkMapper;

/**
 * Created by jim.robbins on 9/16/16.
 */

public class Tile {
    TileType type;
    boolean _visible = false;
    boolean isExplored = false;

    public Tile(TileType type)
    {
        this.type = type;
    }

    public boolean get_visble() {
        return _visible;
    }

    public void set_visble(boolean visible) {
        if (visible) isExplored = true;
        _visible = visible;
    }


    public boolean setVisible(boolean visible) {
        this._visible = _visible;

        if (visible && !isExplored) {
            isExplored = true;
            return true;
        }
        return false;
    }

    public boolean isPassable()
    {
        return type.isPassable;
    }

    public boolean isTraversable()
    {
        return type.isPassable || (type.opensTo != null);
    }

    public boolean isTransparent()
    {
        return type.isTransparent;
    }
}
