package org.redstart.gamemechanics.block;

import java.util.HashMap;
import java.util.Map;

public class ColorBlockInitializer {
    private final Map<Integer, ? super ColorBlock> colorBlocks;

    public ColorBlockInitializer() {
        colorBlocks = new HashMap<>();
        colorBlocks.put(0, new YellowColorBlock());
        colorBlocks.put(1, new RedColorBlock());
        colorBlocks.put(2, new GreenColorBlock());
        colorBlocks.put(3, new BlueColorBlock());
    }

    public Map<Integer, ? super ColorBlock> getColorBlocks() {
        return colorBlocks;
    }
}
