package threadpool;

public class ThreadPoolThread extends Thread {
	private StaticThreadPool pool;
	private WaitingRunnableQueue queue;
	private int id;

	public ThreadPoolThread(StaticThreadPool pool, WaitingRunnableQueue queue, int id) {
		this.pool = pool;
		this.queue = queue;
		this.id = id;
	}

	public void run() {
		if (pool.debug == true)
			System.out.println("ThreadPoolThread " + id + " starts.");
		while (!pool.stopped) {
			Runnable runnable = queue.get();
			if (runnable == null) {
				if (pool.debug == true)
					System.out.println("ThreadPoolThread " + this.id + " is being stopped due to an InterruptedException.");
				continue;
			} else {
				if (pool.debug == true)
					System.out.println("ThreadPoolThread " + id + " executes a runnable.");
				runnable.run();
				if (pool.debug == true)
					System.out.println("ThreadPoolThread " + id + " finishes executing a runnable.");
			}
		}
		// if(pool.debug==true) System.out.println("Thread " + id + " stops.");
	}
}
