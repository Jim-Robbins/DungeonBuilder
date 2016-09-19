package com.android.jrobbins.dungeonmaker.hauberkMapper;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Created by jim.robbins on 9/16/16.
 */

public class Monsters {
    static TileType bat = new TileType("bat", false, false, 'b', "0#424242");
    static TileType spider = new TileType("spider", false, false,'a', "0#EF6C00");
    static TileType snake = new TileType("snake", false, false,'s', "0#76FF03");
    static TileType rodent = new TileType("rodent", false, false,'r', "0#5E35B1");
    static TileType wolf = new TileType("wolf", false, false,'w', "0#0097A7");
    static TileType goblin = new TileType("goblin", false, false,'g', "0#43A047");
    static TileType orc = new TileType("orc", false, false,'o', "0#AFB42B");
    static TileType troll = new TileType("troll", false, false,'T', "0#78909C");
    static TileType giant = new TileType("giant", false, false,'G', "0#827717");
    static TileType zombie = new TileType("zombie", false, false,'z',"0#C8E6C9");
    static TileType skeleton = new TileType("skeleton", false, false,'S', "0#F5F5F5");

    static final TileType[] all = new TileType[] {bat, spider, snake, rodent, wolf, goblin, orc, troll, giant, zombie, skeleton};

    public static final List<TileType> VALUES = Collections.unmodifiableList(Arrays.asList(all));
    private static final int SIZE = VALUES.size();
    private static final Random RANDOM = new Random();

    public static TileType randomMonster()  {
        return VALUES.get(RANDOM.nextInt(SIZE));
    }

}
