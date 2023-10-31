package com.example.demo.websocket;

import jakarta.servlet.http.HttpSession;
import jakarta.websocket.HandshakeResponse;
import jakarta.websocket.server.HandshakeRequest;
import jakarta.websocket.server.ServerEndpointConfig;

public class GetHttpSessionConfigurator extends ServerEndpointConfig.Configurator {
    @Override
    //类似servlet，在此处获取httpsession
    public void modifyHandshake(ServerEndpointConfig sec, HandshakeRequest request, HandshakeResponse response) {
        HttpSession session = (HttpSession) request.getHttpSession();
        sec.getUserProperties().put(HttpSession.class.getName(),session);
        sec.getUserProperties().put("QueryString",request.getQueryString());
        sec.getUserProperties().put("ParamMap",request.getParameterMap());
    }
}
