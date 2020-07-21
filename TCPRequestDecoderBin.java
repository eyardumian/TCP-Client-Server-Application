import java.io.*;  // for ByteArrayInputStream
import java.net.*; // for DatagramPacket

public class TCPRequestDecoderBin implements TCPRequestDecoder, TCPRequestBinConst {

   private String encoding;  // Character encoding

   public TCPRequestDecoderBin() {
      encoding = DEFAULT_ENCODING;
   }

   public TCPRequestDecoderBin(String encoding) {
      this.encoding = encoding;
   }

   public TCPRequest decode(InputStream wire) throws IOException {
      DataInputStream src = new DataInputStream(wire);
      src.readByte();   //go past length byte
      System.out.println(src);
      int ID = src.readShort();
      int x  = src.readByte();
      int a4 = src.readByte();
      int a3 = src.readByte(); 
      int a2 = src.readByte();  
      int a1 = src.readByte(); 
      int a0 = src.readByte(); 
      System.out.println(ID);
      
      // //Deal with the lastname
      // int stringLength = src.read(); // Returns an unsigned byte as an int
      // if (stringLength == -1)
         // throw new EOFException();
      // byte[] stringBuf = new byte[stringLength];
      // src.readFully(stringBuf);
      // String lastname = new String(stringBuf, encoding);
   
      return new TCPRequest(ID,x, a4, a3,
         a2, a1, a0);
        
   }

   public TCPRequest decode(DatagramPacket p) throws IOException {
      ByteArrayInputStream payload =
         new ByteArrayInputStream(p.getData(), p.getOffset(), p.getLength());
      return decode(payload);
   }
}
