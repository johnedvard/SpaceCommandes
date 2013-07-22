package com.rauma.lille;

import com.badlogic.gdx.math.Vector2;

public final class Utils {
        
        public static Vector2 Screen2World(final Vector2 vct) {
                return new Vector2(vct.x / SpaceGame.WORLD_SCALE, vct.y / SpaceGame.WORLD_SCALE);
        }

        public static Vector2 Screen2World(final float x, final float y) {
                return new Vector2(x / SpaceGame.WORLD_SCALE, y / SpaceGame.WORLD_SCALE);
        }
        
        public static float Screen2World(final float v) {
                return v / SpaceGame.WORLD_SCALE;
        }
        
        public static Vector2 getWorldBoxCenter(final Vector2 top, final Vector2 size) {
                return new Vector2(top.x + size.x / 2, top.y + size.y / 2);
        }
        
        public static Vector2 Frame2Screen(final int x, final int y) {
                return new Vector2(x, SpaceGame.SCREEN_HEIGHT - y);
        }
}
