package com.enach.Utill;

import org.springframework.stereotype.Service;

@Service
public class CustomerDetailsUtility {


    public String getCustomerDetailsQuary(String applicationNo) {

        String sql = null;

        String quary= " SELECT * FROM ( \n"
                        + " SELECT AP.`Application NUMBER`, AP.`Branch NAME`, AP.`Sanction Loan Amount`,\n"
                        + " AP.`Customer NUMBER`, LD.`CUSTOMER NAME`, AP.`First Disbursal DATE`,\n"
                        + " AP.`First Instalment DATE`, AP.`Installment Amount`, LD.`NEXT INSTALLMENT DUE DATE`,\n"
                        + " AP.`Current STATUS`, ID.PHONE_NUMBER\n"
                        + " FROM application AP\n"
                        + " LEFT JOIN loan LD ON AP.`Application NUMBER` = LD.CASAPPLNO\n"
                        + " LEFT JOIN identification ID ON AP.`Loan Account No` = ID.LOAN_ACCOUNT_NO\n"
                        + " WHERE AP.`Application NUMBER` LIKE '" + applicationNo + "'\n"
                        + " UNION\n"
                        + " SELECT APPLICATION_NUMBER, BRANCH_NAME, SANCTION_AMOUNT, NULL AS CUSTOMER_NUMBER, CUSTOMER_NAME, \n"
                        + " DATE_FORMAT(STR_TO_DATE(FIRST_DISBURSAL_DATE, '%d-%m-%y'), '%Y-%m-%d') AS FIRST_DISBURSAL_DATE,\n"
                        + " DATE_FORMAT(STR_TO_DATE(FIRST_INSTALLMENT_DATE, '%d-%m-%y'), '%Y-%m-%d') AS FIRST_INSTALLMENT_DATE, \n"
                        + " CAST(INSTALLMENT_AMOUNT AS DECIMAL(25,2)) AS INSTALLMENT_AMOUNT, \n"
                        + " DATE_FORMAT(STR_TO_DATE(NEXT_DUE_DATE, '%d-%m-%y'), '%Y-%m-%d') AS NEXT_DUE_DATE, CURRENT_STATUS, Mobile_No AS PHONE_NUMBER\n"
                        + " FROM enach_old\n"
                        + " WHERE APPLICATION_NUMBER LIKE '" + applicationNo + "'\n"
                        + " ) CD WHERE CD.`Application NUMBER` IS NOT NULL AND CD.PHONE_NUMBER IS NOT NULL\n"
                        + " AND LENGTH(CD.PHONE_NUMBER) = 10";

            sql=quary;

        return sql;
    }

    public String getCustomerAmountQuary(String applicationNo) {

        String sql = null;

        String quary= " SELECT `Installment Amount`,`Sanction Amount` FROM (SELECT a.`Application NUMBER`, a.`Branch NAME`, a.`Sanction Amount`,a.`Customer NUMBER`,\n"
                + " l.`CUSTOMER NAME`, a.`First Disbursal DATE`, a.`First Instalment DATE`, l.`Installment Amount`,\n"
                + " l.`NEXT DUE DATE`, a.`Current STATUS`, c.`Mobile No`,l.`LOAN STATUS`\n"
                + " FROM application a\n"
                + " left JOIN  loandetails l ON a.`Application Number`=l.CASAPPLNO\n"
                + " left  JOIN communication c ON a.`Customer Number`=c.`Customer Number`\n"
                + " UNION\n"
                + " SELECT e.APPLICATION_NUMBER,e.BRANCH_NAME,e.SANCTION_AMOUNT,NULL,e.CUSTOMER_NAME,	DATE_FORMAT (STR_TO_DATE (FIRST_DISBURSAL_DATE, '%d-%m-%y'), '%Y-%m-%d') AS FIRST_DISBURSAL_DATE,\n"
                + " DATE_FORMAT (STR_TO_DATE (FIRST_INSTALLMENT_DATE, '%d-%m-%y'), '%Y-%m-%d') AS FIRST_INSTALLMENT_DATE, CAST(e.INSTALLMENT_AMOUNT as DECIMAL(25,2)) AS INSTALLMENT_AMOUNT,\n"
                + " DATE_FORMAT (STR_TO_DATE (NEXT_DUE_DATE, '%d-%m-%y'), '%Y-%m-%d') AS NEXT_DUE_DATE,e.CURRENT_STATUS,e.Mobile_No ,l.`LOAN STATUS`\n"
                + " FROM enach_old	e\n"
                + " left JOIN  loandetails l ON l.`LOAN NO`=e.APPLICATION_NUMBER\n"
                + " ) a   WHERE a.`LOAN STATUS`='A' AND a.`Application Number` LIKE  '"+applicationNo+"' \n";

        sql=quary;

        return sql;
    }
}

