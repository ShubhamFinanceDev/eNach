package com.enach.Service;

import com.enach.Entity.CustomerDetails;
import com.enach.Models.BranchNameDetail;
import com.enach.Models.MandateTypeAmountData;
import com.enach.Repository.CustomerDetailsRepository;
import com.enach.Utill.CustomerDetailsUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DatabaseService implements UserDetailsService {

    @Autowired
    @Qualifier("jdbcJdbcTemplate")
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private CustomerDetailsUtility customerDetailsUtility;
    @Autowired
    private CustomerDetailsRepository customerDetailsRepository;

    @Override
    public UserDetails loadUserByUsername(String applicationNo) throws UsernameNotFoundException {

        CustomerDetails customerDetails = new CustomerDetails();
        try {
            List<CustomerDetails> listData=getCustomerDetails(applicationNo);
            if (!listData.isEmpty()) {
                customerDetails = listData.get(0);
            }
        } catch (Exception e) {
            throw new RuntimeException("user not found");
        }
        return customerDetails;

    }

    public List<CustomerDetails> getCustomerDetails(String applicationNo) throws Exception {
        List<CustomerDetails> listData=new ArrayList<>();

            if (applicationNo.startsWith("APPL")) {
                String application = customerDetailsUtility.getCustomerDetailsQuery(applicationNo);
                 listData = jdbcTemplate.query(application, new BeanPropertyRowMapper<>(CustomerDetails.class));
            } else if (applicationNo.contains("_")) {

                CustomerDetails oldCustomer=customerDetailsRepository.getCustomerDetailsBy(applicationNo);
                if (oldCustomer !=null){listData.add(oldCustomer);}
            }

        return listData;

    }

    public BranchNameDetail branchName(String applicationNo) throws Exception{
        return (applicationNo.startsWith("APPL") ? jdbcTemplate.queryForObject(customerDetailsUtility.getBranchNameQuery(applicationNo), new BeanPropertyRowMapper<>(BranchNameDetail.class)): customerDetailsRepository.getBranchName(applicationNo));
    }

    public MandateTypeAmountData mandateTypeAmount(String applicationNo) throws Exception{
        return (applicationNo.startsWith("APPL") ? jdbcTemplate.queryForObject(customerDetailsUtility.getMandateAmountQuery(applicationNo), new BeanPropertyRowMapper<>(MandateTypeAmountData.class)): customerDetailsRepository.getMandateTypeAmount(applicationNo));

    }
}
