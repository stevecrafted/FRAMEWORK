package org;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import javax.servlet.ServletException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;

import org.Entity.ClassMethodUrl;
import org.Util.CmuUtils;
import org.custom.CustomReflections;

public class FrontServlet extends HttpServlet {

    private RequestDispatcher defaultDispatcher;
    private CustomReflections reflections;
    Map<String, ClassMethodUrl> urlMappings = new HashMap<>();

    @Override
    public void init() {
        defaultDispatcher = getServletContext().getNamedDispatcher("default");

        reflections = new CustomReflections(
                "org.example");

        // Sauvegardena anaty classeMethodeUrl daolo izay mampiasa anle Annotation
        // namboarina
        System.out.println("---------- Sauvegarde des url ----------");
        CmuUtils.saveCmuList(reflections, urlMappings);

        ServletContext context = getServletContext();
        context.setAttribute("urlMappings", urlMappings);
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getRequestURI().substring(req.getContextPath().length());

        ClassMethodUrl cmu = CmuUtils.findMapping(path, urlMappings);

        resp.setContentType("text/plain; charset=UTF-8");
        resp.getWriter().write("FrontServlet a reçu : " + req.getRequestURL() + "\n");
        if (cmu != null) {

            printToClient(resp, "Cette url existe dans la classe " + cmu.getMyClass() + " dans la methode "
                    + cmu.getMyMethod());

            // Jerena ny type de retour any
            // Raha String dia Executena Tenenina ho type string io
            if (cmu.getMyMethod().getReturnType() == String.class) {
                printToClient(resp, "Cette methode renvoie un String\n");
                // Execution anle methode
                cmu.ExecuteMethode(resp);
            }
            // Raha != String sy ModelView
            else {
                printToClient(resp, "Sady tsy String no tsy Model View");
            }

            // Raha Model view
            // Averina ilay test.jsp
        } else {
            resp.getWriter().write("Error 404");
        }
    }

    public void printToClient(HttpServletResponse resp, String message) throws IOException {
        resp.getWriter().write(message);
    }
}
