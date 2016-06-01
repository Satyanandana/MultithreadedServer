package filecache;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.concurrent.locks.ReentrantLock;

public class FileCacheLFU extends FileCache {
	private static FileCacheLFU instance = null;
	private static ReentrantLock lock = new ReentrantLock();

	private FileCacheLFU() {
	};

	public static FileCacheLFU getInstance() {
		lock.lock();
		try {
			if (instance == null)
				instance = new FileCacheLFU();
		} finally {
			lock.unlock();
		}
		return instance;
	}

	@Override
	protected void replace(Path path) throws IOException {
		lock.lock();
		try {
			AccessCounter accessCounter = AccessCounter.getInstance();
			cache.remove(accessCounter.getPathWithLeastCount());
			byte[] content = readFile(path, StandardCharsets.UTF_8);
			cache.put(path, content);
		} finally {
			lock.unlock();
		}

	}

}
