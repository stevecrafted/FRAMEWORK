package org;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;

import java.io.IOException;
import java.util.logging.Logger;

public class FrontServlet extends HttpServlet {

    // On crée un logger lié à cette classe
    private static final Logger logger = Logger.getLogger(FrontServlet.class.getName());

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Log INFO
        logger.info("Requête interceptée par FrontServlet : " + req.getRequestURI());
        System.out.println("Requête interceptée par FrontServlet : " + req.getRequestURI());

        logger.fine("Debugging : paramètres de la requête = " + req.getParameterMap());
        System.out.println("Debugging : paramètres de la requête = " + req.getParameterMap());

        // Log WARNING
        if (req.getRequestURI().contains("forbidden")) {
            logger.warning("Accès suspect : " + req.getRequestURI());
        }

        // Réponse simple
        resp.setContentType("text/plain");
        resp.getWriter().write("FrontServlet a reçu : " + req.getRequestURI());
        System.out.println("FrontServlet a reçu : " + req.getRequestURI());
    }

}
