/*
 * Copyright (c) 2016. Tobias Patzl, Christian Sack
 */

package drawing;


import game.Game;

import javax.swing.*;
import java.awt.*;

/**
 * The drawing window is a window which displays the drawing board. Use this window to start a game.
 */
public class DrawingWindow extends JFrame {
    public DrawingWindow(Game game) throws HeadlessException {
        this.setTitle("RISK");
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        //this.setResizable(false);
        this.getContentPane().add(game.getDrawingBoard());
        this.pack(); // size to its content
        this.setVisible(true);

    }
}
