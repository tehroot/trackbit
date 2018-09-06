import org.apache.commons.cli.*;

import java.util.ArrayList;

public class UtilityMethods {
    public static ArrayList commandLineArgs(String[] args){
        Options options = new Options();
        Option input = new Option("u", "user", true, "database user required");
        Option password = new Option("p", "password", true, "database user password require");
        Option databaseName = new Option("n", "databaseName", true, "databse name required");

        input.setRequired(true);
        password.setRequired(true);
        databaseName.setRequired(true);

        options.addOption(input);
        options.addOption(password);
        options.addOption(databaseName);


        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();
        CommandLine cmd;

        try {
            cmd = parser.parse(options, args);
            String usernameDatabase = cmd.getOptionValue("user");
            String passwordDatabase = cmd.getOptionValue("password");
            String database = cmd.getOptionValue("databaseName");
            ArrayList<String> arguments = new ArrayList();
            arguments.add(usernameDatabase);
            arguments.add(passwordDatabase);
            arguments.add(database);
            return arguments;

        } catch (ParseException e){
            System.out.println(e.getMessage());
            formatter.printHelp("ARGUMENT", options);
            System.exit(1);
        }
        return null;
    }
}
