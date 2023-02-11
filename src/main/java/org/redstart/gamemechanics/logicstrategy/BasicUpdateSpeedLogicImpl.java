package org.redstart.gamemechanics.logicstrategy;

import org.redstart.gamemechanics.logicstrategy.interfaces.UpdateSpeedLogic;

public class BasicUpdateSpeedLogicImpl implements UpdateSpeedLogic {

    public int updateCurrentSpeed(long maxSpeed, long timeCreation) {
        long timePassed = System.currentTimeMillis() - timeCreation;
        return (int) (maxSpeed - timePassed);
    }

}
