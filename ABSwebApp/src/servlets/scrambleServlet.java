package servlets;

import DTO.scrambleRelevantDTO;
import classes.Customer;
import classes.Loan;
import classes.cLoan;
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
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@WebServlet(name = "scramble", urlPatterns = {"/scramble"})
public class scrambleServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        List<Loan> resList;
        String usernameFromSession = SessionUtils.getUsername(request);
        Scanner scanner = new Scanner(request.getInputStream()).useDelimiter("\\A");
        String reqBodyAsString = scanner.hasNext() ? scanner.next() : "";
        scrambleRelevantDTO scrambleDTO = new Gson().fromJson(reqBodyAsString, scrambleRelevantDTO.class);
        List<String> categoriesList = new ArrayList<>(scrambleDTO.getUserCategoriesList());
        int amount = scrambleDTO.getAmount(); //
        int maxShare = scrambleDTO.getMaxShare();
        int minimumInterest = scrambleDTO.getMinimumInterest();
        int minimumYaz = scrambleDTO.getMinimumYaz();
        int maxOpenLoans = scrambleDTO.getMaxOpenLoans();
        Customer theCustomer = initializer.nameToCustomer(usernameFromSession);


        synchronized (this){
            resList = LoanFinder.findLoans(amount, categoriesList, minimumInterest, minimumYaz, theCustomer, maxOpenLoans, maxShare);
        }
        if (resList.size() == 0)
            response.setStatus(HttpServletResponse.SC_NO_CONTENT);
        else {

            List<cLoan> returnList = new ArrayList<>();

            for (Loan l : resList)
            {
                cLoan cloan = new cLoan(l);
                returnList.add(cloan);
            }
            Gson gson = new Gson();
            String jsonResponse = gson.toJson(returnList);
            try (PrintWriter out = response.getWriter()) {
                out.print(jsonResponse);
                out.flush();
            }

            System.out.println("Request URI is: " + request.getRequestURI());
            response.setStatus(HttpServletResponse.SC_OK);
        }
    }

}