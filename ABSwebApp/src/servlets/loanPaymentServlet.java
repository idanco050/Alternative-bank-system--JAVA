package servlets;

import classes.Loan;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import manager.initializer;
import utils.SessionUtils;

import java.io.IOException;

@WebServlet(name = "loanpayment", urlPatterns = {"/loanpayment"})
public class loanPaymentServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/plain;charset=UTF-8");
        String usernameFromSession = SessionUtils.getUsername(request);
        String id = request.getParameter("id");
        boolean paid = false;
        synchronized (this){

            for (Loan l : initializer.loanList)
            {
                if (l.getId().equals(id))
                {
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