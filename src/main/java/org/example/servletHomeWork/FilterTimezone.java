package org.example.servletHomeWork;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;


@WebFilter(value = "/time")
public class FilterTimezone extends HttpFilter {
    @Override
    public void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
        String timezone = req.getParameter("timezone");
        if(timezone==null) chain.doFilter(req, res);
        if(validationTimezone(timezone)) chain.doFilter(req, res);
        else {
            res.setStatus(400);
            res.setContentType("text/html");
            PrintWriter out = res.getWriter();
            out.println("<h1>Invalid timezone </h1>");
        }
    }
    private boolean validationTimezone (String timezone){
        String []data = timezone.split("");
        String utc = data[0]+ data[1]+data[2];
        String symbol = data[3];
        String hoursStr = timezone.substring(4);
        int hours = Integer.parseInt(hoursStr);
        if(data.length<=6&&
                utc.equals("UTC")&&
                (hours>=0&& hours<=24)&&
                (symbol.equals("+")||symbol.equals("-"))) return true;
        else return false;
    }

}
