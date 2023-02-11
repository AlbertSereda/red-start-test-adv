package org.redstart.gamemechanics.spells.interfaces;

public interface WithTimeSpell extends Spell {

    long getTimeToEnd();

    void resetSpell();

    void update();

    void deactivate();

}
