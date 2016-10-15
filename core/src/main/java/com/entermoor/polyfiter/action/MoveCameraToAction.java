package com.entermoor.polyfiter.action;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.scenes.scene2d.actions.TemporalAction;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.utils.GdxRuntimeException;

/**
 * Can be used on any actor, just specific a camera...
 * Nearly copied from {@link MoveToAction}
 */
public class MoveCameraToAction extends TemporalAction {
    private float startX, startY, startZ;
    private float endX, endY, endZ;
    private Camera camera;

    public MoveCameraToAction(Camera camera, float duration) {
        this.camera = camera;
        this.setDuration(duration);
    }

    protected void begin() {
        if (camera == null) {
            throw new GdxRuntimeException("Camera can't be null.");
        }
        startX = camera.position.x;
        startY = camera.position.y;
        startZ = camera.position.z;
    }

    protected void update(float percent) {
        if (camera == null) {
            throw new GdxRuntimeException("Camera can't be null.");
        }
//        Gdx.app.debug("MoveCameraToAction", "" + percent);
//        camera.translate((endX - startX) * percent, (endY - startY) * percent, (endZ - startZ) * percent);
//        camera.lookAt(startX + (endX - startX) * percent, startY + (endY - startY) * percent, startZ + (endZ - startZ) * percent);
        camera.position.x=startX + (endX - startX) * percent;
        camera.position.y=startY + (endY - startY) * percent;
        camera.position.z=startZ + (endZ - startZ) * percent;
    }

    public void reset() {
        super.reset();
    }

    public void setPosition(float x, float y, float z) {
        endX = x;
        endY = y;
        endZ = z;
    }

    public float getX() {
        return endX;
    }

    public void setX(float x) {
        endX = x;
    }

    public float getY() {
        return endY;
    }

    public void setY(float y) {
        endY = y;
    }

    public float getZ() {
        return endZ;
    }

    public void setZ(float z) {
        endZ = z;
    }
}
