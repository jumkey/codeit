package org.cafeboy.idea.plugin.codeit.core.history;

import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author jumkey
 */
public class HistoryConfigurable implements Configurable {

    private final HistoryService historyService;
    private List<String> histories = new ArrayList<>();

    public HistoryConfigurable(Project project) {
        historyService = HistoryService.getInstance(project);
    }

    @Nls
    @Override
    public String getDisplayName() {
        return null;
    }

    @Nullable
    @Override
    public JComponent createComponent() {
        return null;
    }

    @Override
    public boolean isModified() {
        return false;
    }

    /**
     * 1.set the histories object want to save;
     *
     * @param histories histories
     */
    public void setHistories(List<String> histories) {
        this.histories = histories;
    }

    /**
     * 2.apply;
     *
     * @throws ConfigurationException e
     */
    @Override
    public void apply() throws ConfigurationException {
        historyService.setHistories(histories);
    }

    /**
     * get histories that it saved.
     *
     * @return ArrayList<History>
     */
    public List<String> getHistories() {
        return historyService.getHistories();
    }
}
