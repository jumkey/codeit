package org.cafeboy.idea.plugin.codeit.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.fileChooser.FileChooserFactory;
import com.intellij.openapi.fileChooser.FileSaverDescriptor;
import com.intellij.openapi.fileChooser.FileSaverDialog;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectUtil;
import com.intellij.openapi.vfs.VirtualFileWrapper;
import org.cafeboy.idea.plugin.codeit.core.tasks.SavingImageTask;
import org.cafeboy.idea.plugin.codeit.ext.I18nSupport;
import org.cafeboy.idea.plugin.codeit.ext.Utils;
import org.cafeboy.idea.plugin.codeit.ui.CodeitView;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

/**
 * @author jumkey
 */
public class SaveAction extends AnAction {

    @Override
    public void update(@NotNull final AnActionEvent e) {
        // Get required data keys
        final Project project = e.getProject();
        final CodeitView codeitView = (CodeitView) e.getData(PlatformDataKeys.CONTEXT_COMPONENT);

        // Set visibility only in case of existing project and icon
        e.getPresentation().setEnabled(project != null && codeitView != null && codeitView.getContentWidget().getCode() != null);
    }

    @Override
    public void actionPerformed(AnActionEvent e) {
        final Project project = e.getRequiredData(PlatformDataKeys.PROJECT);
        final CodeitView codeitView = (CodeitView) e.getRequiredData(PlatformDataKeys.CONTEXT_COMPONENT);
        final Icon icon = codeitView.getContentWidget().getCode();
        FileSaverDialog dialog = FileChooserFactory.getInstance()
                .createSaveFileDialog(new FileSaverDescriptor(I18nSupport.i18n_str("dialog.save.title"),
                        I18nSupport.i18n_str("dialog.save.description"), "png"), project);
        VirtualFileWrapper wrapper = dialog.save(ProjectUtil.guessProjectDir(project), Utils.getSuggestFileName());
        if (wrapper == null) {
            return;
        }
        new SavingImageTask(project, icon, wrapper).start();
    }

}
