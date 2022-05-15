import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.RecursiveTask;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class CreateMapWebsite extends RecursiveTask<String> {

  private String url;
  private static HashSet<String> listUrl = new HashSet<>();

  public CreateMapWebsite(String url) {
    this.url = url;
    listUrl.add(url);
  }

  @Override
  protected String compute() {
    String levelUrl = getLevel(url) + url + System.lineSeparator();
    StringBuffer buffer = new StringBuffer(levelUrl);
    List<CreateMapWebsite> taskList = new ArrayList<>();
    try {
      Thread.sleep(100);
      Document document = Jsoup.connect(url).get();
      Elements elements = document.select("a");
      elements.forEach(e -> {
        String nextUrl = e.absUrl("href");
        if (!nextUrl.isEmpty() && nextUrl.startsWith(url) && !listUrl.contains(nextUrl) && !nextUrl.contains("#")) {
          CreateMapWebsite createMap = new CreateMapWebsite(nextUrl);
          createMap.fork();
          taskList.add(createMap);
          listUrl.add(nextUrl);
        }
      });

    } catch (Exception e) {
      e.toString();
    }

    taskList.stream().forEach(task -> {
      buffer.append(task.join());
    });

    return buffer.toString();
  }

  private String getLevel(String string) {
    int count = 0;
    String level = "\t";
    for (int i = 0; i < string.length(); i++) {
      char c = string.charAt(i);
      if (c == '/') {
        count++;
      }
    }
    level = level.repeat(string.lastIndexOf("/") != string.length() - 1 ? count - 2 : count - 3);
    return level;
  }
}


