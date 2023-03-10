package org.redstart.gamemechanics.block;

import org.redstart.annotation.ColorClass;
import org.redstart.gamemechanics.GameRoom;
import org.redstart.jsonclasses.Player;

import java.util.logging.Logger;

@ColorClass(numberColor = 2)
public class GreenColorBlock implements ColorBlock {
    private static final Logger log = Logger.getLogger(GreenColorBlock.class.getName());
    @Override
    public void executeAction(GameRoom gameRoom) {
        Player player = gameRoom.getPlayer();

        int countChoose = player.getBlastedBlocks().size();
        player.setHp(player.getHp() + countChoose);
        //log.info("Green color choose - " + gameRoom.getPlayer().getBlastedBlocks().size());
    }
}
