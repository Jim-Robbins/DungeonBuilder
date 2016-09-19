package com.android.jrobbins.dungeonmaker.hauberkMapper;

import android.graphics.Point;

/**
 * Created by jim.robbins on 9/16/16.
 */

public class Thing {
    Point _pos;

    public Thing(Point pos)
    {
        _pos = pos;
    }

    public Point getPos()
    {
        return _pos;
    }
    public void set_pos(Point pos)
    {
        _pos = pos;
    }
    public void setPos(int xPos, int yPos)
    {
        _pos = new Point(xPos,yPos);
    }

    public int getX()
    {
        return _pos.x;
    }
    public void setX(int xPos)
    {
        _pos.x = xPos;
    }

    public int getY()
    {
        return _pos.y;
    }
    public void setY(int yPos)
    {
        _pos.y = yPos;
    }

    public void changePosition(Point from, Point to) {}
}
