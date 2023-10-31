package com.example.demo.config;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;
import org.springframework.web.util.WebAppRootListener;

@Configuration
public class WebSocketConfig implements ServletContextInitializer {
    @Bean
    //注入ServerEndpointExporter bean类 扫描@ServerEndpoint注解的bean
    public ServerEndpointExporter serverEndpointExporter(){
        return new ServerEndpointExporter();
    }

    //在此处修改WebSocket传输的限制，如不修改，无法传输长字符串
    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        servletContext.addListener(WebAppRootListener.class);
        servletContext.setInitParameter("org.apache.tomcat.websocket.textBufferSize","52428800");
        servletContext.setInitParameter("org.apache.tomcat.websocket.binaryBufferSize","52428800");
    }
}
