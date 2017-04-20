package com.ostdlabs.etoyataxi;


import org.eclipse.jetty.server.ForwardedRequestCustomizer;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.HttpConfiguration;
import org.eclipse.jetty.server.HttpConnectionFactory;
import org.eclipse.jetty.server.SecureRequestCustomizer;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.RequestLogHandler;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
import org.eclipse.jetty.util.thread.ThreadPool;
import org.eclipse.jetty.webapp.WebAppContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;


public final class Main {

    private static final Logger log = LoggerFactory.getLogger(Main.class);

    private static final int MIN_THREADS = 10;
    private static final int MAX_THREADS = 100;
    public static final String HOST = "0.0.0.0";
    private static final String ALIAS = "jetty8";
    private static final String ENVIRONMENT_PROPERTIES = "environment.properties";

    private static Properties properties = new Properties();


    private Main() {
    }

    private static final int PORT = 8083;
    private static final String WEB_XML =
            "WEB-INF/web.xml";

    public static void main(String[] args) {
        loadEnvironmentProperties();
        Server server = new Server(createThreadPool());
        server.addConnector(createConnector(server));
        server.setHandler(createHandlers());
        server.setStopAtShutdown(true);
        try {
            server.start();
            server.join();
        } catch (Exception e) {
            log.info("failed to start application server - exiting", e);
            System.exit(1);
        }
    }

    private static void loadEnvironmentProperties() {
        InputStream propertiesStream = Main.class.getClassLoader().getResourceAsStream(ENVIRONMENT_PROPERTIES);
        if (propertiesStream != null) {
            try {
                properties.load(propertiesStream);
            } catch (IOException e) {
                log.error("failed environment properties", e);
            }
        }
    }

    private static ThreadPool createThreadPool() {
        // TODO: You should configure these appropriately
        // for your environment - this is an example only
        QueuedThreadPool threadPool = new QueuedThreadPool();
        threadPool.setMinThreads(MIN_THREADS);
        threadPool.setMaxThreads(MAX_THREADS);
        return threadPool;
    }

    private static ServerConnector createConnector(Server server) {

        // HTTP Configuration
        HttpConfiguration http_config = new HttpConfiguration();
        http_config.setSecureScheme("http");
        http_config.setSecurePort(PORT);
        http_config.setOutputBufferSize(32768);
        http_config.setRequestHeaderSize(8192);
        http_config.setResponseHeaderSize(8192);
        http_config.setSendServerVersion(true);
        http_config.setSendDateHeader(false);
        // Add support for X-Forwarded headers
        http_config.addCustomizer(new ForwardedRequestCustomizer());


        // SSL HTTP Configuration
        HttpConfiguration https_config = new HttpConfiguration(http_config);
        https_config.addCustomizer(new SecureRequestCustomizer());

        // SSL Connector
        ServerConnector connector = new ServerConnector(server,
                new HttpConnectionFactory(https_config));

        connector.setPort(PORT);
        connector.setHost(HOST);
        return connector;
    }

    private static HandlerCollection createHandlers() {
        WebAppContext ctx = new WebAppContext();
        ctx.setContextPath("/");
        ctx.setWar(getShadedWarUrl());
        List<Handler> handlers = new ArrayList<Handler>();

        handlers.add(ctx);

        HandlerList contexts = new HandlerList();
        contexts.setHandlers(handlers.toArray(new Handler[0]));

        RequestLogHandler log = new RequestLogHandler();

        HandlerCollection result = new HandlerCollection();
        result.setHandlers(new Handler[]{contexts, log});

        return result;
    }

    private static String getShadedWarUrl() {
        String urlStr = getResource(WEB_XML).toString();
        // Strip off "WEB-INF/web.xml"
        return urlStr.substring(0, urlStr.length() - WEB_XML.length());
    }

    private static URL getResource(String aResource) {
        return Thread.currentThread().
                getContextClassLoader().getResource(aResource);
    }
}
