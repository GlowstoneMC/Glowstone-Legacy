package net.glowstone.block.itemtype;

import net.glowstone.GlowWorld;
import net.glowstone.block.GlowBlock;
import net.glowstone.entity.GlowPlayer;
import net.glowstone.entity.objects.GlowPainting;
import org.bukkit.Art;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Painting;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Collections;

public class ItemPainting extends ItemType {

    @Override
    public void rightClickBlock(GlowPlayer player, GlowBlock target, BlockFace face, ItemStack holding, Vector clickedLoc) {
        GlowWorld world = player.getWorld();
        ArrayList<Art> validMotives = new ArrayList<Art>();
        for (Art motive : Art.values()) {
            if (GlowPainting.fitsOnWall(world, target.getRelative(face).getLocation(), face, motive)) {
                validMotives.add(motive);
            }
        }
        if (validMotives.size() > 0) {
            Collections.shuffle(validMotives);
            Painting painting = (Painting) world.spawnEntity(target.getRelative(face).getLocation(), EntityType.PAINTING);
            painting.setFacingDirection(face);
            painting.setArt(validMotives.get(0));
        }
    }
}
