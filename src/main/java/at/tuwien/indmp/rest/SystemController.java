package at.tuwien.indmp.rest;

import java.util.List;

import javax.validation.Valid;

import at.tuwien.indmp.model.System;
import at.tuwien.indmp.service.SystemService;
import at.tuwien.indmp.util.Constants;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SystemController {

    @Autowired
    private SystemService systemService;

    public SystemController() {
    }

    /**
     *
     * Create a new system
     *
     * @param system
     */
    @RequestMapping(value = Constants.CREATE_SYSTEM, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public void createSystem(final @Valid @RequestBody System system) {
        systemService.create(system);
    }

    /**
     *
     * Get all services
     *
     * @param system
     */
    @RequestMapping(value = Constants.GET_ALL_SYSTEMS, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<System> getAllSystems() {
        return systemService.getAllSystems();
    }
}
