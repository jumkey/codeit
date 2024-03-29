package org.cafeboy.idea.plugin.codeit.actions;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CustomShortcutSet;
import com.intellij.openapi.ide.CopyPasteManager;
import com.intellij.openapi.project.Project;
import com.intellij.util.ui.TextTransferable;
import org.cafeboy.idea.plugin.codeit.ext.I18nSupport;
import org.cafeboy.idea.plugin.codeit.ui.CodeitView;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class CopyHistoryAction extends AnAction {

    private final CodeitView codeitView;

    public CopyHistoryAction(CodeitView codeitView) {
        super(I18nSupport.i18n_str("action.copy.text"), I18nSupport.i18n_str("action.copy.description"), AllIcons.Actions.Copy);
        this.codeitView = codeitView;
        setShortcutSet(new CustomShortcutSet(KeyStroke.getKeyStroke("control C")));
    }

    @Override
    public void update(@NotNull AnActionEvent e) {
        final Project project = e.getProject();
        e.getPresentation().setEnabled(project != null && codeitView.getHistoryWidget().size() != 0);
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        final String text = codeitView.getHistoryWidget().getSelectedValues();

        CopyPasteManager.getInstance().setContents(new TextTransferable(text));
    }
}
