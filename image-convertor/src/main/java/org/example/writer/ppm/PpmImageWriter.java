package org.example.writer.ppm;

import java.io.File;
import java.io.IOException;
import org.apache.commons.io.FileUtils;
import org.example.model.CustomImage;
import org.example.writer.ImageWriter;

public class PpmImageWriter implements ImageWriter {
    @Override
    public void write(CustomImage image, String filePath) throws IOException {
        byte[] imageData = convertImageToBytes(image);
        FileUtils.writeByteArrayToFile(new File(filePath), imageData);
    }

    private byte[] convertImageToBytes(CustomImage image) {
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
}
