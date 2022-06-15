package com.github.joeskj.contextualstryker.services;

import com.github.joeskj.contextualstryker.listeners.StrykerListener;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectUtil;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;
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

    private @NotNull String getBasePath(@NotNull Project project) {
        VirtualFile projectDir = ProjectUtil.guessProjectDir(project);
        if (projectDir == null) {
            throw new IllegalStateException("Unable to determine project directory");
        }

        VirtualFile file = projectDir.findChild("stryker.conf.json");
        if (file == null) {
            throw new IllegalStateException("Unable to find stryker.conf.json");
        }

        String basePath = file.getParent().getPath();
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
                .map(path -> path.replace(".test.js", ".js"))
                .collect(Collectors.joining(","));

        return "npx stryker run -m " + paths;
    }
}
