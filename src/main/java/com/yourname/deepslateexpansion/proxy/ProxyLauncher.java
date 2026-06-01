package com.yourname.deepslateexpansion.proxy;

import java.io.*;
import java.net.*;

public class ProxyLauncher {

    private static Process proxyProcess;

    public static void startProxy() throws IOException {
        // Extract embedded proxy files to a temporary folder
        File proxyDir = new File(System.getProperty("java.io.tmpdir"), "deepslateproxy");
        proxyDir.mkdirs();

        // Copy BungeeCord.jar, plugins, and config from mod resources to proxyDir
        extractResource("/proxy/BungeeCord.jar", new File(proxyDir, "BungeeCord.jar"));
        extractResource("/proxy/plugins/ViaVersion.jar", new File(proxyDir, "plugins/ViaVersion.jar"));
        extractResource("/proxy/plugins/ViaBackwards.jar", new File(proxyDir, "plugins/ViaBackwards.jar"));
        extractResource("/proxy/plugins/DeepslateProxyPlugin.jar", new File(proxyDir, "plugins/DeepslateProxyPlugin.jar"));
        extractResource("/proxy/config.yml", new File(proxyDir, "config.yml"));

        // Start the proxy
        ProcessBuilder pb = new ProcessBuilder(
            "java", "-jar", "BungeeCord.jar"
        );
        pb.directory(proxyDir);
        proxyProcess = pb.start();
    }

    public static void stopProxy() {
        if (proxyProcess != null) {
            proxyProcess.destroy();
        }
    }

    private static void extractResource(String resourcePath, File destination) throws IOException {
        try (InputStream in = ProxyLauncher.class.getResourceAsStream(resourcePath)) {
            if (in == null) throw new FileNotFoundException("Resource not found: " + resourcePath);
            destination.getParentFile().mkdirs();
            try (FileOutputStream out = new FileOutputStream(destination)) {
                byte[] buffer = new byte[1024];
                int len;
                while ((len = in.read(buffer)) != -1) {
                    out.write(buffer, 0, len);
                }
            }
        }
    }
}
