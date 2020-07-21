import java.io.*;   // for Input/OutputStream
import java.net.*;  // for Socket and ServerSocket

  /**
  * Application that creates a TCP Server.
  *
  * Programming Assignment 2
  * @author Erika Yardumian - CPSC - 3353
  * @version 6/15/20
  **/
public class ServerTCP {
    /**
    * Main method.
    * @param args command-line arguments.
    * @throws IOException.
    **/
   public static void main(String[] args) throws Exception {
   
      if (args.length != 1)  {
         throw new IllegalArgumentException("Parameter(s): <Port>");
      }
   
      int port = Integer.parseInt(args[0]);   // Receiving Port
   
      ServerSocket servSock = new ServerSocket(port);
      
      TCPRequestDecoder decoder = (args.length == 2                 
              ? new TCPRequestDecoderBin(args[1]) : new TCPRequestDecoderBin());
         
      Socket clntSock = servSock.accept();        
      DataInputStream receivedTCPRequest = 
         new DataInputStream(clntSock.getInputStream());
      DataOutputStream out = new DataOutputStream(clntSock.getOutputStream());
      
      while (true) {          
      
         byte[] reply = Response.makeResponse(decoder, receivedTCPRequest); 
         
         System.out.println("Received Binary-Encoded TCPRequest");
         
         StringBuilder sb = new StringBuilder();
         for (byte b : reply) {
            sb.append(String.format("%02X ", b));
         }
         System.out.println(sb.toString());
      
         out.write(reply); 
         out.flush();      
         
         System.out.println("server sent reply");
      }
   }
}
