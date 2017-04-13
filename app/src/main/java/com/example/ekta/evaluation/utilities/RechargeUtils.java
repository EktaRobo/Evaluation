package com.example.ekta.evaluation.utilities;

import com.example.ekta.evaluation.constants.Constants;

/**
 * Created by ekta on 13/4/17.
 */

public class RechargeUtils {

    public static String getFormattedTenDigitMobileNumber(String phoneNumber) {
        String digitsOnlynum = null;
        if (phoneNumber != null) {
            phoneNumber = phoneNumber.replaceAll("\\D+", "");
            digitsOnlynum = removeNonDigits(phoneNumber);
            if (digitsOnlynum.length() > 10) {
                int start = digitsOnlynum.length() - 10;
                digitsOnlynum = digitsOnlynum.substring(start, digitsOnlynum.length());
            }
        }
        return digitsOnlynum;
    }

    private static String removeNonDigits(String inputString) {
        String result = inputString;
        if (result != null) {
            result = result.replaceAll("[^0-9]", "");
        }
        return result;
    }

    public static int getOperatorIndex(String firstFourDigits) {
        switch (firstFourDigits) {
            case Constants.MTNL_NUMBER:
                return Constants.MTNL_CODE;

            case Constants.DOCOMO_NUMBER:
                return Constants.DOCOMO_CODE;

            case Constants.AIRCEL_NUMBER:
                return Constants.AIRCEL_CODE;

            case Constants.BSNL_NUMBER:
                return Constants.BSNL_CODE;

            case Constants.JIO_NUMBER:
                return Constants.JIO_CODE;

            case Constants.IDEA_NUMBER:
                return Constants.IDEA_CODE;

            case Constants.VODAFONE_NUMBER:
                return Constants.VODAFONE_CODE;

            default:
                return Constants.AIRTEL_CODE;
        }
    }
}
