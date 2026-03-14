package com.amazon.utils;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;

/**
 * @Author: Rajat
 * @Desc: This class provides common utility functions for test automation
 */

public class CommonFunction {

    static Random random;

    /**
     * @Author: Rajat
     * @Desc: Used to get the random digits between max and min
     * @param max
     * @param min
     * @return String with range between max and min
     */
    
    public static String randomDigit(long max, long min) {
        long randomDigit = (long) Math.floor(Math.random() * (max - min - 1)) + min;
        return String.valueOf(randomDigit);
    }

    /**
     * @Author: Rajat
     * @Desc: Used to get the random name with length
     * @param sizeOfName
     * @return String
     */
    
    public static String randomName(int sizeOfName) {
        random = new Random();
        String alphabet = "abcdefghijklmnopqrstuvwxyz";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < sizeOfName; i++) {
            sb.append(alphabet.charAt(random.nextInt(alphabet.length())));
        }
        String result = sb.toString();
        return result.substring(0, 1).toUpperCase() + result.substring(1);
    }

    /**
     * @Author: Rajat
     * @Desc: Used to get the random digits with length
     * @param numOfDigit
     * @return String
     */
    
    public static String randomDigits(int numOfDigit) {
        random = new Random();
        String number = "0123456789";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < numOfDigit; i++) {
            sb.append(number.charAt(random.nextInt(number.length())));
        }
        return sb.toString();
    }

    /**
     * @Author: Rajat
     * @Desc: Used to get random phone number
     * @return String
     */
    
    public static String getRandomPhoneNumber() {
        return "0" + CommonFunction.randomDigit(9999999999L, 6000000000L);
    }

    /**
     * @Author: Rajat
     * @Desc: Used to get the birth year based on age
     * @param age
     * @return String
     */
    
    public static String getBirthYear(int age) {
        Calendar now = Calendar.getInstance();
        now.add(Calendar.YEAR, -age);
        return String.valueOf(now.get(Calendar.YEAR));
    }

    /**
     * @Author: Rajat
     * @Desc: Used to get date of birth map by age
     * @param age
     * @return Map<String, String>
     */
    
    public static Map<String, String> getBirthdayByAge(int age) {
        Map<String, String> day = new LinkedHashMap<>();
        Calendar now = Calendar.getInstance();
        now.add(Calendar.YEAR, -age);
        day.put("date", String.valueOf(now.get(Calendar.DATE)));
        day.put("month", new SimpleDateFormat("MMM").format(now.getTime())); // ✅ Fixed
        day.put("year", String.valueOf(now.get(Calendar.YEAR)));
        return day;
    }

    /**
     * @Author: Rajat
     * @Desc: Used to get random OTP (4 digits)
     * @return String
     */
    
    public static String getRandomOTP() {
        return CommonFunction.getRandomDigitWithLength(4);
    }

    /**
     * @Author: Rajat
     * @Desc: Used to get random CCN number
     * @return String
     */
    
    public static String getRandomCCN() {
        return "CCN" + CommonFunction.getRandomDigitWithLength(7);
    }

    /**
     * @Author: Rajat
     * @Desc: Used to get random reference number
     * @return String
     */
    
    public static String getRandomRefNumber() {
        return "REF" + CommonFunction.getRandomDigitWithLength(7);
    }

    /**
     * @Author: Rajat
     * @Desc: Used to get random number using unique prefix
     * @Note: Unique prefix should be based on jobType like
     *        Document (TESTDC), credit card (TESTCC), RP (TESTRP)
     * @param uniquePrefix
     * @return String with prefix
     */
    
    public static String getRandomNumberUsingPrefix(String uniquePrefix) {
        return uniquePrefix + getRandomDigitWithLength(7);
    }

    /**
     * @Author: Rajat
     * @Desc: Used to get random number with exact digit length (1 to 19+)
     * @param numOfDigit
     * @return String
     */
    
    public static String getRandomDigitWithLength(int numOfDigit) {
        if (numOfDigit <= 19 && numOfDigit > 0) {
            long max = (long) (Math.pow(10, numOfDigit) - 1);
            long min = (long) Math.pow(10, (numOfDigit - 1));
            return CommonFunction.randomDigit(max, min);
        } else if (numOfDigit > 19) {
            return getRandomDigitWithLength(19) + getRandomDigitWithLength(numOfDigit - 19);
        } else {
            throw new NumberFormatException("Negative or Zero digit number!!!");
        }
    }

    /**
     * @Author: Rajat
     * @Desc: Used to check if the given value is a valid integer
     * @param number
     * @return boolean
     */
    
    public static boolean checkRandomIntegerNumber(String number) {
        try {
            Integer.parseInt(number);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * @Author: Rajat
     * @Desc: Used to get random alphanumeric string
     * @param size
     * @return String
     */
    
    public static String randomAlphaNumeric(int size) {
        random = new Random();
        String chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < size; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }
        return sb.toString();
    }

    /**
     * @Author: Rajat
     * @Desc: Used to get random email (useful for registration tests)
     * @return String
     */
    
    public static String getRandomEmail() {
        return randomName(6).toLowerCase() + getRandomDigitWithLength(4) + "@test.com";
    }

    /**
     * @Author: Rajat
     * @Desc: Used to get random password
     * @return String
     */
    
    public static String getRandomPassword() {
        return "Pass@" + getRandomDigitWithLength(5);
    }

}
