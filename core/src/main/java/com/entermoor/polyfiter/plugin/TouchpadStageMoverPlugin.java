package com.entermoor.polyfiter.plugin;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.utils.TimeUtils;
import com.entermoor.polyfiter.Polyfiter;
import com.entermoor.polyfiter.action.MoveCameraToAction;

import static com.entermoor.polyfiter.Polyfiter.instance;

public class TouchpadStageMoverPlugin implements IPlugin {

    public static TouchpadStageMoverPlugin inst = new TouchpadStageMoverPlugin();

    public Touchpad touchpad;
    public long padLastTouchTime = -1;
    public boolean drawFuncWhenTouched = false;

    @Override
    public void create() {
        touchpad = new Touchpad(0, instance.skin);
        touchpad.getColor().a *= 0.233;
        instance.stage.addActor(touchpad);

        touchpad.addListener(new EventListener() {
            @Override
            public boolean handle(Event event) {
                if (event instanceof InputEvent) {
                    if (((InputEvent) event).getType().equals(InputEvent.Type.touchUp)) {
                        if (padLastTouchTime > 0) {
                            long delta = TimeUtils.millis() - padLastTouchTime;
                            if (0 < delta && delta < 233) {
                                MoveCameraToAction action = new MoveCameraToAction((instance.innerStage.getViewport().getCamera()), 0.666666F);
                                action.setPosition(0, 0, 0);
                                touchpad.addAction(action);
                            }
                        }
                        padLastTouchTime = TimeUtils.millis();
                    }
                }
                return true;
            }
        });
    }

    @Override
    public void resize(int width, int height) {
        int min = Math.min(width, height);
        touchpad.setBounds(min / 11, min / 11, min / 7 * 2, min / 7 * 2);
    }

    @Override
    public void render() {
        if (touchpad.isTouched()) {
            KeyboardStageMoverPlugin.inst.resetScaleDelta = false;
            float deltaX = touchpad.getKnobPercentX();
            float deltaY = touchpad.getKnobPercentY();
            KeyboardStageMoverPlugin.inst.scaleDelta += Math.abs((deltaX + deltaY)) / 20;
            instance.innerStage.getViewport().getCamera().translate(deltaX * KeyboardStageMoverPlugin.inst.scaleDelta, deltaY * KeyboardStageMoverPlugin.inst.scaleDelta, 0);
            Gdx.graphics.requestRendering();
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

    @Override
    public String getName() {
        return "TouchpadStageMoverPlugin";
    }

    @Override
    public String getDescription() {
        return "Present a Touchpad to move stage.";
    }

    @Override
    public String getVersion() {
        return Polyfiter.VERSION;
    }
}
