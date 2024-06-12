package com.enach.Utill;

import org.springframework.stereotype.Service;

@Service
public class CustomerDetailsUtility {


    public String getCustomerDetailsQuary(String applicationNo) {

        String sql = null;

        String quary= " SELECT * FROM ( \n"
                + " SELECT AP.`Application NUMBER`, AP.`Branch NAME`, AP.`Sanction Loan Amount`,\n"
                + " AP.`Customer NUMBER`, LD.`CUSTOMER NAME`, AP.`First Disbursal DATE`,\n"
                + " AP.`First Instalment DATE`, AP.`Installment Amount`,LD.`NEXT INSTALLMENT DUE DATE`,\n"
                + " AP.`Current STATUS`, ID.PHONE_NUMBER\n"
                + " FROM Application AP\n"
                + " left JOIN  Loan LD ON AP.`Application Number`=LD.CASAPPLNO\n"
                + "  left  JOIN Identification ID ON AP.`Neo CIF ID`=ID.CUSTOMER_INFO_FILE_NUMBER \n"
                + " UNION\n"
                + " SELECT APPLICATION_NUMBER,BRANCH_NAME,SANCTION_AMOUNT,NULL,CUSTOMER_NAME,DATE_FORMAT (STR_TO_DATE (FIRST_DISBURSAL_DATE, '%d-%m-%y'), '%Y-%m-%d') AS FIRST_DISBURSAL_DATE,\n"
                + " DATE_FORMAT (STR_TO_DATE (FIRST_INSTALLMENT_DATE, '%d-%m-%y'), '%Y-%m-%d') AS FIRST_INSTALLMENT_DATE, cast(INSTALLMENT_AMOUNT as DECIMAL(25,2)) AS INSTALLMENT_AMOUNT,\n"
                + " DATE_FORMAT (STR_TO_DATE (NEXT_DUE_DATE, '%d-%m-%y'), '%Y-%m-%d') AS NEXT_DUE_DATE,CURRENT_STATUS,Mobile_No\n"
                + " FROM enach_old\n"
                + " )CD WHERE CD.`Application NUMBER` IS NOT NULL AND CD.PHONE_NUMBER  IS NOT NULL\n"
                + " AND LENGTH(CD.PHONE_NUMBER)=10  AND CD.`Application NUMBER` LIKE '"+applicationNo+"' \n";


            sql=quary;
          System.out.println(sql);
        return sql;
    }

    public String getCustomerAmountQuary(String applicationNo) {

        String sql = null;


        String quary= " SELECT CD.`Installment Amount`,CD.`Sanction Loan Amount` FROM ( \n"
                + "  SELECT AP.`Application NUMBER`, AP.`Branch NAME`, AP.`Sanction Loan Amount`,\n"
                + "  AP.`Customer NUMBER`, LD.`CUSTOMER NAME`, AP.`First Disbursal DATE`,\n"
                + "  AP.`First Instalment DATE`, AP.`Installment Amount`,LD.`NEXT INSTALLMENT DUE DATE`,\n"
                + "  AP.`Current STATUS`, ID.PHONE_NUMBER\n"
                + "  FROM Application AP\n"
                + "  left JOIN  Loan LD ON AP.`Application Number`=LD.CASAPPLNO\n"
                + "  left  JOIN Identification ID ON AP.`Neo CIF ID`=ID.CUSTOMER_INFO_FILE_NUMBER \n"
                + " UNION\n"
                + " SELECT APPLICATION_NUMBER,BRANCH_NAME,SANCTION_AMOUNT,NULL,CUSTOMER_NAME,DATE_FORMAT (STR_TO_DATE (FIRST_DISBURSAL_DATE, '%d-%m-%y'), '%Y-%m-%d') AS FIRST_DISBURSAL_DATE,\n"
                + " DATE_FORMAT (STR_TO_DATE (FIRST_INSTALLMENT_DATE, '%d-%m-%y'), '%Y-%m-%d') AS FIRST_INSTALLMENT_DATE, cast(INSTALLMENT_AMOUNT as DECIMAL(25,2)) AS INSTALLMENT_AMOUNT,\n"
                + " DATE_FORMAT (STR_TO_DATE (NEXT_DUE_DATE, '%d-%m-%y'), '%Y-%m-%d') AS NEXT_DUE_DATE,CURRENT_STATUS,Mobile_No\n"
                + " FROM enach_old\n"
                + " )CD WHERE CD.`Application NUMBER` IS NOT NULL AND CD.PHONE_NUMBER  IS NOT NULL\n"
                + " AND LENGTH(CD.PHONE_NUMBER)=10  AND CD.`Application NUMBER` LIKE '"+applicationNo+"' \n";



        sql=quary;

        return sql;
    }



    public String getCustomerBranchDetailsQuary(String applicationNo) {


        String sql = null;


        String quary= " SELECT CD.`Application NUMBER`,CD.`Branch NAME` FROM ( \n"
                + "  SELECT AP.`Application NUMBER`, AP.`Branch NAME`, AP.`Sanction Loan Amount`,\n"
                + "  AP.`Customer NUMBER`, LD.`CUSTOMER NAME`, AP.`First Disbursal DATE`,\n"
                + "  AP.`First Instalment DATE`, AP.`Installment Amount`,LD.`NEXT INSTALLMENT DUE DATE`,\n"
                + "  AP.`Current STATUS`, ID.PHONE_NUMBER\n"
                + "  FROM application AP\n"
                + "  left JOIN  loan LD ON AP.`Application Number`=LD.CASAPPLNO\n"
                + "  left  JOIN identification ID ON AP.`Loan Account No`=ID.LOAN_ACCOUNT_NO\n"
                + " UNION\n"
                + " SELECT APPLICATION_NUMBER,BRANCH_NAME,SANCTION_AMOUNT,NULL,CUSTOMER_NAME,DATE_FORMAT (STR_TO_DATE (FIRST_DISBURSAL_DATE, '%d-%m-%y'), '%Y-%m-%d') AS FIRST_DISBURSAL_DATE,\n"
                + " DATE_FORMAT (STR_TO_DATE (FIRST_INSTALLMENT_DATE, '%d-%m-%y'), '%Y-%m-%d') AS FIRST_INSTALLMENT_DATE, cast(INSTALLMENT_AMOUNT as DECIMAL(25,2)) AS INSTALLMENT_AMOUNT,\n"
                + " DATE_FORMAT (STR_TO_DATE (NEXT_DUE_DATE, '%d-%m-%y'), '%Y-%m-%d') AS NEXT_DUE_DATE,CURRENT_STATUS,Mobile_No\n"
                + " FROM enach_old\n"
                + " )CD WHERE CD.`Application NUMBER` IS NOT NULL AND CD.PHONE_NUMBER  IS NOT NULL\n"
                + " AND LENGTH(CD.PHONE_NUMBER)=10  AND CD.`Application NUMBER` LIKE '"+applicationNo+"' \n";



        sql=quary;

        return sql;
    }
}

