package com.enach.ServiceIMPL;

import com.enach.Entity.EnachPayment;
import com.enach.Models.EmailDetails;
import com.enach.Models.MandateTypeAmountResponse;
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
    public MandateTypeAmountResponse getMandateTypeAmount(String loanNo) {

        MandateTypeAmountResponse mandateTypeAmountResponse = new MandateTypeAmountResponse();

        try {

            String sql = "SELECT amount FROM customer_details WHERE loan_no='"+loanNo+"';";
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
    public EnachPayment saveEnachPayment(String transactionNo, String loanNo, String mandateType, Timestamp transactionStartDate) throws Exception {

        EnachPayment enachPayment = new EnachPayment();
        String transactionStatus ="inprocess";

        try {

            enachPayment.setTransactionNo(transactionNo);
            enachPayment.setLoanNo(loanNo);
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
