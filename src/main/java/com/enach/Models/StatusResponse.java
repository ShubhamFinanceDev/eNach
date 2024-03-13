package com.enach.Models;

import lombok.Data;

@Data
public class StatusResponse extends  CommonResponse{

    private String loanNo;


    public StatusResponse(){

        this.loanNo = "";
    }


}
