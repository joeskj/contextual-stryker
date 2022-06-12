package com.github.joeskj.contextualstryker.services;

import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.diagnostic.Logger;

public class NotificationService {

    private static final Logger LOG = Logger.getInstance(NotificationService.class);

    private static boolean hasErrorOccurred;

    private NotificationService() {}

    public static void notifyError(Exception e) {
        hasErrorOccurred = true;
        LOG.error(e);
        Notification notification = new Notification("Plugin Error", "Contextual Stryker plugin error", e.getMessage(), NotificationType.ERROR);
        Notifications.Bus.notify(notification);
    }

    public static void notifySuccess() {
        if (hasErrorOccurred) {
            return;
        }
        LOG.info("Stryker ran successfully");
        Notification notification = new Notification("Run Anything", "Stryker run completed", "", NotificationType.INFORMATION);
        Notifications.Bus.notify(notification);
    }

}
