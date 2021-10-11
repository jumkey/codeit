package org.cafeboy.idea.plugin.codeit.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowManager;
import com.intellij.ui.content.Content;
import org.cafeboy.idea.plugin.codeit.ui.CodeitContent;
import org.cafeboy.idea.plugin.codeit.ui.CodeitToolWindowFactory;
import org.jetbrains.annotations.NotNull;

/**
 * @author jumkey
 */
public class GenQRCodeAction extends AnAction {

    @Override
    public void update(@NotNull final AnActionEvent e) {
        final Project project = e.getProject();
        final Editor editor = e.getData(CommonDataKeys.EDITOR);

        // Make sure at least one caret is available
        boolean menuAllowed = false;
        boolean menuEnabled = false;
        if (editor != null && project != null) {
            // Ensure the list of carets in the editor is not empty
            menuAllowed = !editor.getCaretModel().getAllCarets().isEmpty();
            menuEnabled = editor.getSelectionModel().hasSelection();
        }
        e.getPresentation().setVisible(menuAllowed);
        e.getPresentation().setEnabled(menuEnabled);
    }

    @Override
    public void actionPerformed(AnActionEvent e) {
        final Project project = e.getRequiredData(CommonDataKeys.PROJECT);
        final Editor editor = e.getRequiredData(CommonDataKeys.EDITOR);
        final String selectedText = editor.getSelectionModel().getSelectedText();
        // open toolWindow
        final ToolWindow toolWindow = ToolWindowManager.getInstance(project).getToolWindow(CodeitToolWindowFactory.TOOL_WINDOW_ID);
        assert toolWindow != null;
        toolWindow.show(() -> {
            // changeText
            // genQRCode
            for (Content selectedContent : toolWindow.getContentManager().getSelectedContents()) {
                ((CodeitContent) selectedContent).getCodeitView().getContentWidget().setText(selectedText);
                ((CodeitContent) selectedContent).getCodeitView().gen(project);
            }
        });
    }
}
