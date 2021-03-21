package org.example.reader.bmp;

import java.util.Arrays;
import org.apache.commons.lang3.ArrayUtils;
import org.example.misc.Utils;
import org.example.model.bmp.Bmp;
import org.springframework.stereotype.Component;

@Component
public class BmpValidator {
    private static final byte[] ZERO_FOUR_BYTES_ARRAY = {0, 0, 0, 0};
    private static final byte[] ZERO_TWO_BYTES_ARRAY = {0, 0};

    public void validateBmpData(byte[] rawBytes) {
        byte[] fileType = Arrays.copyOfRange(
                rawBytes, Bmp.FILE_TYPE_START_INDEX, Bmp.FILE_SIZE_START_INDEX);

        if (!Bmp.FILE_TYPE.equals(new String(fileType))) {
            throw new RuntimeException();
        }

        byte[] pixelDataOffset = Arrays.copyOfRange(
                rawBytes, Bmp.PIXEL_DATA_OFFSET_START_INDEX, Bmp.INFO_SIZE_START_INDEX);
        ArrayUtils.reverse(pixelDataOffset);
        pixelDataOffset = ArrayUtils.addAll(ZERO_FOUR_BYTES_ARRAY, pixelDataOffset);

        if (Bmp.PIXEL_DATA_OFFSET != Utils.byteArrayToLong(pixelDataOffset)) {
            throw new RuntimeException();
        }

        byte[] infoSize = Arrays.copyOfRange(
                rawBytes, Bmp.INFO_SIZE_START_INDEX, Bmp.WIDTH_START_INDEX);
        ArrayUtils.reverse(infoSize);
        infoSize = ArrayUtils.addAll(ZERO_FOUR_BYTES_ARRAY, infoSize);

        if (Bmp.INFO_SIZE != Utils.byteArrayToLong(infoSize)) {
            throw new RuntimeException();
        }

        byte[] bitsPerPixel = Arrays.copyOfRange(
                rawBytes, Bmp.BITS_PER_PIXEL_START_INDEX, Bmp.COMPRESSION_START_INDEX);
        ArrayUtils.reverse(bitsPerPixel);
        bitsPerPixel = ArrayUtils.addAll(ZERO_TWO_BYTES_ARRAY, bitsPerPixel);

        if (Bmp.DEFAULT_BITS_PER_PIXEL != Utils.byteArrayToInt(bitsPerPixel)) {
            throw new RuntimeException();
        }
    }
}
