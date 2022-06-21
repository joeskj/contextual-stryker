package com.github.joeskj.contextualstryker.services;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;

public class FileService {

    private static final Logger LOG = Logger.getInstance(FileService.class);

    public Collection<VirtualFile> getFiles(@NotNull AnActionEvent event) {
        VirtualFile[] selectedFilesAndFolders = event.getData(CommonDataKeys.VIRTUAL_FILE_ARRAY);

        if (selectedFilesAndFolders == null || selectedFilesAndFolders.length == 0) {
            LOG.debug("Unable to detect selected file(s)");
            return new HashSet<>();
        }

        return getFiles(selectedFilesAndFolders);
    }

    private Collection<VirtualFile> getFiles(VirtualFile[] filesAndFolders) {
        Collection<VirtualFile> files = new HashSet<>();

        LOG.debug("Iterating over " + filesAndFolders.length + " selected files/folders...");
        for (VirtualFile fileOrFolder : filesAndFolders) {
            List<VirtualFile> children = VfsUtil.collectChildrenRecursively(fileOrFolder);
            children.removeIf(VirtualFile::isDirectory);
            files.addAll(children);
        }
        LOG.debug("Found " + files.size() + " file(s)");

        return files;
    }

    public boolean isStrykable(VirtualFile file) {
        return file != null
                && "js".equalsIgnoreCase(file.getExtension());
    }
}
