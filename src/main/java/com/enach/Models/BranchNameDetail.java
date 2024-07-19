package com.enach.Models;

import lombok.Data;


@Data
public class BranchNameDetail {

    private String applicationNumber;
    private String branchName;

    public BranchNameDetail(String branchName, String applicationNumber) {
        this.branchName = branchName;
        this.applicationNumber = applicationNumber;
    }

    public BranchNameDetail() {

    }
}
