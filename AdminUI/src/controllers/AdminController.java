package controllers;

import DTO.adminInformationDTO;
import DTO.customerInformationDTO;
import classes.Customer;
import classes.Loan;
import classes.cCustomer;
import classes.cLoan;
import classes.operations.Transaction;
import controllers.tables.CustomerTableController;
import controllers.tables.LoanTableController;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import manager.initializer;
import manager.timeline;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import util.Constants;
import util.http.HttpClientUtil;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static util.Constants.REFRESH_RATE;

public class AdminController {

    @FXML
    private HBox AdminBottomHBox;
    @FXML
    private Label adminErrorLabel;
    @FXML
    private TableView<cLoan> loanTableView;
    @FXML
    private LoanTableController loanTableViewController;
    @FXML
    private TableView<cCustomer> customerTableView;
    @FXML
    private CustomerTableController customerTableViewController;

    @FXML
    private BorderPane mainBorderPane;

    private String adminName;

    @FXML
    private Label CurrentYazLabel;

    @FXML
    private Label FilePathLabel;

    @FXML
    private ComboBox<String> customersCB;

    List<cLoan> loanList = new ArrayList<>();
    List<cCustomer> customerList = new ArrayList<>();

    public AdminController(){


        autoUpdate = new SimpleBooleanProperty();
        totalUsers = new SimpleIntegerProperty();
    }

    @FXML
    private Button YazButton;

    @FXML
    private Button LoadFileButton;

    @FXML
    void YazButtonActionListener(ActionEvent event) {

            String finalUrl = HttpUrl
                    .parse(Constants.YAZ_UP)
                    .newBuilder()
                    .build()
                    .toString();

            HttpClientUtil.runAsync(finalUrl, new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    Platform.runLater(() ->
                            System.out.println("Didn't move time!  " + e.getMessage())
                    );
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    if (response.code() != 200) {
                        Platform.runLater(() ->
                        {
                            try {
                                adminErrorLabel.setText(response.body().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            response.body().close();
                        });
                    } else {
                        Platform.runLater(() -> {
                            adminErrorLabel.setText("Moved to next yaz successfully.");
                        });
                    }
                }
            });


    }

    @FXML
    private void initialize(){
        loanTableViewController.setAdminctrl(this);
        customerTableViewController.setAdminctrl(this);
        startInfoRefresh();
    }

    private Timer timer;
    private TimerTask infoRefresher;
    private final BooleanProperty autoUpdate;
    private final IntegerProperty totalUsers;

    public void startInfoRefresh(){
        autoUpdate.set(true);
        infoRefresher = new adminInformationRefresher(autoUpdate, this::adminInfoRefresh, this);
        timer = new Timer();
        timer.schedule(infoRefresher, REFRESH_RATE, REFRESH_RATE);
    }

    private void adminInfoRefresh(adminInformationDTO infoDTO) {
        if (adminName.equals("nullstring"))
            return;
        else {
            Platform.runLater(() -> {
                Integer yaz = infoDTO.getYaz();
                CurrentYazLabel.setText("Current Yaz: " + yaz.toString());

                loanList = infoDTO.getcLoansList();
                loanTableViewController.refreshItems(loanList);

                customerList = infoDTO.getcCustomerList();
                customerTableViewController.refreshItems(customerList);
                initLoanInfoCB();

                newLoansLabel.setText(infoDTO.getNewLoans()!=-1 ? "" + infoDTO.getNewLoans() : "");
                givenPendingLabel.setText(infoDTO.getgPending()!=-1 ? "" + infoDTO.getgPending() : "");
                takenPendingLabel.setText(infoDTO.gettPending()!=-1 ? "" + infoDTO.gettPending() : "");

                givenActiveLabel.setText(infoDTO.getgActive()!=-1 ? "" + infoDTO.getgActive() : "");
                takenActiveLabel.setText(infoDTO.gettActive()!=-1 ? "" + infoDTO.gettActive() : "");

                givenRiskLabel.setText(infoDTO.getgRisk()!=-1 ? "" + infoDTO.getgRisk() : "");
                takenRiskLabel.setText(infoDTO.gettRisk()!=-1 ? "" + infoDTO.gettRisk() : "");

                givenFinishedLabel.setText(infoDTO.getgFinished()!=-1 ? "" + infoDTO.getgFinished() : "");
                takenFinishedLabel.setText(infoDTO.gettFinished()!=-1 ? "" + infoDTO.gettFinished() : "");
            });
        }
    }

/////////////////////////////////////////////////////// LOAN INFO ///////////////////////////////////

    @FXML
    private ComboBox<String> loanInfoCB;

    @FXML
    private Label newLoansLabel;

    @FXML
    private Label givenPendingLabel;

    @FXML
    private Label takenPendingLabel;

    @FXML
    private Label givenActiveLabel;

    @FXML
    private Label takenActiveLabel;

    @FXML
    private Label takenRiskLabel;

    @FXML
    private Label givenRiskLabel;

    @FXML
    private Label givenFinishedLabel;

    @FXML
    private Label takenFinishedLabel;

    @FXML
    void loanInfoCBActionListener(ActionEvent event) {
//        String choice = loanInfoCB.getValue();
//        loanInfoCB.setValue(choice);
    }


    public void setAdmin(String user)
    {
        this.adminName = user;
    }

    public String getComboBoxCustomer(){
        String str = loanInfoCB.getValue();
        if(str == null || str.equals("Choose:"))
            return null;
        else
            return loanInfoCB.getValue();
    }


    private void initLoanInfoCB(){
        loanInfoCB.getItems().clear();
        for (cCustomer cus : customerList)
        {
            loanInfoCB.getItems().add(cus.getName());
        }
    }

}
