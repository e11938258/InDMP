package at.tuwien.indmp.util;

import java.util.List;
import java.util.stream.Collectors;

import at.tuwien.indmp.model.Property;

public class Functions {

    private Functions() {
        throw new AssertionError();
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
