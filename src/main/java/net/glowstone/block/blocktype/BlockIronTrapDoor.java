package net.glowstone.block.blocktype;

import net.glowstone.inventory.ToolType;

public class BlockIronTrapDoor extends DefaultBlockType {

    public BlockIronTrapDoor() {
        super(
                new BlockTrapDoor(),
                new BlockDropWithoutData(ToolType.PICKAXE)
        );
    }
}
