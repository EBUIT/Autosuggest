package framework.service.callback;

import javafx.concurrent.WorkerStateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggerCallbackService implements CallbackService {

    private static final Logger LOG = LoggerFactory.getLogger(LoggerCallbackService.class);



    @Override
    public void running(WorkerStateEvent e) {
        LOG.debug("Run workerStateEvent={}", e);
    }


    @Override
    public void afterAlways(WorkerStateEvent e) {

        if (e.getSource() != null) {
            LOG.debug("After value={} exception={} message={}", e.getSource().getValue(), e.getSource().getException(), e.getSource().getMessage());
        } else {
            LOG.debug("After workerStateEvent={}", e);
        }

    }


}
