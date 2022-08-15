package controllers;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import util.Constants;
import util.http.HttpClientUtil;

import java.io.IOException;
import java.net.URL;
import java.util.Timer;

public class AdminLoginController {

    @FXML
    public Label errorMessageLabel;

    private AdminController adminController;

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
        adminController = new AdminController();
    }




    @FXML
    private void loginButtonActionListener(ActionEvent event) {

        String userName = loginTextField.getText();
        if (userName.isEmpty()) {
            errorMessageProperty.set("User name is empty. You can't login with empty user name");
            return;
        }

        String finalUrl = HttpUrl
                .parse(Constants.ADMIN_LOGIN)
                .newBuilder()
                .addQueryParameter("username", userName)
                .build()
                .toString();

        HttpClientUtil.runAsync(finalUrl, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Platform.runLater(() ->
                        errorMessageProperty.set("Something went wrong: " + e.getMessage())
                );
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.code() != 200) {
                    String responseBody = response.body().string();
                    Platform.runLater(() ->
                            errorMessageProperty.set("Something went wrong: " + responseBody)
                    );
                } else {
                    Platform.runLater(() -> {
                        userLabel.setText("Current user: " + userName);
                        FXMLLoader loader = new FXMLLoader();
                        URL url = getClass().getResource("/controllers/AdminController.fxml");
                        loader.setLocation(url);
                        BorderPane adminBP = null;
                        try {
                            adminBP = loader.load(url.openStream());
                            adminController = loader.getController();
                            adminController.setAdmin(userName);
                            display(adminBP);
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
        loginBP.setBottom(bp.getBottom());
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


    @FXML
    private Label serverStatusLabel;
    private Timer pingTimer;
    private adminPingServer ping;
    public void startPingTask(){
        ping = new adminPingServer(this);
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
        // smileyStr = "☼○";
        switch (code){
            case 200:
                smiley.setText("☼");
                errorMessageProperty.set("");
                break;
            default:
                smiley.setText("○");
                break;

        }
    }

}
