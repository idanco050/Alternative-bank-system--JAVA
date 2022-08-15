package DTO;

import java.util.ArrayList;
import java.util.List;

public class assignLoansDTO {

    private List<String> loanIDs;
    private int amount;

    public assignLoansDTO(List<String> loanIDs, int amount){
        this.loanIDs = new ArrayList<>(loanIDs);
        this.amount = amount;
    }

    // addLoans(choiceList, theCustomer, Integer.parseInt(investmentAmountTF.getText()));

    public List<String> getLoanIDs() {
        return loanIDs;
    }

    public int getAmount() {
        return amount;
    }


    @Override
    public String toString() {
        return "assignLoansDTO{" +
                "loanIDs=" + loanIDs +
                ", amount=" + amount +
                '}';
    }

}
