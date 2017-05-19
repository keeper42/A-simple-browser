package main.HTTPClientEnd;
// Created by LJF on 2017/5/19. 

import javax.swing.*;
import java.awt.*;

public class Main {

    public static void main(String[] args) {

        Browser browser = new Browser();
        browser.setVisible(true);
        browser.setLocationRelativeTo(null);

        Dimension ScreenSize = browser.getToolkit().getScreenSize();
        int width = ScreenSize.width * 8 / 10;
        int height = ScreenSize.height * 8 / 10;
        browser.setBounds(width / 8, height / 8, width, height);
        browser.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

    }

}
