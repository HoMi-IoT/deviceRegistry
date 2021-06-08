module org.homi.deviceRegistry {
    exports deviceRegistry;
    requires org.homi.plugin.api;
	requires org.homi.plugin.specification;
	requires java.sql;
	requires deviceRegistrySpec;
    requires nosqlspec;
    requires org.homi.plugins.actionRegistry.specification;
    provides org.homi.plugin.api.basicplugin.IBasicPlugin 
    	with deviceRegistry.DeviceRegistry;
}