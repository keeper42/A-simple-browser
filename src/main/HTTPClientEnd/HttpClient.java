package main.HTTPClientEnd;
// Created by LJF on 2017/5/17.

import com.sun.deploy.net.HttpResponse;
import sun.misc.IOUtils;

import java.awt.*;
import javax.swing.*;
import javax.swing.event.HyperlinkEvent;
import javax.swing.text.Document;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.HTMLFrameHyperlinkEvent;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class HttpClient extends JFrame {

    private JPanel masterPanel = new JPanel();
    private JPanel topPanel = new JPanel();
    private JPanel bottomPanel = new JPanel(new BorderLayout());
    private JPanel ipPanel = new JPanel(new GridLayout(1, 1));
    private JTextField ipText = new JTextField(50);
    private JButton gotoButton = new JButton("GO");
    private JEditorPane editorPane = new JEditorPane();
    private JScrollPane scrollPane;
    private JButton forwardButton = new JButton();
    private JButton backwardButton = new JButton();
    private ArrayList<URL> urls = new ArrayList<URL>();

    private void ShowGUI() {
        Dimension ScreenSize = getToolkit().getScreenSize();
        int width = ScreenSize.width * 8 / 10;
        int height = ScreenSize.height * 8 / 10;
        setBounds(width / 8, height / 8, width, height);
        setVisible(true);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    private void SetTopPanel() {
        setTitle("A simple browser");
        ipPanel.add(ipText);
        topPanel.add(ipPanel);
        topPanel.add(gotoButton);
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.X_AXIS));
    }

    public static void main(String[] args) {
        new HttpClient();
    }

    HttpClient() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                CreateAndShowGUI();
            }
        });
    }

    public void CreateAndShowGUI() {

        SetTopPanel();
        MyActionListener myActionListener = new MyActionListener(ipText);
        gotoButton.addActionListener(myActionListener);

        forwardButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });

        backwardButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });

        editorPane.setEditable(false);
        scrollPane = new JScrollPane(editorPane);
        bottomPanel.add(scrollPane);

        masterPanel.add(topPanel);
        masterPanel.add(bottomPanel);
        masterPanel.setLayout(new BoxLayout(masterPanel, BoxLayout.Y_AXIS));
        setContentPane(masterPanel);
        ShowGUI();
    }

    private class MyActionListener implements ActionListener{

        private JTextField textField = new JTextField();

        public MyActionListener(JTextField textField) {
            this.textField = textField;
        }

        public void actionPerformed(ActionEvent e){
            try {
                String urlStr = ipText.getText();
                if(urlStr == null || urlStr.equals("")){
                    return;
                }
                urlStr = urlStr.trim();  // Remove the space
                String urlSubStr;
                if(!urlStr.contains("http://")){
                    urlSubStr = urlStr;
                    urlStr = "http://" + urlStr;
                }else{
                    int beginIndex = urlStr.indexOf("//");
                    urlSubStr = urlStr.substring(beginIndex + 2, urlStr.length());
                }

//                String request = "GET " + urlSubStr + " HTTP/1.1\\r\\n";
//                InetAddress address = InetAddress.getByName(urlSubStr);

//                getConnect(address, request);  // Connect to the server

                URL url = new URL(urlStr);
                executePostHeaders(urlStr);

                editorPane.setPage(url);
                editorPane.setContentType("text/html");
                editorPane.addHyperlinkListener(this::hyperlinkUpdate);

                HTMLEditorKit kit = new HTMLEditorKit();
                editorPane.setEditorKit(kit);

                urls.add(url);

                /*
                 * Thinking about the interface of editorpane...
                 * And to achieve the function of submitting data to the server
                 */

            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }

        private void executePostHeaders(String urlStr) {
            URL url = null;
            try {
                url = new URL(urlStr);
                HttpURLConnection urlConnection = null;
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setDoOutput(true);
                urlConnection.setDoInput(true);
                urlConnection.setUseCaches(false);  // User Caches
                urlConnection.setRequestMethod("POST");
                // SetRequestProperty constructs a request to tell the server client for configuration
                urlConnection.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
                urlConnection.setRequestProperty("Accept-Encoding", "gzip, deflate, sdch, br");
                urlConnection.setRequestProperty("Accept-Language", "zh-CN,zh;q=0.8");
                urlConnection.setRequestProperty("Cache-Control", "no-cache");
                urlConnection.setRequestProperty("Connection","keep-alive");
                urlConnection.setRequestProperty("Host", urlStr);
                urlConnection.setRequestProperty("Pragma", "no-cache");
                urlConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36");
                urlConnection.connect();

                OutputStream os = urlConnection.getOutputStream ();
                DataOutputStream dos = new DataOutputStream (os);
                String urlParameters = URLEncoder.encode("key1", "UTF-8") + "=" + URLEncoder.encode("value1", "UTF-8")
                            + "&" + URLEncoder.encode("key2", "UTF-8") + "=" + URLEncoder.encode("value2", "UTF-8");
                dos.writeBytes (urlParameters);
                dos.flush ();

                InputStream is = urlConnection.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(is));
                // The following is the result of processing the server feedback
                String response = new String();
                String responseline = br.readLine();
                while(responseline != null){
                    System.out.println(responseline);
                    response = response.concat(responseline);
                    responseline = br.readLine();
                }

                br.close();
                is.close();
                dos.close();
                os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void hyperlinkUpdate(HyperlinkEvent e) {
            if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
                URL linkUrl = e.getURL();
                JEditorPane pane = (JEditorPane) e.getSource();
                if (linkUrl != null) {
                    if (e instanceof HTMLFrameHyperlinkEvent) {
                        // Special treat HTML frame event
                        HTMLFrameHyperlinkEvent evt = (HTMLFrameHyperlinkEvent) e;
                        HTMLDocument doc = (HTMLDocument) pane.getDocument();
                        doc.processHTMLFrameHyperlinkEvent(evt);
                    } else {
                        try {
                            pane.setPage(linkUrl);
                            urls.add(linkUrl);

                        } catch (Throwable t) {
                            t.printStackTrace();
                        }
                    }
                }
            }
        }

    }

    private void getConnect(InetAddress address, String request) throws IOException{

        Socket socket = new Socket(address, 80);
        socket.setSoTimeout(10000);

        // Get an output stream
        OutputStream os = socket.getOutputStream();
        PrintWriter pw = new PrintWriter(new OutputStreamWriter(os));
        // The user request to write the output stream to the server
        pw.println(request);
        pw.flush();

        // Get an input stream
        InputStream is = socket.getInputStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        // The following is the result of processing the server feedback
        String response = new String();
        String responseline = br.readLine();
        while(responseline != null){
            System.out.println(responseline);
            response = response.concat(responseline);
            responseline = br.readLine();
        }

        br.close();
        is.close();
        pw.close();
        os.close();
        socket.close();
    }
}

