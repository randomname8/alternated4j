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

    private Supplier<T> thunk;
    private T evaluatedThunk;

    private LazyImpl(Supplier<T> thunk) {
      this.thunk = thunk;
    }

    @Override
    public T value() {
      if (thunk != null) {
        evaluatedThunk = thunk.get();
        thunk = null;
      }
      return evaluatedThunk;
    }

    @Override
    public String toString() {
      return thunk == null ? evaluatedThunk.toString() : "<thunk>";
    }

  }

  static class ThreadSafeLazyImpl<T> implements Lazy<T> {

    private volatile Supplier<T> thunk;
    private T evaluatedThunk;

    private ThreadSafeLazyImpl(Supplier<T> thunk) {
      this.thunk = thunk;
    }

    @Override
    public T value() {
      if (thunk != null) {
        synchronized (this) {
          if (thunk != null) {
            evaluatedThunk = thunk.get();
            thunk = null;
          }
        }
      }
      return evaluatedThunk;
    }

    @Override
    public String toString() {
      return thunk == null ? evaluatedThunk.toString() : "<thunk>";
    }
  }
}
