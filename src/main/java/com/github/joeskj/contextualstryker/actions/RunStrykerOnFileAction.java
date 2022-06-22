package com.github.joeskj.contextualstryker.actions;

import com.github.joeskj.contextualstryker.services.FileService;
import com.github.joeskj.contextualstryker.services.ProjectService;
import com.github.joeskj.contextualstryker.services.StrykerService;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

import static com.github.joeskj.contextualstryker.services.NotificationService.notifyError;

public class RunStrykerOnFileAction extends AnAction {

    private static final Logger LOG = Logger.getInstance(RunStrykerOnFileAction.class);

    private final FileService fileService = new FileService();
    private final ProjectService projectService = new ProjectService();
    private final StrykerService strykerService = new StrykerService(fileService);

    @Override
    public void update(@NotNull AnActionEvent event) {
        // Check if any of the selected files can be mutated by Stryker
        Collection<VirtualFile> files = fileService.getFiles(event);
        boolean isStrykable = files.stream().anyMatch(fileService::isStrykable);

        // Enable if any of the selected files can be mutated by Stryker, else disable
        event.getPresentation().setEnabledAndVisible(isStrykable);
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent event) {
        try {
            LOG.debug("Beginning RunStrykerOnFileAction.actionPerformed...");
            Project project = projectService.getProject(event);
            Collection<VirtualFile> files = fileService.getFiles(event);
            strykerService.runStryker(project, files);
        } catch (Exception e) {
            notifyError(e);
        }
    }

}
