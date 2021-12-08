package com.arcane22.dbdbdeep.v1;

public class MainApp extends Thread{

    // private constants, instance variables
    private final int FRAME_RATE = 100;      // 100 FPS

    private MainFrame mainFrame;
    private MainPanel mainPanel;

    // constructor
    public MainApp()
    {
        mainFrame = new MainFrame();
        mainPanel = new MainPanel(mainFrame.getScreenWidth(), mainFrame.getScreenHeight());
        mainFrame.add(mainPanel);
    }

    @Override
    public void run() {

        while(true){

            try {
                Thread.sleep(1000 / FRAME_RATE);
                mainFrame.repaint();
            }
            catch(InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

