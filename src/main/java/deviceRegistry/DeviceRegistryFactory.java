package deviceRegistry;

import org.homi.plugin.api.IPlugin;
import org.homi.plugin.api.IPluginConfiguration;
import org.homi.plugin.api.IPluginFactory;
import org.homi.plugin.api.IServiceDescriptor;

public class DeviceRegistryFactory implements IPluginFactory{

	@Override
	public String getGAV() {
		// TODO Auto-generated method stub
		return "org.homi.plugins.deviceRegistry";
	}

	@Override
	public IPlugin getPlugin(IPluginConfiguration arg0) {
		// TODO Auto-generated method stub
		return new DeviceRegistry();
	}

	@Override
	public IServiceDescriptor getServiceDescriptor() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
