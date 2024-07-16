package com.enach.ServiceIMPL;

import com.enach.Entity.EnachPayment;
import com.enach.Repository.EnachPaymentRepository;
import com.enach.Service.DatabaseService;
import com.enach.Service.ReqstrService;
import com.enach.Utill.CustomerDetailsUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Timestamp;


@Service
public class ReqstrServiceIMPL implements ReqstrService {

    @Autowired
    @Qualifier("jdbcJdbcTemplate")
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private EnachPaymentRepository enachPaymentRepository;
    @Autowired
    private CustomerDetailsUtility customerDetailsUtility;
    @Autowired
    private DatabaseService databaseService;


    @Override
    public void saveEnachPayment(String transactionNo, String applicationNo, String paymentMethod, String mandateType, Timestamp transactionStartDate, BigDecimal amount) throws Exception {

        EnachPayment enachPayment = new EnachPayment();
        String transactionStatus ="inprocess";

        try {
            enachPaymentRepository.unprocessTransaction(applicationNo,mandateType);
            enachPayment.setTransactionNo(transactionNo);
            enachPayment.setApplicationNo(applicationNo);
            enachPayment.setPaymentMethod(paymentMethod);
            enachPayment.setMandateType(mandateType);
            enachPayment.setTransactionStartDate(transactionStartDate);
            enachPayment.setTransactionStatus(transactionStatus);
            enachPayment.setAmount(amount);
            enachPaymentRepository.save(enachPayment);

        } catch (DataIntegrityViolationException ex) {
            throw new DataIntegrityViolationException(ex.getMessage());

        } catch (Exception e) {
            throw new Exception(e);
        }
    }

}
