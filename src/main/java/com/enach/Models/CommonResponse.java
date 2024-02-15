package com.enach.Models;

import lombok.Data;

@Data
public class CommonResponse {
    private String msg;
    private String code;

    public CommonResponse()
    {
        msg="Successfully process.";
        code="0000";
    }
}
