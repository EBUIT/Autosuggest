package framework.rest;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.HashMap;
import java.util.Map;

/**
 * Facade to manage mapping between Entities and Url.<br/>
 * For each Entities the facade register his Url for the web framework.service.<br/>
 * With the method getUrl, the system retrieve the url corresponding to the Entity Class.
 */
public class RestUrlFacade implements ApplicationContextAware {

    private Map<String, String> references = new HashMap<>();
    private Map<Class<?>, String> referencesFromClass = new HashMap<Class<?>, String>();
    private ApplicationContext applicationContext;


    /**
     * @param clazz - Entity Class
     * @return the url for the webService defined for the {@code clazz}
     */
    public String getUrl(Class<?> clazz) {
        return referencesFromClass.get(clazz);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    /**
     * Register Reference mapping.
     *
     * @param references
     * @throws ClassNotFoundException
     */
    public void setReferences(Map<String, String> references) throws ClassNotFoundException {
        this.references = references;

        for (Map.Entry<String, String> repoClassForBeanName : references.entrySet()) {
            referencesFromClass.put(Class.forName(repoClassForBeanName.getKey()), repoClassForBeanName.getValue());
        }
    }

}
