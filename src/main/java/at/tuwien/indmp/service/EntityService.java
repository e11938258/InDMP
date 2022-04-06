package at.tuwien.indmp.service;

import java.util.List;

import at.tuwien.indmp.model.DataService;
import at.tuwien.indmp.model.Entity;

public interface EntityService {

    public void persist(List<Entity> entities, DataService dataService);

    public Entity findEntity(String atLocation, String specializationOf, String value);

    public List<Entity> findEntities(String atLocation, String specializationOf);

    public List<Entity> findAllEntities(String atLocation, String specializationOf);

    public void updateOrCreateEntities(List<Entity> entities, DataService dataService);

    public void update(Entity entity, Entity currentEntity, DataService dataService);

    public void changeNestedEntities(String currentLocation, String newLocation);

    public void removeAllNestedEntities(String currentLocation);

    public List<Entity> loadIdentifierHistory(String dmpIdentifier, String specializationOf);

}
