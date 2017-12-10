package fenrirmma.hreysti_app.login.ssnValidation;

/**
 * Created by Copyright (c) 2014 Hilmar Ævar Hilmarsson
 * https://github.com/hilmarh/java-kennitala
 */

public final class KennitalaValidator {

    /**
     * This class cannot be constructed.
     */
    private KennitalaValidator() {
    }

    /**
     * Check whether a kennitala is valid using number derivation.
     * (http://en.wikipedia.org/wiki/Kennitala).
     *
     * <p>
     *   The check will check whether the ninth number of the kennitala
     *   matches the following formula:
     * </p>
     *
     * <p>
     *   y = (x1 * 3) + (x2 * 2) + (x3 * 7) + (x4 * 6) + (x5 * 5) + (x6 * 4) + (x7 * 3) + (x8 * 2)
     *   j = y % 11
     * </p>
     *
     * <p>
     *   Where x is the kennitala and j should match the ninth number of the kennitala.
     * </p>
     *
     * @param kennitala A kennitala with or without the hyphen.
     * @return True if the kennitala passes the ninth digit check, else false.
     */
    public static boolean isValid(final String kennitala) {
        if (kennitala == null) {
            return false;
        }

        final String kennitalaClean = KennitalaUtil.cleanKennitala(kennitala);

        if (kennitalaClean.length() != 10) {
            return false;
        }

        int sum =
                (Integer.parseInt(kennitalaClean.substring(0, 1)) * 3)
                        + (Integer.parseInt(kennitalaClean.substring(1, 2)) * 2)
                        + (Integer.parseInt(kennitalaClean.substring(2, 3)) * 7)
                        + (Integer.parseInt(kennitalaClean.substring(3, 4)) * 6)
                        + (Integer.parseInt(kennitalaClean.substring(4, 5)) * 5)
                        + (Integer.parseInt(kennitalaClean.substring(5, 6)) * 4)
                        + (Integer.parseInt(kennitalaClean.substring(6, 7)) * 3)
                        + (Integer.parseInt(kennitalaClean.substring(7, 8)) * 2);

        int num = 11 - (sum % 11);
        num = (num == 11) ? 0 : num;

        return num == Integer.parseInt(kennitalaClean.substring(8, 9));
    }

    /**
     * Check whether the kennitala belongs to a company. If the first letter of the kennitala
     * is above 3 then it cannot possibly be a person.
     *
     * @param kennitala A kennitala with or without the hyphen.
     * @return Whether the kennitala belongs to a company.
     * @throws java.lang.IllegalArgumentException An invalid kennitala was provided.
     */
    public static boolean isCompany(final String kennitala) {
        if (KennitalaUtil.cleanKennitala(kennitala).length() < 10) {
            throw new IllegalArgumentException("Invalid kennitala");
        }

        return Integer.parseInt(kennitala.substring(0, 1)) > 3;
    }
}
