package org.example.servletHomeWork;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templateresolver.WebApplicationTemplateResolver;
import org.thymeleaf.web.servlet.JakartaServletWebApplication;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Map;
@WebServlet(value = "/time")
public class ThymleafClass extends HttpServlet {
    private TemplateEngine templateEngine;
    private static final String NAMECOOKIE = "timezone";
    private String lastTimezone = "";

        @Override
        public void init() throws ServletException {
            templateEngine = new TemplateEngine();

            JakartaServletWebApplication jswa =
                    JakartaServletWebApplication.buildApplication(this.getServletContext());

            WebApplicationTemplateResolver
                    resolver = new WebApplicationTemplateResolver(jswa);
            resolver.setPrefix("/WEB-INF/temp/");
            resolver.setSuffix(".html");
            resolver.setTemplateMode("HTML5");
            resolver.setOrder(templateEngine.getTemplateResolvers().size());
            resolver.setCacheable(false);
            templateEngine.addTemplateResolver(resolver);
        }
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        resp.setContentType("text/html");
        LocalDate data = LocalDate.now();
        LocalTime time = LocalTime.now();

        try {
            if(!req.getParameter(NAMECOOKIE).isEmpty()){
                resp.addCookie(new Cookie(NAMECOOKIE,req.getParameter(NAMECOOKIE)));
                lastTimezone = specificCookie(req);
                time= LocalTime.now().plusHours(getTimeFromParam(lastTimezone)) ;
            }
        }catch (Exception e){
            if(lastTimezone.length()<4){
                lastTimezone = "UTC";
                time= LocalTime.now();
            }
            else time= LocalTime.now().plusHours(getTimeFromParam(lastTimezone)) ;

        }

        Context dataToApp = new Context(req.getLocale(),
                Map.of("dataTh",data,"timeTh",time, "timezoneTh",lastTimezone));

        templateEngine.process("time",dataToApp, resp.getWriter());
        resp.getWriter().close();
    }

    private int getTimeFromParam(String req){
        int result = 0;
        String[] data = req.split("");
        int hours = Integer.parseInt(req.substring(4));
        if (data[3].equals("+")) result +=hours;
        if(data[3].equals("-")) result -=hours;
        return result ;
    }
    private String specificCookie(HttpServletRequest req){
        String result = "";
        if( req.getCookies().length>0){
            Cookie[] cookies = req.getCookies();
            for (Cookie cookie : cookies) {
                if(cookie.getName().equals(NAMECOOKIE) ) result=cookie.getValue();
            }
        }
        return result;
    }
}
