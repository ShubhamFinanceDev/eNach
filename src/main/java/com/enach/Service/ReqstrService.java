package com.enach.Service;

import com.enach.Entity.MandateRespDoc;

public interface ReqstrService {


    MandateRespDoc saveMandateRespDoc(String status, String msgId, String refId, String errors, String errorCode, String errorMessage, String filler1, String filler2, String filler3, String filler4, String filler5, String filler6, String filler7, String filler8, String filler9, String filler10);
}

