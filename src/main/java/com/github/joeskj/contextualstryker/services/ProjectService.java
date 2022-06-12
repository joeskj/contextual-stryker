package com.github.joeskj.contextualstryker.services;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

public class ProjectService {

    private static final Logger LOG = Logger.getInstance(ProjectService.class);

    @NotNull
    public Project getProject(@NotNull AnActionEvent event) {
        Project project = event.getProject();
        if (project == null) {
            throw new IllegalStateException("Unable to detect project");
        }
        LOG.debug("Project detected: " + project.getName());
        return project;
    }
}
