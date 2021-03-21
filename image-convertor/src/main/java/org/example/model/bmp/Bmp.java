package org.example.model.bmp;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.ArrayUtils;
import org.example.misc.Utils;

@Getter
@Setter
public class Bmp {
    public static final int FILE_TYPE_START_INDEX = 0;
    public static final int FILE_SIZE_START_INDEX = 2;
    public static final int RESERVED1_START_INDEX = 6;
    public static final int RESERVED2_START_INDEX = 8;
    public static final int PIXEL_DATA_OFFSET_START_INDEX = 10;
    public static final int INFO_SIZE_START_INDEX = 14;
    public static final int WIDTH_START_INDEX = 18;
    public static final int HEIGHT_START_INDEX = 22;
    public static final int PLANES_START_INDEX = 26;
    public static final int BITS_PER_PIXEL_START_INDEX = 28;
    public static final int COMPRESSION_START_INDEX = 30;
    public static final int IMAGE_SIZE_START_INDEX = 34;
    public static final int X_PIXELS_PER_METER_START_INDEX = 38;
    public static final int Y_PIXELS_PER_METER_START_INDEX = 42;
    public static final int TOTAL_COLORS_START_INDEX = 46;
    public static final int IMPORTANT_COLORS_START_INDEX = 50;

    public static final String FILE_TYPE = "BM";
    public static final int DEFAULT_RESERVED1 = 0;
    public static final int DEFAULT_RESERVED2 = 0;
    public static final long PIXEL_DATA_OFFSET = 54L;
    public static final long INFO_SIZE = 40L;
    public static final int DEFAULT_PLANES = 1;
    public static final int DEFAULT_BITS_PER_PIXEL = 24;
    public static final long DEFAULT_COMPRESSION = 0L;
    public static final long DEFAULT_IMAGE_SIZE = 0L;
    public static final int DEFAULT_X_PIXELS_PER_METER = 0;
    public static final int DEFAULT_Y_PIXELS_PER_METER = 0;
    public static final long DEFAULT_TOTAL_COLORS = 0L;
    public static final long DEFAULT_IMPORTANT_COLORS = 0L;

    private byte[] fileType;
    private byte[] fileSize;
    private byte[] reserved1;
    private byte[] reserved2;
    private byte[] pixelDataOffset;
    private byte[] infoSize;
    private byte[] width;
    private byte[] height;
    private byte[] planes;
    private byte[] bitsPerPixel;
    private byte[] compression;
    private byte[] imageSize;
    private byte[] xPixelsPerMeter;
    private byte[] yPixelsPerMeter;
    private byte[] totalColors;
    private byte[] importantColors;

    private byte[] data;

    public Bmp() {
        fileType = FILE_TYPE.getBytes();
        reserved1 = Utils.intToByteArray(DEFAULT_RESERVED1);
        reserved2 = Utils.intToByteArray(DEFAULT_RESERVED2);
        pixelDataOffset = Utils.longToByteArraySize4(PIXEL_DATA_OFFSET);
        infoSize = Utils.longToByteArraySize4(INFO_SIZE);
        planes = Utils.intToByteArray(DEFAULT_PLANES);
        bitsPerPixel = Utils.intToByteArray(DEFAULT_BITS_PER_PIXEL);
        compression = Utils.longToByteArraySize4(DEFAULT_COMPRESSION);
        imageSize = Utils.longToByteArraySize4(DEFAULT_IMAGE_SIZE);
        xPixelsPerMeter = Utils.intToByteArray(DEFAULT_X_PIXELS_PER_METER);
        yPixelsPerMeter = Utils.intToByteArray(DEFAULT_Y_PIXELS_PER_METER);
        totalColors = Utils.longToByteArraySize4(DEFAULT_TOTAL_COLORS);
        importantColors = Utils.longToByteArraySize4(DEFAULT_IMPORTANT_COLORS);
        reverseArrays();
    }

    private void reverseArrays() {
        ArrayUtils.reverse(reserved1);
        ArrayUtils.reverse(reserved2);
        ArrayUtils.reverse(pixelDataOffset);
        ArrayUtils.reverse(infoSize);
        ArrayUtils.reverse(planes);
        ArrayUtils.reverse(bitsPerPixel);
        ArrayUtils.reverse(compression);
        ArrayUtils.reverse(imageSize);
        ArrayUtils.reverse(xPixelsPerMeter);
        ArrayUtils.reverse(yPixelsPerMeter);
        ArrayUtils.reverse(totalColors);
        ArrayUtils.reverse(importantColors);
    }
}