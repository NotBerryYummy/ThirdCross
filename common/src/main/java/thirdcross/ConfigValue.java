package thirdcross;

@FunctionalInterface
public interface ConfigValue<T> {
    T get();
}
