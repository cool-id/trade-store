package com.sample.trade.store.project.persist;

import java.util.List;
import java.util.Map;

public interface ObjectStore {
	public void store(Map<String, String> data);

	public void update(Map<String, String> data);

	public List<Map<String, String>> retrieveAll();

	public Map<String, String> retrieve(String tradeId);

	public Map<String, String> retrieve(String tradeId, String version);

	public void clear();
}
