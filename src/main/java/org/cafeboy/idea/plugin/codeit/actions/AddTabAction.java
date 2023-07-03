package org.cafeboy.idea.plugin.codeit.actions;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import org.cafeboy.idea.plugin.codeit.ext.I18nSupport;
import org.cafeboy.idea.plugin.codeit.ui.CodeitManager;
import org.jetbrains.annotations.NotNull;

/**
 * @author jumkey
 */
public class AddTabAction extends AnAction {

    private final Project myProject;

    public AddTabAction(Project project) {
        super(I18nSupport.i18n_str("action.add.tab.text"), I18nSupport.i18n_str("action.add.tab.description"), AllIcons.General.Add);
        this.myProject = project;
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        CodeitManager.getInstance(myProject).add();
    }
}
