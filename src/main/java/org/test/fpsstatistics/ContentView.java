package org.test.fpsstatistics;

import javafx.scene.layout.BorderPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

/***
 * 场景编辑/查看
 * Created by Bony on 2017-09-03.
 * Author: ZhangBoyi (Bony)
 ***/
public class ContentView extends BorderPane {
    WebView webView = new WebView();
    WebEngine engine = webView.getEngine();

    static ContentView INSTANCE = null;


    public ContentView() {
        webView.setContextMenuEnabled(false);
        engine.load(ContentView.class.getResource("/html/fps.html").toExternalForm());
        setCenter(webView);
//        try {
//            Thread.sleep(500);
//            ContentView.getInstance().addFpsData("[3,3]");
//            Thread.sleep(500);
//            ContentView.getInstance().addFpsData("[4,5]");
//            Thread.sleep(500);
//            ContentView.getInstance().addFpsData("[5,12]");
//            Thread.sleep(500);
//            ContentView.getInstance().addFpsData("[6,2]");
//            Thread.sleep(500);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
    }

    public static ContentView getInstance() {
        if (INSTANCE == null) INSTANCE = new ContentView();
        return INSTANCE;
    }
    /**
     * 添加JS回调方法类
     */
    public void addFpsData(String fps) {
        setCenter(webView);
        //javaScript -> java
//        JSObject win = (JSObject) engine.executeScript("window");
//        win.setMember("Scene", scene);
//        win.setMember("ctmsAlert", alert);
        //java -> javaScript
        //特殊字符处理
//        String content = scene.getSceneContent().replaceAll("\\\\","\\\\\\\\");
//        content=content.replaceAll("\\\"", "\\\\\"");
//        content=content.replaceAll("\n", "\\\\n");
        engine.executeScript(String.format("FpsAddData(%s)", fps));
    }

    public void clearData(){
        engine.load(ContentView.class.getResource("/html/fps.html").toExternalForm());
        setCenter(webView);
//        engine.executeScript("clearData()");
    }
}