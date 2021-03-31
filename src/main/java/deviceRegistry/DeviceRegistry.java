package deviceRegistry;

import org.homi.plugin.api.IPlugin;
import java.util.Hashtable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.Arrays;

/*
 * 
 * Start with key value 
 * command will be 'add'
 * 
 */

public class DeviceRegistry implements IPlugin {
	//map device name and device address
	ConcurrentMap<String, String> devices = new ConcurrentHashMap<>();
	
	@Override
	public Object execute(String arg0, Object... arg1) {
		// TODO Auto-generated method stub
		if (arg0.equals("add")) {
			devices.put(String.valueOf(arg1[0]), String.valueOf(arg1[0]));
			System.out.println("Device " + arg1[0] + " added!\n");
		} else if (arg0.equals("remove")) {
			devices.remove(arg1[0]);
			System.out.println("Device " + arg1[0] + " removed!\n");
		} else if (arg0.contentEquals("show")) {
			Object[] keys = devices.keySet().toArray();
			Arrays.sort(keys);
			for (Object k : keys) {
				System.out.println(k);
			}
		} else {
			System.out.println("No such command exists.");
		}
		return null;
	}
}
