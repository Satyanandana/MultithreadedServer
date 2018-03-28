package threadpool;

import java.util.Vector;
import java.util.concurrent.locks.ReentrantLock;

public final class StaticThreadPool {
	private static StaticThreadPool staticThreadPool;
	protected boolean debug = false;
	protected boolean stopped = false;
	private WaitingRunnableQueue queue;
	private Vector<ThreadPoolThread> availableThreads;
	private static final ReentrantLock lock = new ReentrantLock();

	private StaticThreadPool(int maxThreadNum, boolean debug) {
		this.debug = debug;
		queue = new WaitingRunnableQueue(this);
		availableThreads = new Vector<ThreadPoolThread>();
		for (int i = 0; i < maxThreadNum; i++) {
			ThreadPoolThread th = new ThreadPoolThread(this, queue, i);
			availableThreads.add(th);
			th.start();
		}
	}

	public static StaticThreadPool getInstance(int maxThreadNum, boolean debug) {
		lock.lock();
		try {
			if (staticThreadPool == null) {
				staticThreadPool = new StaticThreadPool(maxThreadNum, debug);
			}
		} finally {
			lock.unlock();
		}
		return staticThreadPool;
	}

	public void execute(Runnable runnable) {
		lock.lock();
		try {
			queue.put(runnable);
		} finally {
			lock.unlock();
		}

	}

	public int getWaitingRunnableQueueSize() {
		lock.lock();
		try {
			return queue.size();
		} finally {
			lock.unlock();
		}

	}

	public int getThreadPoolSize() {
		lock.lock();
		try {
			return availableThreads.size();
		} finally {
			lock.unlock();
		}

	}

	public void shutdown() {
		lock.lock();
		try {
			this.stopped = true;
			for (ThreadPoolThread t : availableThreads) {
				t.interrupt();
			}

		} finally {
			lock.unlock();
		}
	}
}
