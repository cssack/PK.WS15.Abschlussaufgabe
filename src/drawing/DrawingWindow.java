package drawing;


import dataObjects.game.Game;

import javax.swing.*;
import java.awt.*;

/**
 * Created by chris on 07.01.2016.
 */
public class DrawingWindow extends JFrame {
    public DrawingWindow(Game game) throws HeadlessException {
        this.setTitle("RISK");
        this.setBounds(0,0,1250,750);
        this.getContentPane().setLayout(null);
        this.getContentPane().add(game.getDrawingBoard());

        game.getDrawingBoard().setBounds(0,100,1250, 650);
        this.setVisible(true);

    }
}
