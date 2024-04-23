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

           // String sql = "SELECT `Installment Amount`,`Sanction Amount` FROM enach WHERE `Application Number`='"+applicationNo+"'";

            String sql= " SELECT  `Installment Amount`,`Sanction Amount` FROM (SELECT a.`Application NUMBER`, a.`Branch NAME`, a.`Sanction Amount`,a.`Customer NUMBER`,\n"
                    + " l.`CUSTOMER NAME`, a.`First Disbursal DATE`, a.`First Instalment DATE`, l.`Installment Amount`,\n"
                    + " l.`NEXT DUE DATE`, a.`Current STATUS`, c.`Mobile No`\n"
                    + " FROM application a \n"
                    + " RIGHT JOIN  loandetails l ON a.`Application Number`=l.CASAPPLNO \n"
                    + " left  JOIN communication c ON a.`Customer Number`=c.`Customer Number` \n"
                    + " UNION \n"
                    + " SELECT APPLICATION_NUMBER,BRANCH_NAME,SANCTION_AMOUNT,NULL,CUSTOMER_NAME, DATE_FORMAT (STR_TO_DATE (FIRST_DISBURSAL_DATE, '%d-%m-%y'), '%Y-%m-%d') AS FIRST_DISBURSAL_DATE, \n"
                    + " DATE_FORMAT (STR_TO_DATE (FIRST_INSTALLMENT_DATE, '%d-%m-%y'), '%Y-%m-%d')  AS FIRST_INSTALLMENT_DATE,cast(INSTALLMENT_AMOUNT as DECIMAL(25,2)) AS INSTALLMENT_AMOUNT, DATE_FORMAT (STR_TO_DATE (NEXT_DUE_DATE, '%d-%m-%y'), '%Y-%m-%d') AS NEXT_DUE_DATE,CURRENT_STATUS,Mobile_No \n"
                    + " FROM enach_old	   \n"
                    + " ) a   WHERE a.`Application Number` LIKE '"+applicationNo+"'  ";


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
