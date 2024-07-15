package com.enach.Utill;

import org.springframework.stereotype.Service;

@Service
public class CustomerDetailsUtility {


    public String getCustomerDetailsQuary(String applicationNo) {

        String sql = null;
        if(applicationNo.startsWith("APPL")) {
            String query = "SELECT * FROM (\n" +
                    "    SELECT AP.\"Application Number\", AP.\"Branch Name\", AP.\"Sanction Loan Amount\",\n" +
                    "           AP.\"Customer Number\", IC.\"Customer Name\", AP.\"First Disbursal Date\",\n" +
                    "           AP.\"First Instalment Date\", AP.\"Installment Amount\",\n" +
                    "           AP.\"Current Status\",(select distinct \"Mobile Number\" from NEO_CAS_LMS_SIT1_SH.\"Address Details\" AD\n" +
                    "where \"Customer Number\"=ap.\"Customer Number\") as PHONE_NUMBER, AP.\"Loan Account No\"\n" +
                    "    FROM neo_cas_lms_sit1_sh.application_newprod AP\n" +
                    "    LEFT JOIN NEO_CAS_LMS_SIT1_SH.\"Identification Details\" ID ON AP.\"Neo CIF ID\" = ID.CUSTOMER_INFO_FILE_NUMBER\n" +
                    "    LEFT JOIN NEO_CAS_LMS_SIT1_SH.\"Individual Customer\" IC ON AP.\"Customer Number\"= IC.\"Customer Number\"\n" +
                    ") CD \n" +
                    "WHERE CD.\"Application Number\" IS NOT NULL\n" +
                    "  AND CD.\"Sanction Loan Amount\" IS NOT NULL\n" +
                    "  AND CD.\"Customer Name\" IS NOT NULL\n" +
                    "  AND CD.\"Installment Amount\" IS NOT NULL\n" +
                    "  AND CD.\"Branch Name\" IS NOT NULL\n" +
                    "  AND CD.PHONE_NUMBER IS NOT NULL\n" +
                    "  AND LENGTH(CD.PHONE_NUMBER) = 10\n" +
                    "  AND CD.\"Application Number\" LIKE '"+applicationNo+"'";
            sql=query;

        } else if (applicationNo.contains("_")) {
           String query="SELECT * FROM ( SELECT APPLICATION_NUMBER, BRANCH_NAME, SANCTION_AMOUNT as SANCTION_LOAN_AMOUNT, NULL, CUSTOMER_NAME,\n" +
                   "           TO_DATE(FIRST_DISBURSAL_DATE, 'DD-MM-RR') AS FIRST_DISBURSAL_DATE,\n" +
                   "           TO_DATE(FIRST_INSTALLMENT_DATE, 'DD-MM-RR') AS FIRST_INSTALLMENT_DATE,\n" +
                   "           TO_NUMBER(INSTALLMENT_AMOUNT) AS INSTALLMENT_AMOUNT,\n" +
                   "           TO_DATE(NEXT_DUE_DATE, 'DD-MM-RR') AS NEXT_DUE_DATE, CURRENT_STATUS,\n" +
                   "           Mobile_No as PHONE_NUMBER, LOAN_ACCOUNT_NO\n" +
                   "    FROM enach_old ) CD \n" +
                   "WHERE CD.\"APPLICATION_NUMBER\" IS NOT NULL\n" +
                   "  AND CD.\"SANCTION_LOAN_AMOUNT\" IS NOT NULL\n" +
                   "  AND CD.\"CUSTOMER_NAME\" IS NOT NULL\n" +
                   "  AND CD.\"INSTALLMENT_AMOUNT\" IS NOT NULL\n" +
                   "  AND CD.\"BRANCH_NAME\" IS NOT NULL\n" +
                   "  AND CD.PHONE_NUMBER IS NOT NULL\n" +
                   "  AND LENGTH(CD.PHONE_NUMBER) = 10\n" +
                   "  AND CD.APPLICATION_NUMBER LIKE '"+applicationNo+"'";
            sql=query;


        }
        System.out.println(sql);
        return sql;
    }

}

