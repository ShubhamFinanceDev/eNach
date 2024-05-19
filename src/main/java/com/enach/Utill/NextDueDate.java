package com.enach.Utill;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;



public class NextDueDate {

    public static LocalDate findNextDueDate(LocalDate currentDate) {

        LocalDate nextDueDate = null;
        int days = currentDate.getDayOfMonth();

        if (days < 4) {

            LocalDate dueDate = currentDate.withDayOfMonth(4);
            nextDueDate = dueDate;
        } else {
            LocalDate dueDate = currentDate.plusMonths(1).withDayOfMonth(4);
            nextDueDate = dueDate;
        }

        return nextDueDate;
    }
}
