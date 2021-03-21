package org.example.reader.ppm;

import org.example.exception.UnableToReadPpmException;
import org.example.model.CustomImage;
import org.example.model.Pixel;
import org.example.model.ppm.Ppm;
import org.example.reader.ImageReader;
import org.example.reader.common.RawByteReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.example.CommandUtils.PPM;

@Component
public class PpmImageReader implements ImageReader {
    private static final String METADATA_PARSE_REGEX =
            "(" + Ppm.MetaData.MAGIC_NUMBER.getRegex() + ")" +
                    "\\s+" + "(" + Ppm.MetaData.WIDTH.getRegex() + ")" +
                    "\\s" + "(" + Ppm.MetaData.HEIGHT.getRegex() + ")" +
                    "\\s" + "(" + Ppm.MetaData.MAX_COLOR_VALUE.getRegex() +
                    ")" + "\\s";
    private final RawByteReader reader;
    private final PpmValidator validator;
    private File file;

    @Autowired
    public PpmImageReader(RawByteReader reader, PpmValidator validator) {
        this.reader = reader;
        this.validator = validator;
    }

    @Override
    public CustomImage read(File sourceFile) {
        this.file = sourceFile;
        Ppm ppmData = convertByteToPpm();
        CustomImage image = new CustomImage();
        writeRawImageData(ppmData, image);
        return image;
    }

    @Override
    public boolean isSupportedExtension(String extension) {
        return extension.equalsIgnoreCase(PPM);
    }

    private void writeRawImageData(Ppm source, CustomImage targetImage) {
        targetImage.setWidth(source.getWidth());
        targetImage.setHeight(source.getHeight());
        targetImage.setPixels(convertPpmToPixels(source.getData()));
    }

    private Pixel[] convertPpmToPixels(byte[] data) {
        Pixel[] pixels = new Pixel[data.length / 3];
        for (int i = 0; i < pixels.length; i++) {
            pixels[i] = new Pixel(data[i * 3], data[i * 3 + 1], data[i * 3 + 2]);
        }
        return pixels;
    }

    private Ppm convertByteToPpm() {
        byte[] rawBytes = reader.readBytes(file);
        Ppm ppmData = new Ppm();
        String rawDataString = new String(rawBytes);
        Pattern pattern = Pattern.compile(METADATA_PARSE_REGEX);
        Matcher matcher = pattern.matcher(rawDataString);
        if (matcher.find()) {
            int offset = matcher.group().getBytes().length;
            String magicNumber = matcher.group(Ppm.MetaData.MAGIC_NUMBER.getOrder());
            ppmData.setMagicNumber(magicNumber);
            ppmData.setWidth(Integer.parseInt(matcher.group(Ppm.MetaData.WIDTH.getOrder())));
            ppmData.setHeight(Integer.parseInt(matcher.group(Ppm.MetaData.HEIGHT.getOrder())));
            int maxColorValue = Integer.parseInt(matcher.group(Ppm.MetaData.MAX_COLOR_VALUE.getOrder()));
            ppmData.setMaxColorValue(maxColorValue);
            ppmData.setData(Arrays.copyOfRange(rawBytes, offset, rawBytes.length));
            validator.validate(magicNumber, maxColorValue);
            return ppmData;
        } else {
            throw new UnableToReadPpmException(file);
        }
    }
}
