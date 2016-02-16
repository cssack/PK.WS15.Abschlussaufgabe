/*
 * Copyright (c) 2016. Tobias Patzl, Christian Sack
 */

package game;

import bases.GameBase;
import bases.TacticalMovement;
import dataObjects.Player;
import dataObjects.Territory;
import dataObjects.enums.Phases;
import dataObjects.enums.PlayerStates;

import java.awt.*;
import java.awt.event.*;
import java.awt.font.OpenType;
import java.util.Optional;

/**
 * The game engine handles events and calls state methods accordingly on the GameState class.
 */
public class GameEngine extends GameBase implements MouseMotionListener, MouseListener, KeyListener {
    private Territory hoverTerritory;
    private boolean isMouseLeftButtonValid;
    private boolean isMouseRightButtonValid;
    private boolean repaintRequested;
    private boolean isMouseOverEndRoundButton;

    @Override
    public void init(Game game) {
        super.init(game);

        drawingBoard.addMouseMotionListener(this);
        drawingBoard.addMouseListener(this);
        drawingBoard.addKeyListener(this);
        drawingBoard.setFocusable(true);// used to allow keyboard recognition
    }

    public void checkMouseHover(MouseEvent e) {
        validateEndRoundButton(e);

        Territory newHoverTerritory = GetTerritoryAtPos(e.getPoint());
        if (newHoverTerritory == hoverTerritory)
            return;

        hoverTerritory = newHoverTerritory;

        validateMouseButtons();

        requestRepaint();
    }


    public void validateMouseButtons() {
        boolean isMouseLeftButtonValidTmp = isMouseLeftButtonValid;
        boolean isMouseRightButtonValidTmp = isMouseRightButtonValid;

        if (hoverTerritory == null) {
            isMouseLeftButtonValid = false;
            isMouseRightButtonValid = false;
        } else {
            validateLeftMouseButton();
            validateRightMouseButton();
        }


        if (isMouseLeftButtonValidTmp != isMouseLeftButtonValid || isMouseRightButtonValidTmp != isMouseRightButtonValid)
            requestRepaint();
    }

    private void validateEndRoundButton(MouseEvent e) {

        boolean old = isMouseOverEndRoundButton;
        isMouseOverEndRoundButton = (state.getGamePhase() == Phases.AttackOrMove || state
                .getGamePhase() == Phases.QuickOverViewBefore || state
                .getGamePhase() == Phases.Fortifying) && design.endButton
                .contains(e.getPoint());
        if (old != isMouseOverEndRoundButton)
            requestRepaint();
    }

    private void validateLeftMouseButton() {
        Phases gamePhase = state.getGamePhase();
        Player human = data.getHumanPlayer();
        PlayerStates humanState = human.getState();
        Territory humanSelectedTerritory = human.getSelectedTerritory();
        Player comp = data.getCompPlayer();
        Player hoverOccupant = hoverTerritory.getOccupant();

        if (humanState == PlayerStates.Waiting) {
            isMouseLeftButtonValid = false;
            return;
        }

        if (gamePhase == Phases.Landerwerb) {
            isMouseLeftButtonValid = hoverOccupant == null;
            return;
        }

        if (humanState == PlayerStates.Fortifying && state.isHumanVictoryTarget(hoverTerritory)) {
            isMouseLeftButtonValid = true;
            return;
        }

        if (humanState == PlayerStates.Reinforcing) {
            isMouseLeftButtonValid = hoverOccupant == human;
            return;
        }

        if (human.getTransferMovement() != null && human.getTransferMovement().to == hoverTerritory) {
            isMouseLeftButtonValid = true;
            return;
        }
        if (human.getAttackMovements().stream().anyMatch(x -> x.from == hoverTerritory)) {
            isMouseLeftButtonValid = true;
            return;
        }

        if (humanState == PlayerStates.FirstTerritorySelection) {
            isMouseLeftButtonValid = hoverOccupant == human && hoverTerritory.getArmyCount() > 1;
            return;
        }
        if (humanState == PlayerStates.FirstTerritorySelected) {
            boolean isNeighbor = humanSelectedTerritory.getNeighbors().contains(hoverTerritory);
            isMouseLeftButtonValid =
                    (
                            // attack able target
                            isNeighbor && hoverOccupant == comp && humanSelectedTerritory.getArmyCount() > 1
                                    && !human.getAttackMovements().stream()
                                    .anyMatch(x -> x.to == hoverTerritory) // avoid double attacks
                    )
                            || // attack cancellation
                            human.getAttackMovements().stream()
                                    .anyMatch(x -> x.consistsOf(humanSelectedTerritory, hoverTerritory))
                            || //used to be able to remove an attack
                            (hoverOccupant == human && hoverTerritory.getArmyCount() > 1); // new selection
            return;
        }
        isMouseLeftButtonValid = false;
    }

