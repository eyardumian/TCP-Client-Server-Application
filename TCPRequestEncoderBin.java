import java.io.*;  // for ByteArrayOutputStream and DataOutputStream

public class TCPRequestEncoderBin implements TCPRequestEncoder, TCPRequestBinConst {

   private String encoding;  // Character encoding

   public TCPRequestEncoderBin() {
      encoding = DEFAULT_ENCODING;
   }

   public TCPRequestEncoderBin(String encoding) {
      this.encoding = encoding;
   }

   public byte[] encode(TCPRequest request) throws Exception {
   
      ByteArrayOutputStream buf = new ByteArrayOutputStream();
      DataOutputStream out = new DataOutputStream(buf);
      byte[] bytes;
      out.writeByte(10);
      out.writeShort(request.ID);
      out.writeByte(request.x);
      out.writeByte(request.a4);
      out.writeByte(request.a3);
      out.writeByte(request.a2);
      out.writeByte(request.a1);
      out.writeByte(request.a0);
      out.writeByte(0);
      out.flush();
      bytes = buf.toByteArray();
      
      bytes[9] = TCPRequest.byteCheckSum(bytes, 9);
      return bytes;
   }   
}
