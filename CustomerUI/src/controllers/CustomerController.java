package controllers;

import DTO.assignLoansDTO;
import DTO.scrambleRelevantDTO;

import classes.cLoan;
import classes.operations.Transaction;
import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import util.Constants;
import DTO.customerInformationDTO;
import util.http.HttpClientUtil;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.*;

import static util.Constants.GSON_INSTANCE;
import static util.Constants.REFRESH_RATE;


public class CustomerController {

    private String username;

    private boolean first;

    public CustomerController() {
        autoUpdate = new SimpleBooleanProperty();
        totalUsers = new SimpleIntegerProperty();
    }


    @FXML
    private Label CurrentYazLabel;

    @FXML
    private BorderPane customerBP;

    public BorderPane getCustomerBP(){
        return customerBP;
    }

    @FXML
    private TableView<cLoan> infoTabGivenLoanTable;

    @FXML
    private TableColumn<?, ?> balance;

    @FXML
    private Button chargeButton;

    @FXML
    private Button withdrawButton;

    //@FXML private StatusController httpStatusComponentController;

    ////////////////////////////////////////////////CATEGORIES COMBO BOX ////////////////////////////

    @FXML
    private ComboBox<String> categoriesCB;

    @FXML
    private ComboBox<String> chosenCategoriesCB;

    @FXML
    private Button clearCategoriesButton;
    public void clearCategoriesButtonActionListener(ActionEvent event) {
            chosenCategoriesCB.getItems().clear();
    }


    @FXML
    private Button clearCCBbutton;

    @FXML
    public void clearCCBbuttonActionListener(ActionEvent event) {
        chosenLoansCB.getItems().clear();
    }


    @FXML
    public void categoriesCBActionListener(ActionEvent event) throws Exception {
        String choice = categoriesCB.getValue();
        chosenCategoriesCB.getItems().add(choice);
    }


    void intializeCategoriesComboBox(List<String> catList)
    {
        categoriesCB.getItems().clear();
        for(String cat : catList)
            categoriesCB.getItems().add(cat);
    }


////////////////////////////////////////////////CATEGORIES COMBO BOX ////////////////////////////
///////////information//////////////


    @FXML
    private TableView<cLoan> givenLoansTable;

    @FXML
    private TableColumn<cLoan, String> owner;

    @FXML
    private TableColumn<cLoan, Integer> capital;

    @FXML
    private TableColumn<cLoan, Double> capitalRemaining;

    @FXML
    private TableColumn<cLoan, Double> capitalPaid;

    @FXML
    private TableColumn<cLoan, Integer> monthsPerPayment;

    @FXML
    private TableColumn<cLoan, Integer> nextPaymentOn;

    @FXML
    private TableColumn<cLoan, Integer> dateFinished;

    @FXML
    private TableColumn<cLoan, Double> collectedCapital;

    @FXML
    private TableColumn<cLoan, Double> interestPaid;

    @FXML
    private TableColumn<cLoan, Double> interestRate;

    @FXML
    private TableColumn<cLoan, Integer> remainingCapital;

    @FXML
    private TableColumn<cLoan, Integer> totalMonths;

    @FXML
    private TableColumn<cLoan, String> id;

    @FXML
    private TableColumn<cLoan, String> category;

    @FXML
    private TableColumn<cLoan, Double> interestRemaining;

    @FXML
    private TableColumn<cLoan, Integer> activeSince;

    @FXML
    private TableColumn<cLoan, cLoan.loanStatus> status;

    @FXML
    private Label FilePathLabel;

