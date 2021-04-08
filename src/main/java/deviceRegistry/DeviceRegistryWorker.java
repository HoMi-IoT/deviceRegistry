package deviceRegistry;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.lang.Thread;

import org.homi.plugin.api.IPlugin;

public class DeviceRegistryWorker implements Runnable{
//	Object[] keys = devices.keySet().toArray();
//	Arrays.sort(keys);
//	for (Object k : keys) {
//		System.out.println(k);
//	}
	ConcurrentMap<String, String> devices = new ConcurrentHashMap<>();
	public static volatile LinkedBlockingQueue<Task> queue = new LinkedBlockingQueue<Task>();
	//need static queue
	


	@Override
	public void run() {
		// TODO Auto-generated method stub
		while (true) {
			Task task = queue.poll();
			if (task != null) {
				System.out.println(task.getCommand());
				task.setReturn("done!");
				task.complete();
			}
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
		
	public static void main(String[] args) {
		// TODO Auto-generated method stub
	
	}

}
