package com.enach.Service;

import com.enach.Entity.CustomerDetails;
import com.enach.Entity.StatusManage;
import com.enach.Models.SaveStatusRequest;

import java.util.List;

public interface CancellationService {
    List<CustomerDetails> getCustomerDetail(String mobileNo, String otpCode, String applicationNo);
    List<StatusManage> statusRequest(SaveStatusRequest statusRequest) throws Exception;

}
