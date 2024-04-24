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

            String sql= " SELECT `Installment Amount`,`Sanction Amount` FROM (SELECT a.`Application NUMBER`, a.`Branch NAME`, a.`Sanction Amount`,a.`Customer NUMBER`,\n"
                    + " l.`CUSTOMER NAME`, a.`First Disbursal DATE`, a.`First Instalment DATE`, l.`Installment Amount`,\n"
                    + " l.`NEXT DUE DATE`, a.`Current STATUS`, c.`Mobile No`,l.`LOAN STATUS`\n"
                    + " FROM application a\n"
                    + " left JOIN  loandetails l ON a.`Application Number`=l.CASAPPLNO\n"
                    + " left  JOIN communication c ON a.`Customer Number`=c.`Customer Number`\n"
                    + " UNION\n"
                    + " SELECT e.APPLICATION_NUMBER,e.BRANCH_NAME,e.SANCTION_AMOUNT,NULL,e.CUSTOMER_NAME,DATE_FORMAT(e.FIRST_DISBURSAL_DATE,'%Y-%m-%d') AS FIRST_DISBURSAL_DATE,\n"
                    + " DATE_FORMAT(e.FIRST_INSTALLMENT_DATE,'%Y-%m-%d') AS FIRST_INSTALLMENT_DATE, CAST(e.INSTALLMENT_AMOUNT as DECIMAL(25,2)) AS INSTALLMENT_AMOUNT,\n"
                    + " DATE_FORMAT(e.NEXT_DUE_DATE,'%d-%m-%y') AS NEXT_DUE_DATE,e.CURRENT_STATUS,e.Mobile_No ,l.`LOAN STATUS`\n"
                    + " FROM enach_old	e\n"
                    + " left JOIN  loandetails l ON l.`LOAN NO`=e.APPLICATION_NUMBER\n"
                    + " ) a WHERE a.`LOAN STATUS`='A' AND a.`Application Number` LIKE '"+applicationNo+"' \n";

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
