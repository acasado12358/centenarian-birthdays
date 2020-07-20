package gov.llamaland.centenarianbirthdays;

import gov.llamaland.centenarianbirthdays.io.ReadInput;
import gov.llamaland.centenarianbirthdays.model.Citizen;
import gov.llamaland.centenarianbirthdays.process.DataProcessing;
import org.apache.commons.cli.*;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;

public class Main {

    public static void main(String[] args) {

        CommandLine cli = getCommandLine(args);

        String citizenFile = cli.getOptionValue("citizen");
        String optOutEmailFile = cli.getOptionValue("opt-out-emails");
        String date = cli.getOptionValue("date");
        LocalDate currentDate = null;
        if (date == null) {
            currentDate = LocalDate.now(ZoneOffset.UTC);
        } else {
            currentDate = LocalDate.parse(date);
        }

        // Read inputs
        List<Citizen> citizens = null;
        List<String> optOutEmails = null;
        try {
            citizens = ReadInput.readCitizens(citizenFile);
            optOutEmails = ReadInput.readOptOutEmail(optOutEmailFile);
        } catch (IOException e) {
            System.err.println("Error reading file : " + e.getMessage());
            System.exit(1);
        }

        // Process the data
        List<Citizen> emailList = DataProcessing
                .processData(citizens, optOutEmails, currentDate);

        // Output to print stream
        emailList
                .stream()
                .map(Citizen::formatForOutput)
                .forEach(System.out::println);
    }

    public static CommandLine getCommandLine(String[] args) {
        Options options = new Options();

        Option citizen = new Option("c", "citizen", true, "Input file with list of citizens");
        citizen.setRequired(true);
        options.addOption(citizen);

        Option optOut = new Option("o", "opt-out-emails", true, "File with emails who have opted out.");
        optOut.setRequired(true);
        options.addOption(optOut);

        Option date = new Option("d", "date", true, "override current date (format yyyy-mm-dd)");
        date.setRequired(false);
        options.addOption(date);

        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();
        CommandLine cmd = null;

        try {
            cmd = parser.parse(options, args);
        } catch (ParseException e) {
            System.err.println(e.getMessage());
            formatter.printHelp("centenarian-birthdays", options);

            System.exit(1);
        }

        return cmd;
    }
}
