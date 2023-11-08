/**
 *
 *  @author Balcerzak Piotr S25100
 *
 */

package zad1;


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;
import java.util.concurrent.*;
import java.util.concurrent.locks.Lock;

public class Main {
    int BOUND = 10;
    BlockingQueue<Towar> queue = new LinkedBlockingQueue<>(BOUND);
    Towar towar;

      public static void main(String[] args) throws IOException, InterruptedException {
          String fileName = "../Towary.txt";
          Main main = new Main();
          main.readFile(fileName);
          /*FileWriter fileWriter = new FileWriter(fileName);
          PrintWriter printWriter = new PrintWriter(fileWriter);
          for(int id =0; id<10002; id++){
              int waga = 10;
              printWriter.printf("%d %d\n", id, waga);
          }
          printWriter.close();*/


      }
  private void readFile(String fileName) throws IOException, InterruptedException {
        File myObj = new File(fileName);
        Scanner myReader = new Scanner(myObj);
        Thread t1 = null;
        Thread t2 = null;
        t1 = new Thread(()->{
            int objectCounter = 0;
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                String[] splitter = data.split(" ");
                String id = splitter[0];
                String waga = splitter[1];
                towar = new Towar(id, waga);
                objectCounter++;
                try {
                    queue.put(towar);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if(objectCounter%200 == 0){
                    System.out.println("utworzono "+objectCounter +" obiektów");
                }
            }
            myReader.close();
        });

        t2 = new Thread(()->{
            try {
                Towar towar = null;
                int objectCounter2 = 0;
                int suma = 0;
                while ((towar = queue.poll(500, TimeUnit.MILLISECONDS)) != null) {
                    suma += Integer.parseInt(towar.waga);
                    objectCounter2++;
                    if(objectCounter2%100 == 0 && objectCounter2 != 0){
                        System.out.println("policzono wage "+objectCounter2 +" towarów");
                    }
                }
                System.out.println("Waga towarów jest równa " + suma);
            }catch(Exception e){
                System.out.println("Wyjatek");
            }
        });

        t1.start();
        t2.start();
  }

}
