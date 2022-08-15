package DTO;

import classes.cCustomer;
import classes.cLoan;
import classes.operations.Transaction;

import java.util.List;

public class adminInformationDTO {

    private List<cCustomer> cCustomerList;
    private List<cLoan> cLoansList;
    private int yaz;


    private int newLoans;

    private int givenLoans;
    private int gActive;
    private int gPending;
    private int gRisk;
    private int gFinished;

    private int takenLoans;
    private int tActive;
    private int tPending;
    private int tRisk;
    private int tFinished;

    private boolean bUpdateLabels;


    public adminInformationDTO(List<cCustomer> cCustomerList, List<cLoan> cLoansList, int yaz) {
        this.cCustomerList = cCustomerList;
        this.cLoansList = cLoansList;
        this.yaz = yaz;
        this.newLoans = -1;
        this.givenLoans = -1;
        this.gActive = -1;
        this.gPending = -1;
        this.gRisk = -1;
        this.gFinished = -1;
        this.takenLoans = -1;
        this.tActive = -1;
        this.tPending = -1;
        this.tRisk = -1;
        this.tFinished = -1;

        bUpdateLabels = false;
    }

    public adminInformationDTO(List<cCustomer> cCustomerList, List<cLoan> cLoansList, int yaz,
                               int newLoans, int givenLoans, int gActive, int gPending, int gRisk, int gFinished,
                               int takenLoans, int tActive, int tPending, int tRisk, int tFinished) {
        this.cCustomerList = cCustomerList;
        this.cLoansList = cLoansList;
        this.yaz = yaz;
        this.newLoans = newLoans;
        this.givenLoans = givenLoans;
        this.gActive = gActive;
        this.gPending = gPending;
        this.gRisk = gRisk;
        this.gFinished = gFinished;
        this.takenLoans = takenLoans;
        this.tActive = tActive;
        this.tPending = tPending;
        this.tRisk = tRisk;
        this.tFinished = tFinished;

        bUpdateLabels = true;
    }

    public int getYaz() {
        return yaz;
    }

    public List<cCustomer> getcCustomerList() {
        return cCustomerList;
    }

    public List<cLoan> getcLoansList() {
        return cLoansList;
    }

    public int getNewLoans() {
        return newLoans;
    }

    public int getGivenLoans() {
        return givenLoans;
    }

    public int getgActive() {
        return gActive;
    }

    public int getgPending() {
        return gPending;
    }

    public int getgRisk() {
        return gRisk;
    }

    public int getgFinished() {
        return gFinished;
    }

    public int getTakenLoans() {
        return takenLoans;
    }

    public int gettActive() {
        return tActive;
    }

    public int gettPending() {
        return tPending;
    }

    public int gettRisk() {
        return tRisk;
    }

    public int gettFinished() {
        return tFinished;
    }
}
