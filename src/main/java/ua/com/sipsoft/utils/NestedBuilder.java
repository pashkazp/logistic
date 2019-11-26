package ua.com.sipsoft.utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * To get the parent builder
 *
 * @return T the instance of the parent builder V - child builder
 */
public interface NestedBuilder<T, V> {

    T getParentBuilder();

    V getThisBuilder();

    void setParentBuilder(T parentBuilder);

    default public T done() {
	Class<?> parentClass = getParentBuilder().getClass();
	try {
	    V thisBuilder = getThisBuilder();
	    String methodName = "with" + thisBuilder.getClass().getSimpleName();
	    Method method = parentClass.getDeclaredMethod(methodName, thisBuilder.getClass());
	    method.invoke(getParentBuilder(), thisBuilder);
	} catch (NoSuchMethodException
		| IllegalAccessException
		| InvocationTargetException e) {
	    e.printStackTrace();
	}
	return getParentBuilder();
    }

    V build();

    default <P extends NestedBuilder<T, V>> P withParentBuilder(T parentBuilder) {
	setParentBuilder(parentBuilder);
	return (P) this;
    }
}
