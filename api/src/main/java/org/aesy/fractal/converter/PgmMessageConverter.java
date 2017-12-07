package org.aesy.fractal.converter;

import org.apache.commons.imaging.ImageFormats;
import org.apache.commons.imaging.ImageReadException;
import org.apache.commons.imaging.ImageWriteException;
import org.apache.commons.imaging.Imaging;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Provider of response/request conversion between Bufferedimage <-> PGM encoded bytes.
 */
public class PgmMessageConverter extends AbstractHttpMessageConverter<BufferedImage> {
    public PgmMessageConverter() {
        super(new MediaType("image", "x-portable-graymap"));
    }

    @Override
    protected boolean supports(Class<?> clazz) {
        return BufferedImage.class.isAssignableFrom(clazz);
    }

    @Override
    protected BufferedImage readInternal(
        Class<? extends BufferedImage> clazz,
        HttpInputMessage input
    ) throws HttpMessageNotReadableException {
        try {
            return Imaging.getBufferedImage(input.getBody());
        } catch (ImageReadException | IOException exception) {
            throw new HttpMessageNotWritableException("Failed to decode image", exception);
        }
    }

    @Override
    protected void writeInternal(
        BufferedImage image,
        HttpOutputMessage outputMessage
    ) throws HttpMessageNotWritableException {
        Map<String, Object> params = new HashMap<>();
        /*
         * Bug notice:
         *   Setting the following parameter causes an ImageWriteException.
         *   Default is RAWBITS = YES.
         *   Issue ticket can be found here: https://issues.apache.org/jira/browse/IMAGING-209
         */
        // params.put(PnmImageParser.PARAM_KEY_PNM_RAWBITS, PnmImageParser.PARAM_VALUE_PNM_RAWBITS_YES);

        try {
            Imaging.writeImage(image, outputMessage.getBody(), ImageFormats.PGM, params);
        } catch (ImageWriteException | IOException exception) {
            throw new HttpMessageNotWritableException("Failed to encode image", exception);
        }
    }
}
