package com.sample.trade.store.project.receiver;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.sample.trade.store.project.process.TradeProcessor;

public class DataReceiver {

    @Autowired
    private TradeProcessor<Map<String, String>> tradeProcessor;

    @PostMapping(value = "/data")
    public ResponseEntity<String> receive(@RequestBody Map<String, String> data) throws Exception {

    	if (data == null || data.isEmpty()) {
    		new Exception("Empty dataset");
    	}

		return ResponseEntity.status(HttpStatus.OK).body(tradeProcessor.process(data));

    }

    @PostMapping(value = "/maturity")
    public ResponseEntity<String> maturity() {
    	try {
			return ResponseEntity.status(HttpStatus.OK).body(tradeProcessor.update());
		} catch (Exception e) {
    		return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}
    }
}
