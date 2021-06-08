package deviceRegistry;

import deviceRegistrySpec.DeviceRegistrySpec;
import org.homi.plugin.api.*;
import org.homi.plugin.api.basicplugin.AbstractBasicPlugin;
import org.homi.plugin.api.commander.Commander;
import org.homi.plugin.api.commander.CommanderBuilder;

import java.io.Serializable;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.homi.plugin.api.exceptions.InternalPluginException;
import org.homi.plugins.ar.specification.actions.Action;
import org.homi.plugins.ar.specification.actions.ActionQuery;

import deviceRegistrySpec.Device;
import org.homi.plugins.dbs.nosqlspec.query.QueryBuilder;
import org.homi.plugins.dbs.nosqlspec.record.FieldList;
import org.homi.plugins.dbs.nosqlspec.record.Record;
import org.homi.plugins.dbs.nosqlspec.record.Value;
/*
 * 
 * Start with key value 
 * command will be 'add'
 * 
 */

@PluginID(id = "DeviceRegistry")
public class DeviceRegistry extends AbstractBasicPlugin {
	//map device name and device address
	
	//utilize singleton
	//private DeviceRegistyrInstance dri = deviceregistry.getinstance;
	//beta implementation using in memory store
	ConcurrentMap<String, Device> devices = new ConcurrentHashMap<>(); //id, name
	ConcurrentMap<String, String> groups = new ConcurrentHashMap<>();
	ConcurrentMap<String, String> states = new ConcurrentHashMap<>();
	//private RegistryDBConn instance = new RegistryDBConn();


	
	
	//public static volatile LinkedBlockingQueue<Task> queue = new LinkedBlockingQueue<Task>();
	
	@Override
	public void setup() {
		
		CommanderBuilder<DeviceRegistrySpec> cb = new CommanderBuilder<>(DeviceRegistrySpec.class) ;
		Commander<DeviceRegistrySpec> c = cb.onCommandEquals(DeviceRegistrySpec.CREATEDEVICE, this::createDevice).
		onCommandEquals(DeviceRegistrySpec.GETDEVICE, this::getDevice).
		onCommandEquals(DeviceRegistrySpec.GETDEVICES, this::getDevices).
		onCommandEquals(DeviceRegistrySpec.DELETEDEVICE, this::deleteDevice).
		onCommandEquals(DeviceRegistrySpec.SETATTRIBUTE, this::setAttribute).
		onCommandEquals(DeviceRegistrySpec.DELETEATTRIBUTE, this::deleteAttribute).
		onCommandEquals(DeviceRegistrySpec.ADDTOGROUP, this::addToGroup).
		onCommandEquals(DeviceRegistrySpec.DELETEGROUP, this::deleteGroup).
		onCommandEquals(DeviceRegistrySpec.DELETEFROMGROUP, this::deleteFromGroup).
		build();
		addCommander(DeviceRegistrySpec.class, c);

		Action.setPluginProvider(this.getPluginProvider());
		
		/*
		addWorker(null/spec, runnable (lambda))
		
		*/
	}

	@Override
	public void teardown() {
		System.out.println("hola");
	}

	private Boolean createDevice(Object ...objects) throws InternalPluginException { //actual will be string name, string

		Device newDevice = (Device) objects[0];
		/**
		 * _id:
		 * name:
		 * addresses (record):
		 * attributes (record):
		 * groups (list)
		 */
		Record newRecord = new Record();
		newRecord.addField("name", new Value<>(newDevice.getName()));
		newRecord.addField("addresses", Record.of(newDevice.getAddresses()));
		newRecord.addField("attributes", Record.of(newDevice.getAttributes()));
		newRecord.addField("groups", new Value<>((Serializable) newDevice.getGroups()));

		ActionQuery aq = new ActionQuery();
		aq.type(ActionQuery.TYPE.SPECIFICATION).pluginID("NoSQLPlugin").command("STORE");
		try {
			Action<Integer> a1 = Action.getAction(aq);
			a1.set("0", "DeviceRegistry");
			a1.set("1", newRecord);
			int res = a1.run();
			return (res == 0 ? false : true);
		} catch (Exception e) {
			throw new InternalPluginException(e);
		}
	}

