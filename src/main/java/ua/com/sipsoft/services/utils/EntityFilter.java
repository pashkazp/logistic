package ua.com.sipsoft.services.utils;

@FunctionalInterface
public interface EntityFilter<T> {
    boolean isPass(T entity);
}
