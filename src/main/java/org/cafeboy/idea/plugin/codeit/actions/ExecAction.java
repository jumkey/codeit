package org.cafeboy.idea.plugin.codeit.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.project.Project;
import org.cafeboy.idea.plugin.codeit.ui.CodeitView;
import org.jetbrains.annotations.NotNull;

/**
 * @author jumkey
 */
public class ExecAction extends AnAction {
    @Override
    public void update(@NotNull AnActionEvent e) {
        final Project project = e.getProject();
        final CodeitView codeitView = (CodeitView) e.getData(PlatformDataKeys.CONTEXT_COMPONENT);

        e.getPresentation().setEnabledAndVisible(codeitView != null && project != null);
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent anActionEvent) {
        final CodeitView codeitView = (CodeitView) anActionEvent.getRequiredData(PlatformDataKeys.CONTEXT_COMPONENT);
        codeitView.gen(anActionEvent.getProject());
    }
}
