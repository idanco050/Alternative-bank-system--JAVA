package controllers;

import DTO.customerInformationDTO;
import com.google.gson.Gson;
import util.Constants;
import util.http.HttpClientUtil;
import javafx.beans.property.BooleanProperty;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.TimerTask;
import java.util.function.Consumer;

public class customerInformationRefresher extends TimerTask {


    private final Consumer<customerInformationDTO> usersListConsumer;
    private int requestNumber;
    private final BooleanProperty shouldUpdate;


    public customerInformationRefresher(BooleanProperty shouldUpdate, Consumer<customerInformationDTO> usersListConsumer) {
        this.shouldUpdate = shouldUpdate;
        this.usersListConsumer = usersListConsumer;
        requestNumber = 0;
    }

    @Override
    public void run() {

        if (!shouldUpdate.get()) {
            return;
        }
        final int finalRequestNumber = ++requestNumber;

        HttpClientUtil.runAsync(Constants.CUSTOMER_INFO, new Callback() {

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                System.out.println("FAILED");

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.code() != 200)
                {
                    System.out.println("ERROR CODE IS:" + response.code());
                }
                else {
                    Gson gson = new Gson();
                    String jsonString = response.body().string();
                    customerInformationDTO customerInfo = gson.fromJson(jsonString, customerInformationDTO.class);
                    usersListConsumer.accept(customerInfo);
                }
            }
        });
    }
}
