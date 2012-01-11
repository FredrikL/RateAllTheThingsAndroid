package com.tacticalnuclearstrike.rateallthethings;

import com.google.inject.AbstractModule;
import com.google.inject.Module;
import com.tacticalnuclearstrike.rateallthethings.api.IService;
import com.tacticalnuclearstrike.rateallthethings.api.Service;
import roboguice.application.RoboApplication;

import java.util.List;

/**
 * User: Fredrik / 2011-12-19
 */
public class RateAllTheThingsApplication extends RoboApplication {
    
    protected void addApplicationModules(List<Module> modules) {
        modules.add(new Bindings());
    }
    
    private class Bindings extends AbstractModule {

        @Override
        protected void configure() {
            bind(IService.class).to(Service.class);
        }
    }
}
