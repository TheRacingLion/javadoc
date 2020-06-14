package view;

import java.text.*;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Used to facilitate the verification and validation of inputs from the user in order to guarantee that all the information obtained satisfies certain conditions.
 */
public final class Validator {
    /**
     * Verifies if the input is a valid date in the yyyy-MM-dd format
     * @param str The input to verify
     * @return {@code Boolean} indicating if the input is a valid date
     */
    public static boolean isDate(String str) {
        try {
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            format.setLenient(false);
            format.parse(str);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    /**
     * Similar to {@link #isDate(String)} but with the option to specify the minimal date the input can be.
     *
     * @see #isDate(String)
     *
     * @param str The input to verify
     * @param minDate The minimal date that the input can be
     * @return {@code Boolean} indicating if the input is a valid date and is bigger than the {@code minDate}
     */
    public static boolean isDate(String str, String minDate) {
        try {
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            format.setLenient(false);

            Date inputDate = format.parse(str);
            Date testDate = format.parse(minDate);

            return inputDate.after(testDate);
        } catch (ParseException e) {
            return false;
        }
    }

    /**
     * Verifies if the inputs length is lower or equal to a certain length
     * @param str The input to verify
     * @param length The maximum length the input can have
     * @return {@code Boolean} indicating if the inputs length is lower or equal to the {@code length} given.
     */
    public static boolean isLength(String str, int length) {
        return str.length() <= length;
    }

    /**
     * Verifies if the input is a decimal number
     * @param str The input to verify
     * @param size The maximum size the number can have (including decimals)
     * @param decimals The maximum number of decimals de number can hava
     * @return {@code Boolean} indicating if the input is a valid decimal number
     */
    public static boolean isDecimal(String str, int size, int decimals) {
        boolean isActualDecimal = str.contains(".");
        if (str.length() > (isActualDecimal ? size + 1 : size)) return false;
        String patternString = isActualDecimal ? "\\d{0," + (size - decimals) + "}\\.\\d{1," + decimals + "}" : "\\d{1," + size + "}";
        Pattern pattern = Pattern.compile(patternString);
        Matcher m = pattern.matcher(str);
        return m.matches();
    }

    /**
     * Verifies if the input is a integer number
     * @param str The input to verify
     * @param positiveOnly {@code Boolean} indicating if only positive numbers are valid
     * @return {@code Boolean} indicating if the input is a valid integer number
     */
    public static boolean isInteger(String str, boolean positiveOnly) {
        return str.matches(positiveOnly ? "[1-9]\\d*" : "-?(0|[1-9]\\d*)");
    }

    /** REGEX {@code String} for email verification */
    private static String EMAIL_STRING = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    /**
     * A java {@link Pattern} compiled from the {@link #EMAIL_STRING} regex
     *
     * @see Pattern
     */
    private static Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_STRING);

    /**
     * Verifies if the input is an email
     * @param email The input to verify
     * @return {@code Boolean} indicating if the input is a valid email
     */
    private static boolean isEmail(String email) {
        return EMAIL_PATTERN.matcher(email).matches();
    }

    /**
     * Verifies if the input is an phone number
     * @param phone The input to verify
     * @return {@code Boolean} indicating if the input is a valid phone number
     */
    private static boolean isPhoneNumber(String phone) {
        if (!Pattern.matches("[a-zA-Z]+", phone)) {
            return phone.length() > 6 && phone.length() <= 13;
        }
        return false;
    }

    /**
     * Verifies if the input is a valid email or phone number
     *
     * @see #isEmail(String)
     * @see #isPhoneNumber(String)
     *
     * @param str The input to verify
     * @return {@code Boolean} indicating if the input is a valid email or phone number
     */
    public static boolean isEmailOrPhone(String str) {
        return (isEmail(str) || isPhoneNumber(str));
    }
}
