package org.cafeboy.idea.plugin.codeit.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.project.Project;
import org.cafeboy.idea.plugin.codeit.ui.CodeitView;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

/**
 * @author jumkey
 */
public class ExecAction extends AnAction {
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
    public void actionPerformed(@NotNull AnActionEvent e) {
        final CodeitView codeitView = (CodeitView) e.getRequiredData(PlatformDataKeys.CONTEXT_COMPONENT);
        codeitView.gen(e.getProject());
    }
}
