package engine;

import dataObjects.Territory;
import dataObjects.enums.Occupants;
import dataObjects.game.Game;

import java.util.Random;

/**
 * Created by chris on 07.01.2016.
 */
public class PcPlayerEngine {
    private Game game;
    private Random rand = new Random();


    public PcPlayerEngine(Game game) {
        this.game = game;
    }


    public void ChooseSomeTerritory()
    {
        Territory choosen = getRandomTerritory();
        while (choosen.getOccupant() != Occupants.NotDef)
        {
            choosen = getRandomTerritory();
        }
        game.getState().setTerritoryOccupant(choosen, Occupants.Pc);
    }


    private Territory getRandomTerritory()
    {
        return game.getData().getAllTerritories().get(rand.nextInt(game.getData().getAllTerritories().size()));
    }
}
