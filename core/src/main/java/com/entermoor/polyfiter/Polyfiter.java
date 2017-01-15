package com.entermoor.polyfiter;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.entermoor.polyfiter.action.MoveCameraToAction;
import com.entermoor.polyfiter.utils.Polyfit;
import com.kotcrab.vis.ui.VisUI;

import net.hakugyokurou.fds.node.InvalidExpressionException;
import net.hakugyokurou.fds.parser.MathExpressionParser;

import java.io.StringReader;
import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import de.tomgrill.gdxdialogs.core.GDXDialogsSystem;

public class Polyfiter extends ApplicationAdapter {

    public static final Object lock = new Object();

    public Set<Polyfit.Point2> points = new LinkedHashSet<Polyfit.Point2>();
    public Map<String, Set<Polyfit.Point2>> funcs = new LinkedHashMap<String, Set<Polyfit.Point2>>();
    public Set<Runnable> resizeToDo = new LinkedHashSet<Runnable>();

    public SpriteBatch batch;
    public InputMultiplexer inputProcessor;

    public Stage stage;
    public Touchpad touchpad;

    public Stage innerStage;
    public Image img;
    public ShapeRenderer shapeRenderer;
    public ImageButton buttonAdd;
    public ImageButton buttonRefresh;

    public Color pointColor, xColor, yColor, lineColor;
    public float scaleDelta = 1;

    public long padLastTouchTime = -1;
    public int nCachedPointMax = 10000;

    public static float dScaleDelta(float scaleDelta) {
        return (float) 23.3 / scaleDelta;
    }

    /**
     * Filter all pixels using r,g,b parameters and create a new pixmap..
     *
     * @param pixmap The pixmap to filter.
     * @param r      r2 = r1 * r, r >= 0.
     * @param g      g2 = g1 * g, g >= 0.
     * @param b      b2 = b1 * b, b >= 0.
     * @param a      a2 = a1 * a, a >= 0.
     * @return Filtered pixmap.
     */
    public static Pixmap filter(Pixmap pixmap, float r, float g, float b, float a) {
        Pixmap newPixmap = new Pixmap(pixmap.getWidth(), pixmap.getHeight(), pixmap.getFormat());
        Color oldColor = new Color(), newColor = new Color();
        for (int i = 0; i < pixmap.getWidth(); i++) {
            for (int j = 0; j < pixmap.getHeight(); j++) {
                oldColor.set(pixmap.getPixel(i, j));
                Gdx.app.debug("Filter",oldColor.toString());
                newPixmap.drawPixel(i, j, newColor.set(oldColor.r * r, oldColor.g * g, oldColor.b * b, oldColor.a * a).toIntBits());
                Gdx.app.debug("Filter",newColor.toString());
            }
        }
        return newPixmap;
    }

    public static TextureRegionDrawable drawable(Pixmap pixmap) {
        return new TextureRegionDrawable(new TextureRegion(new Texture(pixmap)));
    }

    @Override
    public void create() {
        Gdx.app.setLogLevel(Application.LOG_DEBUG);
        Gdx.graphics.setWindowedMode((int) (Gdx.graphics.getDisplayMode().width * 0.9), (int) (Gdx.graphics.getDisplayMode().height * 0.8));

        batch = new SpriteBatch();
        stage = new Stage(new ScreenViewport(), batch);
        stage.setDebugAll(true);
        resizeToDo.add(new Runnable() {
            @Override
            public void run() {
                inputProcessor.removeProcessor(stage);
                Stage newStage = new Stage(stage.getViewport(), batch);
                for (Actor actor : stage.getActors()) {
                    stage.getRoot().removeActor(actor);
                    newStage.addActor(actor);
                }
                stage.dispose();
                newStage.setDebugAll(true);
                stage = newStage;
                inputProcessor.addProcessor(stage);
                stage.addActor(buttonAdd);
            }
        });

        innerStage = new Stage(new ScreenViewport(new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight())), batch);
        innerStage.setDebugAll(true);
        shapeRenderer = new ShapeRenderer();
        resizeToDo.add(new Runnable() {
            @Override
            public void run() {
                innerStage.getViewport().update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
                stage.getViewport().update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            }
        });

