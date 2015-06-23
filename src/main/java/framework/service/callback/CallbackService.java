package framework.service.callback;

import javafx.concurrent.WorkerStateEvent;

public interface CallbackService<T> {


    /**
     * When process running
     */
    default void running(WorkerStateEvent e) {
    }

    /**
     * Always Process after main process
     */
    default void afterAlways(WorkerStateEvent e) {
    }

    /**
     * Process after main process if succeeded
     */
    default void afterSucceeded(WorkerStateEvent e) {
    }

    /**
     * Process after main process if succeeded
     */
    default void afterFailed(WorkerStateEvent e) {
    }


    /**
     * Process after main process if canceled
     */
    default void afterCancelled(WorkerStateEvent e) {
    }
}
