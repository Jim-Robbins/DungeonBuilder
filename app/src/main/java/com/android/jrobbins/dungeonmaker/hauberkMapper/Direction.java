package com.android.jrobbins.dungeonmaker.hauberkMapper;

import android.graphics.Point;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Created by jim.robbins on 9/16/16.
 */

public class Direction extends Vector2D {
    static final Direction none = new Direction(0, 0);
    static final Direction n    = new Direction(0, -1);
    static final Direction ne   = new Direction(1, -1);
    static final Direction e    = new Direction(1, 0);
    static final Direction se   = new Direction(1, 1);
    static final Direction s    = new Direction(0, 1);
    static final Direction sw   = new Direction(-1, 1);
    static final Direction w    = new Direction(-1, 0);
    static final Direction nw   = new Direction(-1, -1);

    /// The eight cardinal and intercardinal directions.
    static final Direction[] all = new Direction[] {n, ne, e, se, s, sw, w, nw};

    /// The four cardinal directions.
    static final Direction[] cardinal = new Direction[] {n, e, s, w};

    /// The four intercardinal directions.
    static final Direction[] intercardinal = new Direction[] {ne, se, sw, nw};

    public Direction(int x, int y)
    {
        super(new double[]{x, y});
    }

    public static final List<Direction> VALUES = Collections.unmodifiableList(Arrays.asList(all));
    private static final int SIZE = VALUES.size();
    private static final Random RANDOM = new Random();

    public static Direction randomDirection()  {
        return VALUES.get(RANDOM.nextInt(SIZE));
    }

    public Direction rotateLeft45() {
        return rotate(1);
    }

    public Direction rotateRight45() {
        return rotate(-1);
    }

    public Direction rotateLeft90() {
        return rotate(2);
    }

    public Direction rotateRight90() {
        return rotate(-2);
    }

    public Direction rotate180() {
        return rotate(4);
    }

    private Direction rotate(int step)
    {
        int index = getIndex();
        index += step;
        index = index % all.length;
        return all[index];
    }

    private int getIndex() {
        for (int i = 0; i < all.length; i++) {
            if(this.equals(all[i])) {
                return i;
            }
        }
        return 0;
    }

    public String toString() {
        if (this.equals(none)) {
            return "none";
        } else if (this.equals(n)) {
            return "n";
        } else if (this.equals(ne)) {
            return "ne";
        } else if (this.equals(e)) {
            return "e";
        } else if (this.equals(se)) {
            return "se";
        } else if (this.equals(s)) {
            return "s";
        } else if (this.equals(sw)) {
            return "sw";
        } else if (this.equals(w)) {
            return "w";
        } else if (this.equals(nw)) {
            return "nw";
        }

        return null;
    }
}
