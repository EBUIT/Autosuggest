package framework.bean;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public abstract class AbstractBeanConverter<S,D> {

    private Class<S> entitySourceClass;
    private Class<D> entityDestinationClass;

    @SuppressWarnings("unchecked")
    public AbstractBeanConverter(){
        this.entitySourceClass = (Class<S>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        this.entityDestinationClass = (Class<D>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[1];
    }

    public abstract D convertTo(S source);

    public abstract S convertFrom(D destination);

    public Type getSourceType() {
        return entitySourceClass;
    }

    public Type getDestinationType() {
        return entityDestinationClass;
    }
}