        resizeToDo.add(new Runnable() {
            @Override
            public void run() {
                synchronized (lock) {
                    for (Map.Entry<String, Set<Polyfit.Point2>> entry : funcs.entrySet()) {
                        entry.setValue(cacheValue(entry.getKey()));
                    }
                }
            }
        });

        //Avoid exception on hot swap.
        if (VisUI.isLoaded())
            VisUI.dispose();
        VisUI.load();
        try {
            GDXDialogsSystem.getDialogManager();
        } catch (Throwable e) {
            GDXDialogsSystem.install();
        }

        img = new Image(new TextureRegionDrawable(new TextureRegion(new Texture("badlogic.jpg"))));
        innerStage.addActor(img);

        touchpad = new Touchpad(0, VisUI.getSkin());
        touchpad.getColor().a *= 0.233;
        stage.addActor(touchpad);
        resizeToDo.add(new Runnable() {
            @Override
            public void run() {
                int min = Math.min(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
                touchpad.setBounds(min / 11, min / 11, min / 7 * 2, min / 7 * 2);
            }
        });

        Pixmap buttonAddUp = new Pixmap(Gdx.files.internal("plus.png"));
        Pixmap buttonAddDown = filter(buttonAddUp, 0.8F, 0.8F, 1, 1);
        buttonAdd = new ImageButton(drawable(buttonAddUp), drawable(buttonAddDown));
        stage.addActor(buttonAdd);
        resizeToDo.add(new Runnable() {
            @Override
            public void run() {
                int size = Math.min(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()) / 10;
                buttonAdd.setBounds(Gdx.graphics.getWidth() - size, Gdx.graphics.getHeight() - size, size, size);
            }
        });
        buttonRefresh = new ImageButton(VisUI.getSkin());
        stage.addActor(buttonRefresh);
        resizeToDo.add(new Runnable() {
            @Override
            public void run() {
                int size = Math.min(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()) / 10;
                buttonRefresh.setBounds(Gdx.graphics.getWidth() - size * 2, Gdx.graphics.getHeight() - size, size, size);
            }
        });

        inputProcessor = new InputMultiplexer(stage, innerStage);
        Gdx.input.setInputProcessor(inputProcessor);

        pointColor = new Color(0, 1, 0, 1);
        xColor = new Color(1, 1, 0, 1);
        yColor = new Color(0, 1, 1, 1);
        lineColor = new Color(1, 1, 1, 1);

        innerStage.getViewport().getCamera().position.x = 0;
        innerStage.getViewport().getCamera().position.y = 0;

        touchpad.addListener(new EventListener() {
            @Override
            public boolean handle(Event event) {
                if (event instanceof InputEvent) {
                    if (((InputEvent) event).getType().equals(InputEvent.Type.touchUp)) {
                        if (padLastTouchTime > 0) {
                            long delta = TimeUtils.millis() - padLastTouchTime;
                            Gdx.app.debug("Delta time", "" + delta);
                            if (delta < 0) {
                                Gdx.app.error("Delta time", "can't be null! The time has gone back?!");
                            } else if (delta < 233) {
                                MoveCameraToAction action = new MoveCameraToAction((innerStage.getViewport().getCamera()), 0.666666F);
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
        funcs.put("0.02*x*x-2", cacheValue("(x*x)-2"));
    }

    @Override
    public void resize(int width, int height) {
        Gdx.app.debug("Resize", "Width: " + width + ", Height: " + height + ".");
        for (Runnable runnable : resizeToDo) {
            runnable.run();
        }
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0,0,0,0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        Camera innerCamera = innerStage.getViewport().getCamera();
        if (touchpad.isTouched()) {
            Gdx.app.debug("Touchpad", touchpad.getKnobPercentX() + ", " + touchpad.getKnobPercentY());
            float deltaX = touchpad.getKnobPercentX();
            float deltaY = touchpad.getKnobPercentY();
            scaleDelta += Math.abs((deltaX + deltaY)) / 20;
            innerCamera.translate(deltaX * scaleDelta, deltaY * scaleDelta, 0);
        } else if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            scaleDelta += dScaleDelta(scaleDelta);
            innerCamera.translate((float) (-0.1 * scaleDelta), 0, 0);
        } else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            scaleDelta += dScaleDelta(scaleDelta);
            innerCamera.translate((float) (0.1 * scaleDelta), 0, 0);
        } else if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            scaleDelta += dScaleDelta(scaleDelta);
            innerCamera.translate(0, (float) (0.1 * scaleDelta), 0);
        } else if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            scaleDelta += dScaleDelta(scaleDelta);
            innerCamera.translate(0, (float) (-0.1 * scaleDelta), 0);
        } else {
            scaleDelta = 1;
        }

        stage.act();
        innerCamera.update();
        shapeRenderer.setProjectionMatrix(innerCamera.combined);
        shapeRenderer.end();
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(yColor);
        Vector3 position = innerCamera.position;
        if (position.x - Gdx.graphics.getWidth() / 2 < 0 && position.x + Gdx.graphics.getWidth() / 2 > 0) {
            shapeRenderer.line(0, position.y - Gdx.graphics.getHeight() / 2, 0, position.y + Gdx.graphics.getHeight() / 2);
        }
        shapeRenderer.setColor(xColor);
        if (position.y - Gdx.graphics.getHeight() / 2 < 0 && position.y + Gdx.graphics.getHeight() / 2 > 0) {
            shapeRenderer.line(position.x - Gdx.graphics.getWidth() / 2, 0, position.x + Gdx.graphics.getWidth() / 2, 0);
        }
        shapeRenderer.end();
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(pointColor);
        shapeRenderer.circle(0, 0, 6.66F);
        for (Polyfit.Point2 point : points) {
            shapeRenderer.circle((float) point.x.doubleValue(), (float) point.y.doubleValue(), 2.33F);
        }
        shapeRenderer.setColor(lineColor);
        for (Map.Entry<String, Set<Polyfit.Point2>> entry : funcs.entrySet()) {
            for (Polyfit.Point2 point : entry.getValue()) {
                shapeRenderer.circle((float) point.x.doubleValue(), (float) point.y.doubleValue(), 1);
            }
        }
        shapeRenderer.end();
        stage.draw();
    }

    @Override
    public void dispose() {
        shapeRenderer.dispose();
        stage.dispose();
        innerStage.dispose();
        batch.dispose();
//        img.dispose();
        VisUI.dispose();
    }

    public Set<Polyfit.Point2> cacheValue(String func) {
        int n = Math.min(Gdx.graphics.getWidth(), nCachedPointMax);
        if (n > 0) {
            Set<Polyfit.Point2> values = new LinkedHashSet<Polyfit.Point2>(n);
            double deltaX = Gdx.graphics.getWidth() / n;
            for (int i = 0; i <= n; i++) {
                try {
                    Vector3 position = innerStage.getViewport().getCamera().position;
                    float x = (float) (position.x - Gdx.graphics.getWidth() / 2 + deltaX * i * 2);
                    String expression = func.replace("x", "" + x);
                    float y = (float) MathExpressionParser.parseLine(new StringReader(expression)).eval();
                    if (y > position.y - Gdx.graphics.getHeight() / 2 && y < position.y + Gdx.graphics.getHeight() / 2)
                        values.add(new Polyfit.Point2(new BigDecimal(x), new BigDecimal(y)));
                } catch (InvalidExpressionException iee) {
                    Gdx.app.debug("CacheValue", "Failed to cache " + func + ", x=" + i + "\n", iee);
                }
            }
            return values;
        }
        Gdx.app.error("Caching points", "Invalid amount " + n);
        return null;
    }
}
