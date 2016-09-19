package com.android.jrobbins.dungeonmaker.hauberkMapper;

import android.graphics.Rect;

import java.util.List;
import java.util.Random;

import static android.R.attr.x;

/**
 * Created by jim.robbins on 9/16/16.
 */

public class Utils {
    public static Vector2D rngVec2DInRect(Rect rect)
    {
        double[] point = new double[] {range(rect.left,rect.right), range(rect.top, rect.bottom)};
        return new Vector2D(point);
    }

    public static Vector2D rngVec2DInList(List<Vector2D> items)
    {
        if (items == null || items.size()==0) {
            return null;
        }
        return items.get(range(items.size()-1));
    }

    public static boolean rngOneIn(int chance)
    {
        return (range(chance) == 0);
    }

    // Get a random value from the range
    public static int range(int min, int max) {
        Random r = new Random();
        if(min >= max)
        {
            return min;
        }
        int rand = r.nextInt(max-min) + min;;
        return rand;
    }

    public static int range(int max) {
        return range(0, max);
    }

    public static Direction rngDirInList(List<Direction> items) {
        return items.get(range(items.size()-1));
    }

    /// Returns the distance between one Rect and [other]. This is minimum
    /// length that a corridor would have to be to go from one Rect to the other.
    /// If the two Rects are adjacent, returns zero. If they overlap, returns -1.
    public static int distanceTo(Rect current, Rect other) {
        int vertical;
        if (current.top >= other.bottom) {
            vertical = current.top - other.bottom;
        } else if (current.bottom <= other.top) {
            vertical = other.top - current.bottom;
        } else {
            vertical = -1;
        }

        int horizontal;
        if (current.left >= other.right) {
            horizontal = current.left - other.right;
        } else if (current.right <= other.left) {
            horizontal = other.left - current.right;
        } else {
            horizontal = -1;
        }

        if ((vertical == -1) && (horizontal == -1)) return -1;
        if (vertical == -1) return horizontal;
        if (horizontal == -1) return vertical;
        return horizontal + vertical;
    }

    public static Rect inflate(Rect rect, int distance)
    {
        return new Rect(rect.left - distance, rect.top - distance, rect.width() + distance*2, rect.height() + distance*2);
    }
}
