package at.tuwien.indmp.util;

import java.util.List;
import java.util.stream.Collectors;

import at.tuwien.indmp.model.Property;
import at.tuwien.indmp.util.var.ServiceType;

public class Functions {

    private Functions() {
        throw new AssertionError();
    }

    public static boolean isServiceTypeInArray(ServiceType[] servicesTypes, ServiceType serviceType) {
        for (final ServiceType st : servicesTypes) {
            if (st.equals(serviceType)) {
                return true;
            }
        }
        return false;
    }

    public static Property findPropertyInList(String name, List<Property> properties) {
        final List<Property> results = properties.stream().filter(p -> p.getPropertyName().equals(name))
                .collect(Collectors.toList());
        if(results.size() == 1) {
            return results.get(0);
        } else {
            return null;
        }
    }
}
