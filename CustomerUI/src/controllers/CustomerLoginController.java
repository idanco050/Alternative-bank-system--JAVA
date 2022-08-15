package controllers;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Font;
import util.Constants;
import util.http.HttpClientUtil;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import java.io.IOException;
import java.lang.reflect.Array;
import java.net.URL;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;

public class CustomerLoginController {

    @FXML
    public Label errorMessageLabel;

    private CustomerController customerController;

    private final StringProperty errorMessageProperty = new SimpleStringProperty();

    @FXML
    private BorderPane loginBP;

    @FXML
    private TextField loginTextField;

    @FXML
    private Button loginButton;

    @FXML
    private Label userLabel;


    @FXML
    public void initialize() {
        errorMessageLabel.textProperty().bind(errorMessageProperty);
        startPingTask();
        customerController = new CustomerController();
    }




    @FXML
    private void loginButtonActionListener(ActionEvent event) {

        String userName = loginTextField.getText();
        if (userName.isEmpty()) {
            errorMessageProperty.set("User name is empty. You can't login with empty user name");
            return;
        }

        String finalUrl = HttpUrl
                .parse(Constants.LOGIN_PAGE)
                .newBuilder()
                .addQueryParameter("username", userName)
                .build()
                .toString();

        HttpClientUtil.runAsync(finalUrl, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Platform.runLater(() ->
                        errorMessageProperty.set("1. Something went wrong: " + e.getMessage())
                );
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.code() != 200) {
                    String responseBody = response.body().string();
                    Platform.runLater(() ->
                            errorMessageProperty.set("2. Something went wrong: " + responseBody)
                    );
                } else {
                    Platform.runLater(() -> {
                        userLabel.setText("Current user: " + userName);
                        FXMLLoader loader = new FXMLLoader();
                        URL url = getClass().getResource("/controllers/CustomerController.fxml");
                        loader.setLocation(url);
                        BorderPane customerBP = null;
                        try {
                            customerBP = loader.load(url.openStream());
                            customerController = loader.getController();
                            customerController.setUser(userName);
                            display(customerBP);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
                }
            }
        });
    }

    private void display(BorderPane bp){
        loginBP.setCenter(bp.getCenter());
        loginBP.setRight(bp.getRight());
        loginBP.setLeft(bp.getRight());
        //loginBP.setBottom(bp.getBottom());
        loginBP.setTop(bp.getTop());
    }


    @FXML
    private void userNameKeyTyped(KeyEvent event) {
        errorMessageProperty.set("");
    }

    @FXML
    private void quitButtonClicked(ActionEvent e) {
        Platform.exit();
    }

    public void setCustomerController(CustomerController customerController) {
        this.customerController = customerController;
    }

    @FXML
    private Label serverStatusLabel;
    private Timer pingTimer;
    private pingServer ping;
    public void startPingTask(){
        ping = new pingServer(this);
        pingTimer = new Timer();
        pingTimer.schedule(ping, 2000, 3000);
    }

    @FXML
    private Label smiley;

    public void setStatusText(String str)
    {
        serverStatusLabel.setText(str);
    }

    public void setStatusSmiley(int code)
    {
        // smileyStr = "☻☺";
        switch (code){
            case 200:
                smiley.setText("☺");
                errorMessageProperty.set("");
                break;
            default:
                smiley.setText("☻");
                break;

        }
    }

}
