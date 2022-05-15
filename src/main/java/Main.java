import java.io.*;
import java.util.concurrent.ForkJoinPool;

public class Main {

  static String url;
  static String fileName;

  public static void main(String[] args) {
    url = "https://lenta.ru";
    fileName = url.substring(url.lastIndexOf('/') + 1);

    String map = new ForkJoinPool().invoke(new CreateMapWebsite(url));

    toFile(map);
  }

  public static void toFile(String string) {
    try {
      BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
      writer.write(string);
      writer.flush();
      writer.close();
    } catch (Exception e) {
      e.toString();
    }
  }

}