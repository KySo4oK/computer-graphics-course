package org.example.model.ppm;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class Ppm {
    public static final String DEFAULT_MAGIC_NUMBER = "P6";
    public static final int DEFAULT_MAX_COLOR_VALUE = 255;

    private String magicNumber;
    private int width;
    private int height;
    private int maxColorValue;
    private byte[] data;

    public Ppm() {
        magicNumber = DEFAULT_MAGIC_NUMBER;
        maxColorValue = DEFAULT_MAX_COLOR_VALUE;
    }

    public enum MetaData {
        MAGIC_NUMBER(1, "P\\d"),
        WIDTH(2, "\\d+"),
        HEIGHT(3, "\\d+"),
        MAX_COLOR_VALUE(4, "\\d+");

        private final int order;
        private final String regex;

        MetaData(int order, String regex) {
            this.order = order;
            this.regex = regex;
        }

        public int getOrder() {
            return order;
        }

        public String getRegex() {
            return regex;
        }
    }
}
