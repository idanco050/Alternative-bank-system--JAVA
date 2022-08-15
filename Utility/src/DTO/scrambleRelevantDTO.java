package DTO;

import java.util.ArrayList;
import java.util.List;

public class scrambleRelevantDTO {
    String customerName;
    private int amount, minimumInterest, minimumYaz, maxOpenLoans, maxShare;
    private List<String> userCategoriesList;

    public scrambleRelevantDTO(String customerName, List<String> userCategoriesList, int minimumInterest, int minimumYaz, int maxOpenLoans, int amount, int maxShare){
        this.customerName = customerName;
        this.userCategoriesList = new ArrayList<>(userCategoriesList);
        this.minimumInterest = minimumInterest;
        this.minimumYaz = minimumYaz;
        this.maxOpenLoans = maxOpenLoans;
        this.amount = amount;
        this.maxShare = maxShare;
    }
    public int getAmount() {
        return amount;
    }

    public int getMaxShare() {
        return amount;
    }

    public int getMinimumInterest() {
        return minimumInterest;
    }

    public int getMinimumYaz() {
        return minimumYaz;
    }

    public int getMaxOpenLoans() {
        return maxOpenLoans;
    }

    public List<String> getUserCategoriesList() {
        return userCategoriesList;
    }



    @Override
    public String toString() {
        return "RelevantLoansListDTO{" +
                "minimumInterest=" + minimumInterest +
                ", minimumYaz=" + minimumYaz +
                ", maxOpenLoans=" + maxOpenLoans +
                ", userCategoriesList=" + userCategoriesList +
                ", amount=" + amount +
                ", maxShare=" + maxShare +
                '}';
    }

}
