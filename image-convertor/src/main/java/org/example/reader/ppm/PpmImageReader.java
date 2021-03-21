package org.example.reader.ppm;

import java.io.File;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.example.misc.Utils;
import org.example.model.CustomImage;
import org.example.model.ppm.Ppm;
import org.example.reader.ImageReader;
import org.example.reader.common.IRawByteReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PpmImageReader implements ImageReader {
    private static final String LEFT_PARENTHESES = "(";
    private static final String RIGHT_PARENTHESES = ")";
    private static final String WHITESPACE_REGEX = "\\s";
    private static final String WHITESPACES_REGEX = "\\s+";
    private static final String METADATA_PARSE_REGEX =
            LEFT_PARENTHESES + Ppm.MetaData.MAGIC_NUMBER.getRegex() + RIGHT_PARENTHESES +
                    WHITESPACES_REGEX + LEFT_PARENTHESES + Ppm.MetaData.WIDTH.getRegex() + RIGHT_PARENTHESES +
                    WHITESPACE_REGEX + LEFT_PARENTHESES + Ppm.MetaData.HEIGHT.getRegex() + RIGHT_PARENTHESES +
                    WHITESPACE_REGEX + LEFT_PARENTHESES + Ppm.MetaData.MAX_COLOR_VALUE.getRegex() +
                    RIGHT_PARENTHESES + WHITESPACE_REGEX;
    private final IRawByteReader reader;
    private final PpmValidator validator;
    private File file;

    @Autowired
    public PpmImageReader(IRawByteReader reader, PpmValidator validator) {
        this.reader = reader;
        this.validator = validator;
    }

    @Override
    public CustomImage read(File source) {
        this.file = source;
        Ppm ppmData = convertByteToPpm();
        CustomImage image = new CustomImage();
        populateRawData(ppmData, image);
        return image;
    }

    private void populateRawData(Ppm source, CustomImage target) {
        target.setWidth(source.getWidth());
        target.setHeight(source.getHeight());
        target.setPixels(Utils.ppmByteDataToPixels(source.getData()));
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
            throw new RuntimeException();
        }
    }
}
