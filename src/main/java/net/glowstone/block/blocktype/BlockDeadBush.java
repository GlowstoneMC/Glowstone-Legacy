package net.glowstone.block.blocktype;

public class BlockDeadBush extends DefaultBlockType {

    public BlockDeadBush() {
        super(
                new BlockNeedsAttached(),
                new BlockDropless()
        );
    }
}
