package net.glowstone.util;

import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Queue;
import java.util.Set;

public class TaxicabBlockIterator implements Iterator<Block> {
    private static final BlockFace[] VALID_FACES = new BlockFace[] {
        BlockFace.DOWN, BlockFace.UP, BlockFace.NORTH, BlockFace.SOUTH, BlockFace.WEST, BlockFace.EAST
    };

    private final Queue<Block> pendingAnalysis = new LinkedList<>();
    private final Queue<Block> nextValidBlocks = new LinkedList<>();
    private final Set<Block> usedBlocks = new HashSet<>();
    private int currentDistance = 0;
    private int validBlockCount = 0;

    private int maxDistance = Integer.MAX_VALUE;
    private int maxBlocks = Integer.MAX_VALUE;
    private Validator<Block> validator;

    public TaxicabBlockIterator(Block origin) {
        pendingAnalysis.add(origin);
        usedBlocks.add(origin);
    }

    public void setMaxDistance(int maxDistance) {
        this.maxDistance = maxDistance;
    }

    public void setMaxBlocks(int maxBlocks) {
        this.maxBlocks = maxBlocks;
    }

    public void setValidator(Validator<Block> validator) {
        this.validator = validator;
    }

    @Override
    public boolean hasNext() {
        if (!nextValidBlocks.isEmpty()) {
            return true;
        }

        // If we've surpassed the distance limit, return false.
        if (currentDistance > maxDistance) {
            return false;
        }

        // We'll cache the current amount of pending blocks since we'll be appending stuff for the next iteration.
        int pendingCount = pendingAnalysis.size();
        if (pendingCount == 0) {
            return false;
        }

        // Keep going till we've processed all the pending blocks.
        while (pendingCount > 0) {
            Block block = pendingAnalysis.remove();
            pendingCount--;

            // If we've reached the valid block limit, just pop this value off the pending block list.
            if (validBlockCount >= maxBlocks) {
                continue;
            }

            // Check if the validator likes this block.
            if (validator != null && !validator.isValid(block)) {
                continue;
            }

            nextValidBlocks.add(block);
            validBlockCount++;

            // Only add blocks to pending analysis list if we really need to.
            if (currentDistance < maxDistance && validBlockCount < maxBlocks) {
                for (int i = 0; i < VALID_FACES.length; i++) {
                    Block near = block.getRelative(VALID_FACES[i]);

                    // Only analyse file if we haven't checked it yet.
                    if (!usedBlocks.contains(near)) {
                        pendingAnalysis.add(near);
                        usedBlocks.add(near);
                    }
                }
            }
        }

        // We've finished checking all blocks in a certain distance, so we'll increase the counter now.
        currentDistance++;

        return !nextValidBlocks.isEmpty();
    }

    @Override
    public Block next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }

        return nextValidBlocks.remove();
    }
}
