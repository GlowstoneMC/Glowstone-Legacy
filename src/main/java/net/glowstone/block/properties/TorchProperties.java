package net.glowstone.block.properties;

import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.material.Lever;
import org.bukkit.material.Torch;

public class TorchProperties {
    private TorchProperties() {}

    public static Byte doPlace(Player player, Block block, BlockFace face) {
        Torch torch = (Torch)block.getType().getNewData(block.getData());
        torch.setFacingDirection(face.getOppositeFace());
        return torch.getData();
    }
}
