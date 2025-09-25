package org;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import javax.servlet.ServletContext;
import java.io.*;

public class FrontServlet extends HttpServlet {

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String uri = req.getRequestURI();
        String contextPath = req.getContextPath();
        String path = uri.substring(contextPath.length()); // ex: /index.html

        if (path.equals("/") || path.isEmpty()) {
            path = "/index.html"; // page d’accueil
        }

        ServletContext sc = getServletContext();

        // Vérifie si la ressource existe
        InputStream is = sc.getResourceAsStream(path);
        if (is != null) {
            // Détecter le type MIME
            String mime = sc.getMimeType(path);
            if (mime == null) {
                mime = "application/octet-stream"; // fallback
            }
            resp.setContentType(mime);

            try (OutputStream os = resp.getOutputStream()) {
                byte[] buffer = new byte[8192];
                int len;
                while ((len = is.read(buffer)) != -1) {
                    os.write(buffer, 0, len);
                }
            }
            return;
        }
        
        // Sinon : pas de fichier trouvé → traitement appli
        resp.setContentType("text/plain; charset=UTF-8");
        resp.getWriter().write("FrontServlet a reçu : " + uri);
    }
}
