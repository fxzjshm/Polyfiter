package com.entermoor.polyfiter.desktop;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.entermoor.polyfiter.Polyfiter;

public class DesktopLauncher {
    public static void main(String[] arg) {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        // config.allowSoftwareMode = true;
        // config.resizable = false;
        // config.addIcon("icon_32x32.png", Files.FileType.Internal);
        // config.addIcon("icon_128x128.png", Files.FileType.Internal);
        new Lwjgl3Application(new Polyfiter(), config);
    }
}
