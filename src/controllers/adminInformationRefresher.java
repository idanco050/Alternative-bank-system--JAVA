package controllers;

import DTO.adminInformationDTO;
import com.google.gson.Gson;
import javafx.beans.property.BooleanProperty;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import util.Constants;
import util.http.HttpClientUtil;

import java.io.IOException;
import java.util.TimerTask;
import java.util.function.Consumer;

public class adminInformationRefresher extends TimerTask {


    private final Consumer<adminInformationDTO> consume;
    private int requestNumber;
    private AdminController adminctrl;
    private final BooleanProperty shouldUpdate;


    public adminInformationRefresher(BooleanProperty shouldUpdate, Consumer<adminInformationDTO> consume, AdminController adminctrl) {
        this.shouldUpdate = shouldUpdate;
        this.consume = consume;
        this.adminctrl = adminctrl;
        requestNumber = 0;
    }

    @Override
    public void run() {

        if (!shouldUpdate.get()) {
            return;
        }
        String name = adminctrl.getComboBoxCustomer();

        String finalUrl = HttpUrl
                .parse(Constants.ADMIN_INFO)
                .newBuilder()
                .addQueryParameter("name", name)
                .build()
                .toString();

        HttpClientUtil.runAsync(finalUrl, new Callback() {

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                System.out.println("FAILURE");

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
                    adminInformationDTO adminInfo = gson.fromJson(jsonString, adminInformationDTO.class);
                    consume.accept(adminInfo);
                }
            }
        });
    }
}
