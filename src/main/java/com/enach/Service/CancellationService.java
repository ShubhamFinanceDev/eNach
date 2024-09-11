package com.enach.Service;

import com.enach.Entity.CustomerDetails;
import com.enach.Models.SaveStatusRequest;

import java.util.List;

public interface CancellationService {
    List<CustomerDetails> getCustomerDetail(String mobileNo, String otpCode, String applicationNo);
    String statusRequest(SaveStatusRequest statusRequest) throws Exception;

}
