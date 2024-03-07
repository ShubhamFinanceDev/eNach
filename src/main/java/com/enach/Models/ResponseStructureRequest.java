package com.enach.Models;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResponseStructureRequest {

    private String checkSumVal;
    private MandateRespDoc mandateRespDoc;

}
