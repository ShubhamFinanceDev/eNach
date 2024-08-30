package com.enach.Models;

import lombok.Data;

import java.util.List;

@Data
public class OtpVerifyResponseForCancellation
{
    public Data data ;
    public List<LoanDetails> loanDetails;

    @Data
    public static class Data
    {
        public String customerName;
        public String applicationNo;
        public String mobileNo;
    }

    @Data
    public static class LoanDetails
    {
        public String loanNo;
        public String status;
    }


}
