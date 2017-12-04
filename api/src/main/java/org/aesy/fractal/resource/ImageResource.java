package org.aesy.fractal.resource;

import org.aesy.fractal.fractal.EscapeTimeFractal;
import org.aesy.fractal.fractal.FractalImageGenerator;
import org.aesy.fractal.fractal.MandelbrotFractal;
import org.aesy.fractal.fractal.MultiThreadedFractalImageGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;
import java.awt.image.BufferedImage;

@RestController
public class ImageResource {
    private static final Logger log = LoggerFactory.getLogger(ImageResource.class);

    @GetMapping(
        value = "/mandelbrot/{min_c_re}/{min_c_im}/{max_c_re}/{max_c_im}/{x}/{y}/{inf_n}",
        produces = { "image/x-portable-graymap" }
    )
    @ResponseBody
    public BufferedImage mandelbrot(
        @NotNull @PathVariable("min_c_re") Double minReal,
        @NotNull @PathVariable("min_c_im") Double minImaginary,
        @NotNull @PathVariable("max_c_re") Double maxReal,
        @NotNull @PathVariable("max_c_im") Double maxImaginary,
        @NotNull @PathVariable("x") Integer width,
        @NotNull @PathVariable("y") Integer height,
        @NotNull @PathVariable("inf_n") Integer maxIterations
    ) {
        EscapeTimeFractal fractal = new MandelbrotFractal(maxIterations);
        FractalImageGenerator generator = new MultiThreadedFractalImageGenerator(
            fractal, null, minReal, maxReal, minImaginary, maxImaginary);

        return generator.generate(width, height);
    }
}
