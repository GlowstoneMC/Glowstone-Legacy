package net.glowstone.block.itemtype;

import net.glowstone.EventFactory;
import net.glowstone.block.GlowBlock;
import net.glowstone.entity.GlowLivingEntity;
import net.glowstone.entity.GlowPlayer;
import org.bukkit.GameMode;
import org.bukkit.block.BlockFace;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.player.PlayerItemBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public abstract class ItemTool extends ItemType {

    private final int maxDurability;

    public ItemTool(int maxDurability) {
        setMaxStackSize(1);
        this.maxDurability = maxDurability;
    }

    @Override
    public void rightClickBlock(GlowPlayer player, GlowBlock target, BlockFace face, ItemStack holding, Vector clickedLoc) {
        if (onToolRightClick(player, holding, target, face, clickedLoc)) {
            damageTool(player, holding, calculateDamageEfficiency(holding, calculateRightClickDamage(target)));
        }
        
    }
    
    /**
     * Calculate the damage with Unbreaking enchantment
     * @param holding The ItemStack the player was holding
     * @param basic The default damage
     * @return The damage
     */
    private int calculateDamageEfficiency(ItemStack holding, int basic) {
        int durabilityLevel = holding.getItemMeta().getEnchantLevel(Enchantment.DURABILITY);
        if (durabilityLevel == 0)
            return basic;
        return ((int) (Math.random() * (durabilityLevel + 1)) == 0 ? basic : 0);
    }
    
    protected void damageTool(GlowPlayer player, ItemStack holding, int damage) {
        if (holding == null || player.getGameMode() == GameMode.CREATIVE || damage == 0) {
            return;
        }

        holding.setDurability((short) (holding.getDurability() + damage));
        if (holding.getDurability() > maxDurability) {
            EventFactory.callEvent(new PlayerItemBreakEvent(player, holding));
            player.getInventory().remove(holding);
        }
    }

    /**
     * Called when a player used (right clicked with) the tool.
     * @param player The player using the tool
     * @param tool The tool
     * @param target The block right clicked with the tool
     * @param face The clicked BlockFace
     * @param clickedLoc The click location on the block
     * @return true if the tool's durability should be decreased, false otherwise
     */
    protected boolean onToolRightClick(GlowPlayer player, ItemStack tool, GlowBlock target, BlockFace face, Vector clickedLoc) {
        // to be overridden in subclasses
        return false;
    }

    @Override
    public void onBreakBlock(GlowPlayer player, GlowBlock target, ItemStack holding) {
        damageTool(player, holding, calculateDamageEfficiency(holding, calculateRightClickDamage(target)));
    }
    
    @Override
    public void onAttackEntity(GlowPlayer player, GlowLivingEntity target, ItemStack holding) {
        damageTool(player, holding, calculateDamageEfficiency(holding, calculateAttackDamage(target)));
    }

    /**
     * Calculate damage to break a block.
     * @param target The GlowBlock target
     * @return The damage
     */
    public short calculateBreakDamage(GlowBlock target) {
        return 0;
    }

    /**
     * Calculate damage to right click a block.
     * @param target The GlowBlock target
     * @return The damage
     */
    protected short calculateRightClickDamage(GlowBlock target) {
        return 1;
    }
    
    /**
     * Calculate damage to attack entity.
     * @param target The GlowLivingEntity target
     * @return The damage
     */
    protected short calculateAttackDamage(GlowLivingEntity target) {
        return 0;
    }
}
