package com.abidux.craftus;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import lombok.Getter;
import org.bukkit.plugin.PluginLogger;

public class CraftUsBinderModule extends AbstractModule {

    @Getter private static Injector defaultInjector;

    private final CraftUs plugin;

    public CraftUsBinderModule(CraftUs plugin) {
        this.plugin = plugin;
    }

    public Injector createInjector() {
        if (defaultInjector != null) {
            return defaultInjector;
        }

        Injector injector = Guice.createInjector(this);
        defaultInjector = injector;
        return injector;
    }

    @Override
    protected void configure() {
        bind(CraftUs.class).toInstance(plugin);
        bind(PluginLogger.class).toInstance((PluginLogger) plugin.getLogger());
    }

}
