package servlets;

import classes.Loan;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import manager.initializer;
import utils.SessionUtils;

import java.io.IOException;

@WebServlet(name = "sellloan", urlPatterns = {"/sellloan"})
public class sellLoanServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/plain;charset=UTF-8");
        String usernameFromSession = SessionUtils.getUsername(request);
        String id = request.getParameter("id");
        boolean listed = false;
        synchronized (this){
            for (Loan l : initializer.loanList)
            {
                if (l.getId().equals(id))
                {
                    if (l.getStatus() == Loan.loanStatus.ACTIVE)
                    {
                        initializer.loansForSale.add(l);
                        listed = true;
                        System.out.println("Request URI is: " + request.getRequestURI());
                        response.setStatus(HttpServletResponse.SC_OK);
                    }

                }
            }
            if (!listed){
                String errorMessage = "ERROR: NO SUCH LOAN FOUND.";
                response.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
                response.getOutputStream().print(errorMessage);
            }
        }
    }
}