package com.sample.trade.store.project.persist;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class InMemCache implements ObjectStore {

	List<Map<String, String>> cache = Collections.synchronizedList(new ArrayList<>());

	@Override
	public void store(Map<String, String> data) {
		int indexOf = 0;
		for (int index = 0; index < cache.size(); index++) {
			Map<String, String> obj = cache.get(index);
			if (obj != null && obj.get(PersistenceConstants.TRADE_ID).equals(data.get(PersistenceConstants.TRADE_ID))) {
				indexOf = index;
				break;
			}
		}
		if (indexOf == 0) {
			cache.add(data);
		} else {
			cache.add(indexOf, data);
		}
	}

	@Override
	public List<Map<String, String>> retrieveAll() {
		return cache;
	}

	@Override
	public void update(Map<String, String> data) {
		cache.forEach(t -> {
			if (t.get(PersistenceConstants.TRADE_ID).equals(data.get(PersistenceConstants.TRADE_ID))
					&& t.get(PersistenceConstants.VERSION).equals(data.get(PersistenceConstants.VERSION))) {
				t.clear();
				t.putAll(data);
			}
		});
	}

	@Override
	public Map<String, String> retrieve(String tradeId) {
		Map<String, String> obj = null;

		obj = cache.stream().filter(m -> (m.get(PersistenceConstants.TRADE_ID) != null
				&& m.get(PersistenceConstants.TRADE_ID).equals(tradeId))).findAny().orElse(null);

		return obj;
	}

	@Override
	public Map<String, String> retrieve(String tradeId, String version) {
		Map<String, String> obj = null;

		obj = cache.stream()
				.filter(m -> (m.get(PersistenceConstants.TRADE_ID) != null
						&& m.get(PersistenceConstants.TRADE_ID).equals(tradeId)
						&& m.get(PersistenceConstants.VERSION) != null
						&& m.get(PersistenceConstants.VERSION).equals(version)))
				.findAny().orElse(null);

		return obj;
	}

	@Override
	public void clear() {
		cache = new ArrayList<>();
	}
}
