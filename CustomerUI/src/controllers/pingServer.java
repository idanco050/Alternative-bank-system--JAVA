package controllers;

import classes.Customer;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import util.Constants;
import util.http.HttpClientUtil;

import java.io.IOException;
import java.util.TimerTask;


public class pingServer extends TimerTask {

    private int counter;
    private CustomerLoginController ctrl;
    public pingServer(CustomerLoginController ctrl) {
        this.ctrl = ctrl;
    }

    @Override
    public void run() {


        HttpClientUtil.runAsync(Constants.PING, new Callback() {

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Platform.runLater(() -> ctrl.setStatusText("Failure contacting server."));
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.code() != 200)
                    Platform.runLater(() -> {
                        ctrl.setStatusText("Server is down.");
                        ctrl.setStatusSmiley(response.code());
                    });
                else
                    Platform.runLater(() -> {
                        ctrl.setStatusText("Server is up and running!");
                        ctrl.setStatusSmiley(response.code());
                    });
            }
        });
    }
}
