package com.example.demo;

public class CommandUtils {
    public static final String OUTPUT = "--output";
    public static final String SOURCE = "--source";
    public static final String EQUALS_SIGN = "=";

    private CommandUtils() {
    }

    public static ConsoleConvertCommand parseConvertCommand(String... args) {
        ConsoleConvertCommand convertCommand = new ConsoleConvertCommand();
        convertCommand.setSourceFileName(getSourceFileName(args));
        convertCommand.setOutputFileName(getOutputFileName(args, convertCommand));
        return convertCommand;
    }

    private static String getOutputFileName(String[] args, ConsoleConvertCommand convertCommand) {
        for (String arg : args) {
            if (arg.startsWith(OUTPUT)) {
                return arg.split(EQUALS_SIGN)[1];
            }
        }
        throw new RuntimeException("missing output file");
    }

    private static String getSourceFileName(String[] args) {
        for (String arg : args) {
            if (arg.startsWith(SOURCE)) {
                return arg.split(EQUALS_SIGN)[1];
            }
        }
        throw new RuntimeException("missing source file");
    }
}
