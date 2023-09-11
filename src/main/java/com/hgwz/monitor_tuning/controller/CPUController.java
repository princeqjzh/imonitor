package com.hgwz.monitor_tuning.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CPUController {
	
	@RequestMapping("/loop")
	public List<Long> loop(){
		String data="{\"data\":[{\"partnerid\":]";
		return getPartneridsFromJson(data);
	}
	
	private Object lock1=new Object();
	private Object lock2=new Object();
	
	@RequestMapping("/deadlock")
	public String deadlock() {
		new Thread(()->{
			synchronized (lock1) {
				try {
					Thread.sleep(1000);
				} catch (Exception e) {
					// TODO: handle exception
				}
				synchronized (lock2) {
					System.out.println("Thread1 over");
				}
			}
		}).start();
		
		new Thread(()->{
			synchronized (lock2) {
				try {
					Thread.sleep(1000);
				} catch (Exception e) {
					// TODO: handle exception
				}
				synchronized (lock1) {
					System.out.println("Thread2 over");
				}
			}
		}).start();
		
		return "deadlock";
	}

	@RequestMapping("/")
	public String index() {

		return "A root page";
	}

	@RequestMapping("/deadlocking")
	public String deadlocking() {
		Lock lock1 = new ReentrantLock();
		Lock lock2 = new ReentrantLock();
		Thread t1 = new Thread() {
			@Override
			public void run() {
				try {
					lock1.lock();
					System.out.println(Thread.currentThread().getName() + " get the lock1");
					Thread.sleep(1000);
					lock2.lock();
					System.out.println(Thread.currentThread().getName() + " get the lock2");
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		};
		Thread t2 = new Thread() {
			@Override
			public void run() {
				try {
					lock2.lock();
					System.out.println(Thread.currentThread().getName() + " get the lock2");
					Thread.sleep(1000);
					lock1.lock();
					System.out.println(Thread.currentThread().getName() + " get the lock1");
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		};
		//设置线程名字，方便分析堆栈信息
		t1.setName("mythread-t1");
		t2.setName("mythread-t2");
		t1.start();
		t2.start();

		return "Dead Locking";
	}
	
	public static List<Long> getPartneridsFromJson(String data){
	    //{\"data\":[{\"partnerid\":982,\"count\":\"10000\",\"cityid\":\"11\"},{\"partnerid\":983,\"count\":\"10000\",\"cityid\":\"11\"},{\"partnerid\":984,\"count\":\"10000\",\"cityid\":\"11\"}]}
	    //上面是正常的数据
	    List<Long> list = new ArrayList<Long>(2);
	    if(data == null || data.length() <= 0){
	        return list;
	    }    
	    int datapos = data.indexOf("data");
	    if(datapos < 0){
	        return list;
	    }
	    int leftBracket = data.indexOf("[",datapos);
	    int rightBracket= data.indexOf("]",datapos);
	    if(leftBracket < 0 || rightBracket < 0){
	        return list;
	    }
	    String partners = data.substring(leftBracket+1,rightBracket);
	    if(partners == null || partners.length() <= 0){
	        return list;
	    }
	    while(partners!=null && partners.length() > 0){
	        int idpos = partners.indexOf("partnerid");
	        if(idpos < 0){
	            break;
	        }
	        int colonpos = partners.indexOf(":",idpos);
	        int commapos = partners.indexOf(",",idpos);
	        if(colonpos < 0 || commapos < 0){
	            //partners = partners.substring(idpos+"partnerid".length());//1
	            continue;
	        }
	        String pid = partners.substring(colonpos+1,commapos);
	        if(pid == null || pid.length() <= 0){
	            //partners = partners.substring(idpos+"partnerid".length());//2
	            continue;
	        }
	        try{
	            list.add(Long.parseLong(pid));
	        }catch(Exception e){
	            //do nothing
	        }
	        partners = partners.substring(commapos);
	    }
	    return list;
	}

}
