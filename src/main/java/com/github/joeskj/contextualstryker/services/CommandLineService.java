package com.github.joeskj.contextualstryker.services;

import com.intellij.openapi.diagnostic.Logger;

import java.io.*;

public class CommandLineService {

    private static final Logger LOG = Logger.getInstance(CommandLineService.class);

    private CommandLineService() {
    }

    public static String runShellCommand(String basePath, String command) {
        String output = "";

        try {
            ProcessBuilder builder = new ProcessBuilder("cmd", "/c", command);
            builder.directory(new File(basePath));
            Process process = builder.start();
            output = getStringFromInputStream(process.getInputStream());

            int exitCode = process.waitFor();
            if (exitCode != 0) {
                output += getStringFromInputStream(process.getErrorStream());
            }
        } catch (IOException e) {
            LOG.error(e);
        } catch (InterruptedException e) {
            LOG.error(e);
            Thread.currentThread().interrupt();
        }

        return output;
    }

    private static String getStringFromInputStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();

        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        String line;
        while ((line = reader.readLine()) != null) {
            output.append(line).append("\n");
        }

        return output.toString();
    }
}
