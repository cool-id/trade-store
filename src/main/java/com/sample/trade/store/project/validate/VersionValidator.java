package com.sample.trade.store.project.validate;

import java.util.Map;

import com.sample.trade.store.project.persist.PersistenceConstants;
import com.sample.trade.store.project.persist.PersistenceManager;

public class VersionValidator implements Validator<Map<String, String>, Integer> {

	@Override
	public Integer validate(Map<String, String> data) {
		String tradeId = data.get(PersistenceConstants.TRADE_ID);

		Map<String, String> storedValue = PersistenceManager.getCache(PersistenceConstants.CACHE).retrieve(tradeId, data.get(PersistenceConstants.VERSION));
		if (storedValue == null) {

			storedValue = PersistenceManager.getCache(PersistenceConstants.CACHE).retrieve(tradeId);

			if (storedValue != null) {
				int versionNo = Integer.parseInt(data.get(PersistenceConstants.VERSION));
				int vNo = Integer.parseInt(storedValue.get(PersistenceConstants.VERSION));
				if (vNo > versionNo) {
					return -1;
				} else if (vNo == versionNo) {
					return 0;
				} else {
					return 1;
				}
			} else {
				return 1;
			}
		} else {
			return 0;
		}
	}

}
