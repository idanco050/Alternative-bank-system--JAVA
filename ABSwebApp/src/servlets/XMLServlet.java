package servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import manager.initializer;
import utils.SessionUtils;

import javax.xml.bind.JAXBException;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Scanner;

import static constants.Constants.TRUESTRING;

@MultipartConfig(fileSizeThreshold = 1024 * 1024, maxFileSize = 1024 * 1024 * 5, maxRequestSize = 1024 * 1024 * 5 * 5)
@WebServlet(name="xmlservlet", urlPatterns="/xmlservlet")
public class XMLServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String usernameFromSession = SessionUtils.getUsername(request);
        Collection<Part> parts = request.getParts();
        StringBuilder fileContent = new StringBuilder();

        for (Part part : parts) {
            //to write the content of the file to a string
            fileContent.append(readFromInputStream(part.getInputStream()));
        }
        InputStream file = new ByteArrayInputStream(fileContent.toString().getBytes(StandardCharsets.UTF_8));

        synchronized (this){
            try {
                String errorText = initializer.loadXMLfromInputStream(file, usernameFromSession);
                if (errorText.equals(TRUESTRING)){

                    response.setStatus(HttpServletResponse.SC_OK);
                }
                else {
                    response.getOutputStream().print(errorText);
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                }
            }catch (JAXBException | IOException e){
                response.sendError(HttpServletResponse.SC_NOT_ACCEPTABLE);
            } catch (Exception e) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            }
        }
    }

    private String readFromInputStream(InputStream inputStream) {
        return new Scanner(inputStream).useDelimiter("\\Z").next();
    }

}