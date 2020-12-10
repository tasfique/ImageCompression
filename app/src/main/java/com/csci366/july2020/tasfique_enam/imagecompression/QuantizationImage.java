package com.csci366.july2020.tasfique_enam.imagecompression;

public class QuantizationImage {

    public byte[] Y;
    public byte[] CrCb;
    public int y_size;
    public int crcb_size;

    public QuantizationImage(int y_size, int crcb_size) {
        Y = new byte[y_size];
        CrCb = new byte[crcb_size];
        this.y_size = y_size;
        this.crcb_size = crcb_size;
    }
}
