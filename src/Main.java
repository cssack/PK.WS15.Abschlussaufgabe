import dataObjects.game.Game;
import drawing.DrawingWindow;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        Game game = new Game();
        game.load();
        DrawingWindow dw = new DrawingWindow(game);
    }
}
