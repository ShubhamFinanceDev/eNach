package com.enach.Utill;

import com.enach.Repository.CustomerDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerDetailsUtility {
@Autowired
private CustomerDetailsRepository customerDetailsRepository;

    public String getCustomerDetailsQuery(String applicationNo) {

        String sql = null;
        String query = "SELECT * FROM (\n" +
                "    SELECT AP.\"Application Number\", AP.\"Branch Name\", AP.\"Sanction Loan Amount\",\n" +
                "           AP.\"Customer Number\", IC.\"Customer Name\",\n" +
                "            AP.\"Installment Amount\",\n" +
                "           AP.\"Current Status\",(select distinct \"Mobile Number\" from NEO_CAS_LMS_SIT1_SH.\"Address Details\" AD\n" +
                "where \"Customer Number\"=ap.\"Customer Number\" AND \"Mobile Number\" IS NOT NULL AND ROWNUM = 1) as PHONE_NUMBER, AP.\"Loan Account No\"\n" +
                "    FROM neo_cas_lms_sit1_sh.application_newprod AP\n" +
                "    LEFT JOIN NEO_CAS_LMS_SIT1_SH.\"Individual Customer\" IC ON AP.\"Customer Number\"= IC.\"Customer Number\"\n" +
                ") CD \n" +
                "WHERE CD.\"Application Number\" IS NOT NULL\n" +
                "  AND CD.\"Sanction Loan Amount\" IS NOT NULL\n" +
                "  AND CD.\"Customer Name\" IS NOT NULL\n" +
                "  AND CD.\"Installment Amount\" IS NOT NULL\n" +
                "  AND CD.\"Branch Name\" IS NOT NULL\n" +
                "  AND CD.PHONE_NUMBER IS NOT NULL\n" +
                "  AND LENGTH(CD.PHONE_NUMBER) = 10\n" +
                "  AND CD.\"Application Number\" LIKE '" + applicationNo + "'";
        sql = query;

//        System.out.println(sql);
        return sql;
    }
    public String getBranchNameQuery(String applicationNo){
        return "Select \"Branch Name\" from neo_cas_lms_sit1_sh.application_newprod where \"Application Number\"='" + applicationNo+ "'";
    }

    public String getMandateAmountQuery(String applicationNo) {
        return "Select \"Sanction Loan Amount\",\"Installment Amount\" from neo_cas_lms_sit1_sh.application_newprod where \"Application Number\"='" + applicationNo+ "'";

    }

    public String getLoanDetailsQuery(String applicationNo) {
        String query = "SELECT \"LOAN NO\" AS loanAccountNo, \n" +
                "CASAPPLNO AS applicationNumber, \n" +
                "\"LOAN STATUS\" AS currentStatus, \n" +
                "\"CUSTOMER NAME\" AS customerName \n" +
                "FROM neo_cas_lms_sit1_sh.loandetails_newprod \n" +
                "WHERE CASAPPLNO = '" + applicationNo + "' \n";
        return query;
    }
}

