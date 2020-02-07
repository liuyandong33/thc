package build.dream.thc.functions;

@FunctionalInterface
public interface SuppressThrowNoReturnFunction {
    void call() throws Exception;
}
