/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package configuration;

import javax.servlet.Filter;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import org.springframework.orm.jpa.support.OpenEntityManagerInViewFilter;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.filter.DelegatingFilterProxy;
import org.springframework.web.filter.HiddenHttpMethodFilter;
import org.springframework.web.filter.RequestContextFilter;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

/**
 *
 * @author Mihai
 */
public class WebInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

//    @Autowired
//    SessionRegistry sessionRegistry;
    
    @Override
    protected String[] getServletMappings() {
        return new String[]{"/"};
    }

    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class<?>[] {ServiceConfig.class,SecurityConfig.class};
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class<?>[] {WebConfig.class,WebSocketConfig.class};
    }

    @Override
    protected Filter[] getServletFilters() {
        CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter();
        characterEncodingFilter.setEncoding("UTF-8");
        characterEncodingFilter.setForceEncoding(true);
        HiddenHttpMethodFilter methodFilter=new HiddenHttpMethodFilter();
        RequestContextFilter requestContextFilter=new RequestContextFilter();
        return new Filter[] {characterEncodingFilter,methodFilter,requestContextFilter,new DelegatingFilterProxy("springSecurityFilterChain"),new OpenEntityManagerInViewFilter()};
    }

    @Override
    protected void customizeRegistration(ServletRegistration.Dynamic registration) {
        registration.setInitParameter("defaultHtmlEscape", "true");
        registration.setInitParameter("dispatchOptionsRequest", "true");
        registration.setInitParameter("spring.profiles.active", "default");
        registration.setAsyncSupported(true);
    }
    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        servletContext.getSessionCookieConfig().setMaxAge(-1);
//        HttpSessionEventPublisher sessionPublisher=new HttpSessionEventPublisher();
//        servletContext.addListener(sessionPublisher);
        super.onStartup(servletContext);
    }
}