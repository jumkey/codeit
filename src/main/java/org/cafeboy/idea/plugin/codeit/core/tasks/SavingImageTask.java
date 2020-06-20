package org.cafeboy.idea.plugin.codeit.core.tasks;

import com.intellij.openapi.progress.PerformInBackgroundOption;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFileWrapper;
import org.cafeboy.idea.plugin.codeit.ext.I18nSupport;
import org.cafeboy.idea.plugin.codeit.ext.Utils;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

/**
 * @author jumkey
 */
public class SavingImageTask {

    private final Task.Backgroundable task;

    public SavingImageTask(Project project, Icon icon, VirtualFileWrapper wrapper) {
        task = new Task.Backgroundable(project,
                I18nSupport.i18n_str("progress.title.saving"), true, PerformInBackgroundOption.ALWAYS_BACKGROUND) {

            @Override
            public void run(@NotNull ProgressIndicator progressIndicator) {
                try {
                    boolean b = Utils.saveIcon(icon, wrapper.getFile());
                    if (b) {
                        Utils.message(I18nSupport.i18n_str("notification.success.title"), I18nSupport.i18n_str("notification.success.content"));
                    } else {
                        Utils.warning(I18nSupport.i18n_str("notification.failure.title"), I18nSupport.i18n_str("notification.failure.content"));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
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
