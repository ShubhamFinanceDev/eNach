package com.enach.ServiceIMPL;

import com.enach.Controller.CustomerController;
import com.enach.Entity.EnachPayment;
import com.enach.Models.EnachPaymentRequest;
import com.enach.Repository.EnachPaymentRepository;
import com.enach.Service.DatabaseService;
import com.enach.Service.ReqstrService;
import com.enach.Utill.CustomerDetailsUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private final Logger logger = LoggerFactory.getLogger(ReqstrServiceIMPL.class);

    @Override
    public void saveEnachPayment(EnachPaymentRequest request) throws Exception {

        EnachPayment enachPayment = new EnachPayment();
        String transactionStatus ="inprocess";
        String mandateType = ("MNTH".equalsIgnoreCase(request.getMandateType())) ? "e-Mandate" : "security-mandate";

        try {
            enachPaymentRepository.unprocessTransaction(request.getApplicationNo(),request.getMandateType());
            enachPayment.setTransactionNo(request.getTransactionNo());
            enachPayment.setApplicationNo(request.getApplicationNo());
            enachPayment.setPaymentMethod(request.getPaymentMethod());
            enachPayment.setMandateType(mandateType);
            enachPayment.setTransactionStartDate(request.getTransactionStartDate());
            enachPayment.setTransactionStatus(transactionStatus);
            enachPayment.setAmount(request.getAmount());
            enachPayment.setBankName(request.getBankName());
            enachPayment.setBankAccountNo(request.getBankAccountNo());
            enachPayment.setIfscCode(request.getIfscCode());
            enachPayment.setStartDate(request.getStartDate());
            enachPayment.setEndDate(request.getEndDate());
            enachPaymentRepository.save(enachPayment);
            logger.info("Transaction details saved {}",request.getTransactionNo());

        } catch (DataIntegrityViolationException ex) {
            logger.error("Error while saving transaction details {} {}",request.getTransactionNo(),ex.getMessage());
            throw new DataIntegrityViolationException(ex.getMessage());

        } catch (Exception e) {
            logger.error("Error while saving transaction details {} {}",request.getTransactionNo(),e.getMessage());
            throw new Exception(e);
        }
    }

}
