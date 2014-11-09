package net.glowstone.util;

import org.bukkit.Material;
import org.bukkit.block.Block;

import java.util.Set;

public class BlockMaterialValidator implements Validator<Block> {
    private final Set<Material> validMaterials;
    private final Block initialBlock;

    public BlockMaterialValidator(Set<Material> validMaterials) {
        this(validMaterials, null);
    }

    public BlockMaterialValidator(Set<Material> validMaterials, Block initialBlock) {
        this.validMaterials = validMaterials;
        this.initialBlock = initialBlock;
    }

    @Override
    public boolean isValid(Block block) {
        if (block.equals(initialBlock)) {
            return true;
        }

        return validMaterials.contains(block.getType());
    }
}
