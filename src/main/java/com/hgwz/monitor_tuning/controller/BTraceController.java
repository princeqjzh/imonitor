package com.hgwz.monitor_tuning.controller;

import com.hgwz.monitor_tuning.entry.User;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("hgwz")
public class BTraceController {

    @GetMapping(value = "/arg1")
    public String arg1(@RequestParam("name") String name) throws InterruptedException {
        Thread.sleep(2000);
        return "Hello," + name;
    }

    @GetMapping(value = "/arg2")
    public User arg2(User user) {
        return user;
    }

    @GetMapping(value = "/constructor")
    public User constructor(User user) {
        return user;
    }

    @GetMapping(value = "/saynameme1")
    public String same(@RequestParam("name") String name) {
        return "Hello," + name;
    }

    @GetMapping(value = "/saynameme2")
    public String same(@RequestParam("id") int id, @RequestParam("name") String name) {
        return "Hello," + name + "," + id;
    }

    @GetMapping(value = "/exception")
    public String exception() {
        try {
            System.out.println("started...");
            System.out.println(1 / 0); //模拟异常
            System.out.println("end...");
        } catch (Exception e) {}
        return "200";
    }

    @GetMapping(value = "/addex")
    public void adder() throws Exception {

    }

}
