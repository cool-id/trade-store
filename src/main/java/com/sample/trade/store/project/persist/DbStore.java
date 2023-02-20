package com.sample.trade.store.project.persist;

import java.util.List;
import java.util.Map;

public class DbStore implements ObjectStore {

	@Override
	public void store(Map<String, String> data) {
	}

	@Override
	public void update(Map<String, String> data) {

	}

	@Override
	public List<Map<String, String>> retrieveAll() {
		return null;
	}

	@Override
	public Map<String, String> retrieve(String tradeId) {
		return null;
	}

	@Override
	public Map<String, String> retrieve(String tradeId, String version) {
		return null;
	}

	@Override
	public void clear() {

	}

}
