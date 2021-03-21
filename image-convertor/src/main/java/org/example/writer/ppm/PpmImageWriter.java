package org.example.writer.ppm;

import static org.example.model.CustomImage.PPM;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import org.apache.commons.io.FileUtils;
import org.example.exception.UnableToProcessConcatBytes;
import org.example.exception.UnableToWriteImageException;
import org.example.model.CustomImage;
import org.example.model.ppm.Ppm;
import org.example.writer.ImageWriter;
import org.springframework.stereotype.Component;


@Component
public class PpmImageWriter implements ImageWriter {
    private static final String NEW_LINE = "\n";
    private static final String SPACE = " ";

    @Override
    public void write(CustomImage image, String filePath) {
        byte[] imageData = convertImage(convertImageToPpm(image));
        try {
            FileUtils.writeByteArrayToFile(new File(filePath), imageData);
        } catch (IOException e) {
            throw new UnableToWriteImageException(e.getMessage());
        }
    }

    @Override
    public boolean isSupportedExtension(String extension) {
        return extension.equalsIgnoreCase(PPM);
    }

    private Ppm convertImageToPpm(CustomImage image) {
        return Ppm.builder().data(convertImageToBytes(image))
                .height(image.getHeight())
                .width(image.getWidth())
                .magicNumber(Ppm.DEFAULT_MAGIC_NUMBER)
                .maxColorValue(Ppm.DEFAULT_MAX_COLOR_VALUE)
                .build();
    }

    private byte[] convertImage(Ppm image) {
        byte[] header = getHeader(image);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            outputStream.write(header);
            outputStream.write(image.getData());
        } catch (IOException e) {
            throw new UnableToProcessConcatBytes(e.getMessage());
        }

        return outputStream.toByteArray();
    }

    private byte[] getHeader(Ppm image) {
        return buildPpmHeader(image).getBytes();
    }

    private String buildPpmHeader(Ppm image) {
        return image.getMagicNumber() + NEW_LINE +
                image.getWidth() + SPACE +
                image.getHeight() + NEW_LINE +
                image.getMaxColorValue() + NEW_LINE;
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
