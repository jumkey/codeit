package org.cafeboy.idea.plugin.codeit.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.ide.CopyPasteManager;
import com.intellij.openapi.project.Project;
import org.cafeboy.idea.plugin.codeit.core.ImageSelection;
import org.cafeboy.idea.plugin.codeit.ext.Utils;
import org.cafeboy.idea.plugin.codeit.ui.CodeitView;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class CopyAction extends AnAction {

    @Override
    public void update(@NotNull AnActionEvent e) {
        final Project project = e.getProject();
        final CodeitView codeitView = (CodeitView) e.getData(PlatformDataKeys.CONTEXT_COMPONENT);
        e.getPresentation().setEnabled(project != null && codeitView != null && codeitView.getContentWidget().getCode() != null);
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        final CodeitView codeitView = (CodeitView) e.getRequiredData(PlatformDataKeys.CONTEXT_COMPONENT);
        final Icon icon = codeitView.getContentWidget().getCode();

        CopyPasteManager.getInstance().setContents(new ImageSelection(Utils.getBufferedImage((ImageIcon) icon)));
    }
}
