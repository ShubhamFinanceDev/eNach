package com.enach.ServiceIMPL;

import com.enach.Entity.CustomerDetails;
import com.enach.Entity.OtpDetails;
import com.enach.Repository.OtpDetailsRepository;
import com.enach.Service.CancellationService;
import com.enach.Service.DatabaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class CancellationServiceImpl implements CancellationService {

    @Autowired
    private OtpDetailsRepository otpDetailsRepository;

    @Autowired
    private DatabaseService databaseService;


    public List<CustomerDetails> getCustomerDetail(String mobileNo, String otpCode, String applicationNo) {
        try {
            // Fetch list of customer details based on application number
            List<CustomerDetails> listData = databaseService.getCustomerDetailsFromLoans(applicationNo);
            return listData;
        } catch (Exception e) {
            System.out.println("Error fetching customer details: " + e.getMessage());
            e.printStackTrace();  // Log the full stack trace
            return null;
        }
    }

}
