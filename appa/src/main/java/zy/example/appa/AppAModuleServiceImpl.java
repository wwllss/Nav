package zy.example.appa;

import zy.example.app_service.AppAModuleService;
import zy.nav.annotation.Service;

@Service(AppAModuleService.class)
public class AppAModuleServiceImpl implements AppAModuleService {

    @Override
    public void reset() {
        DataManager instance = DataManager.getInstance();
        instance.resetData();
        instance.notifyObservers(instance.getData());
    }
}
