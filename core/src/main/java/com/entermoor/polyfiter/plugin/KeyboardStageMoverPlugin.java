package com.entermoor.polyfiter.plugin;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Camera;
import com.entermoor.polyfiter.Polyfiter;

import static com.entermoor.polyfiter.Polyfiter.instance;

public class KeyboardStageMoverPlugin implements IPlugin {

    public static KeyboardStageMoverPlugin inst = new KeyboardStageMoverPlugin();

    public float scaleDelta = 1;
    public boolean resetScaleDelta = true;

    @Override
    public String getName() {
        return "KeyboardStageMoverPlugin";
    }

    @Override
    public String getDescription() {
        return "Using a keyboard to control the stage.";
    }

    @Override
    public String getVersion() {
        return Polyfiter.VERSION;
    }

    @Override
    public void create() {
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void render() {
        Camera innerCamera = instance.innerStage.getViewport().getCamera();
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            scaleDelta += 23.3 / scaleDelta;
            innerCamera.translate((float) (-0.1 * scaleDelta), 0, 0);
            Gdx.graphics.requestRendering();
        } else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            scaleDelta += 23.3 / scaleDelta;
            innerCamera.translate((float) (0.1 * scaleDelta), 0, 0);
            Gdx.graphics.requestRendering();
        } else if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            scaleDelta += 23.3 / scaleDelta;
            innerCamera.translate(0, (float) (0.1 * scaleDelta), 0);
            Gdx.graphics.requestRendering();
        } else if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            scaleDelta += 23.3 / scaleDelta;
            innerCamera.translate(0, (float) (-0.1 * scaleDelta), 0);
            Gdx.graphics.requestRendering();
        } else {
            if (scaleDelta != 1) {
                FunctionCacherPlugin.inst.recache();
                Gdx.graphics.requestRendering();
            }
            if (resetScaleDelta) {
                scaleDelta = 1;
            } else {
                resetScaleDelta = true;
            }
        }
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void dispose() {
    }
}
