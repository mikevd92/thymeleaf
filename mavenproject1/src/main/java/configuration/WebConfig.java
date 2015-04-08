package configuration;

import controller.LoginController;
import controller.PlayController;
import controller.SeatController;
import controller.SignUpController;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Scope;
import org.springframework.http.MediaType;
import org.springframework.web.accept.ContentNegotiationManager;
import org.springframework.web.context.request.RequestContextListener;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.ContentNegotiatingViewResolver;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.spring4.view.ThymeleafViewResolver;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

@Configuration
@EnableWebMvc
//@EnableTransactionManagement
@ComponentScan(basePackages = {"service"}, includeFilters = @ComponentScan.Filter(value = {org.springframework.stereotype.Service.class}, type = FilterType.ANNOTATION))
public class WebConfig extends WebMvcConfigurerAdapter {

    @Bean
    @Scope("request")
    public LoginController loginController() {
        return new LoginController();
    }

    @Bean
    @Scope("session")
    public PlayController managerController() {
        return new PlayController();
    }

    @Bean
    @Scope("request")
    public SignUpController signUpController() {
        return new SignUpController();
    }

    @Bean
    @Scope("session")
    public SeatController userController() {
        return new SeatController();
    }

    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/resources/**").addResourceLocations("/resources/");
    }

    @Bean
    RequestContextListener requestContextListener() {
        return new RequestContextListener();
    }

    @Override
    public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
        configurer.favorPathExtension(true)
                .useJaf(false)
                .ignoreAcceptHeader(false)
                .mediaType("html", MediaType.TEXT_HTML)
                .mediaType("json", MediaType.APPLICATION_JSON)
                .defaultContentType(MediaType.TEXT_HTML);
    }

    @Bean
    public ViewResolver contentNegotiatingViewResolver(ContentNegotiationManager manager) {
        List<ViewResolver> resolvers = new ArrayList<>();
        ServletContextTemplateResolver templateResolver = new ServletContextTemplateResolver();
        templateResolver.setPrefix("/");
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode("HTML5");
        SpringTemplateEngine templateEngine = new SpringTemplateEngine();
        templateEngine.setTemplateResolver(templateResolver);
        ThymeleafViewResolver viewResolver = new ThymeleafViewResolver();
        viewResolver.setTemplateEngine(templateEngine);
        resolvers.add(viewResolver);
        JsonViewResolver jasonResolver = new JsonViewResolver();
        resolvers.add(jasonResolver);
        ContentNegotiatingViewResolver resolver = new ContentNegotiatingViewResolver();
        resolver.setViewResolvers(resolvers);
        resolver.setContentNegotiationManager(manager);
        return resolver;
    }

    public class JsonViewResolver implements ViewResolver {

        @Override
        public View resolveViewName(String viewName, Locale locale) throws Exception {
            MappingJackson2JsonView view;
            view = new MappingJackson2JsonView();
            view.setPrettyPrint(true);
            return view;
        }
    }
}
