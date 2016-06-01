package threadpool;

public interface Queue<T> {

	public T get();
	public void put(T t);
	public int size();

}
