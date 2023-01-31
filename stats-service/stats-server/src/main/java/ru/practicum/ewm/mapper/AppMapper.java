package ru.practicum.ewm.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.ewm.db.model.App;
import ru.practicum.ewm.db.repository.AppRepository;

@Component
@RequiredArgsConstructor
public class AppMapper {

    private final AppRepository appRepository;

    @StringToApp
    public App stringToApp(String strApp) {
        App app = appRepository.getAppByName(strApp);
        if (app == null) {
            app = new App();
            app.setName(strApp);
            return appRepository.save(app);
        }
        return app;
    }

}
