package net.glowstone.entity.passive;

import com.flowpowered.networking.Message;
import net.glowstone.entity.GlowGolem;
import net.glowstone.entity.meta.MetadataIndex;
import net.glowstone.entity.meta.MetadataMap;
import net.glowstone.net.message.play.entity.EntityMetadataMessage;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.IronGolem;

import java.util.List;

public class GlowIronGolem extends GlowGolem implements IronGolem {

    private boolean isPlayerCreated = false;

    public GlowIronGolem(Location location) {
        super(location, EntityType.IRON_GOLEM);
    }

    @Override
    public boolean isPlayerCreated() {
        return this.isPlayerCreated;
    }

    @Override
    public void setPlayerCreated(boolean playerCreated) {
        this.isPlayerCreated = playerCreated;
    }

    @Override
    public List<Message> createSpawnMessage() {
        List<Message> messages = super.createSpawnMessage();
        MetadataMap map = new MetadataMap(GlowIronGolem.class);
        map.set(MetadataIndex.GOLEM_PLAYER_BUILT, (byte) (isPlayerCreated ? 1 : 0));
        messages.add(new EntityMetadataMessage(id, map.getEntryList()));
        return messages;
    }
}
