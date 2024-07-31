package lk.ijse.webpos.backend.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;


@WebFilter(urlPatterns = "/*")
public class CORSFilter extends HttpFilter {
    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
        var origin = getServletContext().getInitParameter("origin");
        if(origin.contains(getServletContext().getInitParameter("origin"))){

            //To allow only specific origin 5050
            res.setHeader("Access-Control-Allow-Origin", origin);

            //Allow methods
            res.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, PATCH, DELETE, OPTIONS");

            res.setHeader("Access-Control-Allow-Headers","Content-Type");

            //TO expose the Header to Javascript Engine of the browser
            res.setHeader("Access-Control-Expose-Headers","Content-Type");
        }
        chain.doFilter(req, res);
    }
}

