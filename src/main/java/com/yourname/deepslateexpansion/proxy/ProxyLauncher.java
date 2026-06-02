package com.yourname.deepslateexpansion.proxy;

import java.io.*;
import java.nio.file.*;

public class ProxyLauncher {

    private static Process proxyProcess;

    /**
     * Extracts the embedded proxy files (BungeeCord, plugins, config) from the mod's resources
     * to a temporary folder and starts the proxy server.
     */
    public static void startProxy() {
        try {
            // Create a temporary directory for the proxy
            Path proxyDir = Files.createTempDirectory("deepslateproxy");
            File dir = proxyDir.toFile();
            dir.mkdirs();

            // List of resources to extract (source path in JAR → destination file name in proxy dir)
            String[][] resources = {
                { "/proxy/BungeeCord.jar", "BungeeCord.jar" },
                { "/proxy/config.yml", "config.yml" },
                { "/proxy/plugins/ViaVersion.jar", "plugins/ViaVersion.jar" },
                { "/proxy/plugins/ViaBackwards.jar", "plugins/ViaBackwards.jar" },
                { "/proxy/plugins/DeepslateProxyPlugin.jar", "plugins/DeepslateProxyPlugin.jar" }
            };

            // Extract each resource
            for (String[] entry : resources) {
                extractResource(entry[0], new File(dir, entry[1]));
            }

            // Launch the proxy
            ProcessBuilder pb = new ProcessBuilder(
                "java", "-jar", "BungeeCord.jar"
            );
            pb.directory(dir);
            // Redirect output to the game's log (optional – you can remove these lines if you don't want clutter)
            pb.redirectOutput(ProcessBuilder.Redirect.INHERIT);
            pb.redirectError(ProcessBuilder.Redirect.INHERIT);

            proxyProcess = pb.start();
            System.out.println("[DeepslateExpansion] Local proxy started successfully.");
        } catch (Exception e) {
            System.err.println("[DeepslateExpansion] Failed to start local proxy:");
            e.printStackTrace();
        }
    }

    /**
     * Stops the proxy when the game shuts down (call this in a shutdown hook or mod disable event).
     */
    public static void stopProxy() {
        if (proxyProcess != null && proxyProcess.isAlive()) {
            proxyProcess.destroy();
            System.out.println("[DeepslateExpansion] Local proxy stopped.");
        }
    }

    private static void extractResource(String resourcePath, File destination) throws IOException {
        try (InputStream in = ProxyLauncher.class.getResourceAsStream(resourcePath)) {
            if (in == null) {
                throw new FileNotFoundException("Resource not found: " + resourcePath);
            }
            destination.getParentFile().mkdirs();
            Files.copy(in, destination.toPath(), StandardCopyOption.REPLACE_EXISTING);
        }
    }
}