    private void validateRightMouseButton() {
        Phases gamePhase = state.getGamePhase();
        Player human = data.getHumanPlayer();
        PlayerStates humanState = human.getState();
        Territory humanSelectedTerritory = human.getSelectedTerritory();
        Player comp = data.getCompPlayer();
        Player hoverOccupant = hoverTerritory.getOccupant();

        if (humanState == PlayerStates.Fortifying && state.isHumanVictoryTarget(hoverTerritory)) {
            isMouseRightButtonValid = true;
            return;
        }

        if (humanState == PlayerStates.Waiting || gamePhase == Phases.Landerwerb || humanState == PlayerStates.Reinforcing || humanState == PlayerStates.FirstTerritorySelection) {
            isMouseRightButtonValid = false;
            return;
        }

        if (humanState == PlayerStates.FirstTerritorySelected) {
            boolean hoverIsNeighbor = humanSelectedTerritory.getNeighbors().contains(hoverTerritory);
            TacticalMovement transfer = human.getTransferMovement();
            isMouseRightButtonValid = (humanSelectedTerritory
                    .getArmyCount() > 1 && hoverIsNeighbor && hoverOccupant == human) ||
                    (transfer != null && transfer.to == humanSelectedTerritory && transfer.from == hoverTerritory);
            return;
        }
        isMouseRightButtonValid = false;
    }

    private void Repaint() {
        drawingBoard.repaint();
        repaintRequested = false;
    }

    private void tryRepaint() {
        if (!repaintRequested)
            return;
        Repaint();
    }

    public void requestRepaint() {
        repaintRequested = true;
    }


