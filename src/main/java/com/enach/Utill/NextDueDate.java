package com.enach.Utill;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;



public class NextDueDate {

    public static String findNextDueDate(String nextDueDate){

        LocalDate date = LocalDate.parse(nextDueDate);
        int year=date.getYear();
        int month=date.getMonthValue()-1;
        int day=date.getDayOfMonth();


        Calendar calendar = Calendar.getInstance();

        calendar.set(year,month,day);
        int currentDay = calendar.get(Calendar.DAY_OF_MONTH);

        if (currentDay >= 1 && currentDay <= 4) {

            calendar.set(Calendar.DAY_OF_MONTH, 4);
        }
        else
        if(currentDay >= 5 && currentDay <= 10)
        {
            calendar.set(Calendar.DAY_OF_MONTH, 10);

        }

        else {
            calendar.set(Calendar.DAY_OF_MONTH, 4);
            calendar.set(Calendar.MONTH, month+1);

            // System.out.println("month");

        }
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String next4thDate = formatter.format(calendar.getTime());

        System.out.println("Next 4th day of the month: " + next4thDate);
        System.err.println(calendar.getTime());


        return next4thDate;
    }
}
