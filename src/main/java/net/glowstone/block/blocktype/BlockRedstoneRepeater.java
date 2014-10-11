package net.glowstone.block.blocktype;

import net.glowstone.RSManager;
import net.glowstone.block.GlowBlock;
import net.glowstone.block.GlowBlockState;
import net.glowstone.entity.GlowPlayer;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Diode;
import org.bukkit.material.Directional;
import org.bukkit.material.MaterialData;
import org.bukkit.util.Vector;

public class BlockRedstoneRepeater extends BlockType {

    private final Boolean isRepeaterOn;

    public BlockRedstoneRepeater(Boolean isOn) {
        setDrops(new ItemStack(Material.DIODE));
        isRepeaterOn = isOn;
    }

    @Override
    public boolean canBlockEmitPower(GlowBlock block, BlockFace face, boolean isDirect) {
        //Redstone repeater always emits direct
        return isRepeaterOn;
    }

    @Override
    public boolean isRedSource(GlowBlock block) {
        return true;
    }

    @Override
    public Integer getBlockPower(GlowBlock block) {
        return (isRepeaterOn ? 15 : 0);
    }

    @Override
    public void traceBlockPowerInit(GlowBlock block, RSManager rsManager) {
    }

    @Override
    public void traceBlockPowerStart(GlowBlock block, RSManager rsManager) {
        if (!isRepeaterOn) return;
        final MaterialData data = block.getState().getData();
        if (data instanceof Diode) {
            final Diode d = (Diode) data;
            BlockFace diodeDir = d.getFacing();
            rsManager.traceFromBlock(block, diodeDir, 15, true);
        }
    }

    @Override
    public void traceBlockPower(GlowBlock block, RSManager rsManager, Material srcMat, BlockFace flowDir, int inPower, boolean isDirect) {
        final MaterialData data = block.getState().getData();
        if (data instanceof Diode) {
            final Diode d = (Diode) data;
            BlockFace diodeDir = d.getFacing();
            if (flowDir == diodeDir) {
                if (!isRepeaterOn && !isDisabled(block)) {
                    if (rsManager.getNewBlockPower(block) != 15) {
                        rsManager.setBlockPowerDelayed(block, 15, d.getDelay()*2-1); // Minus 1 because tracing starts not until the next tick
                    }
                    rsManager.setBlockPower(block, -1); //Prevent traceBlockPowerEnd from starting the turn-off timer
                } else if(isRepeaterOn) {
                    rsManager.removeBlockPowerDelay(block);
                    rsManager.setBlockPower(block, 15);
                }
            }
        }
    }

    @Override
    public void traceBlockPowerEnd(GlowBlock block, RSManager rsManager, int power) {
        MaterialData data = block.getState().getData();
        if (!(data instanceof Diode)) {
            return;
        }
        Diode d = (Diode) data;
        rsManager.addSource(block);
        boolean isDisabled = isDisabled(block);
        if (power == 15 && !isRepeaterOn && !isDisabled) {
            block.setTypeIdAndData(Material.DIODE_BLOCK_ON.getId(), block.getData(), false);
        } else if (power == 0 && isRepeaterOn) {
            if (isDisabled) {
                rsManager.setBlockPower(block, 0);
                return;
            }
            if (rsManager.getNewBlockPower(block) != 1) {
                rsManager.setBlockPowerDelayed(block, 1, d.getDelay()*2-1); // Minus 1 because tracing starts not until the next tick
            }
        } else if (power == 1) { //Value 1 is used above to detect if it was turned off after a delay
            if(isDisabled) {
                rsManager.setBlockPower(block, 0);
                return;
            }
            block.setTypeIdAndData(Material.DIODE_BLOCK_OFF.getId(), block.getData(), false);
        }
    }

    private boolean isDisabled(GlowBlock block) {
        MaterialData data = block.getState().getData();
        if (!(data instanceof Diode)) {
            return false;
        }
        Diode d = (Diode) data;
        BlockFace dir = d.getFacing();
        BlockFace[] faces = new BlockFace[] {BlockFace.NORTH, BlockFace.WEST, BlockFace.SOUTH, BlockFace.EAST};
        for (BlockFace f : faces) {
            //Repeaters can only be disabled from the side
            if (f == dir || f.getOppositeFace() == dir) continue;
            GlowBlock relativeBlock = block.getRelative(f);
            MaterialData relative = relativeBlock.getState().getData();
            //Repeaters can be disabled by Comparators or other Repeaters
            if (relative instanceof Diode) { //TODO: Also by comparators
                Directional relativeDir = (Directional) relative;
                if (relativeBlock.isBlockPowered() && f.getOppositeFace() == relativeDir.getFacing()) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean blockInteract(GlowPlayer player, GlowBlock block, BlockFace face, Vector clickedLoc) {
        final GlowBlockState state = block.getState();
        final MaterialData data = state.getData();
        if (data instanceof Diode) {
            final Diode d = (Diode) data;
            if (d.getDelay() > 3) {
                d.setDelay(0);
            } else {
                d.setDelay(d.getDelay()+1);
            }
            state.setData(d);
            state.update();
            return true;
        } else {
            warnMaterialData(Diode.class, data);
            return false;
        }
    }

    @Override
    public boolean canPlaceAt(GlowBlock block, BlockFace against) {
        GlowBlock floor = block.getRelative(BlockFace.DOWN);
        if (floor != null) {
            Material mat = floor.getType();
            if (mat.isOccluding()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void placeBlock(GlowPlayer player, GlowBlockState state, BlockFace face, ItemStack holding, Vector clickedLoc) {
        super.placeBlock(player, state, face, holding, clickedLoc);

        final MaterialData data = state.getData();
        if (data instanceof Diode) {
            final Diode d = (Diode) data;
            d.setFacingDirection(getOppositeBlockFace(player.getLocation(), false).getOppositeFace());
            state.setData(d);
        } else {
            warnMaterialData(Diode.class, data);
        }
    }
}
