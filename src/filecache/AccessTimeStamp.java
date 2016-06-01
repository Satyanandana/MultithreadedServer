package filecache;

import java.nio.file.Path;
import java.util.Date;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class AccessTimeStamp {
	private static AccessTimeStamp instance = null;
	private static ReentrantLock conslock = new ReentrantLock();
	private HashMap<Path, Date> map = new HashMap<>();
	private ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

	private AccessTimeStamp() {
	};

	public static AccessTimeStamp getInstance() {
		conslock.lock();
		try {
			if (instance == null)
				instance = new AccessTimeStamp();
		} finally {
			conslock.unlock();
		}
		return instance;
	}

	public void updateTimeStamp(Path path) {
		lock.writeLock().lock();
		try {
			map.put(path, new Date());
		} finally {
			lock.writeLock().unlock();
		}
	}

	public Date getTimeStamp(Path path) {
		Date latest = new Date();
		lock.readLock().lock();
		try {
			if (map.containsKey(path)) {
				latest = map.get(path);
			}
		} finally {
			lock.readLock().unlock();
		}
		return latest;
	}

	public Path getPathWithLeastTimeStamp() {
		Path path = null;
		lock.readLock().lock();
		try {
			Date minDate = new Date();
			for (Entry<Path, Date> entry : map.entrySet()) {

				if (minDate.after(entry.getValue())) {
					minDate = entry.getValue();
				}
			}

			for (Entry<Path, Date> entry : map.entrySet()) {
				if (entry.getValue().equals(minDate)) {
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
