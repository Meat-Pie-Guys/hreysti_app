package fenrirmma.hreysti_app.login.ssnValidation;

/**
 * Created by Copyright (c) 2014 Hilmar Ævar Hilmarsson
 * https://github.com/hilmarh/java-kennitala
 */

import java.util.Calendar;
import java.util.GregorianCalendar;

public final class KennitalaUtil {
    /**
     * Get a kennitala in a 10 letter format without the hyphen.
     *
     * @param kennitala The kennitala.
     * @return The kennitala in a 10 letter format.
     */
    public static String cleanKennitala(final String kennitala) {
        return kennitala.replaceAll("[^0-9]", "");
    }

    /**
     * Get a kennitala in a 11 letter format with a hyphen.
     *
     * @param kennitala The kennitala.
     * @return The kennitala in a 11 letter format.
     */
    public static String kennitala11(final String kennitala) {
        final String kennitalaClean = cleanKennitala(kennitala);
        return kennitalaClean.substring(0, 6) + "-" + kennitalaClean.substring(6, 10);
    }

    /**
     * Create a new legal and valid kennitala which passes the ninth letter checks.
     *
     * @param day   The day on which the person is born.
     * @param month The month in which the person is born.
     * @param year  The year on which the person is born.
     * @return A legal and valid kennitala which passes the ninth letter checks.
     */
    public static String fromBirthday(final int day, final int month, final int year) {
        final StringBuilder kennitala = new StringBuilder();

        // Construct the first 6 letters of the kennitala which contain DDMMYY.
        kennitala.append(String.format("%02d", day));
        kennitala.append(String.format("%02d", month));
        kennitala.append(String.valueOf(year).substring(2, 4));

        // Create two completely random numbers which compose the seventh and the eight
        // letters of the kennitala.
        kennitala.append(String.format("%02d", (((int) (Math.random() * 100)))));

        // Create the ninth number which will be used to check if the kennitala is valid.
        final int sum =
                (Integer.parseInt(kennitala.substring(0, 1)) * 3)
                        + (Integer.parseInt(kennitala.substring(1, 2)) * 2)
                        + (Integer.parseInt(kennitala.substring(2, 3)) * 7)
                        + (Integer.parseInt(kennitala.substring(3, 4)) * 6)
                        + (Integer.parseInt(kennitala.substring(4, 5)) * 5)
                        + (Integer.parseInt(kennitala.substring(5, 6)) * 4)
                        + (Integer.parseInt(kennitala.substring(6, 7)) * 3)
                        + (Integer.parseInt(kennitala.substring(7, 8)) * 2);

        int num = 11 - (sum % 11);
        num = (num == 11) ? 0 : num;

        // The ninth number cannot be 10. Then the kennitala will be invalid, we try again.
        if (num == 10) {
            return fromBirthday(day, month, year);
        }

        // We append the ninth letter of the kennitala.
        kennitala.append(num);

        // We construct the tenth letter of the kennitala. This letter tells us in which
        // millenium this kennitala is born.
        final int millenium = year / 100;

        switch (millenium) {
            case 18:
                kennitala.append("8");
                break;
            case 19:
                kennitala.append("9");
                break;
            case 20:
                kennitala.append("0");
                break;
            default:
                kennitala.append(String.valueOf(millenium));
                break;
        }

        return kennitala.toString();
    }

    /**
     * Build a completely random, legal and valid kennitala which passes ninth letter checks. This
     * kennitala will be born somewhere between the date today and first of January 1800.
     *
     * @return A random kennitala.
     */
    public static String random() {
        final Calendar gc = new GregorianCalendar();

        // Build a year somewhere between 1800 and the year today.
        final int year = 1800 + ((int) Math.round(Math.random() * (gc.get(gc.YEAR) - 1800)));

        // Build a day somewhere between 1 and 365.
        final int day = 1
                + (int) Math.round(Math.random()
                * (gc.getActualMaximum(gc.DAY_OF_YEAR) - 1));

        // Construct a birthdate.
        gc.set(gc.YEAR, year);
        gc.set(gc.DAY_OF_YEAR, day);

        // Create and return a new kennitala from this birthdate.
        return fromBirthday(gc.get(gc.DAY_OF_MONTH), gc.get(gc.MONTH) + 1, gc.get(gc.YEAR));
    }

    /**
     * Calculate the age of a kennitala. The age will be 0 if the kennitala is invalid.
     *
     * @param kennitala The kennitala, with or without the hyphen.
     * @return The age of a kennitala.
     */
    public static int age(final String kennitala) {
        final String kennitalaClean = cleanKennitala(kennitala);

        if (!KennitalaValidator.isValid(kennitalaClean)) {
            return 0;
        }
        // Parse the kennitala for the day, month and year it is born.
        final int day = Integer.parseInt(kennitalaClean.substring(0, 2));
        final int month = Integer.parseInt(kennitalaClean.substring(2, 4));
        final int year;

        // Check in which millenium the kennitala is born.
        switch (kennitalaClean.charAt(9)) {
            case '8':
                year = Integer.parseInt("18" + kennitalaClean.substring(4, 6));
                break;
            case '9':
                year = Integer.parseInt("19" + kennitalaClean.substring(4, 6));
                break;
            case '0':
                year = Integer.parseInt("20" + kennitalaClean.substring(4, 6));
                break;
            default:
                year = Integer.parseInt("00" + kennitalaClean.substring(4, 6));
                break;
        }

        // Set the birthday of this kennitala, rememering that the Java Date API begins months on 0.
        final Calendar birthday = new GregorianCalendar();

        birthday.set(year, month - 1, day);
        birthday.set(Calendar.HOUR_OF_DAY, 0);
        birthday.set(Calendar.MINUTE, 0);
        birthday.set(Calendar.SECOND, 0);
        birthday.set(Calendar.MILLISECOND, 0);

        // Get todays date.
        final Calendar now = new GregorianCalendar();
        now.set(Calendar.HOUR_OF_DAY, 0);
        now.set(Calendar.MINUTE, 0);
        now.set(Calendar.SECOND, 0);
        now.set(Calendar.MILLISECOND, 0);

        // Set the age by comparing the years.
        int age = now.get(Calendar.YEAR) - birthday.get(Calendar.YEAR);

        // Fine tune the age by comparing both the month and the day of each object.
        if (now.get(Calendar.MONTH) < birthday.get(Calendar.MONTH)) {
            age--;
        } else if (now.get(Calendar.MONTH) == birthday.get(Calendar.MONTH)
                && now.get(Calendar.DAY_OF_MONTH) < birthday.get(Calendar.DAY_OF_MONTH)) {
            age--;
        }

        return age;
    }
}
