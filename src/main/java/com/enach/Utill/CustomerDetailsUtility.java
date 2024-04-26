package com.enach.Utill;

import org.springframework.stereotype.Service;

@Service
public class CustomerDetailsUtility {


    public String getCustomerDetailsQuary(String applicationNo) {

        String sql = null;


            String quary = " SELECT a.\"Application Number\", a.\"Branch Name\", a.\"Sanction Loan Amount\", a.\"Customer Number\", \n"
                          +" l.\"CUSTOMER NAME\", a.\"First Disbursal Date\", a.\"First Instalment Date\", \n"
                          +" l.\"NEXT INSTALLMENT AMOUNT\", l.\"NEXT INSTALLMENT DUE DATE\", \n"
                          +" c.\"Mobile No\", l.\"LOAN STATUS\" \n"
                          +" FROM neo_cas_lms_sit1_sh.application_newprod a \n "
                          +" LEFT JOIN neo_cas_lms_sit1_sh.loandetails_newprod l ON a.\"Application Number\"=l.\"CASAPPLNO\" \n"
                          +" LEFT JOIN neo_cas_lms_sit1_sh.\"Communication Details\" c ON a.\"Customer Number\"=c.\"Customer Number\" \n"
                          +" WHERE l.\"LOAN STATUS\"='A' AND a.\"Application Number\"  LIKE  '"+applicationNo+"' ";
            sql=quary;

        return sql;
    }

    public String getMandateTypeAmountDataQuary(String applicationNo) {

        String sql = null;

        String quary = " SELECT l.\"NEXT INSTALLMENT AMOUNT\", a.\"Sanction Loan Amount\" \n"
                      +" FROM neo_cas_lms_sit1_sh.application_newprod a \n "
                      +" LEFT JOIN neo_cas_lms_sit1_sh.loandetails_newprod l ON a.\"Application Number\"=l.\"CASAPPLNO\" \n"
                      +" LEFT JOIN neo_cas_lms_sit1_sh.\"Communication Details\" c ON a.\"Customer Number\"=c.\"Customer Number\" \n"
                      +" WHERE l.\"LOAN STATUS\"='A' AND a.\"Application Number\"  LIKE  '"+applicationNo+"' ";
            sql=quary;

        return sql;

    }

}

