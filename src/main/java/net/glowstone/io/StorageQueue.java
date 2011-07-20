package net.glowstone.io;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class StorageQueue extends Thread {
        private List<StorageOperation> pending =
            Collections.synchronizedList(new ArrayList<StorageOperation>());
    private final List<ParalellTaskThread> active =
            Collections.synchronizedList(new ArrayList<ParalellTaskThread>());

    @Override
    public void run() {
        while (!this.isInterrupted()) {
            if (pending.size() > 0) {
                StorageOperation op = pending.get(0);
                if (op != null) {
                    op.run();
                    pending.remove(op);
                }
            }
        }
    }

    public void queue(StorageOperation op) {
        if (isInterrupted()) {
                throw new IllegalStateException(
                        "Cannot queue tasks while thread is not running");
        }
        if (op.isParallel()) {
            ParalellTaskThread thread = new ParalellTaskThread(op);
            if (!active.contains(thread)) {
               thread.start();
            }
        } else {
            if (!pending.contains(op)) {
                pending.add(op);
            }
        }
    }


    public void reset() {
        end();
        start();
    }

    public void end() {
        interrupt();
        pending.clear();
        for (ParalellTaskThread thread : active) {
            thread.interrupt();
        }
    }

    class ParalellTaskThread extends Thread {
        private final StorageOperation op;
        public ParalellTaskThread(StorageOperation op) {
            this.op = op;
        }

        @Override
        public void run() {
            active.add(this);
            try {
                if (!interrupted()) op.run();
            } finally {
                active.remove(this);
            }
        }

        @Override
        public boolean equals(Object other) {
            if (!(other instanceof ParalellTaskThread)) {
                return false;
            }
            return op.equals(((ParalellTaskThread) other).op);
        }
    }
}
