package com.mindhub.homebanking.utils;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Random;

public final class CardUtils {

    private CardUtils(){}

    public static String generateCreditNumber () {
        SecureRandom random = new SecureRandom();
        BigInteger maxLimit = new BigInteger("10000000000000000");
        BigInteger randomNumber = new BigInteger(maxLimit.bitLength(), random);

        // me aseguro de que el número generado sea menor que el límite máximo
        while (randomNumber.compareTo(maxLimit) >= 0) {
            randomNumber = new BigInteger(maxLimit.bitLength(), random);
        }

        return randomNumber.toString();
    }


    public static int generateCvv () {
        Random random = new Random();
        int randomNumber = random.nextInt(999);
        return randomNumber;
    }



    public static String generateDebitNumber () {
        SecureRandom random = new SecureRandom();
        BigInteger maxLimit = new BigInteger("10000000000000000");
        BigInteger randomNumber = new BigInteger(maxLimit.bitLength(), random);

        // me aseguro de que el número generado sea menor que el límite máximo
        while (randomNumber.compareTo(maxLimit) >= 0) {
            randomNumber = new BigInteger(maxLimit.bitLength(), random);
        }

        return randomNumber.toString();
    }

}
