package network;

import entity.TaskListItem;

public interface ClientCallback {
    void startSendFile(TaskListItem item);
    void currentProgress(TaskListItem item);
}
