package session;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

public class Session {

	private Map<String, Object> sessionMap = new HashMap<>();
	private ReentrantLock lock = new ReentrantLock();

	public Session() {

	}

	public void setAttribute(String JSESSIONID, Session session) {
		lock.lock();
		try {
			sessionMap.put(JSESSIONID, session);
		} finally {
			lock.unlock();
		}
	}

	public void removeAttribute(String JSESSIONID) {
		lock.lock();
		try {
			sessionMap.remove(JSESSIONID);
		} finally {
			lock.unlock();
		}
	}

	public Object getAttribute(String JSESSIONID) {
		lock.lock();
		try {
			return sessionMap.get(JSESSIONID);

		} finally {
			lock.unlock();
		}

	}
}
