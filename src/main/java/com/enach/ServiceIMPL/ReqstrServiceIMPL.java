package com.enach.ServiceIMPL;

import com.enach.Entity.EnachPayment;
import com.enach.Models.CustomerDetails;
import com.enach.Models.MandateTypeAmountData;
import com.enach.Repository.EnachOldRepository;
import com.enach.Repository.EnachPaymentRepository;
import com.enach.Service.ReqstrService;
import com.enach.Utill.CustomerDetailsUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


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
    private EnachOldRepository enachOldRepository;


    @Override
    public MandateTypeAmountData getMandateTypeAmount(String applicationNo) {

        MandateTypeAmountData mandateTypeAmountResponse = new MandateTypeAmountData();

        try {

            List<MandateTypeAmountData> listData = new ArrayList<>();
            if(!applicationNo.contains("_")){

               /* String quary = "SELECT l.`Installment Amount`,a.`Sanction Amount`\n"
                        + " FROM application a \n"
                        + "left JOIN  loandetails l ON a.`Application Number`=l.CASAPPLNO\n"
                        + "left  JOIN communication c ON a.`Customer Number`=c.`Customer Number`\n"
                        + "WHERE l.`LOAN STATUS`='A' AND a.`Application Number` LIKE  '"+applicationNo+"' \n";
*/
                String quary = customerDetailsUtility.getMandateTypeAmountDataQuary(applicationNo);
                listData = jdbcTemplate.query(quary, new BeanPropertyRowMapper<>(MandateTypeAmountData.class));

            }else{

                List<MandateTypeAmountData> MandateTypeAmountDataList = new ArrayList<>();
                List<?> list = enachOldRepository.findMandateTypeAmount(applicationNo);
                if (list.size()>0) {
                    for (int i = 0; i < list.size(); i++) {

                        Object object[] = (Object[]) list.get(i);

                            mandateTypeAmountResponse.setNextInstallmentAmount(StringUtils.isEmpty(object[0]) ? null : new BigDecimal(object[0] + ""));
                            mandateTypeAmountResponse.setSanctionLoanAmount(StringUtils.isEmpty(object[1]) ? null :new BigDecimal(object[1] + ""));

                        MandateTypeAmountDataList.add(mandateTypeAmountResponse);
                    }
                }
                listData = MandateTypeAmountDataList;
            }

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
