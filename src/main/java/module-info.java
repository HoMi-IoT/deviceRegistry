import org.homi.plugin.api.IPlugin;

module org.homi.deviceRegistry {
	requires org.homi.plugin.api;
	requires org.homi.plugin.specification;
	requires org.homi.core;
	requires java.sql;
	requires deviceRegistrySpec;
	provides org.homi.plugin.api.IPlugin with deviceRegistry.DeviceRegistry;
}