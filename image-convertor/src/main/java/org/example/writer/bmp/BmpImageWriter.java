package org.example.writer.bmp;

import org.apache.commons.io.FileUtils;
import org.example.model.CustomImage;
import org.example.model.Pixel;
import org.example.writer.ImageWriter;

import java.io.File;
import java.io.IOException;

public class BmpImageWriter implements ImageWriter {
    @Override
    public void write(CustomImage image, String filePath) throws IOException {
        byte[] imageData = convertImageToBytes(image);
        FileUtils.writeByteArrayToFile(new File(filePath), imageData);
    }

    private byte[] convertImageToBytes(CustomImage image) {
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
