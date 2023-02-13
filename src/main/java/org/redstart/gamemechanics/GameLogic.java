package org.redstart.gamemechanics;

import org.redstart.gamemechanics.block.ColorBlock;
import org.redstart.gamemechanics.block.ColorBlockInitializer;
import org.redstart.gamemechanics.spells.interfaces.Spell;
import org.redstart.jsonclasses.Monster;
import org.redstart.jsonclasses.Player;

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class GameLogic {
    private static final Logger log = Logger.getLogger(GameLogicExecutor.class.getName());

    private static final int LENGTH_FIELD = 9;
    private static final int COUNT_COLOR = 4;

    private static final int INDEX_NAME = 0;
    private static final int INDEX_CLIENT = 1;
    private static final int INDEX_COLOR = 2;

    private Map<Integer, ? super ColorBlock> colorBlocks;


    public GameLogic() {
        colorBlocks = new ColorBlockInitializer().getColorBlocks();
    }

    public void fillFieldForServer(Player player) {
        int[][][] fieldForServer = new int[LENGTH_FIELD][LENGTH_FIELD][];

        int nextNameBlock = player.getNextNameBlock();
        int nextIndexBlock = 0;
        for (int i = fieldForServer.length - 1; i >= 0; i--) {
            for (int j = 0; j < fieldForServer[i].length; j++) {

                int color = getRandomColor();
                //int color = 1;
                int[] block = new int[]{nextNameBlock, nextIndexBlock, color};
                nextIndexBlock++;
                fieldForServer[i][j] = block;
                player.getSpawnedBlocks().add(block);
                nextNameBlock++;
            }
        }
        player.setFieldForServer(fieldForServer);
        player.setNextNameBlock(nextNameBlock);
        //testing(fieldForServer);
    }

    public static void testing(int[][][] fieldForServer) {
        for (int i = 0; i < fieldForServer.length; i++) {
            for (int j = 0; j < fieldForServer[i].length; j++) {
                System.out.print(fieldForServer[i][j][INDEX_COLOR] + " ");

            }
            for (int j = 0; j < fieldForServer[i].length; j++) {
                System.out.printf("%4d", fieldForServer[i][j][INDEX_NAME]);
            }
            System.out.print(" ");
            for (int j = 0; j < fieldForServer[i].length; j++) {
                System.out.printf("%3d", fieldForServer[i][j][INDEX_CLIENT]);
            }


            System.out.println();
        }
    }

    private int getRandomColor() {
        return (int) (Math.random() * COUNT_COLOR);
    }


    public void playerMove(GameRoom gameRoom, int nameBlockDestroyed) {
        //System.out.println("choose block - " + nameBlockDestroyed);
        Player player = gameRoom.getPlayer();
        List<Integer> blastedBlocks = player.getBlastedBlocks();
        List<int[]> spawnedBlocks = player.getSpawnedBlocks();

        blastedBlocks.clear();
        spawnedBlocks.clear();

        int[][][] fieldForServer = player.getFieldForServer();

        int chooseColor = 0;
        boolean isBreak = false;
        for (int i = 0; i < fieldForServer.length; i++) {
            if (isBreak) break;
            for (int j = 0; j < fieldForServer[i].length; j++) {
                if (fieldForServer[i][j][INDEX_NAME] == nameBlockDestroyed) {
                    blastedBlocks.add(fieldForServer[i][j][INDEX_NAME]);
                    chooseColor = fieldForServer[i][j][INDEX_COLOR];
                    actionChooseCell(player, j, i);
                    isBreak = true;
                    break;
                }
            }
        }

        ColorBlock colorBlock = (ColorBlock) colorBlocks.get(chooseColor);
        colorBlock.executeAction(gameRoom);

        //testing(fieldForServer);
    }

    public void decrementPlayerHp(Player player, int countDamage) {
        int playerHp = player.getHp();
        int playerShield = player.getShield();

        int damageToHp = countDamage / 2;
        int damageToShield = countDamage - damageToHp;

        //log.info("Damage to player - " + countDamage);

        playerShield -= damageToShield;
        if (playerShield < 0) {
            damageToHp += playerShield * (-1);
            playerShield = 0;
        }
        playerHp -= damageToHp;

        //TODO убрать потом if, для тестов нужен
        if (playerHp < 1) {
            playerHp = 100;
        }

        player.setHp(playerHp);
        player.setShield(playerShield);
    }

    public void decrementMonsterHP(Monster monster, int countDamage) {
        int monsterHP = monster.getHp() - countDamage;
        if (monsterHP <= 0) {
            monster.setHp(100);
        } else {
            monster.setHp(monsterHP);
        }
    }

    private void actionChooseCell(Player player, int x, int y) {

        int[][][] fieldForServer = player.getFieldForServer();

        int chooseColor = fieldForServer[y][x][INDEX_COLOR];
        fieldForServer[y][x][INDEX_COLOR] = -1;
        int countChange = 1;
        int countCheck = 0;

        while (countChange != countCheck) {
            for (int i = 0; i < fieldForServer.length; i++) {
                for (int j = 0; j < fieldForServer[i].length; j++) {
                    if (fieldForServer[i][j][INDEX_COLOR] == -1) {
                        countChange += changeAroundCell(player, j, i, chooseColor);
                        fieldForServer[i][j][INDEX_COLOR] = -2;
                        countCheck++;
                    }
                }
            }
        }

        moveDestroyedCellsToTop(fieldForServer);
        fillingEmptyCells(player);
    }

    private int changeAroundCell(Player player, int x, int y, int chooseColor) {

        int[][][] fieldForServer = player.getFieldForServer();
        List<Integer> blastedBlocks = player.getBlastedBlocks();

        int countChange = 0;
        int ky = -1;
        int kx = 0;
        if (y + ky >= 0 && fieldForServer[y + ky][x + kx][INDEX_COLOR] == chooseColor) {
            fieldForServer[y + ky][x + kx][INDEX_COLOR] = -1;
            countChange++;
            blastedBlocks.add(fieldForServer[y + ky][x + kx][INDEX_NAME]);
        }

        ky = 1;
        if (y + ky <= fieldForServer.length - 1 && fieldForServer[y + ky][x + kx][INDEX_COLOR] == chooseColor) {
            fieldForServer[y + ky][x + kx][INDEX_COLOR] = -1;
            countChange++;
            blastedBlocks.add(fieldForServer[y + ky][x + kx][INDEX_NAME]);
        }

        ky = 0;
        kx = -1;
        if (x + kx >= 0 && fieldForServer[y + ky][x + kx][INDEX_COLOR] == chooseColor) {
            fieldForServer[y + ky][x + kx][INDEX_COLOR] = -1;
            countChange++;
            blastedBlocks.add(fieldForServer[y + ky][x + kx][INDEX_NAME]);
        }

        kx = 1;
        if (x + kx <= fieldForServer[y].length - 1 && fieldForServer[y + ky][x + kx][INDEX_COLOR] == chooseColor) {
            fieldForServer[y + ky][x + kx][INDEX_COLOR] = -1;
            countChange++;
            blastedBlocks.add(fieldForServer[y + ky][x + kx][INDEX_NAME]);
        }
        return countChange;
    }

    private void moveDestroyedCellsToTop(int[][][] fieldForServer) {

        for (int i = 0; i < fieldForServer[0].length; i++) {
            for (int j = 0; j < fieldForServer.length; j++) {
                if (fieldForServer[j][i][INDEX_COLOR] < 0) {
                    moveColumns(fieldForServer, i);
                    break;
                }
            }
        }
    }

    private void moveColumns(int[][][] fieldForServer, int x) {

        for (int i = fieldForServer.length - 1; i >= 0; i--) {
            for (int j = fieldForServer.length - 1; j > (fieldForServer.length - 1 - i); j--) {
                if (fieldForServer[j][x][INDEX_COLOR] < 0 && fieldForServer[j - 1][x][INDEX_COLOR] >= 0) {
                    int tempColor = fieldForServer[j][x][INDEX_COLOR];
                    fieldForServer[j][x][INDEX_COLOR] = fieldForServer[j - 1][x][INDEX_COLOR];
                    fieldForServer[j - 1][x][INDEX_COLOR] = tempColor;

                    int tempName = fieldForServer[j][x][INDEX_NAME];
                    fieldForServer[j][x][INDEX_NAME] = fieldForServer[j - 1][x][INDEX_NAME];
                    fieldForServer[j - 1][x][INDEX_NAME] = tempName;
                }
            }
        }
    }

    private void fillingEmptyCells(Player player) {
        int[][][] fieldForServer = player.getFieldForServer();

        for (int i = 0; i < fieldForServer.length; i++) {
            for (int j = 0; j < fieldForServer[i].length; j++) {
                if (fieldForServer[i][j][INDEX_COLOR] < 0) {

                    int indexColor = getRandomColor();
                    int nameBlock = player.getNextNameBlock();
                    fieldForServer[i][j][INDEX_COLOR] = indexColor;
                    fieldForServer[i][j][INDEX_NAME] = nameBlock;

                    player.getSpawnedBlocks().add(fieldForServer[i][j]);
                }
            }
        }
    }

    public void spellMove(GameRoom gameRoom, int clientMessage) {
        Player player = gameRoom.getPlayer();
        List<Spell> availableSpells = player.getAvailableSpells();
        int numSpell = clientMessage * (-1) - 1;
        if (numSpell < availableSpells.size()) {
            Spell chooseSpell = availableSpells.get(numSpell);
            int costSpell = chooseSpell.getCost();
            int mana = player.getMana();
            if (mana >= costSpell) {
                chooseSpell.activate();
            }
        } else {
            log.info("нет доступных заклинаний");
        }
    }
}