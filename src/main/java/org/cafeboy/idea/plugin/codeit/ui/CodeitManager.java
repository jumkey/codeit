package org.cafeboy.idea.plugin.codeit.ui;

import com.google.common.collect.Sets;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ex.ToolWindowEx;
import com.intellij.openapi.wm.ex.ToolWindowManagerListener;
import com.intellij.ui.content.Content;
import org.cafeboy.idea.plugin.codeit.actions.AddTabAction;
import org.cafeboy.idea.plugin.codeit.ext.I18nSupport;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class CodeitManager {
    private static final Logger LOG = Logger.getInstance(CodeitManager.class);

    private ToolWindow myToolWindow;
    private final Project myProject;
    public static final String TAB_SUGGESTED_NAME = I18nSupport.i18n_str("tab.name");

    public CodeitManager(Project myProject) {
        this.myProject = myProject;
    }

    public static CodeitManager getInstance(@NotNull Project project) {
        return project.getService(CodeitManager.class);
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
        Content content = new CodeitContent(myProject);
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
        String newSdkName = CodeitManager.TAB_SUGGESTED_NAME;
        int i = 0;
        while (names.contains(newSdkName)) {
            newSdkName = CodeitManager.TAB_SUGGESTED_NAME + " (" + (++i) + ")";
        }
        return newSdkName;
    }
}
