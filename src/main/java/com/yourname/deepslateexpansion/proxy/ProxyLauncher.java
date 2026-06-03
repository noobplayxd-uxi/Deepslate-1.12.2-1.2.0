package com.yourname.deepslateexpansion.proxy;

import java.io.*;
import java.nio.file.*;
import java.util.zip.*;

public class ProxyLauncher {
    private static Process proxyProcess;

    public static void startProxy() {
        try {
            Path tempDir = Files.createTempDirectory("deepslateproxy");
            File dir = tempDir.toFile();

            String[][] resources = {
                { "/proxy/BungeeCord.jar", "BungeeCord.jar" },
                { "/proxy/config.yml", "config.yml" },
                { "/proxy/plugins/ViaVersion.jar", "plugins/ViaVersion.jar" },
                { "/proxy/plugins/ViaBackwards.jar", "plugins/ViaBackwards.jar" },
                { "/proxy/plugins/ViaBackwards/config.yml", "plugins/ViaBackwards/config.yml" },
                { "/proxy/java17.zip", "java17.zip" }
            };

            for (String[] entry : resources) {
                extractResource(entry[0], new File(dir, entry[1]));
            }

            // Unzip the mini JRE
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
            javaZip.delete();

            String javaExe = new File(javaDir, "bin/java.exe").getAbsolutePath();
            ProcessBuilder pb = new ProcessBuilder(javaExe, "-jar", "BungeeCord.jar");
            pb.directory(dir);
            pb.redirectOutput(ProcessBuilder.Redirect.INHERIT);
            pb.redirectError(ProcessBuilder.Redirect.INHERIT);

            proxyProcess = pb.start();
            System.out.println("[DeepslateExpansion] Local proxy started with mini Java 17.");
        } catch (Exception e) {
            System.err.println("[DeepslateExpansion] Failed to start proxy:");
            e.printStackTrace();
        }
    }

    public static void stopProxy() {
        if (proxyProcess != null && proxyProcess.isAlive()) {
            proxyProcess.destroy();
        }
    }

    private static void extractResource(String resourcePath, File destination) throws IOException {
        try (InputStream in = ProxyLauncher.class.getResourceAsStream(resourcePath)) {
            if (in == null) throw new FileNotFoundException("Resource not found: " + resourcePath);
            destination.getParentFile().mkdirs();
            Files.copy(in, destination.toPath(), StandardCopyOption.REPLACE_EXISTING);
        }
    }
}
