package net.glowstone.block.blocktype;

public class BlockWoodenTrapDoor extends DefaultBlockType {

    public BlockWoodenTrapDoor() {
        super(
                new BlockOpenable(),
                new BlockTrapDoor(),
                new BlockDropWithoutData()
        );
    }
}
