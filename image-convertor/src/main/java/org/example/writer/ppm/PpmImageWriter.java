package org.example.writer.ppm;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import org.apache.commons.io.FileUtils;
import org.example.model.CustomImage;
import org.example.model.ppm.Ppm;
import org.example.writer.ImageWriter;
import org.springframework.stereotype.Component;

@Component
public class PpmImageWriter implements ImageWriter {
    @Override
    public void write(CustomImage image, String filePath) throws IOException {
        byte[] imageData = convertImage(convertImageToPpm(image));
        FileUtils.writeByteArrayToFile(new File(filePath), imageData);
    }

    private Ppm convertImageToPpm(CustomImage image) {
        return Ppm.builder().data(convertImageToBytes(image))
                .height(image.getHeight())
                .width(image.getWidth())
                .magicNumber(Ppm.DEFAULT_MAGIC_NUMBER)
                .maxColorValue(Ppm.DEFAULT_MAX_COLOR_VALUE)
                .build();
    }

    private byte[] convertImage(Ppm image) throws IOException {
        byte[] header = getHeader(image);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(header);
        outputStream.write(image.getData());
        return outputStream.toByteArray();
    }

    private byte[] getHeader(Ppm image) {
        return buildPpmHeader(image).getBytes();
    }

    private String buildPpmHeader(Ppm image) {
        return image.getMagicNumber() + "\n" +
                image.getWidth() + " " +
                image.getHeight() + "\n" +
                image.getMaxColorValue() + "\n";
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
