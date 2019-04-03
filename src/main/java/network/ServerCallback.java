package network;

import entity.TaskListItem;

public interface ServerCallback {
    void receiveFile(TaskListItem item);
    void updateProgress(TaskListItem item);
    void receiveFinish(TaskListItem item);
}
