package deviceRegistry;

import deviceRegistrySpec.deviceRegistrySpec;
import org.homi.plugin.api.*;
import org.homi.plugin.specification.ISpecification;
//import deviceRegistry.DeviceRegistryWorker;
import java.util.Hashtable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.Arrays;
import deviceRegistry.Task;
import deviceRegistry.DeviceRegistryWorker;
import java.lang.Thread;
import java.sql.SQLException;
/*
 * 
 * Start with key value 
 * command will be 'add'
 * 
 */

@PluginID(id = "DeviceRegistry")
public class DeviceRegistry extends AbstractPlugin{
	//map device name and device address
	
	//utilize singleton
	//private DeviceRegistyrInstance dri = deviceregistry.getinstance;
	//beta implementation using in memory store
	ConcurrentMap<String, String> devices = new ConcurrentHashMap<>(); //id, name
	ConcurrentMap<String, String> groups = new ConcurrentHashMap<>();
	ConcurrentMap<String, String> states = new ConcurrentHashMap<>();
	private RegistryDBConn instance = new RegistryDBConn();
	
	
	//public static volatile LinkedBlockingQueue<Task> queue = new LinkedBlockingQueue<Task>();
	
	@Override
	public void setup() {
		
		CommanderBuilder<deviceRegistrySpec> cb = new CommanderBuilder<>(deviceRegistrySpec.class) ;
		
		
		
		Commander<deviceRegistrySpec> c = cb.onCommandEquals(deviceRegistrySpec.CREATEDEVICE, this::createDevice).
		onCommandEquals(deviceRegistrySpec.GETDEVICES, this::getDevices).
		onCommandEquals(deviceRegistrySpec.DELETEDEVICES, this::deleteDevices).
		onCommandEquals(deviceRegistrySpec.WRITESTATE, this::writeState).
		onCommandEquals(deviceRegistrySpec.DELETESTATE, this::deleteState).
		onCommandEquals(deviceRegistrySpec.GETSTATE, this::getState).
		onCommandEquals(deviceRegistrySpec.CREATEGROUP, this::createGroup).
		onCommandEquals(deviceRegistrySpec.SETGROUP, this::setGroup).
		onCommandEquals(deviceRegistrySpec.DELETEGROUP, this::deleteGroup).
		onCommandEquals(deviceRegistrySpec.DELETEFROMGROUP, this::deleteFromGroup).
		onCommandEquals(deviceRegistrySpec.UPDATESTATE, this::updateState).
		build();
		addCommander(deviceRegistrySpec.class, c);
		
		
		/*
		addWorker(null/spec, runnable (lambda))
		
		*/
	}
	
	private String createDevice(Object ...objects) { //actual will be string name, string 
		
		return instance.createDevice((int) objects[1], objects[2].toString(), objects[3].toString());
	}
	
	private String[] getDevices(Object ...objects) {
		String[] devices;
		try {
			devices = instance.getDevices();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		return devices;
	}
	
	private String deleteDevices(Object ...objects) { //string[] deviceIDs
		String output;
		
		try {
			output = instance.deleteDevices((int[]) objects[1]);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			output = e.getSQLState();
		}
		return output;
	}
	
	private String writeState(Object ...objects) { // String id, String key, String value
		String output;
		try {
			output = instance.writeState((int) objects[1], objects[2].toString(), objects[3].toString());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			output = e.getSQLState();
		}
		return output;
	}
	
	private String updateState(Object ...objects) { // String id, String key, String value
		String output;
		try {
			output = instance.updateState((int) objects[1], objects[2].toString(), objects[3].toString());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			output = e.getSQLState();
		}
		return output;
	}
	
	private String deleteState(Object ...objects) { //String deviceID, String key
		String output;
		try {
			output = instance.deleteState((int) objects[1], objects[2].toString());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			output = e.getSQLState();
		}
		return output;
	}
	
	private String getState(Object ...objects) { //String deviceID, String key
		return null;
	}
	
	private String createGroup(Object ...objects) { //String deviceID, String key
		String output;
		try {
			output = instance.createGroup((int) objects[1], objects[2].toString());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			output = e.getSQLState();
		}
		return output;
	}
	
	private String setGroup(Object ...objects) { //String deviceID, String groupID
		String output;
		try {
			output = instance.setGroup((int) objects[1], (int) objects[2]);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			output = e.getSQLState();
		}
		return output;
	}
	
	private String deleteGroup(Object ...objects) { //String groupID
		String output;
		try {
			output = instance.deleteGroup((int) objects[1]);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			output = e.getSQLState();
		}
		return output;
	}
	
	private String deleteFromGroup(Object ...objects) { //String deviceID, groupID
		String output;
		try {
			output = instance.deleteFromGroup((int) objects[1], (int) objects[2]);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			output = e.getSQLState();
		}
		return output;
	}

}
