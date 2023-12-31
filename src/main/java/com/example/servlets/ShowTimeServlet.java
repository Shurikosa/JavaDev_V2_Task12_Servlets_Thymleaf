package com.example.servlets;

import com.example.servlets.template_configuration.ThymleafConfiguration;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.web.IWebExchange;
import org.thymeleaf.web.servlet.JakartaServletWebApplication;

import java.io.IOException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@WebServlet("/showTime")
public class ShowTimeServlet extends HttpServlet {
        private String timezone;
        private ZoneId zoneId;
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
         timezone = request.getParameter("timezone");
        zoneId = timezone != null ? ZoneId.of(timezone) : null;
        checkCookies(request);
        putCookies(timezone, response);
        ZonedDateTime currentTime = ZonedDateTime.now(zoneId);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedTime = currentTime.format(formatter);

        TemplateEngine templateEngine = (TemplateEngine) getServletContext().getAttribute(
                ThymleafConfiguration.TEMPLATE_ENGINE_ATTR);
        IWebExchange webExchange = JakartaServletWebApplication.buildApplication(getServletContext())
                .buildExchange(request, response);
        WebContext context = new WebContext(webExchange);
        context.setVariable("currentTime", formattedTime);
        context.setVariable("selectedTimezone", zoneId.getId());
        templateEngine.process("show_time", context, response.getWriter());

    }

    private void putCookies(String timezone, HttpServletResponse response){
        Cookie timezoneCookie = new Cookie("timezone", timezone);
        timezoneCookie.setMaxAge(24 * 60);
        response.addCookie(timezoneCookie);
    }
    private void checkCookies(HttpServletRequest request){
        if (request.getParameter("timezone") == null){
            Cookie[] cookies = request.getCookies();
            if(cookies != null){
                for (Cookie cookie : cookies){
                    if(cookie.getName().equals("timezone")){
                        zoneId = ZoneId.of(cookie.getValue());
                        timezone = cookie.getValue();
                    }
                }
            }
        }
    }
}
