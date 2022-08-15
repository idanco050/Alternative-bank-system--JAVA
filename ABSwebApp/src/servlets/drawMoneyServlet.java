package servlets;

import classes.Customer;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import manager.initializer;
import utils.SessionUtils;

import java.io.IOException;

@WebServlet(name="drawmoneyservlet", urlPatterns="/drawmoney")
public class drawMoneyServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/plain;charset=UTF-8");

        String usernameFromSession = SessionUtils.getUsername(request);
        Customer theCustomer = initializer.nameToCustomer(usernameFromSession);

        int amount = Integer.parseInt(request.getParameter("amount"));

        System.out.println("DrawMoneyServlet user name: " + usernameFromSession);
        System.out.println("DrawMoneyServlet amount: " + amount);
        if (amount > theCustomer.getBalance())
            response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
        else {
            synchronized (this) {
                //add money to user
                initializer.nameToCustomer(usernameFromSession).drawMoney(amount);
            }

            System.out.println("Request URI is: " + request.getRequestURI());
            response.setStatus(HttpServletResponse.SC_OK);
        }
    }
}