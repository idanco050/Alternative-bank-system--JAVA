package servlets;

import classes.Loan;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import manager.initializer;
import utils.SessionUtils;

import java.io.IOException;

@WebServlet(name = "buyloan", urlPatterns = {"/buyloan"})
public class buyLoanServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/plain;charset=UTF-8");
        String usernameFromSession = SessionUtils.getUsername(request);
        String id = request.getParameter("id");
        boolean done = false;
        synchronized (this){
            for (Loan l : initializer.loanList)
            {
                if (l.getId().equals(id))
                {
                    if (l.getStatus() == Loan.loanStatus.ACTIVE)
                    {
                        String error = initializer.buyLoan(l, usernameFromSession);
                        if (error.equals("False"))
                        {
                            if(l.getInvolvedCustomers().size() == 1 && l.getInvolvedCustomers().containsKey(usernameFromSession))
                            {
                                response.getOutputStream().print("Debt cancelled out with yourself. What are you even trying to do?");
                            }
                            else
                            {
                                response.getOutputStream().print("Purchase successful!");
                            }
                            System.out.println("Request URI is: " + request.getRequestURI());
                            response.setStatus(HttpServletResponse.SC_OK);
                        }
                        else
                        {
                            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                            response.getOutputStream().print(error);
                        }
                        done = true;
                    }
                }
            }
            if (!done){
                String errorMessage = "ERROR: NO SUCH LOAN FOUND.";
                response.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
                response.getOutputStream().print(errorMessage);
            }
        }
    }
}