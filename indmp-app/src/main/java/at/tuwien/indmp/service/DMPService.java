package at.tuwien.indmp.service;

import java.util.List;

import at.tuwien.indmp.model.DataService;
import at.tuwien.indmp.model.Entity;
import at.tuwien.indmp.model.dmp.DMP;
import at.tuwien.indmp.model.dmp.DMPScheme;

public interface  DMPService {

    public void create(DMP dmp, DataService rdmService);

    public DMP identifyDMP(DMP dmp, DataService dataService);

    public DMPScheme loadWholeDMP(DMP dmp);

    public void update(DMP dmp, DataService dataService);

    public void changeIdentifiers(DMP dmp, Entity identifier, DataService dataService);

    public void deleteInstance(Entity entity);

    public List<Entity> loadIdentifierHistory(DMP dmp);

}
