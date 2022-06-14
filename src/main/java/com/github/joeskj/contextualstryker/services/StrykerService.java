package com.github.joeskj.contextualstryker.services;

import com.github.joeskj.contextualstryker.listeners.StrykerListener;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.plugins.terminal.ShellTerminalWidget;
import org.jetbrains.plugins.terminal.TerminalView;

import java.io.IOException;
import java.util.Collection;
import java.util.stream.Collectors;

public class StrykerService {

    private static final Logger LOG = Logger.getInstance(StrykerService.class);

    private final FileService fileService;

    public StrykerService(FileService fileService) {
        this.fileService = fileService;
    }

    public void verifyStrykerInstallation(Project project) {
        String basePath = getBasePath(project);
        String output = CommandLineService.runShellCommand(basePath, "npm ls --depth 0 @stryker-mutator/core");
        LOG.debug("verifyStrykerInstallation output: " + output);
        if (output.contains("(empty)")) {
            throw new IllegalStateException("Unable to detect Stryker installation at base path: " + basePath);
        }
    }

    @Nullable
    private String getBasePath(@NotNull Project project) {
        String basePath = project.getBasePath();
        LOG.debug("Base path: " + basePath);
        return basePath;
    }

    public void runStryker(Project project, Collection<VirtualFile> files) throws IOException {
        String command = getStrykerCommand(files);
        LOG.debug("Stryker command: " + command);

        TerminalView terminalView = TerminalView.getInstance(project);
        ShellTerminalWidget shellWidget = terminalView.createLocalShellWidget(project.getBasePath(), "Stryker");
        shellWidget.getTerminalTextBuffer().addModelListener(new StrykerListener(shellWidget));
        shellWidget.executeCommand(command);
    }

    private String getStrykerCommand(Collection<VirtualFile> files) {
        String paths = files.stream()
                .filter(fileService::isStrykable)
                .map(VirtualFile::getPath)
                .collect(Collectors.joining(","));

        return "npx stryker run -m " + paths;
    }
}