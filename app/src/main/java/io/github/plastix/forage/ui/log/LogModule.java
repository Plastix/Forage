package io.github.plastix.forage.ui.log;

import dagger.Module;
import dagger.Provides;

@Module
public class LogModule {

    private LogView logView;

    public LogModule(LogView logView) {
        this.logView = logView;
    }

    @Provides
    public LogView providesLogView() {
        return logView;
    }
}
