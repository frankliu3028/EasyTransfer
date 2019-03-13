package entity;

public class TaskListItem {
	
	public static final int TYPE_SEND = 0;
	public static final int TYPE_RECEIVE = 1;
	
	private int id;
	private int type;
	private String peerIp;
	private String path;
	private int progress;
	
	public TaskListItem() {
		
	}
	

	public TaskListItem(int type, String peerIp, String path, int progress) {
		this.type = type;
		this.peerIp = peerIp;
		this.path = path;
		this.progress = progress;
	}



	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getPeerIp() {
		return peerIp;
	}

	public void setPeerIp(String peerIp) {
		this.peerIp = peerIp;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public int getProgress() {
		return progress;
	}

	public void setProgress(int progress) {
		this.progress = progress;
	}
	
	public String getTypeString() {
		String res = null;
		switch(getType()) {
		case TYPE_SEND:
			res = "发送";
			break;
		case TYPE_RECEIVE:
			res = "接收";
			break;
			default:
				break;
		}
		return res;
	}
	
	public String getProgressString() {
		return progress + "%";
	}
	
	
}