    public void initializeGivenLoansTable(ObservableList<cLoan> list) {
        id.setCellValueFactory(new PropertyValueFactory<cLoan, String>("id"));
        owner.setCellValueFactory(new PropertyValueFactory<cLoan, String>("ownerName"));
        category.setCellValueFactory(new PropertyValueFactory<cLoan, String>("category"));
        capital.setCellValueFactory(new PropertyValueFactory<cLoan, Integer>("capital"));
        totalMonths.setCellValueFactory(new PropertyValueFactory<cLoan, Integer>("totalMonths"));
        interestRate.setCellValueFactory(new PropertyValueFactory<cLoan, Double>("interestRate"));
        monthsPerPayment.setCellValueFactory(new PropertyValueFactory<cLoan, Integer>("monthsPerPayment"));

        status.setCellValueFactory(new PropertyValueFactory<cLoan, cLoan.loanStatus>("status"));
        interestPaid.setCellValueFactory(new PropertyValueFactory<cLoan, Double>("interestPaid"));
        capitalPaid.setCellValueFactory(new PropertyValueFactory<cLoan, Double>("capitalPaid"));
        collectedCapital.setCellValueFactory(new PropertyValueFactory<cLoan, Double >("moneyInvestedSoFar"));
        remainingCapital.setCellValueFactory(new PropertyValueFactory<cLoan, Integer>("moneyNeeded"));
        activeSince.setCellValueFactory(new PropertyValueFactory<cLoan, Integer>("dateOfActivation"));
        dateFinished.setCellValueFactory(new PropertyValueFactory<cLoan, Integer>("dateFinished"));
        nextPaymentOn.setCellValueFactory(new PropertyValueFactory<cLoan, Integer>("nextPaymentOn"));
        interestRemaining.setCellValueFactory(new PropertyValueFactory<cLoan, Double>("interestRemaining"));
        capitalRemaining.setCellValueFactory(new PropertyValueFactory<cLoan, Double>("capitalRemaining"));

        givenLoansTable.setItems(list);
    }

    @FXML
    void LoadFileButtonListener(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        String currentPath = Paths.get("./Engine/src/resources").toAbsolutePath().normalize().toString();
        fileChooser.setInitialDirectory(new File(currentPath));
        FileChooser.ExtensionFilter fileExtension = new FileChooser.ExtensionFilter("XML files", "*.xml");
        fileChooser.getExtensionFilters().add(fileExtension);
        fileChooser.setTitle("Choose an XML file");
        File xmlFile = fileChooser.showOpenDialog(new Stage());
        if (xmlFile != null) {
            String path = xmlFile.getAbsolutePath();
            File file = new File(path);
            RequestBody body =
                    new MultipartBody.Builder()
                            .addFormDataPart("file1", file.getName(), RequestBody.create(file, MediaType.parse("text/xml")))
                            .build();

            String finalUrl = HttpUrl
                    .parse(Constants.XML_LOAD)
                    .newBuilder()
                    .build()
                    .toString();

            Request request = new Request.Builder()
                    .url(finalUrl)
                    .post(body)
                    .build();

            HttpClientUtil.runAsyncRequest(request, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Platform.runLater(() ->
                    {
                        Alert alert = new Alert(Alert.AlertType.ERROR,"Unknown Error");
                        alert.showAndWait();
                    });
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if(response.code() != 200){
                        Platform.runLater(() ->
                        {
                            try {
                                FilePathLabel.setText(response.body().string());
                                response.body().close();
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        });
                    } else{
                        Platform.runLater(() ->
                        {
                            FilePathLabel.setText("Enter another XML:");
                        });
                    }
                }
            });

        }
        else
            FilePathLabel.setText("null XML file");

    }

