package deviceRegistry;

import deviceRegistrySpec.Device;
import org.homi.plugins.dbs.nosqlspec.record.*;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class DeviceRegistryStorageVisitor implements IStorageComponentVisitor<Object> {
    private static class DeviceRegistrySubvisitor implements IStorageComponentVisitor<Object> {

        @Override
        public Object visit(FieldList fieldList) {
            return fieldList.getList().stream().map((c)->{return c.accept(this);}).collect(Collectors.toList());
        }

        @Override
        public Object visit(Record record) {

            Map<String, Object> m = new HashMap<>();

            for (Map.Entry<String, IStorageComponent> entry : record.getFields().entrySet()) {
                m.put(entry.getKey(), entry.getValue().accept((IStorageComponentVisitor<Object>) this));
            }
            return m;
        }

        @Override
        public <T extends Serializable> Object visit(Value<T> value) {
            return value.getValue();
        }
    }
    @Override
    public List<Device> visit(FieldList fieldList) {

        return fieldList.getList().stream().map((c)->{return (Device) c.accept(this);}).collect(Collectors.toList());
    }

    @Override
    public Device visit(Record record) {

        String name = (String) record.getFields().get("name").accept(this);
        Device d = new Device(name);
        d.setAddresses((Map<String, String>)record.getFields().get("addresses").accept((IStorageComponentVisitor<Object>) new DeviceRegistrySubvisitor()));
        d.setAttributes((Map<String, Serializable>)record.getFields().get("attributes").accept((IStorageComponentVisitor<Object>) new DeviceRegistrySubvisitor()));
        d.setGroups((Set<String>)record.getFields().get("groups").accept((IStorageComponentVisitor<Object>) this));
        return d;
    }

    @Override
    public <T extends Serializable> Object visit(Value<T> value) {

        return value.getValue();
    }
}
