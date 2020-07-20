package gov.llamaland.centenarianbirthdays.io;

import gov.llamaland.centenarianbirthdays.model.Citizen;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.stream.Collectors.toList;

public class ReadInput {

    public static List<Citizen> readCitizens(String citizenCsv) throws IOException {
        return readCitizens(Paths.get(citizenCsv));
    }

    public static List<Citizen> readCitizens(Path citizenCsv) throws IOException {
        return CSVParser
                .parse(citizenCsv, Charset.defaultCharset(), CSVFormat.DEFAULT)
                .getRecords()
                .stream()
                .map(Citizen::fromCSV)
                .filter(c -> validEmail(c.getEmail()))
                .collect(toList());
    }

    public static List<String> readOptOutEmail(String optOutEmails) throws IOException {
        return readOptOutEmail(Paths.get(optOutEmails));
    }

    public static List<String> readOptOutEmail(Path optOutEmails) throws IOException {
        return Files.readAllLines(optOutEmails)
                .stream()
                .filter(c -> validEmail(c))
                .collect(toList());
    }

    public static Boolean validEmail(String mail) {
        String regex = "^(.+)@(.+)$";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(mail);
        return matcher.matches() ? Boolean.TRUE : Boolean.FALSE;
    }
}
