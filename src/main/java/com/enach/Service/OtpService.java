package com.enach.Service;

import com.enach.Models.CustomerDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OtpService implements UserDetailsService {

    @Autowired
    @Qualifier("jdbcJdbcTemplate")
    private JdbcTemplate jdbcTemplate;

    @Override
    public UserDetails loadUserByUsername(String applicationNo) throws UsernameNotFoundException {

        CustomerDetails customerDetails = new CustomerDetails();
        try {

           // String sql = "SELECT * FROM enach WHERE `Application Number`='" + applicationNo + "'";

            String sql= " SELECT * FROM (SELECT a.`Application NUMBER`, a.`Branch NAME`, a.`Sanction Amount`,a.`Customer NUMBER`,\n"
                    + " l.`CUSTOMER NAME`, a.`First Disbursal DATE`, a.`First Instalment DATE`, l.`Installment Amount`,\n"
                    + " l.`NEXT DUE DATE`, a.`Current STATUS`, c.`Mobile No`,l.`LOAN STATUS`\n"
                    + " FROM application a\n"
                    + " left JOIN  loandetails l ON a.`Application Number`=l.CASAPPLNO\n"
                    + " left  JOIN communication c ON a.`Customer Number`=c.`Customer Number`\n"
                    + " UNION\n"
                    + " SELECT e.APPLICATION_NUMBER,e.BRANCH_NAME,e.SANCTION_AMOUNT,NULL,e.CUSTOMER_NAME,	DATE_FORMAT (STR_TO_DATE (FIRST_DISBURSAL_DATE, '%d-%m-%y'), '%Y-%m-%d') AS FIRST_DISBURSAL_DATE,\n"
                    + " DATE_FORMAT (STR_TO_DATE (FIRST_INSTALLMENT_DATE, '%d-%m-%y'), '%Y-%m-%d') AS FIRST_INSTALLMENT_DATE, CAST(e.INSTALLMENT_AMOUNT as DECIMAL(25,2)) AS INSTALLMENT_AMOUNT,\n"
                    + " DATE_FORMAT (STR_TO_DATE (NEXT_DUE_DATE, '%d-%m-%y'), '%Y-%m-%d') AS NEXT_DUE_DATE,e.CURRENT_STATUS,e.Mobile_No ,l.`LOAN STATUS`\n"
                    + " FROM enach_old	e\n"
                    + " left JOIN  loandetails l ON l.`LOAN NO`=e.APPLICATION_NUMBER\n"
                    + " ) a   WHERE a.`LOAN STATUS`='A' AND a.`Application Number` LIKE  '"+applicationNo+"' \n";

            List<CustomerDetails> listData = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(CustomerDetails.class));

            if (!listData.isEmpty() && listData.size() > 0) {
                customerDetails = listData.get(0);
            }
        } catch (Exception e) {
            throw new RuntimeException("user not found");
        }
        return customerDetails;

    }
}
