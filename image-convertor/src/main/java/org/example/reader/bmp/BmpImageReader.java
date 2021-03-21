package org.example.reader.bmp;

import java.io.File;
import java.util.Arrays;
import org.apache.commons.lang3.ArrayUtils;
import org.example.misc.Utils;
import org.example.model.Pixel;
import org.example.model.bmp.Bmp;
import org.example.model.CustomImage;
import org.example.reader.ImageReader;
import org.example.reader.common.RawByteReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BmpImageReader implements ImageReader {
    private final RawByteReader reader;
    private final BmpValidator validator;
    private File file;

    @Autowired
    public BmpImageReader(RawByteReader reader, BmpValidator validator) {
        this.reader = reader;
        this.validator = validator;
    }

    @Override
    public CustomImage read(File file) {
        this.file = file;
        Bmp bmpData = convertByteToBmp();
        CustomImage image = new CustomImage();
        writeRawImageData(bmpData, image);
        return image;
    }

    private void writeRawImageData(Bmp bmpData, CustomImage targetImage) {
        byte[] width = Arrays.copyOf(bmpData.getWidth(), bmpData.getWidth().length);
        ArrayUtils.reverse(width);
        targetImage.setWidth(Utils.byteArrayToInt(width));
        byte[] height = Arrays.copyOf(bmpData.getHeight(), bmpData.getHeight().length);
        ArrayUtils.reverse(height);
        targetImage.setHeight(Utils.byteArrayToInt(height));
        targetImage.setPixels(convertBmpToPixels(bmpData.getData(),
                Utils.byteArrayToInt(width), Utils.byteArrayToInt(height)));
    }


    private Pixel[] convertBmpToPixels(byte[] data, int width, int height) {
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

    private Bmp convertByteToBmp() {
        byte[] rawBytes = reader.readBytes(file);
        validator.validateBmpData(rawBytes);
        Bmp bmpData = new Bmp();
        bmpData.setFileType(Arrays.copyOfRange(
                rawBytes,
                Bmp.FILE_TYPE_START_INDEX,
                Bmp.FILE_SIZE_START_INDEX));
        bmpData.setFileSize(Arrays.copyOfRange(
                rawBytes,
                Bmp.FILE_SIZE_START_INDEX,
                Bmp.RESERVED1_START_INDEX));
        bmpData.setReserved1(Arrays.copyOfRange(
                rawBytes,
                Bmp.RESERVED1_START_INDEX,
                Bmp.RESERVED2_START_INDEX));
        bmpData.setReserved2(Arrays.copyOfRange(
                rawBytes,
                Bmp.RESERVED2_START_INDEX,
                Bmp.PIXEL_DATA_OFFSET_START_INDEX));
        bmpData.setPixelDataOffset(Arrays.copyOfRange(
                rawBytes,
                Bmp.PIXEL_DATA_OFFSET_START_INDEX,
                Bmp.INFO_SIZE_START_INDEX));
        bmpData.setInfoSize(Arrays.copyOfRange(
                rawBytes,
                Bmp.INFO_SIZE_START_INDEX,
                Bmp.WIDTH_START_INDEX));
        bmpData.setWidth(Arrays.copyOfRange(
                rawBytes,
                Bmp.WIDTH_START_INDEX,
                Bmp.HEIGHT_START_INDEX));
        bmpData.setHeight(Arrays.copyOfRange(
                rawBytes,
                Bmp.HEIGHT_START_INDEX,
                Bmp.PLANES_START_INDEX));
        bmpData.setPlanes(Arrays.copyOfRange(
                rawBytes,
                Bmp.PLANES_START_INDEX,
                Bmp.BITS_PER_PIXEL_START_INDEX));
        bmpData.setBitsPerPixel(Arrays.copyOfRange(
                rawBytes,
                Bmp.BITS_PER_PIXEL_START_INDEX,
                Bmp.COMPRESSION_START_INDEX));
        bmpData.setCompression(Arrays.copyOfRange(
                rawBytes,
                Bmp.COMPRESSION_START_INDEX,
                Bmp.IMAGE_SIZE_START_INDEX));
        bmpData.setImageSize(Arrays.copyOfRange(
                rawBytes,
                Bmp.IMAGE_SIZE_START_INDEX,
                Bmp.X_PIXELS_PER_METER_START_INDEX));
        bmpData.setXPixelsPerMeter(Arrays.copyOfRange(
                rawBytes,
                Bmp.X_PIXELS_PER_METER_START_INDEX,
                Bmp.Y_PIXELS_PER_METER_START_INDEX));
        bmpData.setYPixelsPerMeter(Arrays.copyOfRange(
                rawBytes,
                Bmp.Y_PIXELS_PER_METER_START_INDEX,
                Bmp.TOTAL_COLORS_START_INDEX));
        bmpData.setTotalColors(Arrays.copyOfRange(
                rawBytes,
                Bmp.TOTAL_COLORS_START_INDEX,
                Bmp.IMPORTANT_COLORS_START_INDEX));
        bmpData.setTotalColors(Arrays.copyOfRange(
                rawBytes,
                Bmp.IMPORTANT_COLORS_START_INDEX,
                Bmp.IMPORTANT_COLORS_START_INDEX + 4));
        bmpData.setData(Arrays.copyOfRange(rawBytes,
                Bmp.IMPORTANT_COLORS_START_INDEX + 4,
                rawBytes.length));
        return bmpData;
    }
}
