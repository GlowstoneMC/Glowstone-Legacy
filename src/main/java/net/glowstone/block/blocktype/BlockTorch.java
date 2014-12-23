package net.glowstone.block.blocktype;

public class BlockTorch extends DefaultBlockType {

    public BlockTorch() {
        super(
                new BlockAttachable(),
                new BlockDropWithoutData()
        );
    }
}
