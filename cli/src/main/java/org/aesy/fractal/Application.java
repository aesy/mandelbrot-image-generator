package org.aesy.fractal;

import org.aesy.fractal.command.ImageGeneratorCommand;
import picocli.CommandLine;

public class Application {
    public static void main(String[] args) {
        CommandLine.call(new ImageGeneratorCommand(), System.err, args);
    }
}

