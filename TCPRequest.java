public class TCPRequest {

   public int ID;     // Identification number
   public int x;      // Number for which we compute
   public int a4;     // 4th polynomial coefficient
   public int a3;     // 3rd polynomial coefficient
   public int a2;     // 2nd polynomial coefficient
   public int a1;     // 1st polynomial coefficient
   public int a0;     // 0th polynomial coefficient
    

   public TCPRequest(int ID, int x, int a4, int a3, 
   	int a2, int a1, int a0)  {
      this.ID = ID;
      this.x = x;
      this.a4 = a4;
      this.a3 = a3;
      this.a2 = a2;
      this.a1 = a1;
      this.a0 = a0;
   }

   public String toString() {
      final String EOLN = java.lang.System.getProperty("line.separator");
      String value = "Request # = " + ID + EOLN +
                   "x = " + x + EOLN +
                   "4th polynomial coefficient = " + a4 + EOLN +
                   "3rd polynomial coefficient = " + a3 + EOLN +
                   "2nd polynomial coefficient = " + a2 + EOLN +
                   "1st polynomial coefficient = " + a1 + EOLN +
                   "0th polynomial coefficient = " + a0 + EOLN;
      return value;
   }
   public static byte byteCheckSum(byte[] message, int length) {
      int checkSum = 0;
      for (int i=0; i < length; i++)
      {
         checkSum += Byte.toUnsignedInt(message[i]);
         if (checkSum > 255)
            checkSum++;
      }
      checkSum = ~checkSum;
      return (byte) checkSum;
   }

}
