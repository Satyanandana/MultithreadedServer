package filecache;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class AccessCounter {
	private static AccessCounter instance = null;
	private static ReentrantLock conslock = new ReentrantLock();
	private HashMap<Path, Integer> map = new HashMap<>();
	private ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

	private AccessCounter() {

	};

	public static AccessCounter getInstance() {
		conslock.lock();
		try {
			if (instance == null)
				instance = new AccessCounter();
		} finally {
			conslock.unlock();
		}
		return instance;
	}

	public void increment(Path path) {
		lock.writeLock().lock();
		try {
			if (map.containsKey(path)) {
				Integer value = map.get(path) + 1;
				map.put(path, value);
			} else {
				map.put(path, 1);
			}
		} finally {
			lock.writeLock().unlock();
		}
	}

	public Integer getCount(Path path) {
		Integer count;
		lock.readLock().lock();
		try {
			if (map.containsKey(path)) {
				count = map.get(path);
			} else {
				count = 0;
			}
		} finally {
			lock.readLock().unlock();
		}
		return count;
	}

	public Path getPathWithLeastCount() {
		Path path = null;
		lock.readLock().lock();
		try {
			int minCount = Integer.MAX_VALUE;
			for (Entry<Path, Integer> entry : map.entrySet()) {

				if (entry.getValue() <= minCount) {
					minCount = entry.getValue();
				}
			}

			for (Entry<Path, Integer> entry : map.entrySet()) {
				if (entry.getValue() == minCount) {
					path = entry.getKey();
					break;
				}

			}

		} finally {
			lock.readLock().unlock();
		}
		return path;
	}

}
