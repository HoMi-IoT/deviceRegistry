package deviceRegistry;

import org.homi.plugin.api.IPlugin;
//import deviceRegistry.DeviceRegistryWorker;
import java.util.Hashtable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.Arrays;
import deviceRegistry.Task;
import deviceRegistry.DeviceRegistryWorker;
import java.lang.Thread;
/*
 * 
 * Start with key value 
 * command will be 'add'
 * 
 */

public class DeviceRegistry implements IPlugin {
	//map device name and device address
	
	
	@Override
	public Object execute(String arg0, Object... arg1) {
		// TODO Auto-generated method stub
		Task task = null;
		switch(arg0) {
			case "create":
				if (arg1[0].equals("device")) {
					task = new Task("create", "device", new String[]{(String) arg1[1], (String) arg1[2]});
				} else {
					task = new Task("create", "group", new String[]{(String) arg1[1], (String) arg1[2]});
				}
				break;
			case "read":
				if (arg1[0].equals("device")) {
					task = new Task("read", "device", new String[]{(String) arg1[1]});
				} else {
					task = new Task("read", "group", new String[]{(String) arg1[1]});
				}
				break;
			case "update":
				if (arg1[0].equals("device")) {
					
					task = new Task("update", "device", new String[]{(String) arg1[1]});
				} else {
					task = new Task("update", "group", new String[]{(String) arg1[1], (String) arg1[2]});
				}
				break;
			case "delete":
				if (arg1[0].equals("device")) {
					task = new Task("delete", "device", new String[]{(String) arg1[1]});
				} else if(arg1[0].equals("grouping")) {
					task = new Task("delete", "grouping", new String[]{(String) arg1[1], (String) arg1[2]});
				}
				break;
			default:
				System.out.println("no command found");
				break;
		}
		if (task != null) {
			try {
				DeviceRegistryWorker.queue.put(task);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			while (true) { //this part will hang as long as the task is incomplete, so is it possible to make execute asynchronous as well?
				if (task != null && task.isComplete()) {
					return task.getReturn();
				}
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}	
		}
		return null;

	}
}
