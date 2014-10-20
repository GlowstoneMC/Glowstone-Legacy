package net.glowstone.net.handler.play.player;

import com.flowpowered.networking.MessageHandler;
import com.google.common.collect.ImmutableList;
import net.glowstone.EventFactory;
import net.glowstone.GlowServer;
import net.glowstone.constants.ItemDamage;
import net.glowstone.entity.GlowEntity;
import net.glowstone.entity.GlowLivingEntity;
import net.glowstone.entity.GlowPlayer;
import net.glowstone.net.GlowSession;
import net.glowstone.net.message.play.player.InteractEntityMessage;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public final class InteractEntityHandler implements MessageHandler<GlowSession, InteractEntityMessage> {

    private final List<GameMode> invulnerableGamemodes = ImmutableList.of(GameMode.CREATIVE, GameMode.SPECTATOR);

    @Override
    public void handle(GlowSession session, InteractEntityMessage message) {
        GlowPlayer player = session.getPlayer();

        // You can't do anything when you're dead
        if (player.isDead()) {
            GlowServer.logger.info("Player " + player + " tried to interact with an entity while dead");
            return;
        }

        GlowEntity possibleTarget = player.getWorld().getEntityById(message.getId());
        GlowLivingEntity target = possibleTarget instanceof GlowLivingEntity ? (GlowLivingEntity) possibleTarget : null;

        if (message.getAction() == InteractEntityMessage.Action.ATTACK.ordinal()) {
            if (target == null) {
                GlowServer.logger.info("Player " + player + " tried to attack an entity that does not exist");
            } else {
                ItemStack hand = player.getItemInHand();
                Material type = hand == null ? Material.AIR : hand.getType();

                boolean critical = false; // TODO: Actual critical hit check
                float damage = critical ? ItemDamage.getCriticalDamageFor(type) : ItemDamage.getDamageFor(type);

                // TODO: Calculate damage modifiers
                // ... also make sure the correct event constructor is used

                if (!target.isDead() && (!(target instanceof Player) || !invulnerableGamemodes.contains(((Player) target).getGameMode()))) {
                    EntityDamageByEntityEvent event = new EntityDamageByEntityEvent(player, target, EntityDamageEvent.DamageCause.ENTITY_ATTACK, damage);
                    EventFactory.callEvent(event);

                    if (!event.isCancelled()) {
                        target.damage(damage, player, EntityDamageEvent.DamageCause.ENTITY_ATTACK);

                        // Apply durability loss (if applicable)
                        short durabilityLoss = ItemDamage.getDurabilityLoss(type);
                        if (durabilityLoss > 0) {
                            // Yes, this actually subtracts
                            hand.setDurability((short) (hand.getDurability() + durabilityLoss));
                        }
                        player.updateInventory();
                    }
                }
            }
        } else if (message.getAction() == InteractEntityMessage.Action.ATTACK_AT.ordinal()) {
            // TODO: Interaction with entity (right click)
        } else if (message.getAction() == InteractEntityMessage.Action.INTERACT.ordinal()) {
            // TODO: Handle something (unknown cause for this action)
        } else {
            GlowServer.logger.info("Player " + player + " sent unknown interact action: " + message.getAction());
        }
    }
}
