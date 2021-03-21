package org.example.writer.ppm;

import org.apache.commons.io.FileUtils;
import org.example.misc.Utils;
import org.example.model.CustomImage;
import org.example.writer.ImageWriter;

import java.io.File;
import java.io.IOException;

public class PpmImageWriter implements ImageWriter {
    @Override
    public void write(CustomImage image, String filePath) throws IOException {
        byte[] imageData = Utils.ppmImageToByteData(image);
        FileUtils.writeByteArrayToFile(new File(filePath), imageData);
    }
}
