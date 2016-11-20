package com.entermoor.polyfiter.desktop;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.entermoor.polyfiter.Polyfiter;

public class DesktopLauncher {
    public static void main(String[] arg) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.allowSoftwareMode = true;
        config.resizable = false;
        config.addIcon("icon_32x32.png", Files.FileType.Internal);
        config.addIcon("icon_128x128.png", Files.FileType.Internal);
        new LwjglApplication(new Polyfiter(), config);
    }
}
