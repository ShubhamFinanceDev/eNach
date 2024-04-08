package com.enach.Models;

import lombok.Data;

@Data
public class StatusResponse extends  CommonResponse{

    private String applicationNo;


    public StatusResponse(){

        this.applicationNo = "";
    }


}
