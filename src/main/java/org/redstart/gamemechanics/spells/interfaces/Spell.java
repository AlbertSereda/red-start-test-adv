package org.redstart.gamemechanics.spells.interfaces;

import org.redstart.jsonclasses.Player;

//TODO заменить doExecute на активировать
//длительного действия, добавить update который будет вызываться постоянно. Так же добавить активировать,
//что бы он добавлял себя в активные заклинания gameRoom
public interface Spell {
    void activate();

    int getCost();

    int getDamage();

    default void decrementMana(Player player, int cost) {
        player.setMana(player.getMana() - cost);
    }
}
