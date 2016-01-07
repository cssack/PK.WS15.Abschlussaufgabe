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
        this.setBounds(0, 0, 1250, 740);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        this.getContentPane().setLayout(null);
        this.getContentPane().add(game.getDrawingBoard());

        game.getDrawingBoard().setBounds(0, 000, 1250, 690);
        this.setVisible(true);
    }
}
