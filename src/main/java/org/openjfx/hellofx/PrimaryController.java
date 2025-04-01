package org.openjfx.hellofx;

import java.time.LocalDate;
import java.time.LocalTime;

public class PrimaryController {
    public static void main(String[] args) {
        LocalDate date = LocalDate.now();
        LocalTime time = LocalTime.now();
        System.out.println("Date: " + date + ", Time: " + time);
    }
}
