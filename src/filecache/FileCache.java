package filecache;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public abstract class FileCache {

	protected static Map<Path, byte[]> cache = new HashMap<Path, byte[]>();
	private static ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

	private static final int cacheSize = 10;

	public byte[] fetch(Path path) throws IOException {
		byte[] content = null;
		lock.writeLock().lock();
		try {

			if (!cache.containsKey(path)) {
				content = cacheFile(path);
				return content;
			}

			lock.readLock().lock();
			try {
				lock.writeLock().unlock();
				content = cache.get(path);
				return content;
			} finally {
				lock.readLock().unlock();
			}

		} finally {
			if (lock.isWriteLockedByCurrentThread()) {
				lock.writeLock().unlock();
			}

		}

	}

	private byte[] cacheFile(Path path) throws IOException {
		byte[] content = null;

		if (cache.size() >= cacheSize) {
			replace(path);
		} else {
			content = readFile(path, StandardCharsets.UTF_8);
			cache.put(path, content);
		}
		content = cache.get(path);

		return content;
	}

	protected abstract void replace(Path path) throws IOException;

	public static byte[] readFile(Path path, Charset encoding) throws IOException {
		byte[] encoded = Files.readAllBytes(path);
		// return new String(encoded, encoding);
		return encoded;
	}

}
