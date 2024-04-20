package com.enach.ServiceIMPL;

import com.enach.Entity.EnachPayment;
import com.enach.Models.MandateTypeAmountData;
import com.enach.Repository.EnachPaymentRepository;
import com.enach.Service.ReqstrService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

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
    public MandateTypeAmountData getMandateTypeAmount(String applicationNo) {

        MandateTypeAmountData mandateTypeAmountResponse = new MandateTypeAmountData();

        try {

            String sql = "SELECT `Installment Amount`,`Sanction Amount` FROM enach WHERE `Application Number`='"+applicationNo+"'";
            List<MandateTypeAmountData> listData = jdbcTemplate.query(sql,new BeanPropertyRowMapper<>(MandateTypeAmountData.class));

            if(!listData.isEmpty() && listData.size()>0) {
                mandateTypeAmountResponse = listData.get(0);
            }

        } catch (Exception e) {
            System.out.println(e);
        }

        return mandateTypeAmountResponse;
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
