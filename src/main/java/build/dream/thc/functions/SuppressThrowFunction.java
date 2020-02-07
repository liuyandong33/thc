package build.dream.thc.functions;

@FunctionalInterface
public interface SuppressThrowFunction<T> {
    T call() throws Exception;
}
