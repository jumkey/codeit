package org.cafeboy.idea.plugin.codeit.actions;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.actionSystem.CustomShortcutSet;
import com.intellij.openapi.project.Project;
import org.cafeboy.idea.plugin.codeit.ext.I18nSupport;
import org.cafeboy.idea.plugin.codeit.ui.CodeitView;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class ShowAction extends AnAction {

    private final CodeitView codeitView;

    public ShowAction(CodeitView codeitView) {
        super(I18nSupport.i18n_str("action.show.text"), I18nSupport.i18n_str("action.show.description"), AllIcons.Actions.ShowWriteAccess);
        this.codeitView = codeitView;
        setShortcutSet(new CustomShortcutSet(KeyStroke.getKeyStroke("control R")));
    }

    @Override
    public void update(@NotNull AnActionEvent e) {
        final Project project = e.getProject();
        e.getPresentation().setEnabled(project != null && codeitView.getHistoryWidget().size() != 0);
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        final Project project = e.getRequiredData(CommonDataKeys.PROJECT);
        codeitView.getHistoryWidget().changeText();
        codeitView.gen(project);
    }
}
