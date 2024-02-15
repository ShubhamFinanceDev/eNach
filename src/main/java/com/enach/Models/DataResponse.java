package com.enach.Models;

import lombok.Data;

import java.util.List;

@Data
public class DataResponse {
    private String msg;
    private String code;
    private List<DsaDataModel> dsaExportList;
}
