package com.enach.ServiceIMPL;

import com.enach.Entity.MandateRespDoc;
import com.enach.Repository.ReqStrDetailsRepository;
import com.enach.Service.ReqstrService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReqstrServiceIMPL implements ReqstrService {

    @Autowired
    private ReqStrDetailsRepository reqStrDetailsRepository;



    @Override
    public MandateRespDoc saveMandateRespDoc(String status, String msgId, String refId, String errors, String errorCode, String errorMessage, String filler1, String filler2, String filler3, String filler4, String filler5, String filler6, String filler7, String filler8, String filler9, String filler10) {

        MandateRespDoc mandateRespDoc = new MandateRespDoc();

        mandateRespDoc.setStatus(status);
        mandateRespDoc.setMsgId(msgId);
        mandateRespDoc.setRefId(refId);
        mandateRespDoc.setErrors(errors);
        mandateRespDoc.setErrorCode(errorCode);
        mandateRespDoc.setErrorMessage(errorMessage);
        mandateRespDoc.setFiller1(filler1);
        mandateRespDoc.setFiller2(filler2);
        mandateRespDoc.setFiller3(filler3);
        mandateRespDoc.setFiller4(filler4);
        mandateRespDoc.setFiller5(filler5);
        mandateRespDoc.setFiller6(filler6);
        mandateRespDoc.setFiller7(filler7);
        mandateRespDoc.setFiller8(filler8);
        mandateRespDoc.setFiller9(filler9);
        mandateRespDoc.setFiller10(filler10);

        reqStrDetailsRepository.save(mandateRespDoc);

        return mandateRespDoc;
    }
}
