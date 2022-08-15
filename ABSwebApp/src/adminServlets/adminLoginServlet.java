package adminServlets;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import manager.initializer;
import utils.SessionUtils;

import java.io.IOException;

import static constants.Constants.USERNAME;

@WebServlet(name="adminlogin", urlPatterns="/adminlogin")
public class adminLoginServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/plain;charset=UTF-8");
        String usernameFromSession = SessionUtils.getUsername(request);

        if (usernameFromSession == null) {
            response.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
        }

        String usernameFromParameter = request.getParameter(USERNAME);
        if (usernameFromParameter == null || usernameFromParameter.isEmpty()) {

            response.setStatus(HttpServletResponse.SC_CONFLICT);
        } else {

            usernameFromParameter = usernameFromParameter.trim();
            synchronized (this) {
                if ((initializer.setAdmin(usernameFromParameter)) || (usernameFromParameter.equals(initializer.getAdminName()))) {
                    request.getSession(true).setAttribute(USERNAME, usernameFromParameter);
                    System.out.println("On login, request URI is: " + request.getRequestURI());
                    response.setStatus(HttpServletResponse.SC_OK);

                }
                else {
                    String errorMessage = "Admin " + usernameFromParameter + " is already set. Try logging in with that name, restart the session or log in as a customer.";
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.getOutputStream().print(errorMessage);
                }
            }
        }
    }
}
