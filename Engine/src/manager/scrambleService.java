package manager;

import classes.Customer;
import classes.Loan;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import java.util.List;

public class scrambleService  extends Service<ObservableList<Loan>> {
    private Customer theCustomer;
    private int minInterest;
    private int minMonths;
    private int maxOpenLoans;
    private int amount;
    private int maxShare;
    private List<String> categories;
    private ObservableList<Loan> userFilteredLoanList =  FXCollections.observableArrayList();
    private List<Loan> goodLoans;
    private int maxProgress = 500;
    private int sleepTime = 300;


    public Customer getTheCustomer() {
        return theCustomer;
    }

    public void setTheCustomer(Customer theCustomer) {
        this.theCustomer = theCustomer;
    }

    public int getMinInterest() {
        return minInterest;
    }

    public void setMinInterest(int minInterest) {
        this.minInterest = minInterest;
    }

    public int getMinMonths() {
        return minMonths;
    }

    public void setMinMonths(int minMonths) {
        this.minMonths = minMonths;
    }

    public int getMaxOpenLoans() {
        return maxOpenLoans;
    }

    public void setMaxOpenLoans(int maxOpenLoans) {
        this.maxOpenLoans = maxOpenLoans;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getMaxShare() {
        return maxShare;
    }

    public void setMaxShare(int maxShare) {
        this.maxShare = maxShare;
    }

    public List<String> getCategories() {
        return categories;
    }

    public void setCategories(List<String> categories) {
        this.categories = categories;
    }

    public void setGoodLoans(List<Loan> goodLoans) {
        this.goodLoans = goodLoans;
    }

    public scrambleService(Customer theCustomer, int minInterest, int minMonths, int maxOpenLoans, int amount, int maxShare, List<String> categories, List<Loan> goodLoans) {
        this.theCustomer = theCustomer;
        this.minInterest = minInterest;
        this.minMonths = minMonths;
        this.maxOpenLoans = maxOpenLoans;
        this.amount = amount;
        this.maxShare = maxShare;
        this.categories = categories;
        this.goodLoans = goodLoans;
    }

    @Override
    protected Task<ObservableList<Loan>> createTask() {
        return new Task<ObservableList<Loan>>() {
            final Customer customer=getTheCustomer();
            final int minInterest=getMinInterest();
            final int minMonths=getMinMonths();
            final int maxOpenLoans=getMaxOpenLoans();
            final int amount=getAmount();
            final int maxShare=getMaxShare();
            final List<String> categories =getCategories();
            @Override
            protected ObservableList<Loan> call() throws Exception {
                for (int i =0; i < maxProgress; i += 50)
                {
                    updateProgress(i, maxProgress);
                    Thread.sleep(sleepTime);
                }

                userFilteredLoanList.addAll(LoanFinder.findLoans(amount,categories,minInterest,minMonths,theCustomer,maxOpenLoans,maxShare));
                Thread.sleep(sleepTime);
                updateProgress(500, 500); //100% task complete
                goodLoans.addAll(userFilteredLoanList);
                return userFilteredLoanList;
            }
        };
    }

    @Override
    public void start() {
        setTheCustomer(theCustomer);
        setMinInterest(minInterest);
        setMinMonths(minMonths);
        setMaxOpenLoans(maxOpenLoans);
        setAmount(amount);
        setMaxShare(maxShare);
        setCategories(categories);
        setGoodLoans(goodLoans);
        super.start();

    }
}
