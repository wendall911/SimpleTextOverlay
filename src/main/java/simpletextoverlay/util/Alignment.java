package simpletextoverlay.util;

import java.util.Locale;

public enum Alignment {
    TOPLEFT(2, 2),
    LEFTCOL1(2, 2),
    LEFTCOL2(150, 2),
    LEFTCOL3(430, 2),
    LEFTCOL4(590, 2),
    LEFTCOL5(740, 2),
    TOPCENTER(0, 2),
    TOPRIGHT(-2, 2),
    MIDDLELEFT(2, 0),
    MIDDLECENTER(0, 0),
    MIDDLERIGHT(-2, 0),
    BOTTOMLEFT(2, -2),
    BOTTOMCENTER(0, -45),
    BOTTOMRIGHT(-2, -2);

    private static final int MASK_X = 0x0C;
    private static final int MASK_Y = 0x03;

    private static final int TOP = 0x01;
    private static final int COL1 = 0x01;
    private static final int COL2 = 0x01;
    private static final int COL3 = 0x01;
    private static final int COL4 = 0x01;
    private static final int COL5 = 0x01;
    private static final int MIDDLE = 0x03;
    private static final int BOTTOM = 0x02;

    private static final int LEFT = 0x04;
    private static final int CENTER = 0x0C;
    private static final int RIGHT = 0x08;

    private int alignment;
    public final int defaultX;
    public final int defaultY;
    public int x;
    public int y;

    Alignment(final int x, final int y) {
        this.defaultX = x;
        this.defaultY = y;
        this.x = x;
        this.y = y;
        this.alignment = 0;
    }

    public static Alignment parse(String str) {
        String value = "";

        str = str.toLowerCase(Locale.ENGLISH);

        if (str.startsWith("top")) {
            value = "TOP";
        } else if (str.startsWith("mid")) {
            value = "MIDDLE";
        } else if (str.startsWith("bot")) {
            value = "BOTTOM";
        } else if (str.startsWith("left")) {
            value = "LEFT";
        }

        if (str.endsWith("left")) {
            value += "LEFT";
        } else if (str.endsWith("col1")) {
            value += "COL1";
        } else if (str.endsWith("col2")) {
            value += "COL2";
        } else if (str.endsWith("col3")) {
            value += "COL3";
        } else if (str.endsWith("col4")) {
            value += "COL4";
        } else if (str.endsWith("col5")) {
            value += "COL5";
        } else if (str.endsWith("center")) {
            value += "CENTER";
        } else if (str.endsWith("right")) {
            value += "RIGHT";
        }

        try {
            return valueOf(value);
        } catch (final Exception e) {
            return null;
        }
    }

    public void setX(final int x) {
        this.x = x;
    }

    public void setY(final int y) {
        this.y = y;
    }

    public int getX(final int screenwidth, final int textwidth) {
        switch (this.alignment & MASK_X) {
            case LEFT:
                return this.x;

            case CENTER:
                return this.x + (screenwidth - textwidth) / 2;

            case RIGHT:
                return this.x + screenwidth - textwidth;
        }

        return 0;
    }

    public int getY(final int screenheight, final int textheight) {
        switch (this.alignment & MASK_Y) {
            case TOP:
                return this.y;

            case MIDDLE:
                return this.y + (screenheight - textheight) / 2;

            case BOTTOM:
                return this.y + screenheight - textheight;
        }

        return 0;
    }

    static {
        TOPLEFT.alignment = TOP | LEFT;
        LEFTCOL1.alignment = LEFT | COL1;
        LEFTCOL2.alignment = LEFT | COL2;
        LEFTCOL3.alignment = LEFT | COL3;
        LEFTCOL4.alignment = LEFT | COL4;
        LEFTCOL5.alignment = LEFT | COL5;
        TOPCENTER.alignment = TOP | CENTER;
        TOPRIGHT.alignment = TOP | RIGHT;
        MIDDLELEFT.alignment = MIDDLE | LEFT;
        MIDDLECENTER.alignment = MIDDLE | CENTER;
        MIDDLERIGHT.alignment = MIDDLE | RIGHT;
        BOTTOMLEFT.alignment = BOTTOM | LEFT;
        BOTTOMCENTER.alignment = BOTTOM | CENTER;
        BOTTOMRIGHT.alignment = BOTTOM | RIGHT;
    }

}
