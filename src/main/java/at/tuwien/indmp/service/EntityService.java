package at.tuwien.indmp.service;

import java.time.LocalDateTime;
import java.util.List;

import at.tuwien.indmp.model.DataService;
import at.tuwien.indmp.model.Entity;

public interface EntityService {

    public void persist(List<Entity> entities, DataService dataService);

    public Entity findEntity(String atLocation, String specializationOf, String value);

    public List<Entity> findEntities(String atLocation, String specializationOf, String value, boolean onlyActive);

    public List<Entity> findAllEntities(String atLocation, String specializationOf, boolean onlyActive);

    public void deactivateAndCreateEntities(List<Entity> entities, DataService dataService);

    public void deactivateAndCreateEntity(Entity entity, DataService dataService);

    public void changeNestedEntities(String currentLocation, String newLocation);

    public void removeAllNestedEntities(String currentLocation, LocalDateTime endTime);

}
