package simpletextoverlay.wrappers;

public class Wrapper<T> {

    protected final T wrapped;

    public Wrapper(T object) {
        this.wrapped = object;
    }

    public T get() {
        return wrapped;
    }

}
