package org.aesy.fractal.resource;

import org.aesy.fractal.color.ColorGradient;
import org.aesy.fractal.color.SimpleClampedLinearGradient;
import org.aesy.fractal.fractal.EscapeTimeFractal;
import org.aesy.fractal.fractal.FractalImageGenerator;
import org.aesy.fractal.fractal.MandelbrotFractal;
import org.aesy.fractal.fractal.MultiThreadedFractalImageGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;

@RestController
public class ImageResource {
    private static final Logger log = LoggerFactory.getLogger(ImageResource.class);

    /**
     * Generates an image of the mandelbrot set within a given space.
     * The image only contains interpolated color values between black (0) and white (255),
     * where white represent the maximum amount of iterations.
     *
     * @param minReal
     * @param minImaginary
     * @param maxReal
     * @param maxImaginary
     * @param width Horizontal resolution of the generated image, in pixels.
     * @param height Vertical resolution of the generated image, in pixels.
     * @param maxIterations The maximum allowed iterations to perform on each point.
     * @return A binary PGM encoded image.
     */
    @GetMapping(
        value = "/mandelbrot/{min_c_re}/{min_c_im}/{max_c_re}/{max_c_im}/{x}/{y}/{inf_n}",
        produces = { "image/x-portable-graymap" }
    )
    @ResponseBody
    public ResponseEntity<BufferedImage> mandelbrot(
        @NotNull @PathVariable("min_c_re") BigDecimal minReal,
        @NotNull @PathVariable("min_c_im") BigDecimal minImaginary,
        @NotNull @PathVariable("max_c_re") BigDecimal maxReal,
        @NotNull @PathVariable("max_c_im") BigDecimal maxImaginary,
        @NotNull @PathVariable("x") Integer width,
        @NotNull @PathVariable("y") Integer height,
        @NotNull @PathVariable("inf_n") BigInteger maxIterations
    ) {
        MathContext context = MathContext.UNLIMITED;
        EscapeTimeFractal fractal = new MandelbrotFractal(maxIterations, context);
        ColorGradient colorSampler = new SimpleClampedLinearGradient(
            0, maxIterations.intValue(), Color.BLACK, Color.WHITE);
        FractalImageGenerator imageGenerator = new MultiThreadedFractalImageGenerator(
            fractal, colorSampler, minReal, maxReal, minImaginary, maxImaginary, context);
        BufferedImage image = imageGenerator.generate(width, height);

        return ResponseEntity.ok()
                             .header("Content-Disposition", "attachment; filename=\"mandelbrot.pgm\"")
                             .body(image);
    }
}
