package org.aesy.fractal.task;

import org.apache.commons.imaging.ImageReadException;
import org.apache.commons.imaging.Imaging;
import org.apache.commons.io.IOUtils;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Task that makes a HTTP request to recieve an image which is then painted onto another image.
 */
public class ImageSubDivisionGeneratorTask implements Runnable {
    private final URL server;
    private final BufferedImage image;

    /**
     * @param server The server endpoint.
     * @param image The image to paint over.
     */
    public ImageSubDivisionGeneratorTask(URL server, BufferedImage image) {
        this.server = server;
        this.image = image;
    }

    @Override
    public void run() {
        HttpURLConnection connection = null;

        try {
            connection = (HttpURLConnection) server.openConnection();
            connection.setRequestMethod("GET");
            InputStream in = connection.getInputStream();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            IOUtils.copy(in, baos);

            in.close();
            baos.close();

            BufferedImage requestImage = Imaging.getBufferedImage(baos.toByteArray());

            Graphics graphics = image.getGraphics();
            graphics.drawImage(requestImage, 0, 0, requestImage.getWidth(), requestImage.getHeight(), null);
        } catch (IOException | ImageReadException exception) {
            System.err.println(
                "An exception was thrown while performing mandelbrot image generation task: " + exception.getMessage());
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }
}
