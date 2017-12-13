package org.aesy.fractal.command;

import org.aesy.fractal.image.ImageSubDivider;
import org.aesy.fractal.task.ImageSubDivisionGeneratorTask;
import org.apache.commons.imaging.ImageFormats;
import org.apache.commons.imaging.ImageWriteException;
import org.apache.commons.imaging.Imaging;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.StringJoiner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static picocli.CommandLine.Parameters;

/**
 * Default CLI command. Generates a mandelbrot set by dividing the work between the given hosts and
 * combines the results into one image. The resulting image is saved in the working directory as a
 * binary PGM (portable graymap map).
 */
public class ImageGeneratorCommand implements Runnable {
    @Parameters(index = "0")
    private Double min_c_re;

    @Parameters(index = "1")
    private Double min_c_im;

    @Parameters(index = "2")
    private Double max_c_re;

    @Parameters(index = "3")
    private Double max_c_im;

    @Parameters(index = "4")
    private Integer max_n;

    @Parameters(index = "5")
    private Integer width;

    @Parameters(index = "6")
    private Integer height;

    @Parameters(index = "7")
    private Integer divisions;

    @Parameters(index = "8", arity = "1..*")
    private List<URL> hosts;

    @Override
    public void run() {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        List<ImageSubDivider.SubDivision> subDividedImages = new ImageSubDivider(divisions, divisions).divide(image);

        ExecutorService threadPool = Executors.newCachedThreadPool();
        double realRange = max_c_re - min_c_re;
        double imaginaryRange = max_c_im - min_c_im;

        for (int i = 0; i < subDividedImages.size(); i++) {
            ImageSubDivider.SubDivision subDivision = subDividedImages.get(i);
            BufferedImage subDividedImage = subDivision.getImage();
            URL host = hosts.get(i % hosts.size());

            // Recalculate space for current subdivision
            double xPercent = subDivision.getOriginalMinX() / (double) width;
            double yPercent = subDivision.getOriginalMinY() / (double) height;
            double widthPercent = subDividedImage.getWidth() / (double) width;
            double heightPercent = subDividedImage.getHeight() / (double) height;

            String resource = new StringJoiner("/")
                                 .add("/mandelbrot")
                                 .add(Double.toString(min_c_re + realRange * xPercent))
                                 .add(Double.toString(min_c_im + imaginaryRange * yPercent))
                                 .add(Double.toString(min_c_re + realRange * xPercent + realRange * widthPercent))
                                 .add(Double.toString(min_c_im + imaginaryRange * yPercent + imaginaryRange * heightPercent))
                                 .add(Integer.toString(subDividedImage.getWidth()))
                                 .add(Integer.toString(subDividedImage.getHeight()))
                                 .add(Integer.toString(max_n))
                                 .toString();

            URL url;
            try {
                url = new URL(host, resource);
            } catch (MalformedURLException exception) {
                System.err.println("An exception was thrown while performing mandelbrot image generation task: " +
                                   exception.getMessage());
                return;
            }

            threadPool.submit(new ImageSubDivisionGeneratorTask(url, subDividedImage));
        }

        try {
            // Block until completion.
            threadPool.shutdown();
            threadPool.awaitTermination(1, TimeUnit.HOURS);
        } catch (InterruptedException exception) {
            System.err.println("An exception was thrown while performing mandelbrot image generation task: "
                               + exception.getMessage());
            return;
        }

        File file = new File("output.pgm");
        HashMap<String, Object> params = new HashMap<>();

        try {
            Imaging.writeImage(image, file, ImageFormats.PGM, params);
        } catch (ImageWriteException | IOException exception) {
            System.err.println("An exception was thrown while writing image to disk: "
                               + exception.getMessage());
            return;
        }
    }
}
