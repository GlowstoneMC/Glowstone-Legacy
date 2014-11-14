package net.glowstone.generator;

import java.util.Random;

import net.glowstone.constants.GlowStructure;
import net.glowstone.generator.structures.*;
import net.glowstone.util.BlockStateDelegate;

import org.bukkit.Location;

public class StructureGenerator {

    private final BlockStateDelegate delegate;
    private boolean forceUpdate;

    public StructureGenerator() {
        this(new BlockStateDelegate());
        forceUpdate = true;
    }

    public StructureGenerator(BlockStateDelegate delegate) {
        this.delegate = delegate;
        forceUpdate = false;
    }

    public boolean generate(Random random, Location loc, GlowStructure type) {
        Structure structure;

        switch (type) {
            case DUNGEON:
                structure = new Dungeon(random, loc, delegate);
                break;
            case DESERT_WELL:
                structure = new DesertWell(loc, delegate);
                break;
            case WITCH_HUT:
                structure = new WitchHut(random, loc, delegate);
                break;
            case JUNGLE_TEMPLE:
                structure = new JungleTemple(random, loc, delegate);
                break;
            default:
                return false;
        }

        if (structure.generate()) {
            if (forceUpdate) {
                delegate.updateBlockStates();
            }
            return true;
        }

        return false;
    }
}
