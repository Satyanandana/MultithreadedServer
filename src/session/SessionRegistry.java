package session;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

public class SessionRegistry {
	private static SessionRegistry sessionRegistry = null;
	private Map<String, Session> registry = new HashMap<>();
	private static ReentrantLock lock = new ReentrantLock();

	private SessionRegistry() {

	}

	public static SessionRegistry getInstance() {
		lock.lock();
		try {
			if (sessionRegistry == null) {
				sessionRegistry = new SessionRegistry();
			}
		} finally {
			lock.unlock();
		}
		return sessionRegistry;
	}

	public void addSession(String JSESSIONID, Session session) {
		lock.lock();
		try {
			registry.put(JSESSIONID, session);
		} finally {
			lock.unlock();
		}
	}

	public void removeSession(String JSESSIONID) {
		lock.lock();
		try {
			registry.remove(JSESSIONID);
		} finally {
			lock.unlock();
		}
	}

	public Session getSession(String JSESSIONID) {
		lock.lock();
		try {
			return registry.get(JSESSIONID);

		} finally {
			lock.unlock();
		}

	}

}
