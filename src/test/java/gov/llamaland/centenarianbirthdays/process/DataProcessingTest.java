package gov.llamaland.centenarianbirthdays.process;

import gov.llamaland.centenarianbirthdays.model.Citizen;
import org.junit.Test;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static gov.llamaland.centenarianbirthdays.process.DataProcessing.*;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;

public class DataProcessingTest {

    @Test
    public void testCleanOptOutEmails() {
        Set<String> cleanedEmails = cleanOptOutEmail(
                Arrays.asList("a@b.com ", "a@b.com", "c@d.com", "e@f.com", "  e@f.com"));

        assertThat(cleanedEmails, hasItems("a@b.com", "c@d.com", "e@f.com"));
    }

    @Test
    public void testIsHundredYrs() {
        assert isHundredYrsOn(
                LocalDate.of(1920, 1, 1),
                LocalDate.of(2020, 1, 1)
        );

        assert !isHundredYrsOn(
                LocalDate.of(1920, 1, 2),
                LocalDate.of(2020, 1, 1)
        );
    }

    Citizen citizenWithDob(LocalDate dob) {
        return new Citizen("LN", "FN", dob, "email");
    }

    @Test
    public void testTurnsHundredOn() {
        Citizen citizen1 = citizenWithDob(LocalDate.of(1920, 1, 1));
        Citizen citizen2 = citizenWithDob(LocalDate.of(1920, 1, 2));
        Citizen citizen3 = citizenWithDob(LocalDate.of(1919, 1, 2));
        Citizen citizen4 = citizenWithDob(LocalDate.of(1920, 2, 1));
        List<Citizen> citizens = Arrays.asList(citizen1, citizen2, citizen3, citizen4);

        List<LocalDate> dates = Arrays.asList(LocalDate.of(2020, 1, 1), LocalDate.of(2020, 1, 2));
        List<Citizen> actual = turnsHundredOn(citizens, dates);

        assertThat(
                actual, hasItems(citizen1, citizen2)
        );
    }

    @Test
    public void testDatesForMail() {
        // 13th July 2020 is a Monday
        List<LocalDate> dates = datesForMail(LocalDate.of(2020, 7, 13), 5);
        assertThat(dates, hasItem(LocalDate.of(2020, 7, 18)));

        // 17th July we must notify all citizens that turn 100 on 17, 18, 19 since we will not run on 18th / 19th weekend.
        List<LocalDate> datesFriday = datesForMail(LocalDate.of(2020, 7, 17), 5);
        assertThat(datesFriday, hasItems(
                LocalDate.of(2020, 7, 22),
                LocalDate.of(2020, 7, 23),
                LocalDate.of(2020, 7, 24)
        ));
    }

    @Test
    public void testEmailEligibilityFor_simple() {
        Citizen citizen1 = citizenWithDob(LocalDate.of(1920, 7, 20));
        Citizen citizen2 = citizenWithDob(LocalDate.of(1920, 7, 21));
        Citizen citizen3 = citizenWithDob(LocalDate.of(1919, 7, 2));
        Citizen citizen4 = citizenWithDob(LocalDate.of(1920, 2, 1));
        List<Citizen> citizens = Arrays.asList(citizen1, citizen2, citizen3, citizen4);

        List<Citizen> actual = emailEligibililityFor(citizens, LocalDate.of(2020, 7, 15));
        System.out.println(actual);
        assertThat(
                actual, hasItem(citizen1)
        );
    }

    @Test
    public void testEmailEligibilityFor_weekends() {
        Citizen citizen0 = citizenWithDob(LocalDate.of(1920, 7, 20));
        Citizen citizen1 = citizenWithDob(LocalDate.of(1920, 7, 21));
        Citizen citizen2 = citizenWithDob(LocalDate.of(1920, 7, 22));
        Citizen citizen3 = citizenWithDob(LocalDate.of(1920, 7, 23));
        Citizen citizen4 = citizenWithDob(LocalDate.of(1920, 7, 24));
        Citizen citizen5 = citizenWithDob(LocalDate.of(1920, 7, 25));
        Citizen citizen6 = citizenWithDob(LocalDate.of(1920, 7, 26));
        List<Citizen> citizens = Arrays
                .asList(citizen0, citizen1, citizen2, citizen3, citizen4, citizen5, citizen6);

        List<Citizen> actual16 = emailEligibililityFor(citizens, LocalDate.of(2020, 7, 16));
        List<Citizen> actual17 = emailEligibililityFor(citizens, LocalDate.of(2020, 7, 17));
        List<Citizen> actual18 = emailEligibililityFor(citizens, LocalDate.of(2020, 7, 18));
        List<Citizen> actual19 = emailEligibililityFor(citizens, LocalDate.of(2020, 7, 19));
        List<Citizen> actual20 = emailEligibililityFor(citizens, LocalDate.of(2020, 7, 20));
        assertThat(
                actual16, hasItem(citizen1)
        );
        assertThat(
                actual17, hasItems(citizen2, citizen3, citizen4)
        );
        assertThat(
                actual18, empty()
        );
        assertThat(
                actual19, empty()
        );
        assertThat(
                actual20, hasItem(citizen5)
        );
    }
}
