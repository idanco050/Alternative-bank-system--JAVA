package servlets;

import DTO.assignLoansDTO;
import classes.Customer;
import classes.Loan;
import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import manager.LoanFinder;
import manager.initializer;
import utils.SessionUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@WebServlet(name = "assignloans", urlPatterns = {"/assignloans"})
public class assignLoansServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String usernameFromSession = SessionUtils.getUsername(request);

        Scanner scanner = new Scanner(request.getInputStream()).useDelimiter("\\A");
        String reqBodyAsString = scanner.hasNext() ? scanner.next() : "";

        assignLoansDTO assignDTO = new Gson().fromJson(reqBodyAsString, assignLoansDTO.class);

        List<String> loanIDs = assignDTO.getLoanIDs();
        int amount = assignDTO.getAmount();
        Customer theCustomer = initializer.nameToCustomer(usernameFromSession);
        List<Loan> choiceList = new ArrayList<>();

        for (Loan l : initializer.loanList)
        {
            for (String id : loanIDs)
            {
                if (l.getId().equals(id))
                    choiceList.add(l);
            }
        }

        synchronized (this){
            if (loanIDs == null || amount == 0 || theCustomer == null){
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            }
            else{
                LoanFinder.addLoans(choiceList, theCustomer, amount);
                System.out.println("Request URI is: " + request.getRequestURI());
                response.setStatus(HttpServletResponse.SC_OK);
            }
        }
    }
}