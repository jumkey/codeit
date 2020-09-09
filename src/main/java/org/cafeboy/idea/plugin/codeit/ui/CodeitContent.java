package org.cafeboy.idea.plugin.codeit.ui;

import com.intellij.openapi.actionSystem.ActionGroup;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.ActionToolbar;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.SimpleToolWindowPanel;
import com.intellij.ui.content.impl.ContentImpl;
import org.cafeboy.idea.plugin.codeit.ext.Constant;
import org.jetbrains.annotations.NotNull;

/**
 * 内容管理(tab/content)
 *
 * @author jumkey
 */
public class CodeitContent extends ContentImpl {
    private final @NotNull CodeitView codeitView;

    public CodeitContent(@NotNull Project project) {
        super(null, "", false);

        codeitView = new CodeitView(project);
        SimpleToolWindowPanel panel = new SimpleToolWindowPanel(false, true);
        panel.setToolbar(createToolbar().getComponent());
        panel.setContent(codeitView);
        setComponent(panel);
    }

    public @NotNull CodeitView getCodeitView() {
        return codeitView;
    }

    private ActionToolbar createToolbar() {
        ActionGroup group = (ActionGroup) ActionManager.getInstance().getAction("my.toolbar");
        ActionToolbar actionToolbar = ActionManager.getInstance().createActionToolbar(Constant.APP_ID, group, false);
        actionToolbar.setTargetComponent(codeitView);
        return actionToolbar;
    }
}