//    void startUp(File xmlFile){
//        SchemaJAXB.getDescList(xmlFile.getAbsolutePath());
//        initializer init = new initializer(SchemaJAXB.descList);
//        XMLerrorCheck checker = new XMLerrorCheck(initializer.loanList, initializer.customerList, initializer.categories.getCategories());
//        String checkStr = checker.check();
//        if(!(checkStr=="True")) {
//            FilePathLabel.setText("Invalid: " + checkStr);
//            CurrentYazLabel.setText("Load new XML");
//        }
//        else{
//            setAdminForChildControllers();
//            timeline.resetCurrentDate();
//            CurrentYazLabel.setText("Current Yaz: " + timeline.getCurrentDate());
//            FilePathLabel.setText(xmlFile.getAbsolutePath());
//            initializeComboBox();
//            AdminLoanTable.refresh();
//            AdminCustomerTable.refresh();
//        }
//    }

    @FXML
    private TableView<cLoan> takenLoansTable;

    @FXML
    private TableColumn<cLoan, String> owner1;

    @FXML
    private TableColumn<cLoan, Integer> capital1;

    @FXML
    private TableColumn<cLoan, Double> capitalRemaining1;

    @FXML
    private TableColumn<cLoan, Double> capitalPaid1;

    @FXML
    private TableColumn<cLoan, Integer> monthsPerPayment1;

    @FXML
    private TableColumn<cLoan, Integer> nextPaymentOn1;

    @FXML
    private TableColumn<cLoan, Integer> dateFinished1;

    @FXML
    private TableColumn<cLoan, Double> collectedCapital1;

    @FXML
    private TableColumn<cLoan, Double> interestPaid1;

    @FXML
    private TableColumn<cLoan, Double> interestRate1;

    @FXML
    private TableColumn<cLoan, Integer> remainingCapital1;

    @FXML
    private TableColumn<cLoan, Integer> totalMonths1;

    @FXML
    private TableColumn<cLoan, String> id1;

    @FXML
    private TableColumn<cLoan, String> category1;

    @FXML
    private TableColumn<cLoan, Double> interestRemaining1;

    @FXML
    private TableColumn<cLoan, Integer> activeSince1;

    @FXML
    private TableColumn<cLoan, cLoan.loanStatus> status1;

    @FXML
    private Label currentBalanceLabel;


    public void initializeTakenLoansTable(ObservableList<cLoan> list) {
        id1.setCellValueFactory(new PropertyValueFactory<cLoan, String>("id"));
        owner1.setCellValueFactory(new PropertyValueFactory<cLoan, String>("ownerName"));
        category1.setCellValueFactory(new PropertyValueFactory<cLoan, String>("category"));
        capital1.setCellValueFactory(new PropertyValueFactory<cLoan, Integer>("capital"));
        totalMonths1.setCellValueFactory(new PropertyValueFactory<cLoan, Integer>("totalMonths"));
        interestRate1.setCellValueFactory(new PropertyValueFactory<cLoan, Double>("interestRate"));
        monthsPerPayment1.setCellValueFactory(new PropertyValueFactory<cLoan, Integer>("monthsPerPayment"));

        status1.setCellValueFactory(new PropertyValueFactory<cLoan, cLoan.loanStatus>("status"));
        interestPaid1.setCellValueFactory(new PropertyValueFactory<cLoan, Double>("interestPaid"));
        capitalPaid1.setCellValueFactory(new PropertyValueFactory<cLoan, Double>("capitalPaid"));
        collectedCapital1.setCellValueFactory(new PropertyValueFactory<cLoan, Double >("moneyInvestedSoFar"));
        remainingCapital1.setCellValueFactory(new PropertyValueFactory<cLoan, Integer>("moneyNeeded"));
        activeSince1.setCellValueFactory(new PropertyValueFactory<cLoan, Integer>("dateOfActivation"));
        dateFinished1.setCellValueFactory(new PropertyValueFactory<cLoan, Integer>("dateFinished"));
        nextPaymentOn1.setCellValueFactory(new PropertyValueFactory<cLoan, Integer>("nextPaymentOn"));
        interestRemaining1.setCellValueFactory(new PropertyValueFactory<cLoan, Double>("interestRemaining"));
        capitalRemaining1.setCellValueFactory(new PropertyValueFactory<cLoan, Double>("capitalRemaining"));

        takenLoansTable.setItems(list);
    }

    ////////////////////////////////////////////////LOANS COMBO BOX ////////////////////////////

    @FXML
    private ComboBox<String> loansCB;

    private List<cLoan> loansCBList;

    private List<cLoan> chosenLoansCBList;

    @FXML
    private ComboBox<String> chosenLoansCB;

    void initializeLoansComboBox(List<cLoan> loans)
    {
        loansCBList = loans;
        for(cLoan l : loans)
            loansCB.getItems().add(l.getId());
    }

    @FXML
    void loansCBActionListener(ActionEvent event) throws Exception {
        String choice = loansCB.getValue();
        if (chosenLoansCB.getItems().contains(choice))
            return;
        else
            chosenLoansCB.getItems().add(choice);
    }
    ////////////////////////////////////////////////LOANS COMBO BOX ////////////////////////////

    @FXML
    void scrambleButtonActionListener(ActionEvent event) {

        if (loansCB.getItems().isEmpty() || chosenLoansCB.getItems().isEmpty())
            return;

        List<String> choices = chosenLoansCB.getItems();
        assignLoansDTO assignDTO = new assignLoansDTO(choices, Integer.parseInt(investmentAmountTF.getText()));

        String jsonLoansToInvest = GSON_INSTANCE.toJson(assignDTO, assignLoansDTO.class);
        RequestBody body = RequestBody.create(jsonLoansToInvest, MediaType.parse("application/json charset=UTF-8"));

        String finalUrl = HttpUrl
                .parse(Constants.ASSIGN_LOANS)
                .newBuilder()
                .build()
                .toString();

        Request request = new Request.Builder()
                .url(finalUrl)
                .post(body)
                .build();

        HttpClientUtil.runAsyncRequest(request, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Platform.runLater(() ->
                {
                    scrambleInfoLabel.setText("ERROR");
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.code() != 200){
                    Platform.runLater(() ->
                    {
                        try {
                            scrambleInfoLabel.setText(response.body().string());
                            response.body().close();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });
                } else{
                    Platform.runLater(() ->{
                        String size = "" + chosenLoansCB.getItems().size();
                        scrambleInfoLabel.setText("Successfully added " + size + " chosen loans.");
                        cleanupScramble();
                    });
                }
            }
        });
        refreshAll();
    }






    ////////////////////////////////////////////// TRANSACTION TABLE ////////////////////////////////////////////////

    @FXML
    private TableColumn<Transaction, Double> before;

    @FXML
    private TableColumn<Transaction, Double> after;

    @FXML
    private TableColumn<Transaction, Integer> date;

    @FXML
    private TableColumn<Transaction, Double> amount;

    @FXML
    private TableView<Transaction> transactionTable;

    @FXML
    private TextField trancTextField;

    ////////////////////////////////////////////// TRANSACTION TABLE ////////////////////////////////////////////////

    @FXML
    public void initialize(){
        startInfoRefresh();
        refreshAll();
//        initTransactionTable();
//        intializeCategoriesComboBox();
//        initializeTakenLoansT();
//        initializeGivenLoansT();
//        initializeTakenLoansPaymentTAB();
//        refreshAll();
        ///////////// MORE INIT
    }

    private void refreshAll(){
        refreshBalance();
        givenLoansTable.refresh();
        takenLoansTable.refresh();
        takenLoansPayTable.refresh();
        transactionTable.refresh();
    }

    private void initTransactionTable(ObservableList<Transaction> tranlist)
    {
        date.setCellValueFactory(new PropertyValueFactory<Transaction, Integer>("date"));
        amount.setCellValueFactory(new PropertyValueFactory<Transaction, Double>("amount"));
        before.setCellValueFactory(new PropertyValueFactory<Transaction, Double>("balanceBefore"));
        after.setCellValueFactory(new PropertyValueFactory<Transaction, Double>("balanceAfter"));
        transactionTable.setItems(tranlist);
    }


    @FXML
    void chargeButtonActionListener(ActionEvent event) {
        int amount = Integer.parseInt(trancTextField.getText());

        //noinspection ConstantConditions
        String finalUrl = HttpUrl
                .parse(Constants.ADD_MONEY)
                .newBuilder()
                .addQueryParameter("username", username)
                .addQueryParameter("amount", Integer.toString(amount))
                .build()
                .toString();

        HttpClientUtil.runAsync(finalUrl, new Callback() {

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Platform.runLater(() ->
                        System.out.println("Something went wrong: " + e.getMessage())
                );
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.code() != 200) {
                    String responseBody = response.body().string();
                    Platform.runLater(() ->
                            System.out.println("Something went wrong: " + responseBody)
                    );
                } else {
                    Platform.runLater(() -> {
                        trancTextField.clear();
                        refreshBalance();
                    });
                }
            }
        });}

    void refreshBalance(){
        String finalUrl = HttpUrl
                .parse(Constants.GET_BALANCE)
                .newBuilder()
                .build()
                .toString();

        HttpClientUtil.runAsync(finalUrl, new Callback() {

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Platform.runLater(() ->
                        System.out.println("Something went wrong: " + e.getMessage())
                );
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.code() != 200) {
                    String responseBody = response.body().string();
                    Platform.runLater(() ->
                            System.out.println("Something went wrong: " + responseBody)
                    );
                } else {
                    Platform.runLater(() -> {
                        try {
                            currentBalanceLabel.setText("Current Balance:" + response.body().string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
                }
            }
        });


    }


    @FXML
    void withdrawButtonActionListener(ActionEvent event) {


        int drawAmount = Integer.parseInt(trancTextField.getText());
//            if (drawAmount > theCustomer.getBalance())
//                trancTextField.setText("Cannot draw more than the customer's balance. Enter amount again.");

        //noinspection ConstantConditions
        String finalUrl = HttpUrl
                .parse(Constants.DRAW_MONEY)
                .newBuilder()
                .addQueryParameter("username", username)
                .addQueryParameter("amount", Integer.toString(drawAmount))
                .build()
                .toString();

        HttpClientUtil.runAsync(finalUrl, new Callback() {

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Platform.runLater(() ->
                        System.out.println("Something went wrong: " + e.getMessage())
                );
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.code() == 304) { //SC NOT MODIFIED
                    String responseBody = response.body().string();
                    Platform.runLater(() ->
                            System.out.println("Something went wrong: " + responseBody)
                    );
                    trancTextField.setText("Not enough money in the account."); // ADD CURRENT BALANCE HERe?

                }
                if (response.code() != 200) {
                    String responseBody = response.body().string();
                    Platform.runLater(() ->
                            System.out.println("Something went wrong: " + responseBody)
                    );
                } else {
                    Platform.runLater(() -> {
                        refreshBalance();
                        trancTextField.clear();
                    });
                }
            }
        });
    }






