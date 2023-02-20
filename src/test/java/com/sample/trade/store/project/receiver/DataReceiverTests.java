package com.sample.trade.store.project.receiver;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Description;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.sample.trade.store.project.persist.InMemCache;
import com.sample.trade.store.project.persist.PersistenceConstants;
import com.sample.trade.store.project.persist.PersistenceManager;
import com.sample.trade.store.project.process.TradeProcessorImpl;
import com.sample.trade.store.project.validate.DateValidator;
import com.sample.trade.store.project.validate.VersionValidator;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@ContextConfiguration(classes = {DataReceiver.class, TradeProcessorImpl.class, InMemCache.class, DateValidator.class, VersionValidator.class})
public class DataReceiverTests {
    
    @Autowired
    DataReceiver dataReceiver;

    Logger log = Logger.getLogger("DataReceiverTests");

    static Map<String, String> t1, t21, t22, t3, t22_updated, t23;

    static SimpleDateFormat sdf = new SimpleDateFormat("DD/MM/YYYY");

    @BeforeClass
    public static void beforeClassSetup() {
    	t1 = new HashMap<>();
    	t1.put(PersistenceConstants.TRADE_ID,"T1");
    	t1.put(PersistenceConstants.VERSION, "1");
    	t1.put(PersistenceConstants.COUNTER_PARTY_ID, "CP-1");
    	t1.put(PersistenceConstants.BOOK_ID, "B1");
    	t1.put(PersistenceConstants.MATURITY_DATE, "20/05/2020");
    	t1.put(PersistenceConstants.CREATED_DATE, sdf.format(new Date()));
    	t1.put(PersistenceConstants.EXPIRED, "N");

    	t22 = new HashMap<>();
    	t22.put(PersistenceConstants.TRADE_ID,"T2");
    	t22.put(PersistenceConstants.VERSION, "2");
    	t22.put(PersistenceConstants.COUNTER_PARTY_ID, "CP-1");
    	t22.put(PersistenceConstants.BOOK_ID, "B1");
    	t22.put(PersistenceConstants.MATURITY_DATE, "20/05/2021");
    	t22.put(PersistenceConstants.CREATED_DATE, "14/03/2015");
    	t22.put(PersistenceConstants.EXPIRED, "N");
    	
    	t21 = new HashMap<>();
    	t21.put(PersistenceConstants.TRADE_ID,"T2");
    	t21.put(PersistenceConstants.VERSION, "1");
    	t21.put(PersistenceConstants.COUNTER_PARTY_ID, "CP-2");
    	t21.put(PersistenceConstants.BOOK_ID, "B1");
    	t21.put(PersistenceConstants.MATURITY_DATE, "20/05/2021");
    	t21.put(PersistenceConstants.CREATED_DATE, sdf.format(new Date()));
    	t21.put(PersistenceConstants.EXPIRED, "N");

    	t3 = new HashMap<>();
    	t3.put(PersistenceConstants.TRADE_ID,"T3");
    	t3.put(PersistenceConstants.VERSION, "3");
    	t3.put(PersistenceConstants.COUNTER_PARTY_ID, "CP-3");
    	t3.put(PersistenceConstants.BOOK_ID, "B2");
    	t3.put(PersistenceConstants.MATURITY_DATE, "20/05/2014");
    	t3.put(PersistenceConstants.CREATED_DATE, sdf.format(new Date()));
    	t3.put(PersistenceConstants.EXPIRED, "Y");

    	t22_updated = new HashMap<>();
    	t22_updated.put(PersistenceConstants.TRADE_ID,"T2");
    	t22_updated.put(PersistenceConstants.VERSION, "2");
    	t22_updated.put(PersistenceConstants.COUNTER_PARTY_ID, "CP-1");
    	t22_updated.put(PersistenceConstants.BOOK_ID, "N4");
    	t22_updated.put(PersistenceConstants.MATURITY_DATE, "20/05/2021");
    	t22_updated.put(PersistenceConstants.CREATED_DATE, "14/03/2015");
    	t22_updated.put(PersistenceConstants.EXPIRED, "N");

    	t23 = new HashMap<>();
    	t23.put(PersistenceConstants.TRADE_ID,"T2");
    	t23.put(PersistenceConstants.VERSION, "3");
    	t23.put(PersistenceConstants.COUNTER_PARTY_ID, "CP-1");
    	t23.put(PersistenceConstants.BOOK_ID, "B4");
    	t23.put(PersistenceConstants.MATURITY_DATE, "20/05/2021");
    	t23.put(PersistenceConstants.CREATED_DATE, "14/03/2015");
    	t23.put(PersistenceConstants.EXPIRED, "N");
    }

    @After
    public void tearDown() {
    	PersistenceManager.getCache(PersistenceConstants.CACHE).clear();
    }

    @Test (expected = Exception.class)
    public void testReceive() throws Exception {
        dataReceiver.receive(null);
    }

    @Test (expected = Exception.class)
    @Description("Exception Due to past Maturity Date")
    public void scenarioOne() throws Exception {
    	dataReceiver.receive(t1);
    	dataReceiver.receive(t22);
    	dataReceiver.receive(t21);
    	dataReceiver.receive(t3);
    	
    	List<Map<String, String>> cacheValues = PersistenceManager.getCache(PersistenceConstants.CACHE).retrieveAll();
    	
    	cacheValues.stream().forEach(obj -> log.info(obj.toString()));
    	log.info("----------------------------------------------");
    }

