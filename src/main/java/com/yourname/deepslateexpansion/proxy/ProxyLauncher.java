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
            dir.mkdirs();

            // Files to extract – configs are optional and will be skipped if missing
            String[][] resources = {
                { "/proxy/BungeeCord.jar", "BungeeCord.jar", "required" },
                { "/proxy/config.yml", "config.yml", "required" },
                { "/proxy/plugins/ViaVersion.jar", "plugins/ViaVersion.jar", "required" },
                { "/proxy/plugins/ViaBackwards.jar", "plugins/ViaBackwards.jar", "required" },
                { "/proxy/plugins/ViaVersion/config.yml", "plugins/ViaVersion/config.yml", "optional" },
                { "/proxy/java17.zip", "java17.zip", "required" }
            };

            for (String[] entry : resources) {
                try {
                    extractResource(entry[0], new File(dir, entry[1]));
                } catch (FileNotFoundException e) {
                    if ("required".equals(entry[2])) {
                        throw e;   // stop if a required file is missing
                    } else {
                        System.err.println("[DeepslateExpansion] Optional file not found, continuing: " + entry[0]);
                    }
                }
            }

            // Unzip the mini JRE
            File javaZip = new File(dir, "java17.zip");
            if (!javaZip.exists()) {
                throw new FileNotFoundException("java17.zip not found – cannot start proxy.");
            }
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
            System.err.println("[DeepslateExpansion] Failed to start proxy: " + e.getMessage());
            // Don't print full stack trace unless needed for debugging
        }
    }

    public static void stopProxy() {
        if (proxyProcess != null && proxyProcess.isAlive()) {
            proxyProcess.destroy();
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
