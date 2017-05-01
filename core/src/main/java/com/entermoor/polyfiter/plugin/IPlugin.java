package com.entermoor.polyfiter.plugin;

import com.badlogic.gdx.ApplicationListener;

public interface IPlugin extends ApplicationListener {

    /** @return name of this plugin. */
    String getName();

    /** @return description of this plugin. */
    String getDescription();

    /** @return version of this plugin. */
    String getVersion();
}
