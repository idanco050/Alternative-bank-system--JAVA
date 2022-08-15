package servlets;

import classes.Customer;
import classes.Loan;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import manager.initializer;
import utils.SessionUtils;

import java.io.IOException;

@WebServlet(name = "loanentry", urlPatterns = {"/loanentry"})
public class loanEntryServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/plain;charset=UTF-8");

        String usernameFromSession = SessionUtils.getUsername(request);
        String id = request.getParameter("id");
        String category = request.getParameter("category");
        int capital = Integer.parseInt(request.getParameter("capital"));
        int yaz = Integer.parseInt(request.getParameter("yaz"));
        int interest = Integer.parseInt(request.getParameter("interest"));
        int yazPerPayment = Integer.parseInt(request.getParameter("yazPerPayment"));
        double yazModuloCheck = (double) yaz / yazPerPayment;
        boolean uniqueID = true;

        synchronized (this){
            for (Loan l : initializer.loanList)
            {
                if (l.getId().equals(id))
                {
                    String errorMessage = "A loan for '" + id + "' exists in the system. Enter a new loan.";
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.getOutputStream().print(errorMessage);
                    uniqueID = false;
                }
            }
            if (uniqueID) {
                if (yazModuloCheck % 1 != 0) {
                    String errorMessage = "Payment period is not divisible by payment rate.";

                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.getOutputStream().print(errorMessage);
                } else {
                    if (!initializer.categories.getCategories().contains(category))
                        initializer.categories.getCategories().add(category);


                    Loan loan = new Loan(usernameFromSession, id, category, capital, yaz, yazPerPayment, interest);
                    Customer theCustomer = initializer.nameToCustomer(usernameFromSession);
                    initializer.loanList.add(loan);
                    theCustomer.getOwnedLoans().add(loan);

                    System.out.println("Request URI is: " + request.getRequestURI());
                    response.setStatus(HttpServletResponse.SC_OK);
                }
            }
        }

    }

}