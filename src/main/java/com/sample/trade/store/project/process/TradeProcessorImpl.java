package com.sample.trade.store.project.process;

import java.text.ParseException;
import java.util.Date;
import java.util.Map;

import org.springframework.scheduling.annotation.Scheduled;

import com.sample.trade.store.project.persist.PersistenceConstants;
import com.sample.trade.store.project.persist.PersistenceManager;
import com.sample.trade.store.project.validate.DateValidator;
import com.sample.trade.store.project.validate.VersionValidator;

public class TradeProcessorImpl implements TradeProcessor<Map<String, String>> {

	@Override
	public String process(Map<String, String> data) throws Exception {

    	Integer status = new DateValidator().validate(data);
    	if (status != 0) {
        	throw new Exception();
    	}

    	int val = new VersionValidator().validate(data);

    	switch (val) {
		case -1: {
			throw new Exception();
		}
		case 0: {
			// Update
			PersistenceManager.getCache(PersistenceConstants.CACHE).update(data);
			break;
		}
		case 1: {
			// Store
			PersistenceManager.getCache(PersistenceConstants.CACHE).store(data);
			break;
		}
		default:
			throw new Exception();
		}
    	
    	return "Success";
	}

	/**
	 * Scheduling the maturity expiry check midnight at 23:59:59
	 */
	@Override
	@Scheduled(cron = "59 59 23 * * ? *")
	public synchronized String update() throws Exception {
		PersistenceManager.getCache(PersistenceConstants.CACHE).retrieveAll().forEach(m -> {
			String maturity = m.get(PersistenceConstants.MATURITY_DATE);
			try {
				Date maturityDate = PersistenceConstants.sdf.parse(maturity);
				maturityDate.before(new Date());
				m.put(PersistenceConstants.EXPIRED, PersistenceConstants.EXPIRED_Y);
			} catch (ParseException e) {
				// Ignore the parsing issues for Maturity date.
			}
		});

		return "Success";
	}
}
