import java.util.Random;
import java.lang.Math;
import java.util.Scanner;

public class Crc {

    public static void main(String[] args) {

        int total_messages = 0;
        int with_error = 0;
        int without_error = 0;

        while (total_messages < 10000) {

            Random random = new Random();
            int k = 10;
            Scanner o = new Scanner(System.in);
            int[] Message;
            int[] P_Array;
            int[] Zeros;
            int[] FCS;
            int[] CRC;
            int totalBits;

            //FIRST OPERATION:CREATE A K BIT MESSAGE WITH RANDOM DIGITS(0 OR 1)

            Message = new int[k];

            System.out.print("Message = ");

            for (int i = 0; i < k; i++) {
                Message[i] = random.nextInt(2);
                System.out.print(Message[i]);
            }

            System.out.println();

            //SECOND OPERATION:CALCULATION OF CRC(FCS) OF THE PREVIOUS MESSAGE.INPUT P IS GIVEN BY THE USER

            int P;

            System.out.println("Give P binary number ( first and last bit must be 1 ): ");
            P = o.nextInt();

            while (P % 10 != 1) {
                System.out.println("Give P binary number again ( first and last bit must be 1 ): "); // P cannot be only 1 bit
                P = o.nextInt();
            }

            P_Array = new int[(int) (Math.log10(P) + 1)];

            int t = P;
            for (int i = P_Array.length - 1; i >= 0; i--) {
                P_Array[i] = t % 10;
                t = t / 10;
            }

            System.out.println("Binary number P = " + P);
            System.out.println("Number of P bits = " + P_Array.length);

            totalBits = k + P_Array.length - 1;

            Zeros = new int[totalBits];
            CRC = new int[totalBits];
            FCS = new int[totalBits];

            System.arraycopy(Message, 0, Zeros, 0, Message.length);
            System.arraycopy(Zeros, 0, FCS, 0, Zeros.length);

            FCS = divide(P_Array,FCS);


            System.out.print("FCS binary number = ");
            for (int i = 0; i < FCS.length; i++) {
                System.out.print(FCS[i]);
            }

            System.out.println();

            //THIRD OPERATION:SENDING THE CRC TO THE RECEIVER WITH A BIT ERROR RATE

            for (int i = 0; i < Zeros.length; i++) {
                CRC[i] = (Zeros[i] ^ FCS[i]);          //Array <<Zeros>> only used for the creation of array Crc -- xor used so to unit the 2 arrays
            }                                          //Array <<Zeros>> keep the initial message

            for(int i=0;i<CRC.length;i++){
                System.out.print(CRC[i]);
            }
            System.out.println();


            for(int i=0;i<CRC.length;i++){
                if(random.nextInt(1000)==0){   //bit error rate=10^(-3),,dhladh pithanotita 1/1000
                    if (CRC[i] == 0) {
                        CRC[i] = 1;
                    } else {
                        CRC[i] = 0;
                    }
                }
            }


            //FOURTH OPERATION: CRC CHECK AT RECEIVER

            FCS = divide(P_Array, CRC);

            int m=0;
            for(int i=0;i<FCS.length;i++){
                if(FCS[i]==1)
                    m++;
            }

            if(m==0)
                without_error++;
            else
                with_error++;

            total_messages++;

        }


    }

    private static int[] divide(int[] P, int[] Message) {

        int cur = 0;

        while (true) {
            for (int i = 0; i < P.length; i++)
                Message[cur + i] = (Message[cur + i] ^ P[i]);

            while (Message[cur] == 0 && cur != Message.length - 1)
                cur++;

            if ((Message.length - cur) < P.length)
                break;
        }
        return Message;
    }
}