package threadpool;

import java.util.ArrayList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class WaitingRunnableQueue implements Queue<Runnable> {
	private ArrayList<Runnable> runnables = new ArrayList<Runnable>();
	private StaticThreadPool pool;
	private ReentrantLock queueLock;
	private Condition runnablesAvailable;

	public WaitingRunnableQueue(StaticThreadPool pool) {
		this.pool = pool;
		queueLock = new ReentrantLock();
		runnablesAvailable = queueLock.newCondition();
	}

	public int size() {
		queueLock.lock();
		try {
			return runnables.size();
		} finally {
			queueLock.unlock();
		}

	}

	public void put(Runnable obj) {
		queueLock.lock();
		try {
			runnables.add(obj);
			if (pool.debug == true)
				System.out.println("A runnable queued.");
			runnablesAvailable.signalAll();
		} finally {
			queueLock.unlock();
		}
	}

	public Runnable get() {
		queueLock.lock();
		try {
			while (runnables.isEmpty()) {
				if (pool.debug == true)
					System.out.println("Waiting for a runnable...");
				runnablesAvailable.await();
			}
			if (pool.debug == true)
				System.out.println("A runnable dequeued.");
			return runnables.remove(0);
		} catch (InterruptedException ex) {
			return null;
		} finally {
			queueLock.unlock();
		}
	}

}
