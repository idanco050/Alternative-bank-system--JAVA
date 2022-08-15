package servlets;

import DTO.customerInformationDTO;
import classes.Customer;
import classes.Loan;
import classes.cLoan;
import classes.operations.Transaction;
import jakarta.servlet.annotation.WebServlet;
import manager.initializer;
import manager.timeline;
import com.google.gson.Gson;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utils.SessionUtils;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;


@WebServlet(name = "customerinfo", urlPatterns = {"/customerinfo"})
public class customerInformationServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        String usernameFromSession = SessionUtils.getUsername(request);
        Gson gson = new Gson();
        customerInformationDTO customerInfo;
        synchronized (this) {
            PrintWriter out = response.getWriter();
            Customer theCustomer = initializer.nameToCustomer(usernameFromSession);
            int yaz = timeline.getCurrentDate();

            List<cLoan> loanerLoansList = new ArrayList<>();

            for (Loan l : theCustomer.getTaken())
            {
                cLoan cloan = new cLoan(l);
                loanerLoansList.add(cloan);
            }
            List<cLoan> lenderLoansList = new ArrayList<>();
            for (Loan l : theCustomer.getGiven())
            {
                cLoan cloan = new cLoan(l);
                lenderLoansList.add(cloan);
            }


            List<Transaction> transactionsList = theCustomer.getTransactionList();
            List<cLoan> loansForSaleList = new ArrayList<>();
            for (Loan l : initializer.loansForSale)
            {
                if (!l.getOwnerName().equals(usernameFromSession)) {
                    cLoan cloan = new cLoan(l);
                    loansForSaleList.add(cloan);
                }
            }

            List<String> categoriesList = initializer.categories.getCategories();
            List<cLoan> loanerLoansPaymentList = new ArrayList<>();
            for (cLoan loan : loanerLoansList) {
                if ((loan.getStatus() == cLoan.loanStatus.ACTIVE) || (loan.getStatus() == cLoan.loanStatus.RISK)) {
                    loanerLoansPaymentList.add(loan);
                }
            }
            List<String> notificationsList = theCustomer.getNotifications();
            customerInfo = new customerInformationDTO(loanerLoansList, lenderLoansList, transactionsList, loansForSaleList, categoriesList, loanerLoansPaymentList, notificationsList, yaz);


            System.out.println("Request URI is: " + request.getRequestURI());
            response.setStatus(HttpServletResponse.SC_OK);
            String json = gson.toJson(customerInfo);
            out.println(json);
            out.flush();

        }
    }
}

