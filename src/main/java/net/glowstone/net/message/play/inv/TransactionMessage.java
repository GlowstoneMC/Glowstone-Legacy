package net.glowstone.net.message.play.inv;

import com.flowpowered.networking.Message;

public final class TransactionMessage implements Message {

    private final int id, transaction;
    private final boolean accepted;

    public TransactionMessage(int id, int transaction, boolean accepted) {
        this.id = id;
        this.transaction = transaction;
        this.accepted = accepted;
    }

    public int getId() {
        return id;
    }

    public int getTransaction() {
        return transaction;
    }

    public boolean isAccepted() {
        return accepted;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TransactionMessage that = (TransactionMessage) o;

        if (accepted != that.accepted) return false;
        if (id != that.id) return false;
        if (transaction != that.transaction) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + transaction;
        result = 31 * result + (accepted ? 1 : 0);
        return result;
    }

    @Override
    public String toString() {
        return "TransactionMessage{id=" + id + ",transaction=" + transaction + ",isAccepted=" + accepted + "}";
    }
}
