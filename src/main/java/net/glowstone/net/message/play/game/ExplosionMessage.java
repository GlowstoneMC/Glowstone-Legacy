package net.glowstone.net.message.play.game;

import com.flowpowered.networking.Message;

import java.util.Collection;

public class ExplosionMessage implements Message {
    private float x;
    private float y;
    private float z;
    private float radius;
    private Collection<Record> records;
    private float playerMotionX;
    private float playerMotionY;
    private float playerMotionZ;

    public ExplosionMessage(float x, float y, float z, float radius, float playerMotionX, float playerMotionY, float playerMotionZ, Collection<Record> records) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.radius = radius;
        this.playerMotionX = playerMotionX;
        this.playerMotionY = playerMotionY;
        this.playerMotionZ = playerMotionZ;
        this.records = records;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getZ() {
        return z;
    }

    public float getRadius() {
        return radius;
    }

    public Collection<Record> getRecords() {
        return records;
    }

    public float getPlayerMotionX() {
        return playerMotionX;
    }

    public float getPlayerMotionY() {
        return playerMotionY;
    }

    public float getPlayerMotionZ() {
        return playerMotionZ;
    }

    @Override
    public String toString() {
        return "ExplosionMessage{x=" + x + ",y=" + y + ",z=" + z +
                ",radius=" + radius +
                ",motX=" + playerMotionX + ",motY=" + playerMotionY + ",motZ=" + playerMotionZ +
                ",recordCount=" + records.size() + "}";
    }

    public static class Record {
        private byte x, y, z;

        public Record(byte x, byte y, byte z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        public byte getZ() {
            return z;
        }

        public byte getY() {
            return y;
        }

        public byte getX() {
            return x;
        }
    }
}