    @Override
    public void mouseDragged(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {
        checkMouseHover(e);

        tryRepaint();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        int button = e.getButton();

        if (isMouseOverEndRoundButton && button == MouseEvent.BUTTON1) {
            ContinueGamePlay();
            return;
        }
        if (button == MouseEvent.BUTTON1 && !isMouseLeftButtonValid)
            return;
        if (button == MouseEvent.BUTTON3 && !isMouseRightButtonValid)
            return;

        Player human = data.getHumanPlayer();
        PlayerStates humanState = human.getState();
        Phases gamePhase = state.getGamePhase();

        if (button == MouseEvent.BUTTON1) {
            if (gamePhase == Phases.Landerwerb) {
                state.setTerritoryOccupant(hoverTerritory, human);
                if (state.getGamePhase() == Phases.Landerwerb)
                    ki.ChooseSomeTerritory();
            } else if (humanState == PlayerStates.Reinforcing) {
                state.reinforceTerritory(hoverTerritory);
                if (data.getCompPlayer().getState() == PlayerStates.Reinforcing)
                    ki.ReinforceTerritories();
            } else if (humanState == PlayerStates.FirstTerritorySelection) {
                state.setSelectedTerritory(human, hoverTerritory);
            } else if (humanState == PlayerStates.FirstTerritorySelected) {
                if (hoverTerritory.getOccupant() == human)
                    state.setSelectedTerritory(human, hoverTerritory);
                else {
                    state.assignAttackMovement(human, hoverTerritory);
                    state.setSelectedTerritory(human, null);
                }
            } else if (humanState == PlayerStates.Fortifying) {
                Optional<TacticalMovement> opattack = human.getAttackMovements().stream().filter(x -> x.to == hoverTerritory).findFirst();
                if (opattack.isPresent())  {
                    TacticalMovement attack = opattack.get();
                    if (attack.from.getArmyCount() > 1) {
                        attack.to.increaseArmyCount();
                        attack.from.decreaseArmyCount();
                        requestRepaint();
                    }
                }
            }
        } else if (button == MouseEvent.BUTTON3) {
            if (humanState == PlayerStates.Fortifying) {
                Optional<TacticalMovement> opattack = human.getAttackMovements().stream().filter(x -> x.to == hoverTerritory).findFirst();
                if (opattack.isPresent())  {
                    TacticalMovement attack = opattack.get();
                    if (attack.to.getArmyCount() > 1) {
                        attack.to.decreaseArmyCount();
                        attack.from.increaseArmyCount();
                        requestRepaint();
                    }
                }
            } else if (gamePhase == Phases.AttackOrMove){
                state.assignTransferMovement(human, hoverTerritory);
            }
        }


        tryRepaint();
    }
    @Override
    public void keyTyped(KeyEvent e) {
        if (e.getKeyChar() == ' ') {
            if (state.getGamePhase() == Phases.Landerwerb) {
                while (state.getGamePhase() == Phases.Landerwerb) {
                    state.setTerritoryOccupant(data.getRandomUnassignedTerritory(), data.getHumanPlayer());
                    ki.ChooseSomeTerritory();
                    Repaint();
                }
            } else if (state.getGamePhase() == Phases.Reinforcement) {
                while (data.getHumanPlayer().getState() == PlayerStates.Reinforcing) {
                    state.reinforceTerritory(data.getRandomTerritory(data.getHumanPlayer().getOwnedTerritories()));
                    Repaint();
                }
                if (data.getCompPlayer().getState() == PlayerStates.Reinforcing)
                    ki.ReinforceTerritories();
                Repaint();
            } else
                ContinueGamePlay();

        }

    }


    /**
     * If the game is continue able this method end the round and continues with the game flow this likely happens when
     * the user presses the button
     */
    private void ContinueGamePlay() {
        if (state.getGamePhase() == Phases.AttackOrMove) {
            state.setPlayerState(data.getHumanPlayer(), PlayerStates.RoundFinished);
            state.setPlayerState(data.getCompPlayer(), PlayerStates.FirstTerritorySelection);
            ki.AttackAndMove();
            state.setGamePhase(Phases.QuickOverViewBefore);
        } else if (state.getGamePhase() == Phases.QuickOverViewBefore) {
            state.executePlayerMovements();
            state.setGamePhase(Phases.Fortifying);
            ki.FortifyTerritories();
        } else if (state.getGamePhase() == Phases.Fortifying) {
            if (data.getCompPlayer().getOwnedTerritories().size() == 0 || data.getHumanPlayer()
                    .getOwnedTerritories().size() == 0)
                state.setGamePhase(Phases.End);
            else
                state.setGamePhase(Phases.Reinforcement);
        }
        Repaint();
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

    }


    @Override
    public void keyReleased(KeyEvent e) {
    }


    private Territory GetTerritoryAtPos(Point p) {
        for (Territory territory : data.getAllTerritories()) {
            if (territory.contains(p))
                return territory;
        }
        return null;
    }

    public boolean getIsMouseLeftButtonValid() {
        return isMouseLeftButtonValid;
    }

    public boolean getIsMouseRightButtonValid() {
        return isMouseRightButtonValid;
    }

    public boolean getIsMouseOverEndRoundButton() {
        return isMouseOverEndRoundButton;
    }

    public Territory getHoverTerritory() {
        return hoverTerritory;
    }

}
