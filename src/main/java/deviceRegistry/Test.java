package deviceRegistry;

import deviceRegistry.Task;
import deviceRegistry.DeviceRegistryWorker;
import deviceRegistry.DeviceRegistry;
import deviceRegistry.DeviceRegistryFactory;



public class Test {

	public static void main(String[] args) throws InterruptedException {
		DeviceRegistryFactory d = new DeviceRegistryFactory();
		new Thread(new DeviceRegistryWorker()).start();
		new Thread(()-> {while (true) {d.getPlugin(null).execute("create", "device", "hello", "world");try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}}}).start();
		new Thread(()-> {while (true) {d.getPlugin(null).execute("random", "device", "hello", "world");try {
			Thread.sleep(300);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}}}).start();
		new Thread(()-> {while (true) {d.getPlugin(null).execute("update", "device", "hello", "world");try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}}}).start();
		while (true) {
			Thread.sleep(100);
		}
	}

}