//    // SCRAMBLE

    @FXML
    private TextField minInterestTF;

    @FXML
    private TextField maxShareTF;

    @FXML
    private TextField minYazTF;

    @FXML
    private StackPane stackPane1;

    @FXML
    private TextField investmentAmountTF;

    @FXML
    private TextField maxInvolvedLoansTF;

//    @FXML
//    private LoanerLoansTableController LLtableController;

    private ObservableList<cLoan> userFilteredLoanList =  FXCollections.observableArrayList();
    @FXML
    private Label scrambleInfoLabel;

    @FXML
    void findLoansButtonListener(ActionEvent event) throws InterruptedException {
        int minInterest = -1;
        int maxShare = -1;
        int minYaz =-1;
        int investmentAmount = 0;
        int maxInvolvedLoans = -1;
        loansCBList = null;

        List<String> categories = chosenCategoriesCB.getItems();

        String minInterestText = minInterestTF.getText();
        if(minInterestText.matches("[0-9]+"))
            minInterest =  Integer.parseInt(minInterestText);


        String maxShareText = maxShareTF.getText();
        if(maxShareText.matches("[0-9]+"))
            maxShare =  Integer.parseInt(maxShareText);

        String minYazText = minYazTF.getText();
        if(minYazText.matches("[0-9]+"))
            minYaz =  Integer.parseInt(minYazText);


        String investmentAmountText = investmentAmountTF.getText();
        if(investmentAmountText.matches("[0-9]+"))
            investmentAmount =  Integer.parseInt(investmentAmountText);

        String maxInvolvedLoansText = maxInvolvedLoansTF.getText();
        if(maxInvolvedLoansText.matches("[0-9]+"))
            maxInvolvedLoans =  Integer.parseInt(maxInvolvedLoansText);

        if(investmentAmount == 0)
            investmentAmountTF.setText("Invest something.");
        else {
            scrambleRelevantDTO scrambleDTO = new scrambleRelevantDTO(username, categories, minInterest, minYaz, maxInvolvedLoans, investmentAmount, maxShare);
            String jsonSelectedCategories = GSON_INSTANCE.toJson(scrambleDTO, scrambleRelevantDTO.class);
            RequestBody body = RequestBody.create(jsonSelectedCategories, MediaType.parse("application/json charset=UTF-8"));

            String finalUrl = HttpUrl
                    .parse(Constants.SCRAMBLE)
                    .newBuilder()
                    .build()
                    .toString();

            Request request = new Request.Builder()
                    .url(finalUrl)
                    .post(body)
                    .build();

            HttpClientUtil.runAsyncRequest(request, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Platform.runLater(() ->
                    {
                        Alert alert = new Alert(Alert.AlertType.ERROR,"Unknown Error");
                        alert.showAndWait();
                    });
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if(response.code() != 200){
                        Platform.runLater(() ->
                        {
                            try {
                                scrambleInfoLabel.setText(response.body().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        });
                    } else{
                        Platform.runLater(() ->
                        {
                            try {
                                List<cLoan> goodLoans = new ArrayList<>();
                                String jsonOfClientString = response.body().string();
                                response.body().close();
                                cLoan[] relevantLoansArray = GSON_INSTANCE.fromJson(jsonOfClientString, cLoan[].class);
                                ObservableList<cLoan> relevantLoansList = FXCollections.observableArrayList();
                                relevantLoansList.clear();
                                relevantLoansList.addAll(Arrays.asList(relevantLoansArray));
                                goodLoans = Arrays.asList(relevantLoansArray);
                                String size = "" + goodLoans.size();
                                scrambleInfoLabel.setText("Added " + size + " loans to the combo box. Click each loan to add it to your chosen list.");
                                initializeLoansComboBox(goodLoans);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        });
                    }
                }
            });


        }

    }

    private void cleanupScramble(){
        chosenCategoriesCB.getItems().clear();
        chosenLoansCB.getItems().clear();
        loansCB.getItems().clear();
        minInterestTF.clear();
        maxShareTF.clear();
        minYazTF.clear();
        investmentAmountTF.clear();
        maxInvolvedLoansTF.clear();
    }


    //PAYMENT TAB
    @FXML
    private TableView<cLoan> takenLoansPayTable;

    @FXML
    private TableColumn<cLoan, Integer> capital11;

    @FXML
    private TableColumn<cLoan, Integer> nextPaymentOn11;

    @FXML
    private TableColumn<cLoan, Integer> totalMonths11;

    @FXML
    private TableColumn<cLoan, String> id11;

    @FXML
    private TableColumn<cLoan, Double> remainingCapital11;

    @FXML
    private TableColumn<cLoan, cLoan.loanStatus> status11;
    public void initializeTakenLoansPaymentTAB(ObservableList<cLoan> list) {
        id11.setCellValueFactory(new PropertyValueFactory<cLoan, String>("id"));
        capital11.setCellValueFactory(new PropertyValueFactory<cLoan, Integer>("capital"));
        totalMonths11.setCellValueFactory(new PropertyValueFactory<cLoan, Integer>("totalMonths"));
        status11.setCellValueFactory(new PropertyValueFactory<cLoan, cLoan.loanStatus>("status"));
        nextPaymentOn11.setCellValueFactory(new PropertyValueFactory<cLoan, Integer>("nextPaymentOn"));
        remainingCapital11.setCellValueFactory(new PropertyValueFactory<cLoan, Double>("capitalRemaining"));

        takenLoansPayTable.setItems(list);
        takenLoansTable.refresh();

    }
    @FXML
    private Label paymentErrorLabel;

    @FXML
    void payButtonActionListener(ActionEvent event) {
        paymentErrorLabel.setText("");
        cLoan loan = takenLoansPayTable.getSelectionModel().getSelectedItem();
            if(loan == null)
                return;
            else {
                String finalUrl = HttpUrl
                        .parse(Constants.LOAN_PAYMENT)
                        .newBuilder()
                        .addQueryParameter("id", loan.getId())
                        .build()
                        .toString();

                HttpClientUtil.runAsync(finalUrl, new Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                        Platform.runLater(() ->
                                System.out.println("Failure: " + e.getMessage())
                        );
                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        if (response.code() != 200) {
                            String responseBody = response.body().string();
                            Platform.runLater(() ->
                            {
                                paymentErrorLabel.setText(responseBody);
                                response.body().close();
                            });
                        } else {
                            Platform.runLater(() -> {
                                paymentErrorLabel.setText("Payment successful.");
                            });
                        }
                    }
                });
            }
       refreshAll();
    }

    @FXML
    void closeLoanButtonActionListener(ActionEvent event) {
        paymentErrorLabel.setText("");
        cLoan loan = takenLoansPayTable.getSelectionModel().getSelectedItem();
        String finalUrl = HttpUrl
                .parse(Constants.CLOSE_LOAN)
                .newBuilder()
                .addQueryParameter("id", loan.getId())
                .build()
                .toString();

        HttpClientUtil.runAsync(finalUrl, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Platform.runLater(() ->
                        System.out.println("Failure: " + e.getMessage())
                );
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.code() != 200) {
                    String responseBody = response.body().string();
                    Platform.runLater(() ->
                    {
                        paymentErrorLabel.setText(responseBody);
                        response.body().close();
                    });
                } else {
                    Platform.runLater(() -> {
                        paymentErrorLabel.setText("Loan closed successfully.");
                    });
                }
            }
        });

        refreshAll();
    }

