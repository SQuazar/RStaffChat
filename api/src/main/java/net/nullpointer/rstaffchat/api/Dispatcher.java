package net.nullpointer.rstaffchat.api;

/**
 * Redis handler dispatcher. By default: async dispatcher
 */
@FunctionalInterface
public interface Dispatcher {
    /**
     * Dispatch task
     * @param task task dispatcher. You can use Bukkit Scheduler for return handling to main thread
     */
    void dispatch(Runnable task);
}
