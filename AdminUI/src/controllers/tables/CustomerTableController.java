package controllers.tables;

import classes.Customer;
import classes.cCustomer;
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
import manager.initializer;

public class CustomerTableController implements Initializable {

    @FXML
    private TableView<cCustomer> table;

    @FXML
    private TableColumn<cCustomer, Double> balance;

    @FXML
    private TableColumn<cCustomer, String> name;

    @FXML private AdminController adminctrl;
//
//    ObservableList<Customer> list = FXCollections.observableList(initializer.customerList);


    @Override
    public void initialize(URL location, ResourceBundle resources) {


    }

    public void refreshItems(List<cCustomer> list)
    {
        ObservableList<cCustomer> customerList = FXCollections.observableList(list);
        name.setCellValueFactory(new PropertyValueFactory<cCustomer, String>("name"));
        balance.setCellValueFactory(new PropertyValueFactory<cCustomer, Double>("balance"));

        table.setItems(customerList);
    }

    public void setAdminctrl(AdminController adminctrl) {
        this.adminctrl = adminctrl;
    }
}
