package manager;

import classes.Customer;
import classes.Loan;

import java.util.ArrayList;
import java.util.List;

import manager.initializer;

public class timeline {
    private static int currentDate = 1;

    public static int getCurrentDate() {
        return currentDate;
    }

    public static void nextMonth(){
        currentDate += 1;
        for (Loan loan : initializer.activeLoans)
        {
            if (loan.getNextPaymentOn() < currentDate)
            {
                loan.setStatus(Loan.loanStatus.RISK);
                sendRiskNotification(loan);
            }
            else
                sendActiveNotification(loan);
        }
    }

    private static void sendRiskNotification(Loan loan)
    {
        Customer owner = initializer.nameToCustomer(loan.getOwnerName());
        String res = ("Date: " + currentDate + ", Loan: " + (loan.getId()) + ". Loan in risk, you had to pay " + loan.getPayment() + ", " + (currentDate - loan.getNextPaymentOn()) + " yaz ago!");
        owner.getNotifications().add(res);
    }

    private static void sendActiveNotification(Loan loan)
    {
        String res =null;
        Customer owner = initializer.nameToCustomer(loan.getOwnerName());
        int timeLeft = loan.getNextPaymentOn() - currentDate;
        if (timeLeft == 0)
            res = ("Date: " + currentDate + ", Loan: " + (loan.getId()) + ". Time to pay: " + loan.getPayment() + ", payment is due today!");
        else if (timeLeft <= 3)
            res = ("Date: " + currentDate + ", Loan: " + (loan.getId()) + ". Pay " + loan.getPayment() + " in " + timeLeft + " yaz.");

        owner.getNotifications().add(res);
    }

    public static void resetCurrentDate() {
        timeline.currentDate = 1;
    }


}