    @Test (expected = Exception.class)
    @Description("Failed for t21 as old version received late")
    public void scenarioTwo() throws Exception {
    	t1.put(PersistenceConstants.MATURITY_DATE, "01/01/2099");
    	dataReceiver.receive(t1);
    	t22.put(PersistenceConstants.MATURITY_DATE, "01/01/2099");
    	dataReceiver.receive(t22);
    	t21.put(PersistenceConstants.MATURITY_DATE, "01/01/2099");
    	dataReceiver.receive(t21);
    	t3.put(PersistenceConstants.MATURITY_DATE, "01/01/2099");
    	dataReceiver.receive(t3);
    	
    	List<Map<String, String>> cacheValues = PersistenceManager.getCache(PersistenceConstants.CACHE).retrieveAll();

    	cacheValues.stream().forEach(obj -> log.info(obj.toString()));
    	log.info("----------------------------------------------");
    }

    @Test
    @Description("Success scenario")
    public void scenarioThree() throws Exception {
    	t1.put(PersistenceConstants.MATURITY_DATE, "01/01/2099");
    	dataReceiver.receive(t1);
    	t21.put(PersistenceConstants.MATURITY_DATE, "01/01/2099");
    	dataReceiver.receive(t21);
    	t22.put(PersistenceConstants.MATURITY_DATE, "01/01/2099");
    	dataReceiver.receive(t22);
    	t3.put(PersistenceConstants.MATURITY_DATE, "01/01/2099");
    	dataReceiver.receive(t3);

    	List<Map<String, String>> cacheValues = PersistenceManager.getCache(PersistenceConstants.CACHE).retrieveAll();

    	List<Map<String, String>> temp = new ArrayList<>();
    	temp.add(t1);
    	temp.add(t22);
    	temp.add(t21);
    	temp.add(t3);

    	assertTrue(cacheValues.equals(temp));

    	cacheValues.stream().forEach(obj -> log.info(obj.toString()));
    	log.info("----------------------------------------------");
    }

    @Test
    @Description("Test for order check")
    public void scenarioFour() throws Exception {
    	t1.put(PersistenceConstants.MATURITY_DATE, "01/01/2099");
    	dataReceiver.receive(t1);
    	t21.put(PersistenceConstants.MATURITY_DATE, "01/01/2099");
    	dataReceiver.receive(t21);
    	t22.put(PersistenceConstants.MATURITY_DATE, "01/01/2099");
    	dataReceiver.receive(t22);
    	t3.put(PersistenceConstants.MATURITY_DATE, "01/01/2099");
    	dataReceiver.receive(t3);

    	List<Map<String, String>> cacheValues = PersistenceManager.getCache(PersistenceConstants.CACHE).retrieveAll();

    	List<Map<String, String>> temp = new ArrayList<>();
    	temp.add(t1);
    	temp.add(t21);
    	temp.add(t22);
    	temp.add(t3);

    	assertFalse(cacheValues.equals(temp));

    	cacheValues.stream().forEach(obj -> log.info(obj.toString()));
    	log.info("----------------------------------------------");
    }

    @Test
    @Description("Success scenario for override value")
    public void scenarioFive() throws Exception {
    	t1.put(PersistenceConstants.MATURITY_DATE, "01/01/2099");
    	dataReceiver.receive(t1);
    	t21.put(PersistenceConstants.MATURITY_DATE, "01/01/2099");
    	dataReceiver.receive(t21);
    	t22.put(PersistenceConstants.MATURITY_DATE, "01/01/2099");
    	dataReceiver.receive(t22);
    	t3.put(PersistenceConstants.MATURITY_DATE, "01/01/2099");
    	dataReceiver.receive(t3);

    	// Push update for existing trade. It should replace the existing object
    	t22_updated.put(PersistenceConstants.MATURITY_DATE, "01/01/2099");
    	dataReceiver.receive(t22_updated);

    	List<Map<String, String>> cacheValues = PersistenceManager.getCache(PersistenceConstants.CACHE).retrieveAll();

    	List<Map<String, String>> temp = new ArrayList<>();
    	temp.add(t1);
    	temp.add(t22_updated);
    	temp.add(t21);
    	temp.add(t3);

    	assertTrue(cacheValues.equals(temp));

    	cacheValues.stream().forEach(obj -> log.info(obj.toString()));
    	log.info("----------------------------------------------");
    }

    @Test
    @Description("Success scenario with order for new version")
    public void scenarioSix() throws Exception {
    	t1.put(PersistenceConstants.MATURITY_DATE, "01/01/2099");
    	dataReceiver.receive(t1);
    	t21.put(PersistenceConstants.MATURITY_DATE, "01/01/2099");
    	dataReceiver.receive(t21);
    	t22.put(PersistenceConstants.MATURITY_DATE, "01/01/2099");
    	dataReceiver.receive(t22);
    	t3.put(PersistenceConstants.MATURITY_DATE, "01/01/2099");
    	dataReceiver.receive(t3);

    	// Push update for existing trade. It should replace the existing object
    	t22_updated.put(PersistenceConstants.MATURITY_DATE, "01/01/2099");
    	dataReceiver.receive(t22_updated);

    	// Push update for existing trade. It should replace the existing object
    	t23.put(PersistenceConstants.MATURITY_DATE, "01/01/2099");
    	dataReceiver.receive(t23);

    	List<Map<String, String>> cacheValues = PersistenceManager.getCache(PersistenceConstants.CACHE).retrieveAll();

    	List<Map<String, String>> temp = new ArrayList<>();
    	temp.add(t1);
    	temp.add(t23);
    	temp.add(t22_updated);
    	temp.add(t21);
    	temp.add(t3);

    	assertTrue(cacheValues.equals(temp));

    	cacheValues.stream().forEach(obj -> log.info(obj.toString()));
    	log.info("----------------------------------------------");
    }
}
