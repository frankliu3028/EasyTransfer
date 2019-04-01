package network;

import utils.Config;
import utils.Log;
import utils.LogLevel;
import utils.Util;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class FileReceiver implements Runnable{

    private final String TAG = FileReceiver.class.getSimpleName();

    private String fileName;
    private long fileSize;
    private FileReceiverCallback callback;
    private ServerSocket socket;
    private File fileWillBeSaved;

    private final int BLOCK_SIZE = 1024;

    public FileReceiver(String fileName, long fileSize, FileReceiverCallback callback){
        this.fileName = fileName;
        this.fileSize = fileSize;
        this.callback = callback;
    }

    @Override
    public void run() {
        Socket worker = null;
        try{
            int listenPort = Util.getAValidPort();
            socket = new ServerSocket(listenPort);
            callback.ready(listenPort);
            worker = socket.accept();
            receiveFileBySocket(new File(Config.fileSaveDir), worker.getInputStream());
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            try{
                if(socket != null){
                    socket.close();
                }
                if(worker != null){
                    worker.close();
                }
            }catch (Exception e){
                e.printStackTrace();
            }

        }


    }

    private void receiveFileBySocket(File fileSavePath, InputStream inputStream)
    {
        if(!fileSavePath.isDirectory())
        {
            Log.log(TAG, LogLevel.ERROR, "fileSavePath must be a directory!");
            return;
        }
        byte[] fileNameLenByte = new byte[4];
        try {
            int readTemp = inputStream.read(fileNameLenByte);
            if(readTemp != 4)
            {
                Log.log(TAG, LogLevel.ERROR, "read fileNameLenByte error!readTemp is :"+readTemp);
                return;
            }
            int fileNameLen = Util.byteArrayToInt(fileNameLenByte);
            byte[] fileNameByte = new byte[fileNameLen];
            readTemp = inputStream.read(fileNameByte, 0, fileNameLen);
            if(readTemp != fileNameLen)
            {
                Log.log(TAG, LogLevel.ERROR, "read fileNameByte error!");
                return;
            }
            String fileName = new String(fileNameByte, 0, fileNameByte.length);
            Log.log(TAG, LogLevel.INFO, "fileName is :" + fileName);
            fileWillBeSaved = new File(fileSavePath.getPath()+"/"+fileName);
            Log.log(TAG, LogLevel.INFO, "file saved to:"+fileWillBeSaved.getAbsolutePath());
            byte[] fileLenByte = new byte[8];
            readTemp = inputStream.read(fileLenByte);
            if(readTemp != 8)
            {
                Log.log(TAG, LogLevel.ERROR, "read fileLenByte error!");
                return;
            }
            long fileLen = Util.byteArray2Long(fileLenByte);
            Log.log(TAG, LogLevel.INFO, "file length is :" + fileLen);

            readISToFile(inputStream, fileWillBeSaved);
            Log.log(TAG, LogLevel.INFO, "file receive finished!");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void readISToFile(InputStream is, File file) throws IOException
    {
        DataInputStream dis = new DataInputStream(is);
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
        byte[] buffer = new byte[BLOCK_SIZE];
        int once_read_len = 0;
        long totalSizeHaveRead = 0;
        while((once_read_len = dis.read(buffer)) != -1)
        {
            bos.write(buffer, 0, once_read_len);
            totalSizeHaveRead += once_read_len;
            callback.currentProgress((int)(totalSizeHaveRead*100/fileSize));
        }
        bos.flush();
        bos.close();
        Log.log(TAG, LogLevel.INFO, "total read:"+totalSizeHaveRead);
    }

    public String getFileAbPath(){
        if(fileWillBeSaved != null){
            return fileWillBeSaved.getAbsolutePath();
        }
        return null;
    }
}
