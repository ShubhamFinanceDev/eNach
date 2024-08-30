package com.enach.Service;

import com.enach.Entity.CustomerDetails;

import java.util.List;

public interface CancellationService {
    List<CustomerDetails> getCustomerDetail(String mobileNo, String otpCode, String applicationNo);
}
