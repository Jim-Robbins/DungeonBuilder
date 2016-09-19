package com.android.jrobbins.dungeonmaker.hauberkMapper;

import android.graphics.Rect;

/**
 * Created by jim.robbins on 9/16/16.
 */

public class Stage {
    private static final String LOG_TAG = "Stage";
    private Tile[][] tiles;

    public Rect bounds;

    public Stage(int width, int height)
    {
        fillTiles(width, height);
        setBounds(new Rect(0,0,tiles.length, tiles[1].length));
    }

    private void fillTiles(int width, int height)
    {
        tiles = new Tile[width][height];
        for(int x=0;x < tiles.length; x++) {
            for (int y = 0; y < tiles[x].length; y++) {
                tiles[x][y] = new Tile(Tiles.wall);
            }
        }
    }

    public int getWidth()
    {
        return tiles.length;
    }

    public int getHeight()
    {
        return tiles[1].length;
    }

    public Rect getBounds()
    {
        return bounds;
    }

    public Tile getTile(Vector2D pos)
    {
        return tiles[pos.x()][pos.y()];
    }

    public void setTile(Vector2D pos, TileType type)
    {
        tiles[pos.x()][pos.y()] = new Tile(type);
    }

    private void setBounds(Rect bounds)
    {
        //Stage params must be odd-sized
        if (bounds.width() % 2 == 0) {
            bounds.right -= 1;
        }

        if(bounds.height() % 2 == 0)
        {
            bounds.bottom -= 1;
        }

        this.bounds = bounds;
    }

}
