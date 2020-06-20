package org.cafeboy.idea.plugin.codeit.ui;

import com.intellij.openapi.project.Project;

import javax.swing.*;
import java.awt.*;

/**
 * 内容管理(tab/content)
 *
 * @author jumkey
 */
public class CodeitContent extends JPanel {

    /**
     * main first add
     */
    private final ContentWidget contentWidget;
    private final HistoryWidget historyWidget;

    public CodeitContent(Project mProject) {
        super(new BorderLayout());
        historyWidget = new HistoryWidget(mProject, this);
        this.add(historyWidget.historyPanel, BorderLayout.EAST);
        contentWidget = new ContentWidget(mProject);
        this.add(contentWidget.mContent, BorderLayout.CENTER);
    }

    public ContentWidget getContentWidget() {
        return contentWidget;
    }

    public HistoryWidget getHistoryWidget() {
        return historyWidget;
    }
}