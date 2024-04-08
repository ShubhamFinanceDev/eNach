package com.enach.ServiceIMPL;

import com.enach.Entity.EnachPayment;
import com.enach.Models.EmailDetails;
import com.enach.Models.MandateTypeAmountResponse;
import com.enach.Models.SecurityMandateTypeAmountResponse;
import com.enach.Repository.EnachPaymentRepository;
import com.enach.Service.ReqstrService;
import com.enach.Utill.OtpUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.sql.Timestamp;
import java.util.List;


@Service
public class ReqstrServiceIMPL implements ReqstrService {

    @Autowired
    @Qualifier("jdbcJdbcTemplate")
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private EnachPaymentRepository enachPaymentRepository;


    @Override
    public MandateTypeAmountResponse getMandateTypeAmount(String applicationNo) {

        MandateTypeAmountResponse mandateTypeAmountResponse = new MandateTypeAmountResponse();

        try {

            String sql = "SELECT Installment_Amount FROM enach WHERE Application_Number='"+applicationNo+"';";
            List<MandateTypeAmountResponse> listData = jdbcTemplate.query(sql,new BeanPropertyRowMapper<>(MandateTypeAmountResponse.class));

            if(!listData.isEmpty() && listData.size()>0) {
                mandateTypeAmountResponse = listData.get(0);
            }else{
                mandateTypeAmountResponse = null;

            }

        } catch (Exception e) {
            System.out.println(e);
        }

        return mandateTypeAmountResponse;
    }

    @Override
    public SecurityMandateTypeAmountResponse getsecurityMandateTypeAmount(String applicationNo) {

        SecurityMandateTypeAmountResponse securityMandateTypeAmountResponse = new SecurityMandateTypeAmountResponse();

        try {

            String sql = "SELECT Sanction_Amount FROM enach WHERE Application_Number='"+applicationNo+"';";
            List<SecurityMandateTypeAmountResponse> listData = jdbcTemplate.query(sql,new BeanPropertyRowMapper<>(SecurityMandateTypeAmountResponse.class));

            if(!listData.isEmpty() && listData.size()>0) {
                securityMandateTypeAmountResponse = listData.get(0);
            }else{
                securityMandateTypeAmountResponse = null;

            }

        } catch (Exception e) {
            System.out.println(e);
        }

        return securityMandateTypeAmountResponse;
    }


    @Override
    public EnachPayment saveEnachPayment(String transactionNo, String applicationNo, String mandateType, Timestamp transactionStartDate) throws Exception {

        EnachPayment enachPayment = new EnachPayment();
        String transactionStatus ="inprocess";

        try {

            enachPayment.setTransactionNo(transactionNo);
            enachPayment.setApplicationNo(applicationNo);
            enachPayment.setMandateType(mandateType);
            enachPayment.setTransactionStartDate(transactionStartDate);
            //enachPayment.setTransactionCompleteDate(null);
            enachPayment.setTransactionStatus(transactionStatus);

            enachPaymentRepository.save(enachPayment);
        } catch (DataIntegrityViolationException ex) {
            throw new DataIntegrityViolationException(ex.getMessage());

        } catch (Exception e) {
            throw new Exception(e);
        }

        return enachPayment;
    }





}
