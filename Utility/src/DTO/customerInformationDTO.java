package DTO;

import classes.cLoan;
import classes.operations.Transaction;

import java.util.List;

public class customerInformationDTO {

    private List<cLoan> takenLoansList;
    private List<cLoan> givenLoansList;
    private List<Transaction> transactionsList;
    private List<cLoan> loansForSaleList;
    private List<String> categoriesList;
    private List<cLoan> takenLoansPayList;
    private List<String> notificationsList;
    private int yaz;

    public customerInformationDTO(List<cLoan> takenLoansList, List<cLoan> givenLoansList, List<Transaction> transactionsList, List<cLoan> loansForSaleList, List<String> categoriesList, List<cLoan> takenLoansPayList, List<String> notificationsList, int yaz)
    {
        this.takenLoansList = takenLoansList;
        this.givenLoansList = givenLoansList;
        this.transactionsList = transactionsList;
        this.loansForSaleList = loansForSaleList;
        this.categoriesList = categoriesList;
        this.takenLoansPayList = takenLoansPayList;
        this.notificationsList = notificationsList;
        this.yaz = yaz;
    }

    public List<String> getNotificationsList() {
        return notificationsList;
    }

    public int getYaz() {
        return yaz;
    }

    public List<cLoan> getTakenLoansList() {
        return takenLoansList;
    }

    public List<cLoan> getGivenLoansList() {
        return givenLoansList;
    }

    public List<Transaction> getTransactionsList() {
        return transactionsList;
    }

    public List<cLoan> getLoansForSaleList() {
        return loansForSaleList;
    }

    public List<String> getCategoriesList() {
        return categoriesList;
    }

    public List<cLoan> getTakenLoansPayList() {
        return takenLoansPayList;
    }
}
