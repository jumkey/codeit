package org.cafeboy.idea.plugin.codeit.ui;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.project.Project;
import org.apache.http.util.TextUtils;
import org.cafeboy.idea.plugin.codeit.actions.CopyHistoryAction;
import org.cafeboy.idea.plugin.codeit.actions.DeleteAction;
import org.cafeboy.idea.plugin.codeit.actions.ShowAction;
import org.cafeboy.idea.plugin.codeit.callback.OnExecuteListener;
import org.cafeboy.idea.plugin.codeit.core.tasks.CodeGenerateTask;
import org.cafeboy.idea.plugin.codeit.ext.I18nSupport;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author jumkey
 */
public class CodeitView extends JPanel {

    /**
     * main first add
     */
    private final ContentWidget contentWidget;
    private final HistoryWidget historyWidget;

    public CodeitView(Project project) {
        super(new BorderLayout());
        historyWidget = new HistoryWidget(project, this);
        this.add(historyWidget.historyPanel, BorderLayout.EAST);
        contentWidget = new ContentWidget(project, this);
        this.add(contentWidget.mContent, BorderLayout.CENTER);
    }

    public ContentWidget getContentWidget() {
        return contentWidget;
    }

    public HistoryWidget getHistoryWidget() {
        return historyWidget;
    }

    public AnAction @NotNull [] createHistoryActions() {
        List<AnAction> historyActions = new ArrayList<>();
        historyActions.add(new ShowAction(this));
        historyActions.add(new CopyHistoryAction(this));
        historyActions.add(new DeleteAction(this));
        return historyActions.toArray(AnAction.EMPTY_ARRAY);
    }

    public void gen(Project project) {
        String text = contentWidget.getText();
        if (TextUtils.isEmpty(text)) {
            contentWidget.setInfo(I18nSupport.i18n_str("info.content.is.empty"));
            return;
        } else if (text.length() > 190) {
            contentWidget.setInfo(I18nSupport.i18n_str("info.content.to.large"));
            return;
        }
        gen(project, text);
    }

    public void regen(Project project) {
        final String text = contentWidget.getCodeText();
        if (TextUtils.isEmpty(text)) {
            return;
        }
        gen(project, text);
    }

    private void gen(Project project, String finalText) {
        new CodeGenerateTask(project, finalText, contentWidget.getMode(), contentWidget.isCli(), new OnExecuteListener<>() {
            @Override
            public void onStart() {
                contentWidget.setInfo(I18nSupport.i18n_str("info.starting"));
            }

            @Override
            public void onSuccess(Icon icon) {
                contentWidget.setCode(icon);
                contentWidget.setCodeText(finalText);
                contentWidget.setInfo(I18nSupport.i18n_str("info.success"));
            }

            @Override
            public void onError(String msg) {
                contentWidget.setInfo(I18nSupport.i18n_str("info.error"));
            }

            @Override
            public void onComplete() {
                CodeitView.this.getHistoryWidget().insert(finalText);
            }
        }).start();
    }
}
