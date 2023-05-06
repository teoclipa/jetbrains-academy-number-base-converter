package converter;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.print("Enter two numbers in format: {source base} {target base} (To quit type /exit) ");
            String inputLine = scanner.nextLine();
            if (inputLine.equals("/exit")) {
                break;
            }
            String sourceBase = inputLine.split(" ")[0];
            String targetBase = inputLine.split(" ")[1];

            while (true) {
                System.out.print("Enter number in base " + sourceBase + " to convert to base " + targetBase + " (To go back type /back) ");
                String sourceNumber = scanner.nextLine();
                if (sourceNumber.equals("/back")) {
                    break;
                }
                String[] parts = sourceNumber.split("\\.");
                BigDecimal integerPart = convertToDecimal(parts[0], Integer.parseInt(sourceBase));
                BigDecimal fractionalPart = BigDecimal.ZERO;
                if (parts.length > 1) {
                    fractionalPart = convertFractionalToDecimal(parts[1], Integer.parseInt(sourceBase));
                }
                String result = convertFromDecimal(integerPart, Integer.parseInt(targetBase)) + convertFractionalFromDecimal(fractionalPart, Integer.parseInt(targetBase));
                System.out.println("Conversion result: " + result);
            }

        }

    }

    private static String convertFractionalFromDecimal(BigDecimal decimalNumber, int base) {
        if (decimalNumber.compareTo(BigDecimal.ZERO) == 0) {
            return "";
        }
        StringBuilder result = new StringBuilder(".");
        BigDecimal baseDecimal = BigDecimal.valueOf(base);
        int digits = 0;
        while (digits < 5) {
            decimalNumber = decimalNumber.multiply(baseDecimal);
            int digit = decimalNumber.intValue();
            if (digit < 10) {
                result.append(digit);
            } else {
                result.append((char) ('A' + digit - 10));
            }
            decimalNumber = decimalNumber.subtract(BigDecimal.valueOf(digit));
            digits++;
        }
        return result.toString();
    }


    private static BigDecimal convertFractionalToDecimal(String sourceNumber, int sourceBase) {
        BigInteger numerator = new BigInteger(sourceNumber, sourceBase);
        BigInteger denominator = BigInteger.valueOf(sourceBase).pow(sourceNumber.length());
        return new BigDecimal(numerator)
                .divide(new BigDecimal(denominator), 5, RoundingMode.HALF_DOWN);
    }



    private static String convertFromDecimal(BigDecimal decimalNumber, int base) {
        if (decimalNumber.compareTo(BigDecimal.ZERO) == 0) {
            return "0";
        }

        StringBuilder result = new StringBuilder();
        while (decimalNumber.compareTo(BigDecimal.ZERO) > 0) {
            int remainder = decimalNumber.remainder(BigDecimal.valueOf(base)).intValue();
            if (remainder < 10) {
                result.append(remainder);
            } else {
                result.append((char) ('A' + remainder - 10));
            }
            decimalNumber = decimalNumber.divideToIntegralValue(BigDecimal.valueOf(base));
            decimalNumber = decimalNumber.setScale(5, RoundingMode.FLOOR);
        }
        return result.reverse().toString();
    }


    private static BigDecimal convertToDecimal(String sourceNumber, int sourceBase) {
        BigDecimal result = BigDecimal.ZERO;
        BigDecimal base = BigDecimal.valueOf(sourceBase);

        for (int i = 0; i < sourceNumber.length(); i++) {
            int digit = digitValue(sourceNumber.charAt(i));
            BigDecimal power = base.pow(sourceNumber.length() - 1 - i);
            BigDecimal value = BigDecimal.valueOf(digit).multiply(power);
            result = result.add(value);
        }
        return result;
    }


    private static int digitValue(char digit) {
        if (digit >= '0' && digit <= '9') {
            return digit - '0';
        }
        if (digit >= 'a' && digit <= 'z') {
            return digit - 'a' + 10;
        }
        if (digit >= 'A' && digit <= 'Z') {
            return digit - 'A' + 10;
        }
        throw new IllegalArgumentException("Invalid digit: " + digit);
    }
}