package com.arcane22.dbdbdeep.v1;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageSource {

    // Private instance variables
    private BufferedImage img;

    private int width, height;
    private int xPos, yPos;


    // Constructor
    public ImageSource() {
    }

    public ImageSource(String uri) {
        if(readImage(uri)) {
            width = img.getWidth();
            height = img.getHeight();
            xPos = yPos = 0;
        }
    }

    public ImageSource(String uri, int xPos, int yPos) {
        if(readImage(uri)) {
            this.xPos = xPos;
            this.yPos = yPos;
        }
    }


    /* Read Image from uri
     * @Param {String} uri, uri of image source
     * @Return {boolean}, if image is null, return false / else return true
     */
    private boolean readImage(String path) {

        try {
            img = ImageIO.read(new File(path));
            width = img.getWidth();
            height = img.getHeight();
            return true;
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }


    /* Draw Image in Swing Component (JFrame, Jpanel .. etc)
     * @Param {Graphics2D} g2D, Graphics2D object for drawing image
     * @Return {boolean}, if image is null, return false / else draw image & return true
     */
    public boolean drawImage(Graphics2D g2D) {
        if(g2D != null) {
            g2D.drawImage(img, xPos, yPos, width, height, null);
            return true;
        }

        return false;
    }


    /* Set x pos to center of scene
     * @Return {void}
     */
    public void setXToCenter(int screenWidth) {
        xPos = (screenWidth / 2) - (width / 2);
    }


    /* Set y pos to center of scene
     * @Return {void}
     */
    public void setYToCenter(int screenHeight) {
        yPos = (screenHeight / 2) - (height / 2);
    }




    // Get & set image source (BufferedImage)
    public BufferedImage getImage() {
        return img;
    }
    public boolean setImage(String uri) {
        return readImage(uri);
    }
    public boolean setImage(BufferedImage img) {
        if(img != null) {
            this.img = img;
            this.width = img.getWidth();
            this.height = img.getHeight();
            return true;
        }
        return false;
    }


    // Get & set width of image
    public int getWidth() {
        return width;
    }
    public void setWidth(int width) {
        this.width = width;
    }

    // Get & set height of image
    public int getHeight() {
        return height;
    }
    public void setHeight(int height) {
        this.height = height;
    }

    // Get & set x position of image
    public int getXPos() {
        return xPos;
    }
    public void setXPos(int xPos) {
        this.xPos = xPos;
    }

    // Get & set y position of image
    public int getYPos() {
        return yPos;
    }
    public void setYPos(int yPos) {
        this.yPos = yPos;
    }
}
