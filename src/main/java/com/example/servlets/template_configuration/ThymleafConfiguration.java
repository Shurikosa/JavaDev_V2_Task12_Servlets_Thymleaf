package com.example.servlets.template_configuration;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import org.thymeleaf.ITemplateEngine;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.WebApplicationTemplateResolver;
import org.thymeleaf.web.IWebApplication;
import org.thymeleaf.web.servlet.JakartaServletWebApplication;

@WebListener
public class ThymleafConfiguration implements ServletContextListener {
    public static final String TEMPLATE_ENGINE_ATTR = "com.example.thymeleaf.TemplateEngineInstance";
    private ITemplateEngine templateEngine;
    private JakartaServletWebApplication application;
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent){
        this.application = JakartaServletWebApplication.buildApplication(servletContextEvent.getServletContext());
        this.templateEngine = buildTemplateEngine(this.application);
        servletContextEvent.getServletContext().setAttribute(TEMPLATE_ENGINE_ATTR, templateEngine);
    }
    private ITemplateEngine buildTemplateEngine(final IWebApplication application) {
        // Templates will be resolved as application (ServletContext) resources
        final WebApplicationTemplateResolver templateResolver =
                new WebApplicationTemplateResolver(application);
        // HTML is the default mode, but we will set it anyway for better understanding of code
        templateResolver.setTemplateMode(TemplateMode.HTML);
        // This will convert "home" to "/WEB-INF/templates/select_timezone.html"
        templateResolver.setPrefix("/WEB-INF/templates/");
        templateResolver.setSuffix(".html");
        // Set template cache TTL to 1 hour. If not set, entries would live in cache until expelled by LRU
        templateResolver.setCacheTTLMs(Long.valueOf(3600000L));
        // Cache is set to true by default. Set to false if you want templates to
        // be automatically updated when modified.
        templateResolver.setCacheable(true);
        final TemplateEngine templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(templateResolver);
        return templateEngine;
    }
    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent){

    }

}
