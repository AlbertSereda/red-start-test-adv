package org.redstart.gamemechanics.block;

import org.redstart.annotation.ColorClass;
import org.redstart.gamemechanics.GameRoom;
import org.redstart.jsonclasses.Monster;
import org.redstart.jsonclasses.Player;

import java.util.logging.Logger;

@ColorClass(numberColor = 3)
public class BlueColorBlock implements ColorBlock {
    private static final Logger log = Logger.getLogger(BlueColorBlock.class.getName());
    @Override
    public void executeAction(GameRoom gameRoom) {
        Player player = gameRoom.getPlayer();

        int countChoose = player.getBlastedBlocks().size();
        player.setMana(player.getMana() + countChoose);
        log.info("Blue color choose - " + countChoose);
    }
}
