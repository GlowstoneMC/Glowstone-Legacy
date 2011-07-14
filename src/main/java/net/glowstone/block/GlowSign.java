package net.glowstone.block;

import net.glowstone.entity.GlowPlayer;
import net.glowstone.msg.UpdateSignMessage;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;

/**
 * Represents either a SignPost or a WallSign
 */
public class GlowSign extends GlowBlockState implements Sign {

    private String[] lines;

    public GlowSign(String[] lines, final GlowBlock block) {
        super(block);
        if (block.getType() != Material.SIGN_POST && block.getType() != Material.WALL_SIGN)
            throw new IllegalArgumentException();
        this.lines = lines;
    }

    /**
     * Gets all the lines of text currently on this sign.
     *
     * @return Array of Strings containing each line of text
     */
    public String[] getLines() {
        return lines.clone();
    }

    /**
     * Gets the line of text at the specified index.
     *
     * For example, getLine(0) will return the first line of text.
     *
     * @param index Line number to get the text from, starting at 0
     * @throws IndexOutOfBoundsException Thrown when the line does not exist
     * @return Text on the given line
     */
    public String getLine(int index) throws IndexOutOfBoundsException {
        if (index > lines.length)
            throw new IndexOutOfBoundsException();
        return lines[index];
    }

    /**
     * Sets the line of text at the specified index.
     *
     * For example, setLine(0, "Line One") will set the first line of text to
     * "Line One".
     *
     * @param index Line number to set the text at, starting from 0
     * @param line New text to set at the specified index
     * @throws IndexOutOfBoundsException
     */
    public void setLine(int index, String line) throws IndexOutOfBoundsException {
        if (line.length() > 16)
            throw new IndexOutOfBoundsException();
        lines[index] = line;
    }

    public void setLines(String[] lines) {
        this.lines = lines;
    }

    @Override
    public boolean update(boolean force) {
        Block block = getBlock();

        if (block.getTypeId() != type) {
            if (force) {
                block.setTypeId(type);
            } else {
                return false;
            }
        }
        for (Player update : block.getWorld().getPlayers()) {
            GlowPlayer glow = (GlowPlayer)update;
            glow.getSession().send(new UpdateSignMessage(getX(), getY(), getZ(), getLines()));
        }

        return true;
    }

}
