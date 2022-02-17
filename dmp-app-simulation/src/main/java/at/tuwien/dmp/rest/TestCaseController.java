package at.tuwien.dmp.rest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestCaseController {

    @Value("${indmp.host}")
    private String indmpHost;

    @Value("${indmp.endpoint.update.madmp}")
    private String indmpUpdateMaDMP;

    @Value("${indmp.endpoint.update.identifier}")
    private String indmpUpdateIdentifier;

    @Value("${indmp.endpoint.delete.instance}")
    private String indmpDeleteInstance;

    @Value("${indmp.endpoint.get.identifiers}")
    private String indmpGetIdentifiers;
    
    
}
