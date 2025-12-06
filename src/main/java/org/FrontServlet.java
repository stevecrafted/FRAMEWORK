package org;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import javax.servlet.ServletException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;

import java.io.*;
import java.util.Map;
import java.util.HashMap;

import org.Entity.ClassMethodUrl;
import org.Entity.ModelView;
import org.Util.CmuUtils;
import org.Util.ParameterMapper;
import org.custom.CustomReflections;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

public class FrontServlet extends HttpServlet { 

    private RequestDispatcher defaultDispatcher;
    private CustomReflections reflections;
    Map<String, ClassMethodUrl> urlMappings = new HashMap<>();

    @Override
    public void init() {
        defaultDispatcher = getServletContext().getNamedDispatcher("default");

        reflections = new CustomReflections(
                "org.example"
        );
        
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
        ClassMethodUrl cmu;
        
        try {   
            // Jerena raha misy ao amle Url mapping le url miditra io 
            cmu = CmuUtils.findMapping(path, urlMappings, req);

            resp.setContentType("text/plain; charset=UTF-8");
            resp.getWriter().write("FrontServlet a reçu : " + req.getRequestURL() + "\n");
            
            if (cmu != null) { 
                Method methode = cmu.getMyMethod();
                Parameter[] methodParameters = methode.getParameters();
                Method method = cmu.getMyMethod();

                /**
                 * Sprint 6 : Matching des parametres entre l'url get ou
                 * post par formulaire avec les attributs
                 * de la methode
                 */
                Object[] methodArgs = ParameterMapper.mapParameters(methodParameters, req, method);

                printToClient(resp, "Cette url existe dans la classe " + cmu.getMyClass() + " dans la methode "
                        + cmu.getMyMethod());

                // Exécution selon le type de retour
                if (cmu.getMyMethod().getReturnType() == String.class) {
                    printToClient(resp, "Cette methode renvoie un String\n");

                    // Invocation avec les arguments
                    String result = cmu.ExecuteMethodeString(methodArgs);

                    printToClient(resp, "Resultat de la fonction : \n");
                    printToClient(resp, result);

                } else if (cmu.getMyMethod().getReturnType() == ModelView.class) {
                    printToClient(resp, "Cette methode renvoie un Model View\n");

                    // Ato no mandefa ny attribute rehetra any am client
                    String result = cmu.ExecuteMethodeModelView(req, methodArgs);

                    // Affichage du resultat
                    defaultDispatcher = req.getRequestDispatcher("/" + result);
                    defaultDispatcher.forward(req, resp);

                } else {
                    printToClient(resp, "Sady tsy String no tsy Model View ny averiny");
                }

            } else {
                printToClient(resp, "Error 404");
            }
        } catch (Exception e) {
            e.printStackTrace();
            printToClient(resp, "Erreur : " + e.getMessage());
        }
    }

    public void printToClient(HttpServletResponse resp, String message) throws IOException {
        resp.getWriter().write(message);
    }
}
