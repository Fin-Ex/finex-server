package ru.finex.core.management;

import io.hawt.embedded.Options;
import org.apache.commons.io.IOUtils;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.HttpConfiguration;
import org.eclipse.jetty.server.HttpConnectionFactory;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.SslConnectionFactory;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.util.log.Log;
import org.eclipse.jetty.util.log.Slf4jLog;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.eclipse.jetty.webapp.WebAppContext;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.Arrays;
import java.util.Objects;
import javax.annotation.PreDestroy;
import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * @author m0nster.mind
 */
@Singleton
@SuppressWarnings("checkstyle:all")
public class HawtioServiceImpl implements HawtioService {

    private static final String VERSION = "2.14.3";
    private static final String WAR_NAME = "hawtio-default-" + VERSION + ".war";

    private Server server;
    private Slf4jLog log;
    private Options options;
    private boolean enabled;

    @Inject
    public HawtioServiceImpl(ManagementConfig config) {
        options = new Options();
        options.init();

        options.setHost(config.getHost());
        options.setPort(config.getPort());
        options.setContextPath(config.getPath());
        enabled = config.isEnabled();
    }

    @Override
    public void deployHawtio() {
        if (!enabled) {
            return;
        }

        System.setProperty("hawtio.authenticationEnabled", "false");
        extractWarFile();
        configureLogging();

        try {
            server = new Server(new InetSocketAddress(InetAddress.getByName(options.getHost()), options.getPort()));

            HandlerCollection handlers = new HandlerCollection();
            handlers.setServer(server);
            server.setHandler(handlers);

            String scheme = resolveScheme(server);
            WebAppContext webapp = createWebapp(server, scheme);

            // lets set a temporary directory so jetty doesn't bork if some process zaps /tmp/*
            File tempDir = getJettyTempDir();
            log.info("Using temp directory for jetty: {}", tempDir.getPath());
            webapp.setTempDirectory(tempDir);

            // check for 3rd party plugins before we add hawtio, so they are initialized before hawtio
            findThirdPartyPlugins(handlers, tempDir);

            // add hawtio
            handlers.addHandler(webapp);

            log.debug("About to start Hawtio {}", webapp.getWar());
            server.start();

            log.info("Embedded Hawtio: {}://{}:{}{}", scheme, options.getHost(), options.getPort(), options.getContextPath());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @PreDestroy
    private void destroy() throws Exception {
        if (server != null) {
            server.stop();
            server.destroy();
        }
    }

    private File extractWarFile() {
        File warFile;
        try {
            warFile = File.createTempFile("finex-", WAR_NAME);
        } catch (IOException e) {
            throw new RuntimeException("Fail to create temporary file!");
        }

        try (var out = new FileOutputStream(warFile);
             var in = getClass().getClassLoader().getResourceAsStream(WAR_NAME)) {
            Objects.requireNonNull(in, "Embedded WAR file not found!");
            IOUtils.copy(in, out);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }

        warFile.deleteOnExit();

        options.setWar(warFile.getPath());

        return warFile;
    }

    private void configureLogging() {
        System.setProperty("org.eclipse.jetty.util.log.class", Slf4jLog.class.getName());
        log = new Slf4jLog("jetty");
        Log.setLog(log);
    }

    private File getJettyTempDir() {
        String homeDir = System.getProperty("user.home", ".") + "/.hawtio";
        String tempDirPath = homeDir + "/tmp";
        File tempDir = new File(tempDirPath);
        tempDir.mkdirs();
        return tempDir;
    }

    private WebAppContext createWebapp(Server server, String scheme) {
        WebAppContext webapp = new WebAppContext();
        webapp.setServer(server);
        webapp.setContextPath(options.getContextPath());
        webapp.setWar(options.getWar());
        webapp.setParentLoaderPriority(true);
        webapp.setLogUrlOnStart(true);
        webapp.setInitParameter("scheme", scheme);
        webapp.setExtraClasspath(options.getExtraClassPath());
        return webapp;
    }

    private String resolveScheme(Server server) {
        String scheme = "http";
        if (null != options.getKeyStore()) {
            log.debug("Configuring SSL");
            SslContextFactory sslcontf = new SslContextFactory();
            HttpConfiguration httpconf = new HttpConfiguration();
            sslcontf.setKeyStorePath(options.getKeyStore());
            if (null != options.getKeyStorePass()) {
                sslcontf.setKeyStorePassword(options.getKeyStorePass());
            } else {
                log.debug("Attempting to open keystore with no password...");
            }
            try (ServerConnector sslconn = new ServerConnector(server, new SslConnectionFactory(sslcontf, "http/1.1"), new HttpConnectionFactory(httpconf));) {
                sslconn.setPort(options.getPort());
                server.setConnectors(new Connector[] { sslconn });

            }
            scheme = "https";
        }
        String sysScheme = System.getProperty("hawtio.redirect.scheme");
        if (null == sysScheme) {
            log.debug("Implicitly setting Scheme = " + scheme);
            System.setProperty("hawtio.redirect.scheme", scheme);
        } else {
            log.debug("Scheme Was Set Explicitly To = " + scheme);
            scheme = sysScheme;
        }
        return scheme;
    }

    private void findThirdPartyPlugins(HandlerCollection handlers, File tempDir) {
        File dir = new File(options.getPlugins());
        if (!dir.exists() || !dir.isDirectory()) {
            return;
        }

        log.info("Scanning for 3rd party plugins in directory: {}", dir.getPath());

        // find any .war files
        File[] wars = dir.listFiles((d, name) -> isWarFileName(name));
        if (wars == null) {
            return;
        }
        Arrays.stream(wars).forEach(war -> deployPlugin(war, handlers, tempDir));
    }

    private void deployPlugin(File war, HandlerCollection handlers, File tempDir) {
        String contextPath = resolveContextPath(war);

        WebAppContext plugin = new WebAppContext();
        plugin.setServer(handlers.getServer());
        plugin.setContextPath(contextPath);
        plugin.setWar(war.getAbsolutePath());
        // plugin.setParentLoaderPriority(true);
        plugin.setLogUrlOnStart(true);

        // need to have private sub directory for each plugin
        File pluginTempDir = new File(tempDir, war.getName());
        pluginTempDir.mkdirs();

        plugin.setTempDirectory(pluginTempDir);
        plugin.setThrowUnavailableOnStartupException(true);

        handlers.addHandler(plugin);
        log.info("Added 3rd party plugin with context-path: {}", contextPath);
    }

    private String resolveContextPath(File war) {
        String contextPath = "/" + war.getName();
        if (contextPath.endsWith(".war")) {
            contextPath = contextPath.substring(0, contextPath.length() - 4);
        }
        // custom plugins must not use same context-path as Hawtio
        if (contextPath.equals(options.getContextPath())) {
            throw new IllegalArgumentException("3rd party plugin " + war.getName() + " cannot have same name as Hawtio context path. Rename the plugin file to avoid the clash.");
        }
        return contextPath;
    }

    private boolean isWarFileName(String name) {
        return name.toLowerCase().endsWith(".war");
    }

}
