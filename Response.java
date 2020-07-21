import java.net.*;  // for DatagramSocket, DatagramPacket, and InetAddress
import java.io.*;   // for IOException
import java.lang.Math;

public class Response {

   public static byte[] makeResponse(TCPRequestDecoder decoder, DataInputStream message)throws Exception {
      int result = 0;
      byte errorCode = 0;
      //create packet in a line??
      byte[] packet = new byte[10];
      message.read(packet, 0, 10);
      short requestID = 0;
      byte checkSum = TCPRequest.byteCheckSum(packet, packet.length - 1);
      //System.out.println(checkSum);
      
      if (packet.length != packet[0])
      {
         errorCode = 127; 
          //System.out.println("packet length " + packet.length + " not equal to first byte " + packet[0]);        
      }
      else if (packet[packet.length - 1] != checkSum)
      {
         errorCode = 63;
      }
      //System.out.println(errorCode);
      if (errorCode == 0) 
      {     
         ByteArrayInputStream payload =
            new ByteArrayInputStream(packet);
         TCPRequest request = decoder.decode(payload);
      
         System.out.println(payload);
         
         // Calculate the polynomial result
         if (request.a4 != 0) 
            result += request.a4*(Math.pow(request.x, 4));
         if (request.a3 != 0) 
            result += request.a3*(Math.pow(request.x, 3));
         if (request.a2 != 0) 
            result += request.a2*(Math.pow(request.x, 2));
         result += request.a1*request.x;
         result += request.a0;
         requestID = (short) request.ID;
      }
   
      ByteArrayOutputStream buf = new ByteArrayOutputStream();
      DataOutputStream out = new DataOutputStream(buf);
      byte[] bytes;
      out.writeByte(8);
      out.writeShort(requestID);
      out.writeByte(errorCode);
      out.writeInt(result);
      out.writeByte(0);
      out.flush();
      bytes = buf.toByteArray();
      
      bytes[8] = TCPRequest.byteCheckSum(bytes, 8);
      return bytes;
   }

}