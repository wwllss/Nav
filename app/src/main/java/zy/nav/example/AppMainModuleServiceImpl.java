package zy.nav.example;

import zy.example.app_service.AppMainModuleService;
import zy.nav.annotation.Service;

@Service(AppMainModuleService.class)
public class AppMainModuleServiceImpl implements AppMainModuleService {

    @Override
    public void reset() {

    }

}
