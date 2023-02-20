package com.sample.trade.store.project.persist;

import java.text.SimpleDateFormat;

public class PersistenceConstants {

	public static String IN_MEM_CACHE = "InMemCache";
	public static String DB_CACHE = "DbCache";

	public static String CACHE = IN_MEM_CACHE;

	public static String TRADE_ID = "TradeId";
	public static String VERSION = "VersionNo";
	public static String COUNTER_PARTY_ID = "CounterPartyId";
	public static String BOOK_ID = "BookId";
	public static String MATURITY_DATE = "MaturityDate";
	public static String CREATED_DATE = "CreateDate";
	public static String EXPIRED = "Expired";

	public static String EXPIRED_Y = "Y";
	public static String EXPIRED_N = "N";

    public static SimpleDateFormat sdf = new SimpleDateFormat("DD/MM/YYYY");

}
