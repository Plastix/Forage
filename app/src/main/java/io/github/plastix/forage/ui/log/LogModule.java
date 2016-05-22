package io.github.plastix.forage.ui.log;

import dagger.Module;
import io.github.plastix.forage.ui.base.BaseModule;

@Module
public class LogModule extends BaseModule<LogView> {
    public LogModule(LogView view) {
        super(view);
    }
}
