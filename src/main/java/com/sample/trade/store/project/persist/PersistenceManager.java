package com.sample.trade.store.project.persist;

import java.util.HashMap;
import java.util.Map;

public class PersistenceManager {

	private static Map<String, ObjectStore> caches = new HashMap<>();

	static {
		caches.put(PersistenceConstants.IN_MEM_CACHE, new InMemCache());
		caches.put(PersistenceConstants.DB_CACHE, new DbStore());
	}

	public static ObjectStore getCache(String cacheType) {
		return caches.get(cacheType);
	}
}
