package gov.llamaland.centenarianbirthdays.process;

import gov.llamaland.centenarianbirthdays.model.Citizen;

import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.time.DayOfWeek.*;
import static java.util.stream.Collectors.*;

public class DataProcessing {

    private static final int MIN_CITIZENS_FOR_10_DAY_NOTICE = 20;

    static Set<String> cleanOptOutEmail(List<String> rawOptOutEmails) {
        return rawOptOutEmails.stream()
                .map(String::trim) // Remove black spaces
                .collect(toSet()); // Convert to Set to remove duplicates.
    }


    static boolean isHundredYrsOn(LocalDate dob, LocalDate date) {
        int ageInYears = Period.between(dob, date).getYears();
        return ageInYears == 100 && dob.getMonth() == date.getMonth() && dob.getDayOfMonth() == date
                .getDayOfMonth(); // Citizens who turn 100 on the given date
    }

    /**
     * Filters the given list of [[Citizen]] and returns only those who would turn 100 on any of the
     * given dates.
     */
    static List<Citizen> turnsHundredOn(List<Citizen> citizens, List<LocalDate> dates) {
        return citizens.stream()
                .filter(citizen ->
                        dates.stream().anyMatch(d -> isHundredYrsOn(citizen.getDob(), d))
                )
                .collect(toList());
    }

    /**
     * For a given date and the given days of notice, returns all the dates for which mails should be
     * sent
     */
    static List<LocalDate> datesForMail(LocalDate currentDate, int daysNotice) {
        List<LocalDate> birthdays = new ArrayList<>();
        birthdays
                .add(currentDate.plus(daysNotice, ChronoUnit.DAYS)); // Birthdays that appear in 5 days
        if (currentDate.getDayOfWeek() == FRIDAY) {
            // We must mail for SATURDAY and SUNDAY as well
            birthdays.add(currentDate.plus(daysNotice + 1, ChronoUnit.DAYS));
            birthdays.add(currentDate.plus(daysNotice + 2, ChronoUnit.DAYS));
        }
        return birthdays;
    }

    static boolean isWeekDay(LocalDate date) {
        return date.getDayOfWeek() != SATURDAY && date.getDayOfWeek() != SUNDAY;
    }

    /**
     * Finds all Citizens who should be emailed on the given date. This produces a list of citizens
     * who turn 100 in 5 days and if that number if more than 20, then produces the list of citizens
     * 10 days in advance.
     * <p>
     * Assumptions:
     * <p>
     * - This considers only the date and weekend when citizens turn 100 to determine eligibility.
     * <p>
     * - The total number of citizens tuning 100 on consecutive Wed, Thr, Fri are all counted for
     * email on the last weekday of the previous week.
     * <p>
     * - Ten days notice is given only if more than 20 people turn 100 on a given date of a given
     * consecutive Wed, Thr, Fri in a given week.
     * <p>
     * - This does NOT consider the backlog of work when determining eligibility. Example: If there
     * are 25 citizens turning 100 on 24th of July (Friday) and 3 citizens turning 100 on 19th of
     * July(Sunday) they are added to the list on 14th of July (Tuesday) Hence the list for 14th of
     * July contains 28 citizens, however 3 of them should receive a mail in 5 days. This is based off
     * the problem statement where the king expects 10 weekdays for a given date only.
     */
    static List<Citizen> emailEligibililityFor(List<Citizen> citizens, LocalDate currentDate) {

        List<LocalDate> bithDaysIn5WeekDays = datesForMail(currentDate, 5);
        List<LocalDate> bithDaysIn10WeekDays = datesForMail(currentDate, 10);

        List<Citizen> eligibleIn5Days = turnsHundredOn(citizens, bithDaysIn5WeekDays);
        List<Citizen> eligibleIn10Days = turnsHundredOn(citizens, bithDaysIn10WeekDays);

        List<Citizen> eligibleCitizens = new ArrayList<>();

        if (eligibleIn10Days.size() >= MIN_CITIZENS_FOR_10_DAY_NOTICE && isWeekDay(currentDate)) {
            // A lot of citizens would be eligible 10 days later, hence we add them today.
            eligibleCitizens.addAll(eligibleIn10Days);
        }
        if (eligibleIn5Days.size() < MIN_CITIZENS_FOR_10_DAY_NOTICE && isWeekDay(currentDate)) {
            // We only add citizens as eligible if they were not already added as part of the 10 days logic.
            eligibleCitizens.addAll(eligibleIn5Days);
        }
        return eligibleCitizens;
    }

    static Boolean validEmail(String mail) {
        String regex = "^(.+)@(.+)$";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(mail);
        return matcher.matches() ? Boolean.TRUE : Boolean.FALSE;
    }

    static List<Citizen> optInCitizens(List<Citizen> citizens, Set<String> optOutEmails) {
        return citizens
                .stream()
                .collect(groupingBy(Citizen::getEmail)) // Create groups by Email to remove duplicate
                .values().stream()
                .map(cs -> cs.get(0)) // Choose only the first Citizen object for the given group.
                .filter(c -> !optOutEmails
                        .contains(c.getEmail())) // Remove citizens appearing in opt out emails.
                .collect(toList());
    }

    public static List<Citizen> processData(List<Citizen> citizens, List<String> optOutEmails,
                                            LocalDate today) {
        List<Citizen> optInCitizens = optInCitizens(citizens, cleanOptOutEmail(optOutEmails));

        return emailEligibililityFor(optInCitizens, today);
    }

}
