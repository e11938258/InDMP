package at.tuwien.indmp.model.dmp;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import at.tuwien.indmp.model.DataService;
import at.tuwien.indmp.model.Entity;
import at.tuwien.indmp.service.EntityService;

public abstract class AbstractClassEntity extends AbstractEntity {

    public abstract void build(EntityService entityService, String location);

    @JsonIgnore
    public List<Entity> getPropertiesFromIdentifier(DMP dmp, String location, DataService dataService) {
        return new ArrayList<Entity>();
    }

    @JsonIgnore
    public List<Entity> getPropertiesFromNestedClasses(DMP dmp, String location, DataService dataService) {
        return new ArrayList<Entity>();
    }

    @JsonIgnore
    public boolean hasRightsToUpdate(DataService dataService) {
        if (dataService.getRights().contains(getClassType())) {
            return true;
        } else {
            return false;
        }
    }

    @JsonIgnore
    @Override
    public List<Entity> getProperties(DMP dmp, String location, DataService dataService) {
        // Has service rights to update the class or is it new dmp?
        if (dmp.isNew() || hasRightsToUpdate(dataService)) {
            final List<Entity> properties = super.getProperties(dmp, location, dataService);

            // Add identifier
            properties.addAll(getPropertiesFromIdentifier(dmp, location, dataService));

            return properties;
        } else {
            return new ArrayList<>();
        }
    }
}
