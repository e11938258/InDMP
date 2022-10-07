package at.tuwien.indmp.dao;

import org.springframework.stereotype.Repository;

import at.tuwien.indmp.model.Activity;

@Repository
public class ActivityDao extends AbstractDao<Activity> {

    public ActivityDao() {
        super(Activity.class);
    }
}
