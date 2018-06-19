package org.aesy.fractal.image;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

/**
 * An image divider divides a BufferedImage into a list of subimages. Subimages share the same data with the original
 * image, meaning that changing a subimage changes the original as well.
 */
public class ImageSubDivider {
    /**
     * A subdivision contains an subdivided image and its' original minX and minY coordinates.
     */
    public class SubDivision {
        private final int originalX;
        private final int originalY;
        private final BufferedImage image;

        public SubDivision(int originalX, int originalY, BufferedImage image) {
            this.originalX = originalX;
            this.originalY = originalY;
            this.image = image;
        }

        public int getOriginalMinX() {
            return originalX;
        }

        public int getOriginalMinY() {
            return originalY;
        }

        public BufferedImage getImage() {
            return image;
        }
    }

    private final int horizontalDivisions;
    private final int verticalDivisions;

    /**
     * @param horizontalDivisions The amount of horizontal subdivisions.
     * @param verticalDivisions The amount of vertical subdivisions.
     */
    public ImageSubDivider(int horizontalDivisions, int verticalDivisions) {
        this.horizontalDivisions = horizontalDivisions;
        this.verticalDivisions = verticalDivisions;
    }

    /**
     * Creates a list of size 'horizontalDivision * verticalDivisions' of Subdivision containing subimages
     * connected to the given image.
     *
     * @param image The image to subdivide.
     * @return A list of subdivisions.
     */
    public List<SubDivision> divide(BufferedImage image) {
        List<SubDivision> subImages = new ArrayList<>();
        int subDivisionWidth = image.getWidth() / horizontalDivisions;
        int subDivisionHeight = image.getHeight() / verticalDivisions;

        for (int i = 0; i < horizontalDivisions; i++) {
            for (int j = 0; j < verticalDivisions; j++) {
                int width = subDivisionWidth;
                int height = subDivisionHeight;
                int x = subDivisionWidth * i;
                int y = subDivisionHeight * j;

                if (i == 0 && j == 0) {
                    // Assign potential extra space to first image
                    width += image.getWidth() % subDivisionWidth;
                    height += image.getHeight() % subDivisionHeight;
                }

                BufferedImage subImage = image.getSubimage(x, y, width, height);
                subImages.add(new SubDivision(x, y, subImage));
            }
        }

        return subImages;
    }
}
