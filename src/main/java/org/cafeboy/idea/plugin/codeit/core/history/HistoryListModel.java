package org.cafeboy.idea.plugin.codeit.core.history;

import javax.swing.*;
import java.util.List;

/**
 * @author jumkey
 */
public class HistoryListModel extends AbstractListModel<String> {

    private final List<String> histories;

    public HistoryListModel(List<String> histories) {
        this.histories = histories;
    }

    @Override
    public int getSize() {
        return histories == null ? 0 : histories.size();
    }

    @Override
    public String getElementAt(int index) {
        return histories.get(index);
    }

    public void removeAll(List<String> selectedValuesList) {
        histories.removeAll(selectedValuesList);
    }
}
