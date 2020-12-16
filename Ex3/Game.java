import java.util.Random; 
import java.security.SecureRandom; 
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Scanner;
import java.security.NoSuchAlgorithmException;
import java.security.InvalidKeyException;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.math.BigInteger;
import java.util.Base64;
import java.nio.charset.StandardCharsets;


public class Game {
    public static void main(String[] args) {
        if(args.length < 3) {
            System.out.println("The number of arguments cannot be less than 3. Exemple: rock paper scissors");
            System.exit(0);
        }
        else if(args.length % 2 == 0) {
            System.out.println("The number of arguments must be odd. Exemple: rock paper scissors");
            System.exit(0);
        }                    

        int randomStepIdx = new Random().nextInt(args.length);
        String computerMove = args[randomStepIdx];
        
        SecureRandom secureRandom = new SecureRandom();
        byte secretKey[] = new byte[16];
        secureRandom.nextBytes(secretKey);
        SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey, "HmacSHA256");

        Mac mac = null;
        try {
            mac = Mac.getInstance("HmacSHA256");
            mac.init(secretKeySpec);
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            e.printStackTrace();
        }
        byte[] hmac = mac.doFinal(computerMove.getBytes());        
        String hmacStr = new BigInteger(1, hmac).toString(16).toUpperCase();
        System.out.println("HMAC: ");
        System.out.println(hmacStr);

        System.out.println("Available moves:");
        for (int i = 1; i <= args.length ; i++) {
            String a = (i) + (" - ") + (args[i-1]);
            System.out.println(a);
        }
        System.out.println("0 - exit");

        System.out.println("Enter your move: ");
        Scanner scanner = new Scanner(System.in);
        int move = scanner.nextInt();

        System.out.println(("Your move: ") + (args[move - 1]));
        System.out.println(("Computer move: ") + (computerMove));
        
        int half_length = args.length / 2;
    
        int left_wing_start = (move-1 - half_length) % args.length;
        int right_wing_start = (move-1 + 1) % args.length;


        ArrayList left_wing = new ArrayList();
        // int[] left_wing = new int[half_length];
        for (int i = 0; i <= half_length ; i++) {
            left_wing.add((left_wing_start + i) % args.length);
        }
        
        ArrayList right_wing = new ArrayList();
        // int[] right_wing = new int[half_length];
        for (int i = 0; i <= half_length ; i++) {
            right_wing.add((right_wing_start + i) % args.length);
        }
    
        if (move-1 == randomStepIdx) {
            System.out.println("A tie!");
        }
        else if (left_wing.contains(randomStepIdx)) {
            System.out.println("You win!");
        }
        else if (right_wing.contains(randomStepIdx)) {
            System.out.println("Computer win!");
        }
        
        String StrSecretKey = new BigInteger(1, secretKey).toString(16).toUpperCase();
        System.out.println("HMAC key: " + StrSecretKey); 
    }
}