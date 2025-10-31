package org;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import javax.servlet.ServletException;
import javax.servlet.RequestDispatcher;
import java.io.*;
import java.util.List;
import java.util.ArrayList;

import org.Entity.ClassMethodUrl;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
import org.Util.CmuUtils;
import org.custom.CustomReflections;

public class FrontServlet extends HttpServlet {

    private RequestDispatcher defaultDispatcher;
    private CustomReflections reflections;
    private List<ClassMethodUrl> listClassMethodUrl;

    @Override
    public void init() {
        defaultDispatcher = getServletContext().getNamedDispatcher("default");

        reflections = new CustomReflections(
                "org.example"
                );

        // Sauvegardena anaty classeMethodeUrl daolo izay mampiasa anle Annotation
        // namboarina
        System.out.println("---------- Sauvegarde des url ----------");
        listClassMethodUrl = new ArrayList<>();
        CmuUtils.saveCmuList(reflections, listClassMethodUrl);

    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getRequestURI().substring(req.getContextPath().length());

        ClassMethodUrl cmu = isExisted(path);

        resp.setContentType("text/plain; charset=UTF-8");
        resp.getWriter().write("FrontServlet a reçu : " + req.getRequestURL() + "\n");
        if (cmu != null) {
            resp.getWriter().write(
                    "Cette url existe dans la classe " + cmu.getMyClass() + " dans la methode " + cmu.getMyMethod());
        } else {
            resp.getWriter().write("Error 404");
        }
    }

    private ClassMethodUrl isExisted(String path) {
        for (ClassMethodUrl cmu : listClassMethodUrl) {
            if (path.equals(cmu.getMyUrl())) {
                System.out.println("trouvé");
                return cmu;
            }
        }

        return null;
    }

}
