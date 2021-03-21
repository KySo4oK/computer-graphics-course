package org.example;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter
public class ConsoleConvertCommand {
    private String sourceFileName;
    private String sourceFileExtension;
    private String goalFormat;
    private String outputFileName;
}
