package com.kuehnenagel.crypto.date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class DateServiceTest {

    @Spy
    private DateService dateService;

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this).close();
    }

    @Test
    void shouldReturnCorrectDateForFiveDaysAgo() {
        Instant now = Instant.parse("2013-09-06T10:15:30.00Z");
        Instant fiveDaysBack = Instant.parse("2013-09-02T10:15:30.00Z");
        when(dateService.getCurrentDate()).thenReturn(now);
        Instant actual = dateService.getDateForDaysBack(4);
        assertEquals(fiveDaysBack, actual);
    }

}