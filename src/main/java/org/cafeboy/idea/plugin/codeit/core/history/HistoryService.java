package org.cafeboy.idea.plugin.codeit.core.history;

import com.intellij.openapi.components.*;
import com.intellij.openapi.project.Project;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * @author jumkey
 */
@State(
        name = "Codeit.History",
        storages = {
                @Storage(StoragePathMacros.WORKSPACE_FILE)
        }
)
public class HistoryService implements PersistentStateComponent<HistoryService> {

    private List<String> histories;

    public static HistoryService getInstance(Project project) {
        return project.getService(HistoryService.class);
    }

    @Nullable
    @Override
    public HistoryService getState() {
        return this;
    }

    @Override
    public void loadState(@NotNull HistoryService historyService) {
        XmlSerializerUtil.copyBean(historyService, this);
    }

    public List<String> getHistories() {
        return histories;
    }

    public void setHistories(List<String> histories) {
        this.histories = histories;
    }
}
