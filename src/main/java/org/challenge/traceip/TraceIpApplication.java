package org.challenge.traceip;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

import org.challenge.traceip.config.WebMvcConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@EnableWebMvc
@SpringBootApplication
@ComponentScan("org.challenge.traceip")
public class TraceIpApplication implements WebApplicationInitializer {

	public static void main(String[] args) {
		SpringApplication.run(TraceIpApplication.class, args);
	}
	
	@Override
    public void onStartup(final ServletContext servletContext) throws ServletException {
        AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
        context.register(WebMvcConfig.class);
        context.setServletContext(servletContext);
        ServletRegistration.Dynamic dispatcher = servletContext.addServlet("spring-web", new DispatcherServlet(context));
        dispatcher.setLoadOnStartup(1);
        dispatcher.addMapping("/");
    }

}
