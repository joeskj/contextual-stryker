package com.github.joeskj.contextualstryker.services;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.HashSet;

public class FileService {

    private static final Logger LOG = Logger.getInstance(FileService.class);

    public Collection<VirtualFile> getFiles(@NotNull AnActionEvent event) {
        VirtualFile[] selectedFilesAndFolders = event.getData(CommonDataKeys.VIRTUAL_FILE_ARRAY);

        if (selectedFilesAndFolders == null || selectedFilesAndFolders.length == 0) {
            throw new IllegalStateException("Unable to detect selected file(s)");
        }
        LOG.debug("Selected files and folders: " + selectedFilesAndFolders.length);

        return getFiles(selectedFilesAndFolders);
    }

    public boolean isStrykable(VirtualFile file) {
        return file != null
                && "js".equalsIgnoreCase(file.getExtension())
                && !file.getPath().endsWith(".test.js");
    }

    private Collection<VirtualFile> getFiles(VirtualFile[] virtualFiles) {
        Collection<VirtualFile> files = new HashSet<>();

        for (VirtualFile virtualFile : virtualFiles) {
            if (virtualFile.isRecursiveOrCircularSymlink()) {
                LOG.info("Skipping file " + virtualFile.getPath() + " because it is a circular symlink or recursive");
            } else if (virtualFile.isDirectory()) {
                files.addAll(getFiles(virtualFile.getChildren()));
            } else {
                files.add(virtualFile);
            }
        }

        return files;
    }
}
