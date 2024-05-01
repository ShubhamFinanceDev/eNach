package com.enach.Service;

import com.enach.Models.CustomerDetails;
import com.enach.Repository.EnachOldRepository;
import com.enach.Utill.CustomerDetailsUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class OtpService implements UserDetailsService {

    @Autowired
    @Qualifier("jdbcJdbcTemplate")
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private CustomerDetailsUtility customerDetailsUtility;
    @Autowired
    private EnachOldRepository enachOldRepository;

    @Override
    public UserDetails loadUserByUsername(String applicationNo) throws UsernameNotFoundException {

        CustomerDetails customerDetails = new CustomerDetails();
        try {
            List<CustomerDetails> listData = new ArrayList<>();
            if(!applicationNo.contains("_")){

                /*String quary = "SELECT a.`Application NUMBER`, a.`Branch NAME`, a.`Sanction Amount`,a.`Customer NUMBER`,\n"
                        + " l.`CUSTOMER NAME`, a.`First Disbursal DATE`, a.`First Instalment DATE`, l.`Installment Amount`,\n"
                        + " l.`NEXT DUE DATE`, a.`Current STATUS`, c.`Mobile No`,l.`LOAN STATUS`\n"
                        + " FROM application a \n"
                        + "left JOIN  loandetails l ON a.`Application Number`=l.CASAPPLNO\n"
                        + "left  JOIN communication c ON a.`Customer Number`=c.`Customer Number`\n"
                        + "WHERE l.`LOAN STATUS`='A' AND a.`Application Number` LIKE  '"+applicationNo+"' \n";
*/
                String quary = customerDetailsUtility.getCustomerDetailsQuary(applicationNo);
                listData = jdbcTemplate.query(quary, new BeanPropertyRowMapper<>(CustomerDetails.class));

            }else{

                List<CustomerDetails> CustomerDetailsList = new ArrayList<>();
                List<?> list = enachOldRepository.findOldCustomerDetails(applicationNo);
                if (list.size()>0) {
                    for (int i = 0; i < list.size(); i++) {

                        Object object[] = (Object[]) list.get(i);

                        customerDetails.setApplicationNumber(object[0] + "");
                        customerDetails.setBranchName(object[1] + "");
                        customerDetails.setSanctionAmount(StringUtils.isEmpty(object[2])? null : Double.parseDouble(object[2] + ""));
                        customerDetails.setCustomerName(object[3] + "");
                        customerDetails.setFirstDisbursalDate(StringUtils.isEmpty(object[4])? null : LocalDate.parse(object[4] + ""));
                        customerDetails.setFirstInstalmentDate(StringUtils.isEmpty(object[5])? null :LocalDate.parse(object[5] + ""));
                        customerDetails.setInstallmentAmount(StringUtils.isEmpty(object[6])? null :Double.parseDouble(object[6] + ""));
                        customerDetails.setNextInstallmentDueDate(StringUtils.isEmpty(object[7])? null :LocalDate.parse(object[7] + ""));
                        customerDetails.setMobileNo(object[8]+"");
                        customerDetails.setCurrentStatus(object[9]+"");
                        CustomerDetailsList.add(customerDetails);
                    }
                }
                listData = CustomerDetailsList;
            }
            if (!listData.isEmpty() && listData.size() > 0) {
                customerDetails = listData.get(0);
            }
        } catch (Exception e) {
            throw new RuntimeException("user not found");
        }
        return customerDetails;

    }
}
