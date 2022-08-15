package controllers.tables;

import classes.cLoan;
import controllers.AdminController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class LoanTableController implements Initializable {
    @FXML
    private TableView<cLoan> table;

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

    @FXML private AdminController adminctrl;


//    ObservableList<Loan> list = FXCollections.observableList(initializer.loanList);

    public void initialize(URL url, ResourceBundle Resources) {

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

    }


    public void refreshItems(List<cLoan> list)
    {
        ObservableList<cLoan> loanList = FXCollections.observableList(list);
//        id.setCellValueFactory(new PropertyValueFactory<cLoan, String>("id"));
//        owner.setCellValueFactory(new PropertyValueFactory<cLoan, String>("ownerName"));
//        category.setCellValueFactory(new PropertyValueFactory<cLoan, String>("category"));
//        capital.setCellValueFactory(new PropertyValueFactory<cLoan, Integer>("capital"));
//        totalMonths.setCellValueFactory(new PropertyValueFactory<cLoan, Integer>("totalMonths"));
//        interestRate.setCellValueFactory(new PropertyValueFactory<cLoan, Double>("interestRate"));
//        monthsPerPayment.setCellValueFactory(new PropertyValueFactory<cLoan, Integer>("monthsPerPayment"));
//
//        status.setCellValueFactory(new PropertyValueFactory<cLoan, cLoan.loanStatus>("status"));
//        interestPaid.setCellValueFactory(new PropertyValueFactory<cLoan, Double>("interestPaid"));
//        capitalPaid.setCellValueFactory(new PropertyValueFactory<cLoan, Double>("capitalPaid"));
//        collectedCapital.setCellValueFactory(new PropertyValueFactory<cLoan, Double >("moneyInvestedSoFar"));
//        remainingCapital.setCellValueFactory(new PropertyValueFactory<cLoan, Integer>("moneyNeeded"));
//        activeSince.setCellValueFactory(new PropertyValueFactory<cLoan, Integer>("dateOfActivation"));
//        dateFinished.setCellValueFactory(new PropertyValueFactory<cLoan, Integer>("dateFinished"));
//        nextPaymentOn.setCellValueFactory(new PropertyValueFactory<cLoan, Integer>("nextPaymentOn"));
//        interestRemaining.setCellValueFactory(new PropertyValueFactory<cLoan, Double>("interestRemaining"));
//        capitalRemaining.setCellValueFactory(new PropertyValueFactory<cLoan, Double>("capitalRemaining"));

        table.setItems(loanList);
    }

    public void setAdminctrl(AdminController adminctrl) {
        this.adminctrl = adminctrl;
    }

}
