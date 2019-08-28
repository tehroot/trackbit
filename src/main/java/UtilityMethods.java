import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.cli.*;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
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

    public static JsonNode settingsRead(){
        try{
            File file = new File(UtilityMethods.class.getResource("/settings.json").getFile());
            FileReader reader = new FileReader(file.getAbsoluteFile());
            ObjectMapper mapper = new ObjectMapper();
            JsonNode node = mapper.readTree(reader);
            return node;
        } catch(IOException e){
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public static byte[] generateSalt(){
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[64];
        random.nextBytes(salt);
        return salt;
    }

    public static ArrayList<byte[]> PBKDF2(String password, byte[] salt){
        ArrayList<byte[]> result = new ArrayList<>();
        result.add(salt);
        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 100000, 512);
        try{
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512");
            byte[] hash = factory.generateSecret(spec).getEncoded();
            result.add(hash);
            return result;
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e){
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public static Boolean validatePBKDF2(String password, byte[] storedSalt, byte[] storedHash){
        ArrayList<byte[]> list = PBKDF2(password, storedSalt);
        if(list.get(0) == storedHash) {
            return true;
        } else {
            return false;
        }
    }
}