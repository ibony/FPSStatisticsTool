package org.test.fpsstatistics;

import com.oppo.ats.android.Device;
import com.oppo.ats.android.Driver;
import com.oppo.ats.android.adblistener.DeviceChangeListener;
import com.oppo.ats.android.adblistener.DeviceManager;
import com.oppo.ats.android.fps.SurfaceFlinger;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    @FXML
    public BorderPane content;
    int fps_index = 0;
    SurfaceFlinger surfaceFlinger;
    ChoiceBox<Device> devices = new ChoiceBox();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        HBox hBox = new HBox();
        Button button1 = new Button("开始");
        Button button2 = new Button("停止");
        ChoiceBox<Integer> refreshRate = new ChoiceBox();
        refreshRate.getItems().setAll(new Integer[]{60, 90, 120});
        refreshRate.setValue(60);
        button2.setDisable(true);
        Label refreshRatelab = new Label("刷新率:", refreshRate);
        Label deviceslab = new Label("已连接手机:", devices);
        refreshRatelab.setContentDisplay(ContentDisplay.RIGHT);
        deviceslab.setContentDisplay(ContentDisplay.RIGHT);
        devices.setMinWidth(200);
        hBox.getChildren().addAll(deviceslab, refreshRatelab, button1, button2);
//        hBox.setAlignment(v, Pos.CENTER_LEFT);
        content.setCenter(ContentView.getInstance());
        content.setTop(hBox);
        reDevices();
        DeviceManager.getInstance().start(new DeviceChangeListener() {
            @Override
            public void doDeviceChanged() {
                System.out.println("链接设备变化！1");
                reDevices();
            }

            @Override
            public void doDeviceConnected() {
                System.out.println("链接设备变化！2");
            }

            @Override
            public void doDeviceDisConneted() {
                System.out.println("链接设备变化！3");
                reDevices();
            }
        });
        button1.setOnAction((ActionEvent e) -> {
            Device device = devices.getValue();
            if (device != null) {
                System.out.println("开始");
                button1.setDisable(true);
                button2.setDisable(false);
                String activity = device.getFocusedPackageActivity() + "#0";
                System.out.println(activity);
                final int[] sub_fps_index = {0};
                int refreshRateValue = refreshRate.getValue();
                surfaceFlinger = new SurfaceFlinger(device, activity, refreshRateValue) {
                    @Override
                    public void callData(Map<String, Float> data) {
                        sub_fps_index[0]++;
                        System.out.println(data);
                        if (sub_fps_index[0] >= (refreshRateValue / 6)) {
                            fps_index++;
                            Platform.runLater(() -> {
                                ContentView.getInstance().addFpsData("[" + fps_index + "," + data.get("fps") + "]");
                            });
                            sub_fps_index[0] = 0;
                        }

                    }
                };
                surfaceFlinger.start();
            } else ToastUtil.toast("未连接任何手机设备！", 800);
        });
        button2.setOnAction((ActionEvent e) -> {
            System.out.println("停止");
            surfaceFlinger.stop();
            fps_index = 0;
            button1.setDisable(false);
            button2.setDisable(true);
            Platform.runLater(() -> {
                ContentView.getInstance().clearData();
            });
        });
    }

    public void reDevices() {
        Platform.runLater(() -> {
            devices.getItems().clear();
            devices.getItems().addAll(Driver.getDevices());
            if (devices.getItems().size() > 0) {
                devices.setValue(devices.getItems().get(0));
                devices.setDisable(false);
            }
            else devices.setDisable(true);
        });
    }
}
