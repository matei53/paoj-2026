package com.pao.laboratory09.exercise2;

import com.pao.laboratory09.exercise1.TipTranzactie;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.*;

public class Main {
    private static final String OUTPUT_FILE = "src/com/pao/laboratory09/output/lab09_ex2.bin";
    private static final int RECORD_SIZE = 32;

    public static void main(String[] args) throws Exception {
        // TODO: Implementează conform Readme.md
        //
        // 1. Citește N din stdin, apoi cele N tranzacții (id suma data tip)
        // 2. Scrie toate înregistrările în OUTPUT_FILE cu DataOutputStream (format binar, RECORD_SIZE=32 bytes/înreg.)
        //    - bytes 0-3:   id (int, little-endian via ByteBuffer)
        //    - bytes 4-11:  suma (double, little-endian via ByteBuffer)
        //    - bytes 12-21: data (String, 10 chars ASCII, paddat cu spații la dreapta)
        //    - byte 22:     tip (0=CREDIT, 1=DEBIT)
        //    - byte 23:     status (0=PENDING, 1=PROCESSED, 2=REJECTED)
        //    - bytes 24-31: padding (zerouri)
        // 3. Procesează comenzile din stdin până la EOF cu RandomAccessFile:
        //    - READ idx       → seek(idx * RECORD_SIZE), citește și afișează înregistrarea
        //    - UPDATE idx ST  → seek(idx * RECORD_SIZE + 23), scrie noul status (0/1/2)
        //                       afișează "Updated [idx]: STATUS"
        //    - PRINT_ALL      → citește și afișează toate înregistrările
        //
        // Format linie output:
        //   [idx] id=<id> data=<data> tip=<CREDIT|DEBIT> suma=<suma:.2f> RON status=<STATUS>

        Scanner scanner = new Scanner(System.in);
        int N = scanner.nextInt();

        try (DataOutputStream out = new DataOutputStream(
                new FileOutputStream(OUTPUT_FILE))) {

            for (int i = 0; i < N; i++) {
                int id = scanner.nextInt();
                byte[] idBytes = ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN).putInt(id).array();
                out.write(idBytes);

                double suma = scanner.nextDouble();
                byte[] sumaBytes = ByteBuffer.allocate(8).order(ByteOrder.LITTLE_ENDIAN).putDouble(suma).array();
                out.write(sumaBytes);

                String data = scanner.next();
                byte[] dataBytes = new byte[10];
                byte[] v = data.getBytes("ASCII");
                for (int j = 0; j < v.length; j++) dataBytes[j] = v[j];
                for (int j = v.length; j < 10; j++) dataBytes[j] = (byte) ' ';
                out.write(dataBytes);

                TipTranzactie tip = TipTranzactie.valueOf(scanner.next());
                out.write(tip == TipTranzactie.CREDIT ? 0 : 1);

                out.write(0); //pending

                out.write(new byte[8]); //padding
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        while (scanner.hasNext()) {
            String comanda = scanner.next();
            if (comanda.equals("READ")) {
                long idx = scanner.nextLong();
                RandomAccessFile raf = new RandomAccessFile(OUTPUT_FILE, "rw");
                raf.seek(idx * RECORD_SIZE);
                byte[] bytes = new byte[RECORD_SIZE];
                raf.readFully(bytes);
                ByteBuffer buffer = ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN);

                int id = buffer.getInt(0);
                double suma = buffer.getDouble(4);
                String data = new String(bytes, 12, 10, "ASCII").trim();
                String tip = bytes[22] == 0 ? "CREDIT" : "DEBIT";
                String status;
                if (bytes[23] == 0) status = "PENDING";
                else if (bytes[23] == 1) status = "PROCESSED";
                else status = "REJECTED";
                System.out.printf("[%d] id=%d data=%s tip=%s suma=%.2f RON status=%s\n",
                        idx, id, data, tip, suma, status);

                raf.close();
            }
            else if (comanda.equals("UPDATE")) {
                long idx = scanner.nextLong();
                String status = scanner.next();
                RandomAccessFile raf = new RandomAccessFile(OUTPUT_FILE, "rw");
                raf.seek(idx * 32 + 23);
                if (status.equals("PENDING")) raf.write(0);
                else if (status.equals("PROCESSED")) raf.write(1);
                else if (status.equals("REJECTED")) raf.write(2);
                raf.close();
                System.out.println("Updated [" + idx + "]: " + status);
            }
            else if (comanda.equals("PRINT_ALL")) {
                RandomAccessFile raf = new RandomAccessFile(OUTPUT_FILE, "rw");
                int idx = 0;
                int lungime = (int) raf.length() / RECORD_SIZE;
                while(idx < lungime) {
                    byte[] bytes = new byte[RECORD_SIZE];
                    raf.readFully(bytes);
                    ByteBuffer buffer = ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN);

                    int id = buffer.getInt(0);
                    double suma = buffer.getDouble(4);
                    String data = new String(bytes, 12, 10, "ASCII").trim();
                    String tip = bytes[22] == 0 ? "CREDIT" : "DEBIT";
                    String status;
                    if (bytes[23] == 0) status = "PENDING";
                    else if (bytes[23] == 1) status = "PROCESSED";
                    else status = "REJECTED";
                    System.out.printf("[%d] id=%d data=%s tip=%s suma=%.2f RON status=%s\n",
                            idx, id, data, tip, suma, status);
                    idx++;
                }
                raf.close();
            }
        }
    }
}
