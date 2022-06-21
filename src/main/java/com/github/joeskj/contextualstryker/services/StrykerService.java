package com.github.joeskj.contextualstryker.services;

import com.github.joeskj.contextualstryker.listeners.StrykerListener;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectUtil;
import com.intellij.openapi.roots.ContentIterator;
import com.intellij.openapi.vfs.VfsUtilCore;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.plugins.terminal.ShellTerminalWidget;
import org.jetbrains.plugins.terminal.TerminalView;

import java.io.IOException;
import java.util.Collection;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

public class StrykerService {

    private static final Logger LOG = Logger.getInstance(StrykerService.class);

    private final FileService fileService;
    private String basePath;

    public StrykerService(FileService fileService) {
        this.fileService = fileService;
    }

    public void verifyStrykerInstallation(Project project) {
        basePath = getBasePath(project);
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

        AtomicReference<String> strykerFolderPath = new AtomicReference<>("");

        ContentIterator iterator = file -> {
            if ("stryker.conf.json".equals(file.getName())) {
                strykerFolderPath.set(file.getParent().getPath());
                return false;
            }
            return true;
        };
        VfsUtilCore.iterateChildrenRecursively(projectDir, null, iterator);
        LOG.debug("Base path: " + strykerFolderPath);

        return strykerFolderPath.get();
    }

    public void runStryker(Project project, Collection<VirtualFile> files) throws IOException {
        String command = getStrykerCommand(files);
        LOG.debug("Stryker command: " + command);

        TerminalView terminalView = TerminalView.getInstance(project);
        ShellTerminalWidget shellWidget = terminalView.createLocalShellWidget(basePath, "Stryker");
        shellWidget.getTerminalTextBuffer().addModelListener(new StrykerListener(shellWidget));
        shellWidget.executeCommand(command);
    }

    private String getStrykerCommand(Collection<VirtualFile> files) {
        String paths = files.stream()
                .filter(fileService::isStrykable)
                .map(VirtualFile::getPath)
                .map(path -> path.replace(".test.js", ".js"))
                .distinct()
                .collect(Collectors.joining(","));

        return "npx stryker run -m " + paths;
    }
}
