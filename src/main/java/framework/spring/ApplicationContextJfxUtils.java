package framework.spring;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Utils class to get the current context framework.spring in controller
 *
 * @author sopra_om
 */
public class ApplicationContextJfxUtils {
    /**
     * Static ch.ebu.neos.client.framework.ui.ch.ebu.neos.client.core context
     */
    private static ClassPathXmlApplicationContext applicationContext;
    // or
    //private static AnnotationConfigApplicationContext applicationContext;


    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    /**
     * Init the default context
     *
     * @param configurationXml
     */
    public static void initcontext(String configurationXml) {
        applicationContext = new ClassPathXmlApplicationContext(configurationXml);

        // or in config less mode
        
        /*
            applicationContext = new AnnotationConfigApplicationContext();
            applicationContext.scan("ch.ebu.eos.client.ch.ebu.neos.client.framework.ui.ch.ebu.neos.client.core");
            applicationContext.refresh();
            applicationContext.registerShutdownHook();
            applicationContext.start();
        */
    }

    /**
     * Autowired the controller past in domain
     *
     * @param controller
     */
    public static void autowireBean(Object controller) {
        applicationContext.getAutowireCapableBeanFactory().autowireBean(controller);
    }

    public static void stop() {
        applicationContext.stop();
        applicationContext.destroy();
    }
}
