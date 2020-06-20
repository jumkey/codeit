package org.cafeboy.idea.plugin.codeit.ui;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.project.Project;
import org.cafeboy.idea.plugin.codeit.core.history.HistoryConfigurable;
import org.cafeboy.idea.plugin.codeit.core.history.HistoryListModel;
import org.cafeboy.idea.plugin.codeit.ext.I18nSupport;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * @author jumkey
 */
public class HistoryWidget {

    private final CodeitContent codeitContent;
    public JPanel historyPanel;
    private JList<String> jList;
    private List<String> histories = new ArrayList<>();
    private HistoryConfigurable historyConfig;

    public HistoryWidget(Project project, CodeitContent codeitContent) {
        this.codeitContent = codeitContent;
        EventQueue.invokeLater(() -> {
            HistoryConfigurable historyConfigurable = new HistoryConfigurable(project);
            setHistory(historyConfigurable.getHistories());
            historyConfig = new HistoryConfigurable(project);
            setupList();
            setRightMenu();
        });
    }

    private void setRightMenu() {
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
                    createPopupMenu().show(e.getComponent(), e.getX(), e.getY());
                }
            }
        });
    }

    private JPopupMenu createPopupMenu() {
        JPopupMenu jPopupMenu = new JPopupMenu();
        // show
        JMenuItem showLine = new JMenuItem(I18nSupport.i18n_str("menu.show.text"), AllIcons.Actions.ShowWriteAccess);
        showLine.setMnemonic('S');
        showLine.setAccelerator(KeyStroke.getKeyStroke('R', KeyEvent.CTRL_MASK, false));
        // delete
        JMenuItem deleteLine = new JMenuItem(I18nSupport.i18n_str("menu.delete.text"), AllIcons.Actions.Cancel);
        deleteLine.setMnemonic('D');
        deleteLine.setAccelerator(KeyStroke.getKeyStroke('D', KeyEvent.CTRL_MASK, false));
        // listener
        showLine.addActionListener(e -> changeText(jList.getSelectedValue()));
        deleteLine.addActionListener(e -> {
            List<String> selectedValuesList = jList.getSelectedValuesList();
            histories.removeAll(selectedValuesList);
        });
        // add
        jPopupMenu.add(showLine);
        jPopupMenu.addSeparator();
        jPopupMenu.add(deleteLine);

        if (histories == null || histories.size() == 0) {
            showLine.setEnabled(false);
            deleteLine.setEnabled(false);
        }
        return jPopupMenu;
    }

    private void setupList() {
        jList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (jList.getSelectedIndex() != -1) {
                    if (e.getClickCount() == 2) {
                        changeText(jList.getSelectedValue());
                    }
                }
            }
        });
    }

    private void changeText(String selectedValue) {
        ContentWidget contentWidget = codeitContent.getContentWidget();
        contentWidget.setText(selectedValue);
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
