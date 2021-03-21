package org.example.reader.common;

import java.io.File;

public interface IRawByteReader {
    byte[] readBytes(File source);
}
