package net.glowstone.io.entity;

import net.glowstone.entity.objects.GlowPainting;
import net.glowstone.util.nbt.CompoundTag;
import org.bukkit.Art;
import org.bukkit.Location;
import org.bukkit.block.BlockFace;

class PaintingStore extends EntityStore<GlowPainting> {

    public PaintingStore() {
        super(GlowPainting.class, "Painting");
    }

    @Override
    public GlowPainting createEntity(Location location, CompoundTag compound) {
        return new GlowPainting(location, Art.KEBAB);
    }

    @Override
    public void load(GlowPainting entity, CompoundTag tag) {
        super.load(entity, tag);

        if (tag.isString("Motive")) {
            entity.setArt(Art.getByName(tag.getString("Motive")));
        }
        if (tag.isInt("TileX") && tag.isInt("TileY") && tag.isInt("TileZ")) {
            entity.setTilePosition(tag.getInt("TileX"), tag.getInt("TileY"), tag.getInt("TileZ"));
        }
        if (tag.isByte("Facing")) {
            switch (tag.getByte("Facing")) {
                case 0:
                    entity.setFacingDirection(BlockFace.SOUTH);
                    break;
                case 1:
                    entity.setFacingDirection(BlockFace.WEST);
                    break;
                case 2:
                    entity.setFacingDirection(BlockFace.NORTH);
                    break;
                case 3:
                    entity.setFacingDirection(BlockFace.EAST);
                    break;
                default:
                    entity.setFacingDirection(BlockFace.SOUTH);
            }
        } else if (tag.isByte("Direction")) {
            switch (tag.getByte("Direction")) {
                case 0:
                    entity.setFacingDirection(BlockFace.SOUTH);
                    break;
                case 1:
                    entity.setFacingDirection(BlockFace.WEST);
                    break;
                case 2:
                    entity.setFacingDirection(BlockFace.NORTH);
                    break;
                case 3:
                    entity.setFacingDirection(BlockFace.EAST);
                    break;
                default:
                    entity.setFacingDirection(BlockFace.SOUTH);
            }
        } else if (tag.isByte("Dir")) {
            switch (tag.getByte("Dir")) {
                case 0:
                    entity.setFacingDirection(BlockFace.NORTH);
                    break;
                case 1:
                    entity.setFacingDirection(BlockFace.WEST);
                    break;
                case 2:
                    entity.setFacingDirection(BlockFace.SOUTH);
                    break;
                case 3:
                    entity.setFacingDirection(BlockFace.EAST);
                    break;
                default:
                    entity.setFacingDirection(BlockFace.SOUTH);
            }
        }
    }

    @Override
    public void save(GlowPainting entity, CompoundTag tag) {
        super.save(entity, tag);

        tag.putString("Motive", GlowPainting.artToString(entity.getArt()));
        tag.putInt("TileX", entity.getTileX());
        tag.putInt("TileY", entity.getTileY());
        tag.putInt("TileZ", entity.getTileZ());
        switch (entity.getFacing()) {
            case SOUTH:
                tag.putByte("Facing", 0);
                break;
            case WEST:
                tag.putByte("Facing", 1);
                break;
            case NORTH:
                tag.putByte("Facing", 2);
                break;
            case EAST:
                tag.putByte("Facing", 3);
                break;
        }
    }


}
