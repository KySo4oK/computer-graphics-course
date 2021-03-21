package org.example.misc;

import java.nio.ByteBuffer;
import java.util.Arrays;
import org.example.model.CustomImage;
import org.example.model.Pixel;

public class Utils {

    public static byte[] longToByteArraySize4(long value) {
        byte[] arraySize8 = ByteBuffer.allocate(8).putLong(value).array();
        return Arrays.copyOfRange(arraySize8, 4, 8);
    }

    public static long byteArrayToLong(byte[] bytes) {
        return ByteBuffer.wrap(bytes).getLong();
    }

    public static byte[] intToByteArray(int value) {
        return ByteBuffer.allocate(4).putInt(value).array();
    }

    public static int byteArrayToInt(byte[] bytes) {
        return ByteBuffer.wrap(bytes).getInt();
    }

    public static Pixel[] ppmByteDataToPixels(byte[] data) {
        Pixel[] pixels = new Pixel[data.length / 3];

        for (int i = 0; i < pixels.length; i++) {
            pixels[i] = new Pixel(data[i * 3], data[i * 3 + 1], data[i * 3 + 2]);
        }

        return pixels;
    }

    public static byte[] ppmImageToByteData(CustomImage image) {
        byte[] resultData = new byte[image.getWidth() * image.getHeight() * 3];

        for (int i = 0; i < image.getHeight(); i++) {
            for (int j = 0; j < image.getWidth(); j++) {
                resultData[(i * image.getWidth() + j) * 3] = image.getPixel(j, i).getRed();
                resultData[(i * image.getWidth() + j) * 3 + 1] = image.getPixel(j, i).getGreen();
                resultData[(i * image.getWidth() + j) * 3 + 2] = image.getPixel(j, i).getBlue();
            }
        }
        return resultData;
    }

    public static Pixel[] bmpByteDataToPixels(byte[] data, int width, int height) {
        Pixel[] pixels = new Pixel[data.length / 3];
        int counterForPixels = 0;
        for (int i = height - 1; i >= 0; i--) {
            for (int j = 0; j < width; j++) {
                pixels[counterForPixels] = new Pixel(data[(i * width + j) * 3 + 2], data[(i * width + j) * 3 + 1], data[(i * width + j) * 3]);
                counterForPixels++;
            }
        }
        return pixels;
    }

    public static byte[] bmpImageToByteData(CustomImage image) {
        byte[] resultData = new byte[image.getPixels().length * 3];
        int counterForBytes = 0;
        for (int i = image.getHeight() - 1; i >= 0; i--) {
            for (int j = 0; j < image.getWidth(); j++) {
                Pixel pixel = image.getPixel(j, i);
                resultData[counterForBytes] = pixel.getBlue();
                resultData[counterForBytes + 1] = pixel.getGreen();
                resultData[counterForBytes + 2] = pixel.getRed();
                counterForBytes += 3;
            }
        }
        return resultData;
    }
}