	private Device getDevice(Object... objects) throws InternalPluginException {
		ActionQuery aq = new ActionQuery();
		aq.type(ActionQuery.TYPE.SPECIFICATION).pluginID("NoSQLPlugin").command("QUERY");

		try {
			Action<FieldList> a1 = Action.getAction(aq);

			a1.set("0", "DeviceRegistry");
			a1.set("1", QueryBuilder.eq("name", (String)objects[0]));
			List<Device> d = (List<Device>)a1.run().accept(new DeviceRegistryStorageVisitor());
			if (d.size() > 0) {
				return d.get(0);
			} else {
				return null;
			}
		} catch (Exception e) {
			throw new InternalPluginException(e);
		}
	}

	private Device[] getDevices(Object ...objects) throws InternalPluginException {
		ActionQuery aq = new ActionQuery();
		aq.type(ActionQuery.TYPE.SPECIFICATION).pluginID("NoSQLPlugin").command("QUERY");

		try {
			Action<FieldList> a1 = Action.getAction(aq);
			a1.set("0", "DeviceRegistry");
			a1.set("1", null);
			List<Device> d = (List<Device>)a1.run().accept(new DeviceRegistryStorageVisitor());
			return d.toArray(new Device[d.size()]);
		} catch (Exception e) {
			throw new InternalPluginException(e);
		}
	}

	private Boolean deleteDevice(Object ...objects) throws InternalPluginException { //string[] deviceIDs
		ActionQuery aq = new ActionQuery();
		aq.type(ActionQuery.TYPE.SPECIFICATION).pluginID("NoSQLPlugin").command("DELETE");

		try {
			Action<Integer> a1 = Action.getAction(aq);

			a1.set("0", "DeviceRegistry");
			a1.set("1", QueryBuilder.eq("name", (String) objects[0]));
			int res = a1.run();
			return res != 0;
		} catch (Exception e) {
			throw new InternalPluginException(e);
		}
	}

	
	private Boolean setAttribute(Object ...objects) throws InternalPluginException { // String id, String key, String value

		ActionQuery aq = new ActionQuery();
		aq.type(ActionQuery.TYPE.SPECIFICATION).pluginID("NoSQLPlugin").command("UPDATE");

		Device d = this.getDevice(objects[0]);
		d.setAttribute((String) objects[1], (Serializable) objects[2]);

		return updateHelper(aq, d);
	}

	private boolean updateHelper(ActionQuery aq, Device d) throws InternalPluginException {
		try {
			Action<Integer> a1 = Action.getAction(aq);
			a1.set("0", "DeviceRegistry");
			a1.set("1", QueryBuilder.eq("name", d.getName()));
			Record newRecord = new Record();
			newRecord.addField("name", new Value<String>(d.getName()));
			newRecord.addField("addresses", Record.of(d.getAddresses()));
			newRecord.addField("attributes", Record.of(d.getAttributes()));
			newRecord.addField("groups", new Value<>((Serializable)d.getGroups()));
			a1.set("2", newRecord);
			int res = a1.run();
			return res != 0;
		} catch (Exception e) {
			throw new InternalPluginException(e);
		}
	}

	private boolean deleteAttribute(Object ...objects) throws InternalPluginException { //String deviceID, String key

		ActionQuery aq = new ActionQuery();
		aq.type(ActionQuery.TYPE.SPECIFICATION).pluginID("NoSQLPlugin").command("UPDATE");

		Device d = this.getDevice(objects[0]);
		d.deleteAttribute((String) objects[1]);

		return updateHelper(aq, d);
	}
	
	private boolean addToGroup(Object ...objects) throws InternalPluginException { //String deviceID, String key
		ActionQuery aq = new ActionQuery();
		aq.type(ActionQuery.TYPE.SPECIFICATION).pluginID("NoSQLPlugin").command("UPDATE");

		Device d = this.getDevice(objects[0]);
		d.addGroup((String) objects[1]);

		return updateHelper(aq, d);
	}
	
	private boolean deleteGroup(Object ...objects) throws InternalPluginException { //String groupID
//		ActionQuery aq = new ActionQuery();
//		aq.type(ActionQuery.TYPE.SPECIFICATION).pluginID("NOSQL").command("UPDATE");
//
//		Device d = this.getDevice(objects[0]);
//		d.deleteGroup((String) objects[1]);
//
//		return updateHelper(aq, d);
		return true; // implement later
	}
	
	private boolean deleteFromGroup(Object ...objects) throws InternalPluginException { //String deviceID, groupID
		ActionQuery aq = new ActionQuery();
		aq.type(ActionQuery.TYPE.SPECIFICATION).pluginID("NoSQLPlugin").command("UPDATE");

		Device d = this.getDevice(objects[0]);
		d.deleteGroup((String) objects[1]);

		return updateHelper(aq, d);
	}

}