//    public void setLLtableController(LoanerLoansTableController LLtableController) {
//        this.LLtableController = LLtableController;
//    }
//
    @FXML
    private ListView<String> notificationListView;

    private void refreshNotifications(List<String> list){
        notificationListView.getItems().clear();
        int size = list.size();
        if (size == 0)
            return;
        for (int i = size-1; i >= 0; i--)
        {
            notificationListView.getItems().add(list.get(i));
        }
    }


    public void setUser(String user) {
      username = user;
    }

    private Timer timer;
    private TimerTask infoRefresher;
    private final BooleanProperty autoUpdate;
    private final IntegerProperty totalUsers;

    public void startInfoRefresh(){
        autoUpdate.set(true);
        infoRefresher = new customerInformationRefresher(
                autoUpdate,
                this::customerInfoRefresh);
        timer = new Timer();
        timer.schedule(infoRefresher, REFRESH_RATE, REFRESH_RATE);
    }

    private void customerInfoRefresh(customerInformationDTO infoDTO) {
        if (username.equals("nullstring"))
            return;
        else {
            Platform.runLater(() -> {
                Integer yaz = infoDTO.getYaz();
                CurrentYazLabel.setText("Current Yaz: " + yaz.toString());

                ObservableList<cLoan> takenLoansList = FXCollections.observableArrayList(infoDTO.getTakenLoansList());
                ObservableList<cLoan> givenLoansList = FXCollections.observableArrayList(infoDTO.getGivenLoansList());
                ObservableList<Transaction> transactionsList = FXCollections.observableArrayList(infoDTO.getTransactionsList());
                ObservableList<cLoan> takenLoansPayList = FXCollections.observableArrayList(infoDTO.getTakenLoansPayList());
                ObservableList<String> notificationsList = FXCollections.observableArrayList(infoDTO.getNotificationsList());
                ObservableList<cLoan> loansForSaleList = FXCollections.observableArrayList(infoDTO.getLoansForSaleList());
                ObservableList<String> categoriesList = FXCollections.observableArrayList(infoDTO.getCategoriesList());

                List<String> takenIDs = new ArrayList<>();
                for (cLoan l : takenLoansPayList)
                    takenIDs.add(l.getId());


                int choice;
                choice = takenLoansPayTable.getSelectionModel().getSelectedIndex();
                initializeTakenLoansPaymentTAB(takenLoansPayList);
                takenLoansPayTable.getSelectionModel().select(choice);
                takenLoansPayTable.getFocusModel().focus(choice);

                initSellCB(takenIDs);
                initBuyCB(loansForSaleList);
                intializeCategoriesComboBox(categoriesList);
                initializeGivenLoansTable(givenLoansList);
                initializeTakenLoansTable(takenLoansList);
                initTransactionTable(transactionsList);
                refreshNotifications(notificationsList);
                refreshAll();
            });
        }
    }

