package fenrirmma.hreysti_app.login.ssnValidation;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

public final class Validator {
    private Validator() { }

    public static boolean isValidSSN(String ssn) {
        if (ssn == null) return false;
        ssn = ssn.replaceAll("[^0-9]", "");
        if (ssn.length() != 10) return false;
        int century = charToInt(ssn.charAt(9));

        return (century == 0 || century == 9)
                &&
                validDate(String.format(
                        Locale.US,
                        "%d/%d/%d",
                        Integer.parseInt(ssn.substring(0, 2)),
                        Integer.parseInt(ssn.substring(2, 4)),
                        Integer.parseInt(String.format("%s%s", "19", ssn.substring(4, 6)))
                ))
                &&
                (11 - (checkSum(ssn) % 11)) % 11 == charToInt(ssn.charAt(8));

    }

    private static boolean validDate(String dateFormat) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        sdf.setLenient(false);
        try {
            sdf.parse(dateFormat);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    private static int checkSum(String ssn) {
        return charToInt(ssn.charAt(0)) * 3
                + charToInt(ssn.charAt(1)) * 2
                + charToInt(ssn.charAt(2)) * 7
                + charToInt(ssn.charAt(3)) * 6
                + charToInt(ssn.charAt(4)) * 5
                + charToInt(ssn.charAt(5)) * 4
                + charToInt(ssn.charAt(6)) * 3
                + charToInt(ssn.charAt(7)) * 2;
    }

    private static int charToInt(char c) {
        return c - '0';
    }
}
