package org.cafeboy.idea.plugin.codeit.actions;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.ToggleAction;
import org.cafeboy.idea.plugin.codeit.ext.I18nSupport;
import org.cafeboy.idea.plugin.codeit.ui.CodeitContent;
import org.jetbrains.annotations.NotNull;

/**
 * 启用草料二维码生成
 */
public class EnableCliAction extends ToggleAction {

    private final CodeitContent codeitContent;

    private boolean isCli = false;

    public EnableCliAction(CodeitContent codeitContent) {
        super(I18nSupport.i18n_str("action.using.cli.im.text"), I18nSupport.i18n_str("action.using.cli.im.description"), AllIcons.Actions.SetDefault);
        this.codeitContent = codeitContent;
    }

    @Override
    public boolean isSelected(@NotNull AnActionEvent event) {
        return isCli;
    }

    @Override
    public void setSelected(@NotNull AnActionEvent event, boolean state) {
        codeitContent.getContentWidget().toggleGroupData(state);
        isCli = state;
    }
}
