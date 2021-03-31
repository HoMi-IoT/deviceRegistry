import org.homi.plugin.api.IPluginFactory;

module org.homi.deviceRegistry {
	requires org.homi.plugin.api;
	provides IPluginFactory with deviceRegistry.DeviceRegistryFactory;
}