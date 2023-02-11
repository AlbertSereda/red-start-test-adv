package org.redstart.gamemechanics;

import org.redstart.gamemechanics.spells.interfaces.WithTimeSpell;
import org.redstart.jsonclasses.Monster;

import java.nio.channels.SocketChannel;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class GameLoop implements Runnable {

    private static final Logger log = Logger.getLogger(GameLoop.class.getName());

    private Map<SocketChannel, GameRoom> gameRooms;
    private GameLogicExecutor gameLogicExecutor;

    public GameLoop(Map<SocketChannel, GameRoom> gameRooms) {
        this.gameRooms = gameRooms;
    }

    @Override
    public void run() {
        while (true) {

            for (Map.Entry<SocketChannel, GameRoom> entry : gameRooms.entrySet()) {
                SocketChannel socketChannel = entry.getKey();
                GameRoom gameRoom = entry.getValue();
                Monster monster = gameRoom.getMonster();


                List<WithTimeSpell> activeSpells = gameRoom.getPlayer().getActiveSpell();

                int fixSize = 0;
                for (int i = 0; i < activeSpells.size() - fixSize; i++) {
                    WithTimeSpell spell = activeSpells.get(i);
                    spell.update();
                    if (spell.getTimeToEnd() <= 0) {
                        spell.deactivate();
                        activeSpells.remove(i);
                        fixSize++;
                    }
                }

                int currentSpeed = monster.getUpdateSpeedLogic().updateCurrentSpeed(monster.getMaxSpeed(), monster.getTimeCreation());
                monster.setCurrentSpeed(currentSpeed);
                if (monster.getCurrentSpeed() <= 0) {
                    gameLogicExecutor.executeMove(socketChannel, Move.MONSTER);
                    monster.setNewTimeCreation();
                }
            }
        }
    }

    public void setGameLogicExecutor(GameLogicExecutor gameLogicExecutor) {
        this.gameLogicExecutor = gameLogicExecutor;
    }
}