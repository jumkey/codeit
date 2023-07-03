package org.cafeboy.idea.plugin.codeit.actions;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.actionSystem.ToggleAction;
import com.intellij.openapi.project.Project;
import org.cafeboy.idea.plugin.codeit.ui.CodeitView;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

/**
 * 启用草料二维码生成
 */
public class EnableCliAction extends ToggleAction {

    private boolean isCli = false;

    @Override
    public void update(@NotNull AnActionEvent e) {
        final Project project = e.getProject();
        Component component = e.getData(PlatformDataKeys.CONTEXT_COMPONENT);
        CodeitView codeitView = null;
        if (component instanceof CodeitView) {
            codeitView = (CodeitView) component;
        }

        e.getPresentation().setEnabledAndVisible(codeitView != null && project != null);
    }

    @Override
    public boolean isSelected(@NotNull AnActionEvent e) {
        return isCli;
    }

    @Override
    public void setSelected(@NotNull AnActionEvent e, boolean state) {
        final CodeitView codeitView = (CodeitView) e.getRequiredData(PlatformDataKeys.CONTEXT_COMPONENT);
        codeitView.getContentWidget().toggleGroupData(state);
        isCli = state;
    }
}
