package at.tuwien.indmp.service;

import at.tuwien.indmp.model.RDMService;

import java.util.List;

public interface RDMServiceLayer {

    public void create(RDMService rdmService);

    public RDMService findByClientId(String clientId);

    public List<RDMService> getAllRDMServices();

    public void update(RDMService rdmService);

    public void delete(Long id);
}
