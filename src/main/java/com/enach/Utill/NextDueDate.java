package com.enach.Utill;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;



public class NextDueDate {

    public static String findNextDueDate(String nextDueDate){

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);


        LocalDate dueDate = LocalDate.parse(nextDueDate);
        int cycleDate = dueDate.getDayOfMonth();
        int currentDay = calendar.get(Calendar.DAY_OF_MONTH);

        if (cycleDate == 4) {

            System.out.println(currentDay);

            calendar.set(Calendar.DAY_OF_MONTH, 4);
            if (currentDay > 4) {
                calendar.set(Calendar.MONTH, month + 1);
            }
        }

        if (cycleDate == 10) {

            System.out.println(currentDay);

            calendar.set(Calendar.DAY_OF_MONTH, 10);
            if (currentDay > 10) {
                calendar.set(Calendar.MONTH, month + 1);
            }
        }

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String next4thDate = formatter.format(calendar.getTime());
        System.out.println("Next 4th day of the month: " + next4thDate);
        System.err.println(calendar.getTime());


        return next4thDate;
    }
}
