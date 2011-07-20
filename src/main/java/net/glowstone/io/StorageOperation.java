package net.glowstone.io;

/**
 * @author zml2008
 */
public abstract class StorageOperation implements Runnable {
    public abstract boolean isParallel();

    public abstract String getGroup();

    public abstract String getOperation();

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof StorageOperation))
            return false;
        StorageOperation op = (StorageOperation) other;
        return getGroup().equals(op.getGroup()) && getOperation().equals(op.getOperation());
    }
}
