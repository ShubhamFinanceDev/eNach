package com.enach.Models;

import lombok.Data;

import java.util.Objects;

@Data
public class SaveStatusRequest {

    private String applicationNo;
    private String loanNo;
    private String cancelCause;

    public boolean hasNullFields() {
        return Objects.isNull(loanNo) ||
                Objects.isNull(applicationNo) ||
                Objects.isNull(cancelCause);
    }
    public boolean blankFields() {
        return loanNo.isBlank() ||
               applicationNo.isBlank() ||
                cancelCause.isBlank();
    }

    public void validate() {
        if (hasNullFields() || blankFields()) {
            throw new IllegalArgumentException("One or more fields are null or empty.");
        }
    }
}
