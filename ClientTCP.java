import java.net.*;  // for DatagramSocket, DatagramPacket, and InetAddress
import java.io.*;   // for IOException
import java.util.Scanner;
import java.util.Random;
import java.util.Arrays;
import java.math.*;
/**
 * Application that creates a TCP client.
 *
 * Programming Assignment 2
 * @author Erika Yardumian - CPSC - 3353
 * @version 6/25/20
 **/
public class ClientTCP {
    /**
    * Main method.
    * @param args command-line arguments.
    * @throws IOException.
    **/
   public static void main(String[] args) throws Exception {
   
      if (args.length != 2 && args.length != 3)  {        
         throw new IllegalArgumentException("Parameter(s): <Destination>"
                 + " <Port> [<encoding]");
      }
            // Use the encoding scheme given on the command line (args[2])
      TCPRequestEncoder encoder = (args.length == 3 
              ? new TCPRequestEncoderBin(args[2]) 
              : new TCPRequestEncoderBin());
      // UDP socket for sending
      //DatagramSocket sock = new DatagramSocket(); 
      String H = args[0];
      int P = (args.length == 2) ? Integer.parseInt(args[1]) : 7;
      Socket socket = new Socket(H, P);
      DataInputStream in = new DataInputStream(socket.getInputStream());
      DataOutputStream out = new DataOutputStream(socket.getOutputStream());
   
      // Destination address
      InetAddress destAddr = InetAddress.getByName(args[0]); 
      int destPort = Integer.parseInt(args[1]); // Destination port
      String xPrompt = "Please enter the number for which you" 
            + " want to compute(x): (number should be between -128 and 127): ";
      int x = 0;
      String a4Prompt = "Please enter the 4th polynomial coefficient"  
            + "(number should be between -128 and 127): ";
      int a4 = 0;
      String a3Prompt = "Please enter the 3rd polynomial coefficient: " 
            + "(number should be between -128 and 127): ";
      int a3 = 0;
      String a2Prompt = "Please enter the 2nd polynomial coefficient: " 
            + "(number should be between -128 and 127): ";
      int a2 = 0;
      String a1Prompt = "Please enter the 1st polynomial coefficient: " 
            + "(number should be between -128 and 127): ";
      int a1 = 0;
      String a0Prompt = "Please enter the 0th polynomial coefficient: " 
            + "(number should be between -128 and 127): ";
      int a0 = 0;
      double start;
      String a44;
      String a33;
      String a22;
      String a11 = "";
      String a00;
      
      Random rand = new Random(); //Create random number generator for ID
      int ID = rand.nextInt(Short.MAX_VALUE);
                
      while (true) {
         // Collect user input.      
         x = getSignedByte(xPrompt);   
         if (x == 128) 
            break;     
         a4 = getSignedByte(a4Prompt);
         if (a4 == 128) 
            break;           
         a3 = getSignedByte(a3Prompt);
         if (a3 == 128) 
            break;           
         a2 = getSignedByte(a2Prompt);
         if (a2 == 128) 
            break;           
         a1 = getSignedByte(a1Prompt);
         if (a1 == 128) 
            break;           
         a0 = getSignedByte(a0Prompt);
         if (a0 == 128) 
            break;           
         TCPRequest request = new TCPRequest(ID, x,
             a4, a3, a2, a1, a0);
         
         byte[] codedRequest = encoder.encode(request); // Encode request
         StringBuilder sb = new StringBuilder();
         for (byte b : codedRequest) {
            sb.append(String.format("%02X ", b));
         }
         //System.out.println(sb.toString());
      
         // Error testing code
         // codedRequest[0] = 1; //bad message length   
         // codedRequest[9] = 2; //bad checksum
      
         start = System.nanoTime();
         out.write(codedRequest); 
         out.flush(); 
         
         byte[] bs = new byte[9];
         in.read(bs);
      
         double end = System.nanoTime();
         double time = (end - start) / 1000000;   
         
         StringBuilder rb = new StringBuilder();
         for (byte b : codedRequest) {
            rb.append(String.format("%02X ", b));
         }            
         System.out.println("Request message in hexadecimal: " + rb.toString());
         
         if (request.a4 != 0) { a44 = a4 + "x^4 + "; } 
         else { a44 = ""; }
         if (request.a3 != 0) { a33 = a3 + "x^3 + "; } 
         else { a33 = ""; }
         if (request.a2 != 0) { a22 = a2 + "x^2 + "; } 
         else { a22 = ""; }
         if (request.a1 != 0) { a11 = a1 + "x + "; }
         a00 = String.valueOf(request.a0);            
      
         byte[] slice = Arrays.copyOfRange(bs, 4, 8);  
         BigInteger one = new BigInteger(slice);
         String strResult = one.toString();
         
         System.out.println("Original Polynomial: P(x) = " 
            + a44 + a33 + a22 + a11 + a00);
         System.out.println("x = " + request.x);
         System.out.println("P(x) = " + strResult);
         System.out.println("Round trip time: " + time + " nanoseconds");
         ID++; // Increment ID for next message    
      }
      socket.close();
   }
   
   private static int getSignedByte(String prompt) {
      int y = 0;
      Scanner scan = new Scanner(System.in);
      System.out.println(prompt);    
      while (true) {
         if (scan.hasNext("stop"))
         {
            y = 128;
            break;
         }
         y = scan.nextInt();
         if ((y >= -128) && (y <= 127)) {
            break; 
         }
         else {
            System.out.println("The number must be in the range -128 to 127." 
               + "Please enter the number again: ");
         }
      }
      return y;
   }
}
