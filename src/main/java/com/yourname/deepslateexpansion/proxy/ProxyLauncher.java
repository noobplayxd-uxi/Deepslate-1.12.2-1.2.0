package com.yourname.deepslateexpansion.proxy;

import java.io.*;
import java.nio.file.*;
import java.util.zip.*;

public class ProxyLauncher {
    private static Process proxyProcess;

    /**
     * Extracts the embedded proxy files (BungeeCord, plugins, configs, Java 17)
     * and starts the proxy server in the background.
     */
    public static void startProxy() {
        try {
            // 1. Create a temporary directory for the proxy
            Path tempDir = Files.createTempDirectory("deepslateproxy");
            File dir = tempDir.toFile();
            dir.mkdirs();

            // 2. List of resources to extract (source path in JAR → destination file name)
            //    Note: ViaBackwards/config.yml is intentionally omitted – it's not required
            //    because the chunk‑extended feature is handled entirely by ViaVersion.
            String[][] resources = {
                { "/proxy/BungeeCord.jar", "BungeeCord.jar" },
                { "/proxy/config.yml", "config.yml" },
                { "/proxy/plugins/ViaVersion.jar", "plugins/ViaVersion.jar" },
                { "/proxy/plugins/ViaBackwards.jar", "plugins/ViaBackwards.jar" },
                { "/proxy/plugins/ViaVersion/config.yml", "plugins/ViaVersion/config.yml" },
                { "/proxy/java17.zip", "java17.zip" }
            };

            for (String[] entry : resources) {
                extractResource(entry[0], new File(dir, entry[1]));
            }

            // 3. Unzip the mini Java 17 JRE
            File javaZip = new File(dir, "java17.zip");
            File javaDir = new File(dir, "java17");
            javaDir.mkdirs();
            try (ZipInputStream zis = new ZipInputStream(new FileInputStream(javaZip))) {
                ZipEntry entry;
                while ((entry = zis.getNextEntry()) != null) {
                    File destFile = new File(javaDir, entry.getName());
                    if (entry.isDirectory()) {
                        destFile.mkdirs();
                    } else {
                        destFile.getParentFile().mkdirs();
                        Files.copy(zis, destFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                    }
                    zis.closeEntry();
                }
            }
            // Delete the zip after extraction to save space
            javaZip.delete();

            // 4. Build the path to the mini Java executable
            String javaExe = new File(javaDir, "bin/java.exe").getAbsolutePath();

            // 5. Start BungeeCord with the mini Java 17
            ProcessBuilder pb = new ProcessBuilder(javaExe, "-jar", "BungeeCord.jar");
            pb.directory(dir);
            // Redirect proxy output to the Minecraft console (optional, can be removed)
            pb.redirectOutput(ProcessBuilder.Redirect.INHERIT);
            pb.redirectError(ProcessBuilder.Redirect.INHERIT);

            proxyProcess = pb.start();
            System.out.println("[DeepslateExpansion] Local proxy started with mini Java 17.");

        } catch (Exception e) {
            System.err.println("[DeepslateExpansion] Failed to start proxy:");
            e.printStackTrace();
        }
    }

    /**
     * Stops the proxy when the game shuts down.
     */
    public static void stopProxy() {
        if (proxyProcess != null && proxyProcess.isAlive()) {
            proxyProcess.destroy();
            System.out.println("[DeepslateExpansion] Local proxy stopped.");
        }
    }

    /**
     * Extracts a single resource from the mod's JAR to a file on disk.
     */
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
