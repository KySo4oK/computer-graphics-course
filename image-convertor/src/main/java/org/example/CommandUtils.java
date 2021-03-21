package org.example;

import static org.example.model.CustomImage.BMP;
import static org.example.model.CustomImage.PPM;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.example.exception.ExtensionNotSupportedException;
import org.example.exception.GoalFormatMissingException;
import org.example.exception.SourceFileMissingException;

public class CommandUtils {
    public static final String OUTPUT = "--output";
    public static final String GOAL_FORMAT = "--goal-format";
    public static final String SOURCE = "--source";
    public static final String EQUALS_SIGN = "=";
    public static final List<String> supportedFileExtensions = Stream.of(BMP, PPM).collect(Collectors.toList());

    private CommandUtils() {
    }

    public static ConsoleConvertCommand parseConvertCommand(String... args) {
        ConsoleConvertCommand convertCommand = new ConsoleConvertCommand();
        convertCommand.setSourceFileName(getSourceFileName(args));
        convertCommand.setSourceFileExtension(getSourceFileExtension(convertCommand.getSourceFileName()));
        convertCommand.setGoalFormat(getGoalFormat(args));
        convertCommand.setOutputFileName(getOutputFileName(args, convertCommand));
        return convertCommand;
    }

    private static String getOutputFileName(String[] args, ConsoleConvertCommand convertCommand) {
        for (String arg : args) {
            if (arg.startsWith(OUTPUT)) {
                return arg.split(EQUALS_SIGN)[1];
            }
        }
        return getDefaultOutputFileName(args, convertCommand);
    }

    private static String getDefaultOutputFileName(String[] args, ConsoleConvertCommand convertCommand) {
        String sourceFileName = convertCommand.getSourceFileName();
        return sourceFileName.replaceAll(convertCommand.getSourceFileExtension(), getGoalFormat(args));
    }

    private static String getGoalFormat(String[] args) {
        for (String arg : args) {
            if (arg.startsWith(GOAL_FORMAT)) {
                return arg.split(EQUALS_SIGN)[1];
            }
        }
        throw new GoalFormatMissingException("missing goal-format");
    }

    private static String getSourceFileExtension(String sourceFileName) {
        return supportedFileExtensions.stream().filter(sourceFileName::endsWith)
                .findAny()
                .orElseThrow(() -> new ExtensionNotSupportedException("unsupported extension"));
    }

    private static String getSourceFileName(String[] args) {
        for (String arg : args) {
            if (arg.startsWith(SOURCE)) {
                return arg.split(EQUALS_SIGN)[1];
            }
        }
        throw new SourceFileMissingException("missing source file");
    }
}
