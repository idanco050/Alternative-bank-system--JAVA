package servlets;

import classes.Loan;
import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import manager.initializer;
import utils.ServletUtils;
import utils.SessionUtils;

import java.io.IOException;
import java.util.Scanner;

@WebServlet(name = "closeloan", urlPatterns = {"/closeloan"})
public class closeLoanServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/plain;charset=UTF-8");
        String id = request.getParameter("id");
        boolean paid = false;
        synchronized (this){

            for (Loan l : initializer.loanList)
            {
                if (l.getId().equals(id))
                {
                    while(l.getStatus() != Loan.loanStatus.FINISHED)
                        l.payout();

                    System.out.println("Request URI is: " + request.getRequestURI());
                    response.setStatus(HttpServletResponse.SC_OK);
                    paid = true;
                }
            }
            if (!paid){
                String errorMessage = "ERROR: NO SUCH LOAN FOUND.";
                response.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
                response.getOutputStream().print(errorMessage);
            }
        }
    }
}