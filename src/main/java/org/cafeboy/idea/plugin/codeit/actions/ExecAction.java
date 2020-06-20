package org.cafeboy.idea.plugin.codeit.actions;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import org.cafeboy.idea.plugin.codeit.callback.OnExecuteListener;
import org.cafeboy.idea.plugin.codeit.core.tasks.CodeGenerateTask;
import org.cafeboy.idea.plugin.codeit.ext.I18nSupport;
import org.cafeboy.idea.plugin.codeit.ui.ContentWidget;
import org.cafeboy.idea.plugin.codeit.ui.CodeitContent;
import org.apache.http.util.TextUtils;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

/**
 * @author jumkey
 */
public class ExecAction extends AnAction {

    private final CodeitContent codeitContent;

    public ExecAction(CodeitContent codeitContent) {
        super(I18nSupport.i18n_str("action.execute.text"), I18nSupport.i18n_str("action.execute.description"), AllIcons.Actions.Execute);
        this.codeitContent = codeitContent;
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent anActionEvent) {
        ContentWidget contentWidget = codeitContent.getContentWidget();
        String text = contentWidget.getText();
        if (TextUtils.isEmpty(text)) {
            contentWidget.setInfo(I18nSupport.i18n_str("info.content.is.empty"));
            return;
        } else if (text.length() > 190) {
            contentWidget.setInfo(I18nSupport.i18n_str("info.content.to.large"));
            return;
        }
        text = text.replaceAll("[\r\n]", "");
        final String finalText = text;
        final Project project = anActionEvent.getProject();
        new CodeGenerateTask(project, text, contentWidget.getMode(), contentWidget.isCli(), new OnExecuteListener() {
            @Override
            public void onStart() {
                contentWidget.setInfo(I18nSupport.i18n_str("info.starting"));
            }

            @Override
            public void onSuccess(Icon icon) {
                contentWidget.setCode(icon);
                contentWidget.setInfo(I18nSupport.i18n_str("info.success"));
            }

            @Override
            public void onError(String msg) {
                contentWidget.setInfo(I18nSupport.i18n_str("info.error"));
            }

            @Override
            public void onComplete() {
                if (!codeitContent.getHistoryWidget().hasSame(finalText)) {
                    codeitContent.getHistoryWidget().insert(finalText);
                }
            }
        }).start();
    }
}