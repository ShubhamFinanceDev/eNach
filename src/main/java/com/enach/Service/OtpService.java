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

            String sql = "SELECT * FROM enach WHERE Application_Number='" + applicationNo + "' || Old_Application_Number='" + applicationNo +"';";
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
