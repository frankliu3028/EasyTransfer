package entity;

import utils.Log;
import utils.LogLevel;

public class TaskIdPool{

    final String TAG = TaskIdPool.class.getSimpleName();
    int capability = 10;
    int[] id;
    private static TaskIdPool instance;

    private TaskIdPool(){
        id = new int[capability];
    }

    public static TaskIdPool getInstance(){
        if(instance == null){
            instance = new TaskIdPool();
        }
        return instance;
    }

    public int allocate(){
        for(int i = 0; i < capability; i++){
            if(id[i] == 0){
                id[i] = 1;
                return i;
            }
        }
        Log.log(TAG, LogLevel.ERROR, "no valid task id");
        return -1;
    }

    public void release(int i){
        if(i < 0 || i >capability -1){
            Log.log(TAG, LogLevel.ERROR, "task id beyond 0~" + capability);
            return;
        }
        id[i] = 0;
    }
}
