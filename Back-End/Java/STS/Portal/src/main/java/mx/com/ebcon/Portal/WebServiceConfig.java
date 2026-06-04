
package mx.com.ebcon.Portal;

import org.springframework.xml.xsd.XsdSchema;
import org.springframework.xml.xsd.SimpleXsdSchema;

import org.springframework.core.io.ClassPathResource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;

import org.springframework.ws.config.annotation.EnableWs;
import org.springframework.ws.config.annotation.WsConfigurerAdapter;
import org.springframework.ws.transport.http.MessageDispatcherServlet;
import org.springframework.ws.wsdl.wsdl11.DefaultWsdl11Definition;

import org.springframework.boot.web.servlet.ServletRegistrationBean;

@EnableWs
@Configuration
public class WebServiceConfig extends WsConfigurerAdapter
{
    @Bean
    ServletRegistrationBean<MessageDispatcherServlet> messageDispatcherServlet(ApplicationContext applicationContext)
    {
        MessageDispatcherServlet servlet = new MessageDispatcherServlet();

        servlet.setApplicationContext(applicationContext);
        servlet.setTransformWsdlLocations(true);

        return new ServletRegistrationBean<>(servlet, "/ws/*");
    }

    @Bean(name = "cfdis")
    DefaultWsdl11Definition defaultWsdl11Definition(XsdSchema zcfdisSchema)
    {
        DefaultWsdl11Definition wsdl11Definition = new DefaultWsdl11Definition();
        wsdl11Definition.setPortTypeName("DocumentsPort");
        wsdl11Definition.setLocationUri("/ws");
        wsdl11Definition.setTargetNamespace("http://spring.io/guides/gs-producing-web-service");
        wsdl11Definition.setSchema(zcfdisSchema);

        return wsdl11Definition;
    }

    @Bean
    XsdSchema cfdisSchema()
    {
        return new SimpleXsdSchema(new ClassPathResource("cfdis.xsd"));
    }
}
