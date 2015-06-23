package framework.rest;

import framework.service.callback.CallbackService;
import javafx.concurrent.Service;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;

import java.util.List;

/**
 * Add Callback of type {@link ch.ebu.neos.client.util.service.callback.CallbackService} on {@link Service}
 * <p/>
 * Created by laurent on 02.02.2015.
 */
@org.springframework.stereotype.Service
public class RecordCallbackOnService {


    public void addCallback(Service service, CallbackService callback) {
        service.addEventFilter(WorkerStateEvent.WORKER_STATE_RUNNING, new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                callback.running(event);
            }
        });
        service.addEventFilter(WorkerStateEvent.WORKER_STATE_SUCCEEDED, new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                try {
                    callback.afterSucceeded(event);
                } finally {
                    callback.afterAlways(event);
                }

            }
        });
        service.addEventFilter(WorkerStateEvent.WORKER_STATE_FAILED, new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                try {
                    callback.afterFailed(event);
                } finally {
                    callback.afterAlways(event);
                }

            }
        });
        service.addEventFilter(WorkerStateEvent.WORKER_STATE_CANCELLED, new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                try {
                    callback.afterCancelled(event);
                } finally {
                    callback.afterAlways(event);
                }

            }
        });
    }

    public void addCallback(Service service, List<CallbackService> callbacks) {
        for (CallbackService callback : callbacks) {
            this.addCallback(service, callback);
        }
    }

}
