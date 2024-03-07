package com.enach.ServiceIMPL;

import com.enach.Entity.EnachPayment;
import com.enach.Entity.ResponseStructure;
import com.enach.Models.CommonResponse;
import com.enach.Models.EnachPaymentStatusRequest;
import com.enach.Repository.EnachPaymentRepository;
import com.enach.Repository.ReqStrDetailsRepository;
import com.enach.Service.ReqstrService;
import org.apache.poi.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


@Service
public class ReqstrServiceIMPL implements ReqstrService {

    @Autowired
    private ReqStrDetailsRepository reqStrDetailsRepository;
    @Autowired
    private EnachPaymentRepository enachPaymentRepository;


    @Override
    public ResponseStructure saveMandateRespDoc(String checkSum, String status, String msgId,  String refId, String errorCode, String errorMessage, String filler1, String filler2, String filler3, String filler4, String filler5, String filler6, String filler7, String filler8, String filler9, String filler10) {

        ResponseStructure responseStructure = new ResponseStructure();

        responseStructure.setCheckSumVal(checkSum);
        responseStructure.setStatus(status);
        responseStructure.setMsgId(msgId);
        responseStructure.setRefId(refId);
        responseStructure.setErrorCode(errorCode);
        responseStructure.setErrorMessage(errorMessage);
        responseStructure.setFiller1(filler1);
        responseStructure.setFiller2(filler2);
        responseStructure.setFiller3(filler3);
        responseStructure.setFiller4(filler4);
        responseStructure.setFiller5(filler5);
        responseStructure.setFiller6(filler6);
        responseStructure.setFiller7(filler7);
        responseStructure.setFiller8(filler8);
        responseStructure.setFiller9(filler9);
        responseStructure.setFiller10(filler10);

        reqStrDetailsRepository.save(responseStructure);

        return responseStructure;
    }



    @Override
    public EnachPayment saveEnachPayment(String transactionNo, String loanNo, Timestamp transactionStartDate) throws Exception {

        EnachPayment enachPayment = new EnachPayment();
        String transactionStatus ="inprocess";

        try {

            enachPayment.setTransactionNo(transactionNo);
            enachPayment.setLoanNo(loanNo);
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



    @Override
    public EnachPayment updateEnachPaymentStatus(String transactionNo, String transactionStatus) {

        EnachPayment enachPayment = null;

        try {
            enachPayment = enachPaymentRepository.findByTansactionNo(transactionNo);

            if (enachPayment != null && !StringUtils.isEmpty(enachPayment)) {

                Timestamp transactionCompleteDate = new Timestamp(System.currentTimeMillis());
                enachPaymentRepository.updatePaymentStatus(transactionNo, transactionStatus,transactionCompleteDate);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return enachPayment;
    }


}
