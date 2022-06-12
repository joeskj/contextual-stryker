package com.github.joeskj.contextualstryker.listeners;

import com.jediterm.terminal.model.TerminalModelListener;
import org.jetbrains.plugins.terminal.ShellTerminalWidget;

import static com.github.joeskj.contextualstryker.services.NotificationService.notifyError;
import static com.github.joeskj.contextualstryker.services.NotificationService.notifySuccess;

public class StrykerListener implements TerminalModelListener {

    private final ShellTerminalWidget shellWidget;

    private boolean hasOutcomeBeenDetermined;

    public StrykerListener(ShellTerminalWidget shellWidget) {
        this.shellWidget = shellWidget;
    }

    @Override
    public void modelChanged() {
        if (hasOutcomeBeenDetermined) {
            return;
        }

        String output = shellWidget.getTerminalTextBuffer().getScreenLines();
        if (output.contains("'npx' is not recognized")) {
            hasOutcomeBeenDetermined = true;
            notifyError(new IllegalStateException("Unable to detect NPM installation"));
        } else if (output.contains("MutationTestExecutor Done")) {
            hasOutcomeBeenDetermined = true;
            notifySuccess();
        }
    }
}
