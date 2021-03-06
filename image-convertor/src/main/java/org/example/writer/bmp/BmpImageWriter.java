package org.example.writer.bmp;

import static org.example.model.CustomImage.BMP;

import java.io.File;
import java.io.IOException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.example.exception.UnableToWriteImageException;
import org.example.misc.Utils;
import org.example.model.CustomImage;
import org.example.model.Pixel;
import org.example.model.bmp.Bmp;
import org.example.writer.ImageWriter;
import org.springframework.stereotype.Component;

@Component
public class BmpImageWriter implements ImageWriter {
    @Override
    public void write(CustomImage image, String filePath) {
        Bmp data = new Bmp();
        populateBmpData(image, data);
        byte[] resultData = getResultData(data);
        try {
            FileUtils.writeByteArrayToFile(new File(filePath), resultData);
        } catch (IOException e) {
            throw new UnableToWriteImageException(e.getMessage());
        }
    }

    @Override
    public boolean isSupportedExtension(String extension) {
        return extension.equalsIgnoreCase(BMP);
    }

    public void populateBmpData(CustomImage source, Bmp target) {
        long fileSize = source.getPixels().length * 3L + Bmp.PIXEL_DATA_OFFSET;
        byte[] fileSizeInBytes = Utils.longToByteArraySize4(fileSize);
        ArrayUtils.reverse(fileSizeInBytes);
        target.setFileSize(fileSizeInBytes);
        byte[] widthInBytes = Utils.longToByteArraySize4(source.getWidth());
        ArrayUtils.reverse(widthInBytes);
        target.setWidth(widthInBytes);
        byte[] heightInBytes = Utils.longToByteArraySize4(source.getHeight());
        ArrayUtils.reverse(heightInBytes);
        target.setHeight(heightInBytes);
        target.setData(convertImageToBytes(source));
    }

    private byte[] getResultData(Bmp data) {
        byte[] resultData = new byte[(int) Bmp.PIXEL_DATA_OFFSET + data.getData().length];
        System.arraycopy(data.getFileType(), 0,
                resultData, Bmp.FILE_TYPE_START_INDEX, data.getFileType().length);
        System.arraycopy(data.getFileSize(), 0,
                resultData, Bmp.FILE_SIZE_START_INDEX, data.getFileSize().length);
        System.arraycopy(data.getReserved1(), 0,
                resultData, Bmp.RESERVED1_START_INDEX, data.getReserved1().length);
        System.arraycopy(data.getReserved2(), 0,
                resultData, Bmp.RESERVED2_START_INDEX, data.getReserved2().length);
        System.arraycopy(data.getPixelDataOffset(), 0,
                resultData, Bmp.PIXEL_DATA_OFFSET_START_INDEX, data.getPixelDataOffset().length);
        System.arraycopy(data.getInfoSize(), 0,
                resultData, Bmp.INFO_SIZE_START_INDEX, data.getInfoSize().length);
        System.arraycopy(data.getWidth(), 0,
                resultData, Bmp.WIDTH_START_INDEX, data.getWidth().length);
        System.arraycopy(data.getHeight(), 0,
                resultData, Bmp.HEIGHT_START_INDEX, data.getHeight().length);
        System.arraycopy(data.getPlanes(), 0,
                resultData, Bmp.PLANES_START_INDEX, data.getPlanes().length);
        System.arraycopy(data.getBitsPerPixel(), 0,
                resultData, Bmp.BITS_PER_PIXEL_START_INDEX, data.getBitsPerPixel().length);
        System.arraycopy(data.getCompression(), 0,
                resultData, Bmp.COMPRESSION_START_INDEX, data.getCompression().length);
        System.arraycopy(data.getImageSize(), 0,
                resultData, Bmp.IMAGE_SIZE_START_INDEX, data.getImageSize().length);
        System.arraycopy(data.getXPixelsPerMeter(), 0,
                resultData, Bmp.X_PIXELS_PER_METER_START_INDEX, data.getXPixelsPerMeter().length);
        System.arraycopy(data.getYPixelsPerMeter(), 0,
                resultData, Bmp.Y_PIXELS_PER_METER_START_INDEX, data.getYPixelsPerMeter().length);
        System.arraycopy(data.getTotalColors(), 0,
                resultData, Bmp.TOTAL_COLORS_START_INDEX, data.getTotalColors().length);
        System.arraycopy(data.getImportantColors(), 0,
                resultData, Bmp.IMPORTANT_COLORS_START_INDEX, data.getImportantColors().length);
        System.arraycopy(data.getData(), 0,
                resultData, Bmp.IMPORTANT_COLORS_START_INDEX + 4, data.getData().length);
        return resultData;
    }

    private byte[] convertImageToBytes(CustomImage image) {
        int endZeros = (int)
                ((Math.ceil(image.getWidth() * 3 / 4.0) - (image.getWidth() * 3 / 4.0)) * 4);
        byte[] resultData =
                new byte[image.getWidth() * image.getHeight() * 3 + endZeros * image.getHeight()];
        int byteCounter = 0;
        for (int i = image.getHeight() - 1; i >= 0; i--) {
            for (int j = 0; j < image.getWidth(); j++) {
                Pixel pixel = image.getPixel(j, i);
                resultData[byteCounter] = pixel.getBlue();
                resultData[byteCounter + 1] = pixel.getGreen();
                resultData[byteCounter + 2] = pixel.getRed();
                byteCounter += 3;
            }
            for (int j = 0; j < endZeros; j++) {
                resultData[byteCounter] = 0;
                byteCounter++;
            }
        }

        return resultData;
    }
}
