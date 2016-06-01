package filecache;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.concurrent.locks.ReentrantLock;

public class FileCacheLRU extends FileCache {
	private static FileCacheLRU instance = null;
	private static ReentrantLock lock = new ReentrantLock();

	private FileCacheLRU() {
	};

	public static FileCacheLRU getInstance() {
		lock.lock();
		try {
			if (instance == null)
				instance = new FileCacheLRU();
		} finally {
			lock.unlock();
		}
		return instance;
	}

	@Override
	protected void replace(Path path) throws IOException {
		lock.lock();
		try {
			AccessTimeStamp accessTimeStamp = AccessTimeStamp.getInstance();

			cache.remove(accessTimeStamp.getPathWithLeastTimeStamp());
			byte[] content = readFile(path, StandardCharsets.UTF_8);
			cache.put(path, content);
		} finally {
			lock.unlock();
		}

	}

}
