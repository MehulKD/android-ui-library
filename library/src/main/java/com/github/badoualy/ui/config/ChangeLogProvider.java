package com.github.badoualy.ui.config;

import com.github.badoualy.ui.model.ChangeLog;

import java.util.List;

public interface ChangeLogProvider {
    /** @return All changelogs, expected to be in descending order, first item is the most recent version changelog */
    List<ChangeLog> getChangeLogs();
}
