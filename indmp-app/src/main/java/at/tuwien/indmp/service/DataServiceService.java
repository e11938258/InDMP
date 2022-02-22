package at.tuwien.indmp.service;

import java.util.List;

import at.tuwien.indmp.model.DataService;

public interface DataServiceService {

    public void persist(DataService dataService);

    public DataService findByClientId(String accessRights);

    public List<DataService> getAllDataServices();

    public void update(DataService dataService);
}
