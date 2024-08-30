package com.enach.Models;

import lombok.Data;

import java.util.HashMap;
import java.util.List;

@Data
public class OtpVerifyResponseForCancellation
{
    public Data data ;
    public List<NestedLoansDetails> loansDetails;
    @lombok.Data
    public static class Data
    {
        public String customerName;
        public String applicationNo;
        public String mobileNo;
    }

}
