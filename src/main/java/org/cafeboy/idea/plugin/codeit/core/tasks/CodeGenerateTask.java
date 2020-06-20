package org.cafeboy.idea.plugin.codeit.core.tasks;

import com.intellij.openapi.progress.PerformInBackgroundOption;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.project.Project;
import org.cafeboy.idea.plugin.codeit.callback.OnExecuteListener;
import org.cafeboy.idea.plugin.codeit.core.Api;
import org.cafeboy.idea.plugin.codeit.core.CliApiImpl;
import org.cafeboy.idea.plugin.codeit.core.ZxingApiImpl;
import org.cafeboy.idea.plugin.codeit.ext.I18nSupport;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

/**
 * @author jumkey
 */
public class CodeGenerateTask {

    private final Task.Backgroundable task;

    private final Api api = new ZxingApiImpl();
    private final Api api2 = new CliApiImpl();

    public CodeGenerateTask(Project project, String text, String mode, Boolean isCli, OnExecuteListener onExecuteListener) {
        onExecuteListener.onStart();
        task = new Task.Backgroundable(project,
                I18nSupport.i18n_str("progress.title.generating"), true, PerformInBackgroundOption.ALWAYS_BACKGROUND) {

            @Override
            public void run(@NotNull ProgressIndicator progressIndicator) {
                try {
                    Icon icon = (isCli ? api2 : api).getCode(text, mode);
                    onExecuteListener.onSuccess(icon);
                } catch (Exception e) {
                    e.printStackTrace();
                    onExecuteListener.onError(e.getMessage());
                } finally {
                    onExecuteListener.onComplete();
                }
            }
        };
    }

    public void start() {
        if (task != null) {
            ProgressManager.getInstance().run(task);
        }
    }
}
