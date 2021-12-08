package com.arcane22.dbdbdeep.v1;

import javax.swing.*;

public class MainFrame extends JFrame {

    // private constants, instance variables
    private final int SCREEN_SHORT = 540, SCREEN_LONG = 960;
    private final String title = "Public Toilet Application";

    private int screenWidth, screenHeight;
    private int orientation;    // 0: portrait, 1: landscape


    //constructor
    public MainFrame() {

        screenWidth = (orientation == 0) ? SCREEN_SHORT : SCREEN_LONG;
        screenHeight= (orientation == 0) ? SCREEN_LONG : SCREEN_SHORT;

        setTitle(title);
        setSize(screenWidth, screenHeight);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);
        setResizable(false);
        setVisible(true);
    }

    public int getScreenWidth() {
        return screenWidth;
    }
    public int getScreenHeight() {
        return screenHeight;
    }
}
