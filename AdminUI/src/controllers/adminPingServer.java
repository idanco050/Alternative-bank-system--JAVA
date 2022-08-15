package controllers;

import javafx.application.Platform;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import util.Constants;
import util.http.HttpClientUtil;

import java.io.IOException;
import java.util.TimerTask;


public class adminPingServer extends TimerTask {

    private int counter;
    private AdminLoginController adminctrl;
    public adminPingServer(AdminLoginController adminctrl) {
        this.adminctrl = adminctrl;
    }

    @Override
    public void run() {


        HttpClientUtil.runAsync(Constants.PING, new Callback() {

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Platform.runLater(() -> adminctrl.setStatusText("Failure contacting server."));
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.code() != 200)
                    Platform.runLater(() -> {
                        adminctrl.setStatusText("Server is down.");
                        adminctrl.setStatusSmiley(response.code());
                    });
                else
                    Platform.runLater(() -> {
                        adminctrl.setStatusText("Server is up and running!");
                        adminctrl.setStatusSmiley(response.code());
                    });
            }
        });
    }
}
