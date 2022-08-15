package adminServlets;

import DTO.adminInformationDTO;
import classes.Customer;
import classes.Loan;
import classes.cCustomer;
import classes.cLoan;
import classes.operations.Transaction;
import com.google.gson.Gson;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import manager.initializer;
import manager.timeline;
import sun.swing.BakedArrayList;
import utils.SessionUtils;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;


@WebServlet(name = "admininfo", urlPatterns = {"/admininfo"})
public class adminInformationServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        String name = request.getParameter("name");
        Gson gson = new Gson();
        adminInformationDTO adminInfo;
        synchronized (this) {
            PrintWriter out = response.getWriter();
            int yaz = timeline.getCurrentDate();
            List<cLoan> loanList = new ArrayList<>();
            List<cCustomer> customerList = new ArrayList<>();
            for (Loan l : initializer.loanList)
            {
                cLoan cloan = new cLoan(l);
                loanList.add(cloan);
            }
            for (Customer cus : initializer.customerList)
            {
                cCustomer cCus = new cCustomer(cus);
                customerList.add(cCus);
            }
            if(name != null && name != "")
            {
                int newLoans = 0;
                int givenLoans = 0;
                int gActive = 0;
                int gPending = 0;
                int gRisk = 0;
                int gFinished = 0;
                int takenLoans = 0;
                int tActive = 0;
                int tPending = 0;
                int tRisk = 0;
                int tFinished = 0;
                Customer theCustomer = initializer.nameToCustomer(name);
                for(Loan l : theCustomer.getOwnedLoans())
                {
                    switch (l.getStatusInt()){
                        case 0:
                        {
                            newLoans++;
                            break;
                        }
                        case 1:
                        {
                            tPending++;
                            break;
                        }
                        case 2:
                        {
                            takenLoans++;
                            tActive++;
                            break;
                        }
                        case 3:
                        {
                            tFinished++;
                            break;
                        }
                        case 4:
                        {
                            tRisk++;
                            break;
                        }
                    }
                }
                for(Loan l : theCustomer.getGiven())
                {
                    givenLoans++;
                    switch (l.getStatusInt()){
                        case 0:
                        {
                            System.out.println("Error. Given loan can't be new.");
                            break;
                        }
                        case 1:
                        {
                            gPending++;
                            break;
                        }
                        case 2:
                        {
                            gActive++;
                            break;
                        }
                        case 3:
                        {
                            gFinished++;
                            break;
                        }
                        case 4:
                        {
                            gRisk++;
                            break;
                        }
                    }
                }

                adminInfo = new adminInformationDTO(customerList, loanList, yaz,
                        newLoans, givenLoans, gActive, gPending, gRisk, gFinished,
                        takenLoans, tActive, tPending, tRisk, tFinished);
            }
            else
                adminInfo = new adminInformationDTO(customerList, loanList, yaz);


            System.out.println("Request URI is: " + request.getRequestURI());
            response.setStatus(HttpServletResponse.SC_OK);
            String json = gson.toJson(adminInfo);
            out.println(json);
            out.flush();
        }
    }
}

