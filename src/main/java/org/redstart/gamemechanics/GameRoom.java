package org.redstart.gamemechanics;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.redstart.gamemechanics.logicstrategy.BasicMonsterMoveLogicImpl;
import org.redstart.gamemechanics.logicstrategy.BasicUpdateSpeedLogicImpl;
import org.redstart.gamemechanics.spells.FireBallSpell;
import org.redstart.gamemechanics.spells.NextDamageProtectionSpell;
import org.redstart.gamemechanics.spells.StanSpell;
import org.redstart.jsonclasses.AdventureData;
import org.redstart.jsonclasses.Monster;
import org.redstart.jsonclasses.Player;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class GameRoom {
    private Player player;
    private Monster monster;

    private final Lock lock;

    @JsonIgnore
    private GameLogic gameLogic;

    private AdventureData adventureData;

    public GameRoom(GameLogic gameLogic) {
        player = new Player("RedStart", 100, 0, 0);
        player.addAvailableSpell(new FireBallSpell(this,
                20,
                20));
        player.addAvailableSpell(new StanSpell(this,
                20,
                5000,
                0));
        player.addAvailableSpell(new NextDamageProtectionSpell(this,
                20,
                0));

        monster = new Monster(
                "Sladkoeshka",
                100,
                10000,
                new BasicMonsterMoveLogicImpl(this),
                new BasicUpdateSpeedLogicImpl());
        lock = new ReentrantLock();
        this.gameLogic = gameLogic;
        gameLogic.fillFieldForServer(player);
        adventureData = new AdventureData(player, monster);
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Monster getMonster() {
        return monster;
    }

    public void setMonster(Monster monster) {
        this.monster = monster;
    }

    public GameLogic getGameLogic() {
        return gameLogic;
    }

    public void setGameLogic(GameLogic gameLogic) {
        this.gameLogic = gameLogic;
    }

    public AdventureData getAdventureData() {
        return adventureData;
    }

    public Lock getLock() {
        return lock;
    }
}
