package chucknorris;

import java.util.Objects;
import java.util.Scanner;

public class UserInterface {
    private final Scanner scanner;

    public UserInterface(Scanner scanner) {
        this.scanner = scanner;
    }

    public void run() {
        System.out.println("Please input operation (encode/decode/exit):");
        makeRequest(scanner.nextLine());
    }

    private void makeRequest(String input) {
        switch (input) {
            case "encode" -> encodeFunc();
            case "decode" -> decodeFunc();
            case "exit" -> System.out.println("Bye!");
            default -> {
                System.out.println("There is no '" + input + "' operation\n");
                run();
            }
        }
    }

    private void decodeFunc() {
        System.out.println("Input encoded string:");

        String input = scanner.nextLine();

        if (checkDecodeInput(input)) {
            System.out.println(fromBinary(fromChuckNorris(input).toString()));
        } else {
            System.out.println("Encoded string is not valid.");
        }
        System.out.println();
        run();
    }

    /**
     * List of not valid encoded messages:
     * <p>
     * The encoded message includes characters other than 0 or spaces;
     * The first block of each sequence is not 0 or 00;
     * The number of blocks is odd;
     * The length of the decoded binary string is not a multiple of 7.
     */
    private boolean checkDecodeInput(String input) {
        // 0 0 00 00 0 0 00 0000 0 0 00 00000 0 0 00 0 0 0 00 0 0 000 00 0 0 0 00 0000 0 0
        if (!input.matches("[0|\\s]+")) return false;

        String[] strings = input.split(" ");

        if (strings.length % 2 != 0) return false;

        for (int i = 0; i < strings.length; i = i + 2) {
            if (!Objects.equals(strings[i], "0") && !Objects.equals(strings[i], "00")) {
                return false;
            }
        }

        return fromChuckNorris(input).length() % 7 == 0;
    }

    private void encodeFunc() {
        System.out.println("Input string:");
        System.out.println(toChuckNorris(toBinary(scanner.nextLine())));
        System.out.println();
        run();
    }

    private StringBuilder fromBinary(String input) {
        StringBuilder msgBuilder = new StringBuilder("Decoded string:\n");

        // Array by 7 digits
        String[] strings = input.split("(?<=\\G.{7})");

        for (String string : strings) {
            msgBuilder.append((char) Integer.parseInt(string, 2));
        }

        return msgBuilder;
    }

    private StringBuilder fromChuckNorris(String input) {
        StringBuilder msgBuilder = new StringBuilder();
        String[] strings = input.split(" ");

        for (int i = 0; i < strings.length; i = i + 2) {

            for (int j = 0; j < strings[i + 1].length(); j++) {
                if (Objects.equals(strings[i], ("0"))) {
                    msgBuilder.append("1");
                } else {
                    msgBuilder.append("0");
                }
            }
        }

        return msgBuilder;
    }

    private StringBuilder toChuckNorris(String input) {
        char[] chars = input.toCharArray();
        StringBuilder msgBuilder = new StringBuilder("Encoded string:\n");
        boolean newSerial = true;

        for (int i = 0; i < chars.length; i++) {
            if (newSerial) {
                if (i != 0) {
                    msgBuilder.append(" ");
                }
                if (chars[i] == '1') {
                    msgBuilder.append("0 0");
                } else {
                    msgBuilder.append("00 0");
                }
                newSerial = false;
            } else {
                // 1000011  0_0 00_0000 0_00
                if (chars[i] == chars[i - 1]) {
                    msgBuilder.append("0");
                } else {
                    i--;
                    newSerial = true;
                }

            }
        }

        return msgBuilder;
    }

    private String toBinary(String input) {
        char[] chars = input.toCharArray();
        StringBuilder result = new StringBuilder();

        for (char aChar : chars) {
            result.append(
                    String.format("%7s", Integer.toBinaryString(aChar))   // char -> int, auto-cast
                            .replaceAll(" ", "0"));                         // zero pads
        }

//        System.out.println("Binary: " + result.toString()); // DEBUG
        return result.toString();
    }

    private String printWithSpace(String msg) {
        String[] strings = msg.split("");

        StringBuilder msgBuilder = new StringBuilder();
        for (int i = 0; i < strings.length - 1; i++) {

            msgBuilder.append(strings[i]).append(" ");

        }
        msgBuilder.append(strings[strings.length - 1]);

        return msgBuilder.toString();
    }


}
