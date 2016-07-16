package de.kablion.passetrappe.utils;


public class Constants {

    public static final String APP_NAME = "Passe-Trappe";

    public static final int UI_WIDTH = 480;
    public static final int UI_HEIGHT = 800;
    public static final float UI_RATIO = UI_WIDTH / UI_HEIGHT;

    // WORLD / OBJECT DIMENSIONS
    public static final float BOARD_WIDTH = 16;
    public static final float BOARD_HEIGHT = 28;
    public static final float BOARD_HALF_WIDTH = BOARD_WIDTH / 2;
    public static final float BOARD_HALF_HEIGHT = BOARD_HEIGHT / 2;

    public static final float DISK_RADIUS = 1f;

    public static final float WALL_WIDTH = 1f;
    public static final float WALL_HALF_WIDTH = WALL_WIDTH / 2;

    public static final float WALL_CENTER_WIDTH = WALL_WIDTH * 2;
    public static final float WALL_CENTER_HALF_WIDTH = WALL_CENTER_WIDTH / 2;

    public static final float HOLE_WIDTH = 5f;
    public static final float HOLE_HALF_WIDTH = HOLE_WIDTH / 2;

    public static final float BAND_WIDTH = 0.1f;
    public static final float BAND_HALF_WIDTH = BAND_WIDTH / 2;
    public static final int BAND_DIVISIONS = 21;
    public static final float BAND_SEGMENT_LENGTH = BOARD_WIDTH / BAND_DIVISIONS;
    public static final float BAND_SEGMENT_HALF_LENGTH = BAND_SEGMENT_LENGTH / 2;
    public static final float BAND_ELASTICITY = 0.05f;
    public static final float BAND_GAP = 2f;

    public static final float FORCE_FACTOR = 200f;
    public static final float SHOOT_ANGLE_MAX = 2f;
    public static final float FORCE_FACTOR_DEFLECT = 1f;

    public static final float WORLD_WIDTH = BOARD_WIDTH + 2.5f;
    public static final float WORLD_HEIGHT = BOARD_HEIGHT + 3;

    public static final float FIRST_DISK_X = -BOARD_HALF_WIDTH + DISK_RADIUS;
    public static final float FIRST_DISK_Y = WALL_CENTER_HALF_WIDTH + DISK_RADIUS;
    public static final int DISKS_PER_COL = (int) Math.floor((BOARD_HALF_HEIGHT - BAND_GAP - BAND_WIDTH - WALL_CENTER_HALF_WIDTH) / (DISK_RADIUS * 2));

    // COLLISION FILTER BITS
    public static final short BIT_GROUND = 1;
    public static final short BIT_DISK = 2;
    public static final short BIT_WALL = 4;
    public static final short BIT_BAND = 8;
    public static final short BIT_BAND_LIMIT = 16;
    public static final short BIT_HOLE = 32;


    //PATHS
    public static final String FONTS_PATH = "fonts/";
    public static final String SKINS_PATH = "skins/";
    public static final String SPRITESHEETS_PATH = "spritesheets/";
    public static final String SOUNDS_PATH = "sounds/";

    public static final String TEXTURES_ATLAS = SPRITESHEETS_PATH + "textures.atlas";

    public static final String DEFAULT_FONT = "Chewy.ttf";

}