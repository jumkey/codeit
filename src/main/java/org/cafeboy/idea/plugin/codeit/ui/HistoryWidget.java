package org.cafeboy.idea.plugin.codeit.ui;

import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.project.Project;
import org.cafeboy.idea.plugin.codeit.core.history.HistoryConfigurable;
import org.cafeboy.idea.plugin.codeit.core.history.HistoryListModel;
import org.cafeboy.idea.plugin.codeit.ext.Constant;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * @author jumkey
 */
public class HistoryWidget {

    private final Project mProject;
    private final CodeitView codeitView;
    public JPanel historyPanel;
    private JList<String> jList;
    private List<String> histories = new ArrayList<>();
    private HistoryConfigurable historyConfig;

    public HistoryWidget(Project project, CodeitView codeitView) {
        this.mProject = project;
        this.codeitView = codeitView;
        EventQueue.invokeLater(() -> {
            historyConfig = new HistoryConfigurable(project);
            setHistory(historyConfig.getHistories());
            setupList();
            setRightMenu(createPopupMenu());
        });
    }

    private void setRightMenu(JPopupMenu jPopupMenu) {
        jList.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON3 && (jList.getSelectedIndex() == -1 || jList.getSelectedIndices().length == 1)) {
                    // 获取鼠标点击的项
                    jList.setSelectedIndex(jList.locationToIndex(e.getPoint()));
                }
                handleMouse(e);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                handleMouse(e);
            }

            private void handleMouse(MouseEvent e) {
                if (histories != null && histories.size() > 0
                        && jList.getSelectedIndex() == -1) {
                    jList.setSelectedIndex(0);
                }
                if (e.isPopupTrigger()) {
                    jPopupMenu.show(e.getComponent(), e.getX(), e.getY());
                }
            }
        });
    }

    private JPopupMenu createPopupMenu() {
        AnAction[] children = this.codeitView.createHistoryActions();
        for (AnAction child : children) {
            child.registerCustomShortcutSet(jList, null);
        }
        ActionGroup action = new DefaultActionGroup(children);
        ActionPopupMenu actionPopupMenu = ActionManager.getInstance().createActionPopupMenu(Constant.APP_ID, action);
        return actionPopupMenu.getComponent();
    }

    private void setupList() {
        jList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (jList.getSelectedIndex() != -1) {
                    if (e.getClickCount() == 2) {
                        changeText();
                    }
                }
            }
        });
    }

    public void changeText() {
        String selectedValue = jList.getSelectedValue();
        ContentWidget contentWidget = codeitView.getContentWidget();
        contentWidget.setText(selectedValue);
        codeitView.gen(mProject);
    }

    public int size() {
        HistoryListModel model = (HistoryListModel) jList.getModel();
        return model.getSize();
    }

    public void removeAll() {
        java.util.List<String> selectedValuesList = jList.getSelectedValuesList();
        HistoryListModel model = (HistoryListModel) jList.getModel();
        model.removeAll(selectedValuesList);
        jList.repaint();
    }

    public void insert(String history) {
        histories.add(0, history);
        try {
            historyConfig.setHistories(histories);
            historyConfig.apply();
            notifyDataChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean hasSame(String text) {
        return histories.contains(text);
    }

    private void notifyDataChanged() {
        setHistory(histories);
    }

    private void setHistory(List<String> histories) {
        if (histories == null) {
            return;
        }
        this.histories = histories;
        jList.setModel(new HistoryListModel(this.histories));
    }
}