//    public void updateHttpLine(String data) {
//    }



//    public void updateHttpLine(String line) {
//        httpStatusComponentController.addHttpStatusLine(line);
//    }\

    @FXML
    private TextField yazPerPaymentText;
    @FXML
    private TextField yazText;
    @FXML
    private TextField idText;
    @FXML
    private TextField capitalText;
    @FXML
    private TextField interestText;
    @FXML
    private TextField categoryText;
    @FXML
    private Label enterLoanLabel;

    @FXML
    void enterLoanButtonActionListener(ActionEvent event) {

        String id = idText.getText();
        String cat = categoryText.getText();
        String capital = capitalText.getText();
        String yaz = yazText.getText();
        String interest = interestText.getText();
        String yazPerPayment = yazPerPaymentText.getText();

        String finalUrl = HttpUrl
                .parse(Constants.LOAN_ENTRY)
                .newBuilder()
                .addQueryParameter("id", id)
                .addQueryParameter("category", cat)
                .addQueryParameter("capital", capital)
                .addQueryParameter("yaz", yaz)
                .addQueryParameter("interest", interest)
                .addQueryParameter("yazPerPayment", yazPerPayment)
                .build()
                .toString();

        HttpClientUtil.runAsync(finalUrl, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Platform.runLater(() ->
                        System.out.println("Failure: " + e.getMessage())
                );
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.code() != 200) {
                    String responseBody = response.body().string();
                    Platform.runLater(() ->
                    {
                        enterLoanLabel.setText(responseBody);
                        response.body().close();
                    });
                } else {
                    Platform.runLater(() -> {
                        cleanupLoanFields();
                    });
                }
            }
        });
    }
    private void cleanupLoanFields(){
        yazPerPaymentText.clear();
        yazText.clear();
        idText.clear();
        capitalText.clear();
        interestText.clear();
        categoryText.clear();
        enterLoanLabel.setText("Enter another loan:");

    }
    @FXML
    private ComboBox<String> sellLoanCB;

    @FXML
    private ComboBox<String> buyLoanCB;

    @FXML
    private Button sellLoanButton;

    @FXML
    private Button buyLoanButton;

    @FXML
    private Label saleInfoLabel;

    @FXML
    void sellLoanButtonListener(ActionEvent event) {
        String choice = sellLoanCB.getValue();
        if(choice == null)
            return;
        else {
            String finalUrl = HttpUrl
                    .parse(Constants.SELL_LOAN)
                    .newBuilder()
                    .addQueryParameter("id", choice)
                    .build()
                    .toString();
            HttpClientUtil.runAsync(finalUrl, new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    Platform.runLater(() -> {
                        System.out.println("Failure: " + e.getMessage());
                        saleInfoLabel.setText(e.getMessage());
                    });
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    if (response.code() != 200) {
                        String responseBody = response.body().string();
                        Platform.runLater(() ->
                        {
                            saleInfoLabel.setText(responseBody);
                            response.body().close();
                        });
                    } else {
                        Platform.runLater(() -> {
                            saleInfoLabel.setText("Listing of " + sellLoanCB.getValue() + " successful.");
                            sellLoanCB.getItems().clear();
                        });
                    }
                }
            });
        }
    }

    @FXML
    void buyLoanButtonListener(ActionEvent event) {
        String choice = buyLoanCB.getValue();
        String[] stringArr = choice.split(":");
        choice = stringArr[0];
        if(choice == null)
            return;
        else {
            String finalUrl = HttpUrl
                    .parse(Constants.BUY_LOAN)
                    .newBuilder()
                    .addQueryParameter("id", choice)
                    .build()
                    .toString();
            HttpClientUtil.runAsync(finalUrl, new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    Platform.runLater(() -> {
                        System.out.println("Failure: " + e.getMessage());
                        saleInfoLabel.setText(e.getMessage());
                    });
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    if (response.code() != 200) {
                        String responseBody = response.body().string();
                        Platform.runLater(() ->
                        {
                            saleInfoLabel.setText(responseBody);
                            response.body().close();
                        });
                    } else {
                        Platform.runLater(() -> {
                            try {
                                saleInfoLabel.setText(response.body().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            buyLoanCB.getItems().clear();
                        });
                    }
                }
            });
        }

    }

    private void initSellCB(List<String> takenLoans){
        for (String id : takenLoans)
        {
            if (!sellLoanCB.getItems().contains(id))
                sellLoanCB.getItems().add(id);
        }
    }


    private void initBuyCB(ObservableList<cLoan> loansForSale){
        String id;
        for (cLoan loan : loansForSale)
        {
            id = loan.getId() + ": " + loan.getCapitalRemaining() +"$";
            if (!buyLoanCB.getItems().contains(id))
                buyLoanCB.getItems().add(loan.getId() + ": " + loan.getCapitalRemaining() +"$");
        }
    }

}
