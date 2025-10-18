package org;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import javax.servlet.RequestDispatcher;
import java.io.*;

public class FrontServlet extends HttpServlet {

    private RequestDispatcher defaultDispatcher;

    @Override
    public void init() {
        defaultDispatcher = getServletContext().getNamedDispatcher("default");
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getRequestURI().substring(req.getContextPath().length());

        boolean ressourceExist = getServletContext().getResource(path) != null;
        
        if (!ressourceExist) {
            resp.setContentType("text/plain; charset=UTF-8");
            resp.getWriter().write("FrontServlet a reçu : " + req.getRequestURL());
        } else {
            // resp.getWriter().write("Ressource non trouvée, url : " + req.getRequestURL() + "\n")
            defaultDispatcher.forward(req, resp);
        }
    }

}
