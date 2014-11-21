package net.glowstone.generator.structures;

import java.util.Random;

import net.glowstone.util.BlockStateDelegate;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.util.Vector;

public class GlowDesertTemple extends GlowTemplePiece {

    private boolean hasPlacedChest0;
    private boolean hasPlacedChest1;
    private boolean hasPlacedChest2;
    private boolean hasPlacedChest3;

    public GlowDesertTemple() {
        super();
    }

    public GlowDesertTemple(Random random, Location location) {
        super(random, location, new Vector(21, 15, 21));
    }

    public void setHasPlacedChest0(boolean placedChest) {
        this.hasPlacedChest0 = placedChest;
    }

    public boolean getHasPlacedChest0() {
        return hasPlacedChest0;
    }

    public void setHasPlacedChest1(boolean placedChest) {
        this.hasPlacedChest1 = placedChest;
    }

    public boolean getHasPlacedChest1() {
        return hasPlacedChest1;
    }

    public void setHasPlacedChest2(boolean placedChest) {
        this.hasPlacedChest2 = placedChest;
    }

    public boolean getHasPlacedChest2() {
        return hasPlacedChest2;
    }

    public void setHasPlacedChest3(boolean placedChest) {
        this.hasPlacedChest3 = placedChest;
    }

    public boolean getHasPlacedChest3() {
        return hasPlacedChest3;
    }

    @Override
    public boolean generate(World world, Random random, BlockStateDelegate delegate) {
        if (!super.generate(world, random, delegate)) {
            return false;
        }
        return true;
    }
}
