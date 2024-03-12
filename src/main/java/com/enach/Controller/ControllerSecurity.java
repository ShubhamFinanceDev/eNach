package com.enach.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@RequestMapping("/mandate")
@RestController
public class ControllerSecurity {

    @GetMapping("/eMandate")
    public String eMandate()
    {
        return "sucess";
    }
}
