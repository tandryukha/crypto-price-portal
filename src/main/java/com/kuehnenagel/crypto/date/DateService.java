package com.kuehnenagel.crypto.date;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

public class DateService {
    public Instant getCurrentDate() {
        return Instant.now();
    }

    public Instant getDateForDaysBack(int daysBack) {
        return getCurrentDate().minus(daysBack, ChronoUnit.DAYS);
    }
}
