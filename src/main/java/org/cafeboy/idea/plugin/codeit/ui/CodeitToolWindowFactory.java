package org.cafeboy.idea.plugin.codeit.ui;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.openapi.wm.ex.ToolWindowEx;
import org.jetbrains.annotations.NotNull;

/**
 * @author jumkey
 */
public class CodeitToolWindowFactory implements ToolWindowFactory {
    public static final String TOOL_WINDOW_ID = "Codeit";

    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        CodeitManager mainComponent = CodeitManager.getInstance(project);
        mainComponent.init((ToolWindowEx) toolWindow);
    }
}
