package com.enach.ServiceIMPL;

import com.enach.Entity.EnachPayment;
import com.enach.Models.EnachPaymentRequest;
import com.enach.Repository.EnachPaymentRepository;
import com.enach.Service.DatabaseService;
import com.enach.Service.ReqstrService;
import com.enach.Utill.CustomerDetailsUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;


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
    public void saveEnachPayment(EnachPaymentRequest request) throws Exception {

        EnachPayment enachPayment = new EnachPayment();
        String transactionStatus ="inprocess";

        try {
            enachPaymentRepository.unprocessTransaction(request.getApplicationNo(),request.getMandateType());
            enachPayment.setTransactionNo(request.getTransactionNo());
            enachPayment.setApplicationNo(request.getApplicationNo());
            enachPayment.setPaymentMethod(request.getPaymentMethod());
            enachPayment.setMandateType(request.getMandateType());
            enachPayment.setTransactionStartDate(request.getTransactionStartDate());
            enachPayment.setTransactionStatus(transactionStatus);
            enachPayment.setAmount(request.getAmount());
            enachPayment.setBankName(request.getBankName());
            enachPayment.setBankAccountNo(request.getBankAccountNo());
            enachPayment.setIfscCode(request.getIfscCode());
            enachPayment.setStartDate(request.getStartDate());
            enachPayment.setEndDate(request.getEndDate());
            enachPaymentRepository.save(enachPayment);

        } catch (DataIntegrityViolationException ex) {
            throw new DataIntegrityViolationException(ex.getMessage());

        } catch (Exception e) {
            throw new Exception(e);
        }
    }

}
