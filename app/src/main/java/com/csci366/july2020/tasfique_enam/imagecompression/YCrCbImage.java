package com.csci366.july2020.tasfique_enam.imagecompression;

public class YCrCbImage {

    public byte[][] Y;
    public byte[][] Cr;
    public byte[][] Cb;
    public int y_width;
    public int y_height;
    public int cr_width;
    public int cr_height;
    public int cb_width;
    public int cb_height;

    public YCrCbImage(int y_width, int y_height)
    {
        Y = new byte[y_height][y_width];
        Cr = new byte[y_height][y_width];
        Cb = new byte[y_height][y_width];
        this.y_width = y_width;
        this.cr_width = y_width;
        this.cb_width = y_width;
        this.y_height = y_height;
        this.cb_height = y_height;
        this.cr_height = y_height;
    }

    public YCrCbImage(int y_width, int y_height, int cr_width, int cr_height, int cb_width, int cb_height)
    {
        Y = new byte[y_height][y_width];
        Cr = new byte[cr_height][cr_width];
        Cb = new byte[cb_height][cb_width];
        this.y_width = y_width;
        this.cr_width = cr_width;
        this.cb_width = cb_width;
        this.y_height = y_height;
        this.cb_height = cb_height;
        this.cr_height = cr_height;
    }

}
