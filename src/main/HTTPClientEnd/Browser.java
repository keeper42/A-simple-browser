package main.HTTPClientEnd;
// Created by LJF on 2017/5/17.

import main.DownloadFiles.DownloadNetworkFiles;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

/**
 * Implement a simple Web browser that supports displaying HTML page.
 */

public class Browser extends JFrame implements HyperlinkListener, PropertyChangeListener{

    /**
     * The following used the swing component
     */
    private JEditorPane editorPane;
    private JTextField urlField;
    private JButton backButton;
    private JButton forwardButton;
    private JButton homeButton;
    private JToolBar toolbar;
    private JMenuBar menuBar;
    private JLabel messageLine;
    private ArrayList history = new ArrayList();

    // Identifies whether or not to exit the application when all browser windows are closed.
    public static boolean exitWhenLastWindowClosed = false;
    public int currentHistoryPage = -1;
    public static final int MAX_HISTORY = 32;
    public static int numBrowserWindows = 0;

    String home = "http://www.baidu.com";

    /**
     * Constructors and initialization
     */
    public Browser(){
        super("Browser");

        editorPane = new JEditorPane();
        editorPane.setEditable(false);

        editorPane.addHyperlinkListener(this);
        editorPane.addPropertyChangeListener(this);

        this.getContentPane().add(new JScrollPane(editorPane), BorderLayout.CENTER);

        messageLine = new JLabel(" ");
        this.getContentPane().add(messageLine, BorderLayout.SOUTH);

        this.initMenu();
        this.initToolbar();

        Browser.numBrowserWindows++;

        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                close();
            }
        });
        setExitWhenLastWindowClosed(true);
    }

    private void initMenu(){

        JMenu fileMenu = new JMenu("File");

        JMenuItem newMenuItem = new JMenuItem("New");
        newMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                newBrowser();
            }
        });

        JMenuItem downloadMenuItem = new JMenuItem("Download");
        downloadMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                downloadUrlFile();
            }
        });

        JMenuItem exitMenuItem = new JMenuItem("Exit");
        exitMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                exit();
            }
        });

        fileMenu.add(newMenuItem);        fileMenu.add(downloadMenuItem);
        fileMenu.add(exitMenuItem);

        JMenu helpMenu = new JMenu("Help");

        JMenuItem aboutMenuItem = new JMenuItem("About");
        aboutMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                aboutBrowser();
            }
        });

        helpMenu.add(aboutMenuItem);

        menuBar = new JMenuBar();
        menuBar.add(fileMenu);
        menuBar.add(helpMenu);
        this.setJMenuBar(menuBar);
    }

    private void initToolbar(){

        backButton = new JButton();
        backButton.setIcon(new ImageIcon("res/backward.png"));
        backButton.setEnabled(false);
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                back();
            }
        });

        forwardButton = new JButton();
        forwardButton.setIcon(new ImageIcon("res/forward.png"));
        forwardButton.setEnabled(false);
        forwardButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                forward();
            }
        });

        homeButton = new JButton();
        homeButton.setIcon(new ImageIcon("res/home.png"));
        homeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                home();
            }
        });

        urlField = new JTextField();
        urlField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                displayPage(urlField.getText());
            }
        });

        toolbar = new JToolBar();
        toolbar.add(backButton);
        toolbar.add(forwardButton);
        toolbar.add(homeButton);
        toolbar.add(urlField);

        this.getContentPane().add(toolbar, BorderLayout.NORTH);
    }

    public void back(){
        if(currentHistoryPage > 0){
            visit((URL) history.get(--currentHistoryPage));
            backButton.setEnabled(true);
        }
        if(currentHistoryPage < history.size() - 1){
            forwardButton.setEnabled(true);
        }
    }

    public void forward(){
        if(currentHistoryPage < history.size() - 1){
            visit((URL) history.get(++currentHistoryPage));
            forwardButton.setEnabled(true);
        }
        if(currentHistoryPage > 0){
            backButton.setEnabled(true);
        }
    }

    /**
     * Construct common HTTP requests and resolve common HTTP responses.
     * @param urlStr
     */
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
            System.out.println(urlConnection.getRequestProperties());

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

    /**
     * Visit the URL
     * @param url
     * @return
     */
    private boolean visit(URL url){
        try{
            String href = url.toString();
            urlField.setText(href);
            startAnimation("Loading " + href + "...");
            // Construct common HTTP requests
            executePostHeaders(href);
            editorPane.setPage(url);

            return true;
        } catch(IOException ioe){
            stopAnimation();
            messageLine.setText("Can not open the page: " + ioe.getMessage());
            return false;
        }
    }

    /**
     * The browser opens the page specified by the URL
     * @param url
     */
    public void displayPage(URL url){
        if(visit(url)){
            history.add(url);
            int numentries = history.size();
            if(numentries > MAX_HISTORY + 16){
                history = (ArrayList) history.subList(numentries - MAX_HISTORY, numentries);
                numentries = MAX_HISTORY;
            }

            currentHistoryPage = numentries - 1;
            if(currentHistoryPage > 0){
                backButton.setEnabled(true);
            }
            if(currentHistoryPage < history.size() - 1){
                forwardButton.setEnabled(true);
            }
        }
    }

    /**
     * The browser opens the string specified by the page
     * Call the function: displayPage(URL)
     * @param href
     */
    public void displayPage(String href){
        try {
            if (!href.startsWith("http://")){
                href = "http://" + href;
            }
            displayPage(new URL(href));
        }
        catch (MalformedURLException ex) {
            messageLine.setText("Wrong URL: " + href);
        }
    }

    public String getHome(){
        return home;
    }

    public void home(){
        displayPage(getHome());
    }

    public void newBrowser(){
        Browser browser = new Browser();
        browser.setVisible(true);
        browser.setSize(this.getWidth(), this.getHeight());
    }

    public void aboutBrowser(){
        File readme = new File("src/Readme.txt");
        try {
            FileInputStream out = new FileInputStream(readme);
            InputStreamReader isr = new InputStreamReader(out);
            int ch = 0;
            String msg = "";
            while((ch = isr.read()) != -1){
                msg += String.valueOf((char)ch);
            }
            JOptionPane.showConfirmDialog(null, msg, "README", JOptionPane.YES_OPTION);

            out.close();
            isr.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void downloadUrlFile(){
        String href = urlField.getText();
        if(href != null && (href.startsWith("http://") || href.contains("www."))){
            DownloadNetworkFiles file = new DownloadNetworkFiles(href);
            file.downloadUrlFile();
        }
    }

    public void close(){
        this.setVisible(false);
        this.dispose();
        synchronized (Browser.class){
            Browser.numBrowserWindows--;
            if ((numBrowserWindows==0) && exitWhenLastWindowClosed){
                System.exit(0);
            }
        }
    }

    public void exit(){
        if(JOptionPane.showConfirmDialog(this, "If you want to exit browser?", "exit",
                JOptionPane.YES_NO_OPTION) == JOptionPane.YES_NO_OPTION){
            System.exit(0);
        }
    }

    /**
     * Set whether the browser exits when all windows are closed
     * @param flag
     */
    public static void setExitWhenLastWindowClosed(boolean flag){
        exitWhenLastWindowClosed = flag;
    }


    /**
     * Implement the HyperlinkListener interface. Handle Hyperlink events
     * @param e
     */
    @Override
    public void hyperlinkUpdate(HyperlinkEvent e) {
        URL linkUrl = e.getURL();
        if(linkUrl != null){
            HyperlinkEvent.EventType type = e.getEventType();
            if (type == HyperlinkEvent.EventType.ACTIVATED) {
                displayPage(linkUrl);
            }else if(type == HyperlinkEvent.EventType.ENTERED){
                messageLine.setText(linkUrl.toString());
            }else if(type == HyperlinkEvent.EventType.EXITED){
                messageLine.setText(" ");
            }
        }
    }

    /**
     * Implement the PropertyChangeListener interface. Processing attribute change event.
     * Show HTML panel textPane when the properties are changed by this method.
     * When the textPane call setPage method, page property will change.
     * @param evt
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if(evt.getPropertyName().equals("page")){
            stopAnimation();
        }
    }

    /**
     * The animation message, displayed on the bottom of the status bar tab, is used to feed back the status of the browser
     */
    int animationFrame = 0;
    String animationMessage;
    String[] animationFrames = new String[] {
            "-", "\\", "|", "/", "-", "\\", "|", "/",
            ",", ".", "o", "0", "O", "#", "*", "+"
    };

    /**
     * Create a new Swing timer that updates the text of the status bar label every 125 milliseconds
     */
    javax.swing.Timer animator = new javax.swing.Timer(125, new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    animate();
                }
            });

    private void animate() {
        String frame = animationFrames[animationFrame++];
        messageLine.setText(animationMessage + " " + frame);
        animationFrame = animationFrame % animationFrames.length;
    }

    private void startAnimation(String msg) {
        animationMessage = msg;
        animationFrame = 0;
        animator.start();
    }

    private void stopAnimation() {
        animator.stop();
        messageLine.setText(" ");
    }

}
