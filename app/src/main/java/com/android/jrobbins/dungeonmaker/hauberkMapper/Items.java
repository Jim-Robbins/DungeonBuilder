package com.android.jrobbins.dungeonmaker.hauberkMapper;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Created by jim.robbins on 9/16/16.
 */

public class Items {
    static TileType weapon = new TileType("Weapon", true, false, '+', "0#FFCA28");
    static TileType armor = new TileType("Armor", true, false,'@', "0#84FFFF");
    static TileType treasure = new TileType("Treasure", true, false,'$', "0#FFD600");
    static TileType potion  = new TileType("Potion", true, false,'!', "0#76FF03");
    static TileType scroll = new TileType("Scroll", true, false,'&', "0#B388FF");
    static TileType food = new TileType("food", true, false,'*', "0#D50000");

    static final TileType[] all = new TileType[] {weapon, armor, treasure, potion, scroll, food};

    public static final List<TileType> VALUES = Collections.unmodifiableList(Arrays.asList(all));
    private static final int SIZE = VALUES.size();
    private static final Random RANDOM = new Random();

    public static TileType randomItem()  {
        return VALUES.get(RANDOM.nextInt(SIZE));
    }

}
