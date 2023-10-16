package org.cafeboy.idea.plugin.codeit.actions;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CustomShortcutSet;
import com.intellij.openapi.project.Project;
import org.cafeboy.idea.plugin.codeit.ext.I18nSupport;
import org.cafeboy.idea.plugin.codeit.ui.CodeitView;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class DeleteAction extends AnAction {

    private final CodeitView codeitView;

    public DeleteAction(CodeitView codeitView) {
        super(I18nSupport.i18n_str("action.delete.text"), I18nSupport.i18n_str("action.delete.description"), AllIcons.Actions.Cancel);
        this.codeitView = codeitView;
        registerCustomShortcutSet(new CustomShortcutSet(KeyStroke.getKeyStroke("control D")), codeitView);
    }

    @Override
    public void update(@NotNull AnActionEvent e) {
        final Project project = e.getProject();
        e.getPresentation().setEnabled(project != null && codeitView != null && codeitView.getHistoryWidget().size() != 0);
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        codeitView.getHistoryWidget().removeAll();
    }
}
