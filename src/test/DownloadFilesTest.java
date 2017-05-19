package test;
// Created by LJF on 2017/05/16.

import main.DownloadFiles.DownloadNetworkFiles;
import java.util.Scanner;

public class DownloadFilesTest {

    public static void main(String[] args) {

        while(true){
            Scanner scan = new Scanner(System.in);
            String url = scan.nextLine();
            DownloadNetworkFiles file = new DownloadNetworkFiles(url);
            file.downloadUrlFile();
        }

    }

}


//    查找元素
//      getElementById(String id)
//      getElementsByTag(String tag)
//      getElementsByClass(String className)
//      getElementsByAttribute(String key) (and related methods)
//      Element siblings: siblingElements(), firstElementSibling(), lastElementSibling(); nextElementSibling(), previousElementSibling()
//      Graph: parent(), children(), child(int index)
//    元素数据
//      attr(String key)获取属性attr(String key, String value)设置属性
//      attributes()获取所有属性
//      id(), className() and classNames()
//      text()获取文本内容text(String value) 设置文本内容
//      main.javadoc()获取元素内HTMLhtml(String value)设置元素内的HTML内容
//      outerHtml()获取元素外HTML内容
//      data()获取数据内容（例如：script和style标签)
//      tag() and tagName()


//    String html = "<main.javadoc><head><title>开源中国社区</title></head>"
//        + "<body><p>这里是 jsoup 项目的相关文章</p></body></main.javadoc>";
//    Document doc = Jsoup.parse(html);
//    System.out.println(doc + "\n");
//
//    String html1 = "<div><p>Lorem ipsum.</p></div>";
//    Document doc1 = Jsoup.parseBodyFragment(html);
//    Element body1 = doc.body();
//    Elements title = body1.getElementsByTag("p");
//
//    String titleStr = title.text();
//
//    System.out.println(body1 + "\n");
//    System.out.println(title);
//    System.out.println(titleStr);
//
//    // 从URL直接加载 HTML 文档
//    Document doc2 = Jsoup.connect("http://www.oschina.net/").get();
//    String title2 = doc.title();
//    System.out.println(title2 + "\n");
//
//    Document doc3 = Jsoup.connect("http://www.oschina.net/")
//    .data("query", "Java")
//    .userAgent("I’m jsoup")
//    .cookie("auth", "token")
//    .timeout(3000)
//    .post();
//    System.out.println(doc3 + "\n");
//
//    // 从文件中加载 HTML 文档
//    File input = new File("E:/spider.main.javadoc");
//    Document doc4 = Jsoup.parse(input,"UTF-8","http://www.oschina.net/");
//    System.out.println(doc4 + "\n");


