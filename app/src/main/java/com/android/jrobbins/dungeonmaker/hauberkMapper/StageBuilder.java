package com.android.jrobbins.dungeonmaker.hauberkMapper;

import android.app.ProgressDialog;
import android.graphics.Rect;
import android.util.Log;

import static com.android.jrobbins.dungeonmaker.hauberkMapper.Tiles.floor;

/**
 * Created by jim.robbins on 9/16/16.
 */

public class StageBuilder {
    private static final String LOG_TAG = "StageBuilder";
    protected Stage stage;
    protected Rect bounds;

    public StageBuilder()
    {
        Tiles.initialize();
    }

    public void generate(Stage stage, ProgressDialog mProgress)
    {
        this.stage = stage;
        bounds = stage.getBounds();
    }

    public TileType getTile(Vector2D pos)
    {
        return stage.getTile(pos).type;
    }

    public void setTile(Vector2D pos, TileType type)
    {
        if (pos == null) {
            return;
        }
        stage.setTile(pos, type);
    }

    public void setTile(int x, int y, TileType type)
    {
        if(!type.equals(Tiles.wall))
            Log.d(LOG_TAG,"setTile " + x + ", "  +y+ ", "  + type.appearance);
        double[] pos = {x,y};
        setTile(new Vector2D(pos), type);
    }

    public void fillTile(TileType tile)
    {
        if (tile == null) {
            return;
        }

        for (int y = 0; y < stage.getHeight(); y++) {
            for (int x = 0; x < stage.getWidth(); x++) {
                setTile(x,y,tile);
            }
        }
    }

    /// Randomly turns some [wall] tiles into [floor] and vice versa.
    private void erode(int iterations)
    {
        Log.d(LOG_TAG,"erode" + iterations);
        Rect bounds = Utils.inflate(stage.getBounds(),-1);
        for (int i = 0; i < iterations; i++) {
            // TODO: This way this works is super inefficient. Would be better to
            // keep track of the floor tiles near open ones and choose from them.
            Vector2D pos = Utils.rngVec2DInRect(bounds);
            TileType here = getTile(pos);
            if(here.equals(Tiles.wall)) {
                continue;
            }

            // Keep track of how many floors we're adjacent too. We will only erode
            // if we are directly next to a floor.
            int floors = 0;

            for (Direction dir : Direction.all) {
                TileType tile = getTile(pos.plus(dir));
                if (tile == floor) floors++;
            }

            // Prefer to erode tiles near more floor tiles so the erosion isn't too spiky.
            if (floors < 2) continue;
            if (Utils.rngOneIn(9 - floors)) {
                setTile(pos, floor);
            }
        }
    }
}
