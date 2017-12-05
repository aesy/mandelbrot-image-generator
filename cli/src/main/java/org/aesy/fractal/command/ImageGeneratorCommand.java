package org.aesy.fractal.command;

import java.net.URL;
import java.util.List;
import java.util.concurrent.Callable;

import static picocli.CommandLine.Parameters;

/**
 * Default CLI command. Generates a mandelbrot set by dividing the work between the given servers and
 * combines the results into one image. The resulting image is saved in the working directory as a
 * binary PGM (portable graymap map).
 */
public class ImageGeneratorCommand implements Callable<Void> {
    @Parameters(index = "0")
    private Double min_c_re;

    @Parameters(index = "1")
    private Double min_c_im;

    @Parameters(index = "2")
    private Double max_c_re;

    @Parameters(index = "3")
    private Double max_c_im;

    @Parameters(index = "4")
    private Double max_n;

    @Parameters(index = "5")
    private Double width;

    @Parameters(index = "6")
    private Double height;

    @Parameters(index = "7")
    private Double divisions;

    @Parameters(index = "8", arity = "1..*")
    private List<URL> servers;

    @Override
    public Void call() {
        // TODO subdivide image
        // TODO create workers
        // TODO assemble image
        // TODO save image to disk

        return null;
    }
}
