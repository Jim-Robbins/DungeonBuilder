package com.android.jrobbins.dungeonmaker.hauberkMapper;

import android.graphics.Point;

/**
 * Created by jim.robbins on 9/16/16.
 */

public class Monster extends Thing {
    public MonsterType type;

    public Monster(MonsterType type, Point pos) {
        super(pos);
        this.type = type;
    }

    public Character getAppearance() {
        return type.appearance;
    }
}

class MonsterType {
    public String name;
    public Character appearance;

    public MonsterType(String name, Character appearance)
    {
        this.name = name;
        this.appearance = appearance;
    }

}
