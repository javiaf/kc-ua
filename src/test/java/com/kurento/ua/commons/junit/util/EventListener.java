package com.kurento.ua.commons.junit.util;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EventListener<T> {
	private static final Logger log = LoggerFactory
			.getLogger(EventListener.class);

	private String id;
	private BlockingQueue<T> eventQueue = new LinkedBlockingQueue<T>();

	public EventListener(String id) {
		this.id = id;
	}

	public void onEvent(T event) {
		log.info(id + " - TEST FACILITY received an event:" + event);
		try {
			eventQueue.put(event);
		} catch (InterruptedException e) {
			log.error("Unable to insert event into FIFO", e);
		}
	}

	public T poll(int timeoutSec) throws InterruptedException {
		log.info(id
				+ " - TEST FACILITY polling. Asking for new event. Wait until reception");
		T e = eventQueue.poll(timeoutSec, TimeUnit.SECONDS);
		log.info(id + " - TEST FACILITY polling. Found new event, response: "
				+ e);
		return e;
	}

}