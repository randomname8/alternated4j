package discord4j.common;

import java.util.function.Supplier;

/**
 * Definition of a lazily evaluated thunk.
 *
 * Two implementations are provided, one is thread safe, and one is not.
 */
public interface Lazy<T> {

  public T value();

  public static <T> Lazy<T> apply(Supplier<T> thunk) {
    return new LazyImpl<>(thunk);
  }

  public static <T> Lazy<T> threadSafe(Supplier<T> thunk) {
    return new LazyImpl<>(thunk);
  }

  public static <T> Lazy<T> evaluated(T t) {
    return new Lazy<T>() {
      @Override
      public T value() {
        return t;
      }

      @Override
      public String toString() {
        return String.valueOf(t);
      }
    };
  }

  static class LazyImpl<T> implements Lazy<T> {

    private final Supplier<T> thunk;
    private boolean initialized;
    private T evaluatedThunk;

    private LazyImpl(Supplier<T> thunk) {
      this.thunk = thunk;
    }

    @Override
    public T value() {
      if (!initialized) {
        evaluatedThunk = thunk.get();
        initialized = true;
      }
      return evaluatedThunk;
    }

    @Override
    public String toString() {
      return initialized ? evaluatedThunk.toString() : "<thunk>";
    }

  }

  static class ThreadSafeLazyImpl<T> implements Lazy<T> {

    private final Supplier<T> thunk;
    private volatile boolean initialized;
    private T evaluatedThunk;

    private ThreadSafeLazyImpl(Supplier<T> thunk) {
      this.thunk = thunk;
    }

    @Override
    public T value() {
      if (!initialized) {
        synchronized (this) {
          if (!initialized) {
            evaluatedThunk = thunk.get();
            initialized = true;
          }
        }
      }
      return evaluatedThunk;
    }

    @Override
    public String toString() {
      return initialized ? evaluatedThunk.toString() : "<thunk>";
    }
  }
}
