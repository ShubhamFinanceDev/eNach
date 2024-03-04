package com.enach.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/eNach")

public class ResponseController {

    @GetMapping(value = "/")
    public String demo(){

        return "Hello programmer";
    }
    @RequestMapping("/response")
    public String getData()
    {
        return "response";
    }
}

