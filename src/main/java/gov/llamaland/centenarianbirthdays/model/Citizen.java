package gov.llamaland.centenarianbirthdays.model;

import org.apache.commons.csv.CSVRecord;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;

public class Citizen {

    /* Constants for parsing from CSV */
    private static final int LAST_NAME_INDEX = 0;
    private static final int FIRST_NAME_INDEX = 1;
    private static final int DOB_INDEX = 2;
    private static final int EMAIL_INDEX = 3;
    private static final DateTimeFormatter DOB_FORMAT = new DateTimeFormatterBuilder()
            .appendValue(ChronoField.DAY_OF_MONTH)
            .appendLiteral("-")
            .appendValue(ChronoField.MONTH_OF_YEAR)
            .appendLiteral("-")
            .appendValue(ChronoField.YEAR)
            .toFormatter();
    private final String lastName;
    private final String firstName;
    private final LocalDate dob;
    private final String email;

    public Citizen(String lastName, String firstName, LocalDate dob, String email) {
        this.lastName = lastName;
        this.firstName = firstName;
        this.dob = dob;
        this.email = email;
    }

    public static Citizen fromCSV(CSVRecord csvRecord) {

        LocalDate dob =
                LocalDate.parse(
                        csvRecord.get(DOB_INDEX),
                        DOB_FORMAT
                );

        return new Citizen(
                csvRecord.get(LAST_NAME_INDEX),
                csvRecord.get(FIRST_NAME_INDEX),
                dob,
                csvRecord.get(EMAIL_INDEX)
        );
    }

    public String getLastName() {
        return lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public LocalDate getDob() {
        return dob;
    }

    public String getEmail() {
        return email;
    }

    public String formatForOutput() {
        String DELIMITER = ",";
        StringBuilder sb = new StringBuilder();
        sb.append(getLastName());
        sb.append(DELIMITER);
        sb.append(getFirstName());
        sb.append(DELIMITER);
        sb.append(getEmail());
        return sb.toString();
    }
}
