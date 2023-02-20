package com.sample.trade.store.project.validate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import com.sample.trade.store.project.persist.PersistenceConstants;

public class DateValidator implements Validator<Map<String, String>, Integer> {

	private SimpleDateFormat sdf = new SimpleDateFormat("DD/MM/YYYY");

	@Override
	public Integer validate(Map<String, String> data) {
		String sDate = data.get(PersistenceConstants.MATURITY_DATE);
		try {
			if (sDate != null) {
				Date date = sdf.parse(sDate);
				if (date.after(new Date())) {
					return ValidatorConstants.VALID;
				} else {
					return ValidatorConstants.INVALID;
				}
			} else {
				return ValidatorConstants.INVALID;
			}
		} catch (ParseException e) {
			return ValidatorConstants.INVALID;
		}
	}
}
