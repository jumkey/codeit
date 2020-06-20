package org.cafeboy.idea.plugin.codeit.ui;

import com.google.common.collect.Sets;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.ActionToolbar;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ex.ToolWindowEx;
import com.intellij.openapi.wm.ex.ToolWindowManagerListener;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import org.cafeboy.idea.plugin.codeit.actions.*;
import org.cafeboy.idea.plugin.codeit.ext.Constant;
import org.cafeboy.idea.plugin.codeit.ext.I18nSupport;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.util.Set;

/**
 * @author jumkey
 */
public class CodeitView {
    private static final Logger LOG = Logger.getInstance(CodeitView.class);

    private ToolWindow myToolWindow;
    private final Project myProject;
    public static final String TAB_SUGGESTED_NAME = I18nSupport.i18n_str("tab.name");

    public CodeitView(Project project) {
        this.myProject = project;
    }

    public static CodeitView getInstance(@NotNull Project project) {
        return project.getService(CodeitView.class);
    }

    public void init(ToolWindowEx toolWindow) {
        if (myToolWindow != null) {
            LOG.error("Codeit tool window already initialized");
            return;
        }
        myToolWindow = toolWindow;

        toolWindow.setTabActions(new AddTabAction(myProject));
        toolWindow.setToHideOnEmptyContent(true);
        myProject.getMessageBus().connect().subscribe(ToolWindowManagerListener.TOPIC, new ToolWindowManagerListener() {
            @Override
            public void toolWindowShown(@NotNull String id, @NotNull ToolWindow toolWindow) {
                if (CodeitToolWindowFactory.TOOL_WINDOW_ID.equals(id) && myToolWindow == toolWindow &&
                        toolWindow.isVisible() && toolWindow.getContentManager().getContentCount() == 0) {
                    // open a new session if all tabs were closed manually
                    add();
                }
            }
        });
    }

    public void add() {
        Content content = createContent();
        content.setCloseable(true);
        content.setDisplayName(generateUniqueName());
        myToolWindow.getContentManager().addContent(content);
        myToolWindow.getContentManager().setSelectedContent(content);
    }

    private String generateUniqueName() {
        Content[] contents = myToolWindow.getContentManager().getContents();
        Set<String> names = Sets.newHashSet();
        for (Content content : contents) {
            names.add(content.getDisplayName());
        }
        String newSdkName = CodeitView.TAB_SUGGESTED_NAME;
        int i = 0;
        while (names.contains(newSdkName)) {
            newSdkName = CodeitView.TAB_SUGGESTED_NAME + " (" + (++i) + ")";
        }
        return newSdkName;
    }

    private Content createContent() {
        ToolWindowPanel panel = new ToolWindowPanel();
        Content content = ContentFactory.SERVICE.getInstance().createContent(panel, "", false);
        CodeitContent codeitContent = createCodeitContent();
        ActionToolbar toolbar = createToolbar(codeitContent);
        panel.setToolbar(toolbar.getComponent());
        panel.setContent(codeitContent);
        return content;
    }

    private CodeitContent createCodeitContent() {
        return new CodeitContent(myProject);
    }

    private ActionToolbar createToolbar(CodeitContent codeitContent) {
        DefaultActionGroup group = new DefaultActionGroup();
        group.addAll(
                new ExecAction(codeitContent),
                new SaveAction(codeitContent),
                new EnableCliAction(codeitContent));
        ActionToolbar toolbar = ActionManager.getInstance().createActionToolbar(Constant.APP_ID, group, false);
        toolbar.setOrientation(SwingConstants.VERTICAL);
        return toolbar;
    }
}
