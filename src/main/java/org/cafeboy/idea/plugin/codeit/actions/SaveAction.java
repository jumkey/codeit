package org.cafeboy.idea.plugin.codeit.actions;

import com.intellij.icons.AllIcons;
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
import org.cafeboy.idea.plugin.codeit.ui.CodeitContent;

import javax.swing.*;

/**
 * @author jumkey
 */
public class SaveAction extends AnAction {

    private final CodeitContent codeitContent;

    public SaveAction(CodeitContent codeitContent) {
        super(I18nSupport.i18n_str("action.save.text"), I18nSupport.i18n_str("action.save.description"), AllIcons.Actions.Menu_saveall);
        this.codeitContent = codeitContent;
    }

    @Override
    public void actionPerformed(AnActionEvent anActionEvent) {
        final Project project = PlatformDataKeys.PROJECT.getData(anActionEvent.getDataContext());
        final Icon icon = codeitContent.getContentWidget().getCode();
        if (project == null || icon == null) {
            return;
        }
        FileSaverDialog dialog = FileChooserFactory.getInstance()
                .createSaveFileDialog(new FileSaverDescriptor(I18nSupport.i18n_str("dialog.save.title"),
                        I18nSupport.i18n_str("dialog.save.description"), "png"), codeitContent);
        VirtualFileWrapper wrapper = dialog.save(ProjectUtil.guessProjectDir(project), Utils.getSuggestFileName());
        if (wrapper == null) {
            return;
        }
        new SavingImageTask(project, icon, wrapper).start();
    }

}