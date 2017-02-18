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
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.entermoor.polyfiter.action.MoveCameraToAction;
import com.entermoor.polyfiter.utils.GwtRunnablePoster;
import com.entermoor.polyfiter.utils.IRunnablePoster;
import com.entermoor.polyfiter.utils.OrdinaryEntry;
import com.entermoor.polyfiter.utils.Polyfit;
import com.entermoor.polyfiter.utils.Synchronized;
import com.kotcrab.vis.ui.VisUI;

import net.hakugyokurou.fds.node.OperationNode;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import de.tomgrill.gdxdialogs.core.GDXDialogsSystem;
import de.tomgrill.gdxdialogs.core.dialogs.GDXProgressDialog;
import de.tomgrill.gdxdialogs.core.dialogs.GDXTextPrompt;
import de.tomgrill.gdxdialogs.core.listener.TextPromptListener;

public class Polyfiter extends ApplicationAdapter {

    public Set<Polyfit.Point2> points = new Synchronized.SynchronizedSet<Polyfit.Point2>(new LinkedHashSet<Polyfit.Point2>());
    public Map<String, Set<Polyfit.Point2>> funcs = new Synchronized.SynchronizedMap<String, Set<Polyfit.Point2>>(new LinkedHashMap<String, Set<Polyfit.Point2>>());
    public Set<Runnable> resizeToDo = new LinkedHashSet<Runnable>();
    public Stack<OrdinaryEntry<Double, String>> toCacheList = new Stack<OrdinaryEntry<Double, String>>();

    public IRunnablePoster runnablePoster;

    public SpriteBatch batch;
    public InputMultiplexer inputProcessor;

    public Stage stage;
    public Touchpad touchpad;

    public Stage innerStage;
    public Image img;
    public ShapeRenderer shapeRenderer;
    public Button buttonAdd;
    public Button buttonRefresh;
    public GDXProgressDialog dialogCaching;
    public GDXTextPrompt dialogAdd;

    public Color pointColor, xColor, yColor, lineColor;
    public float scaleDelta = 1;

    public long padLastTouchTime = -1;
    public int nCachedPointMax = 10000;
    public boolean drawFuncWhenTouched = false;

    public Polyfiter() {
        this(new GwtRunnablePoster());
    }

    public Polyfiter(IRunnablePoster poster) {
        runnablePoster = poster;
    }

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
        Color oldColor = new Color();
        for (int i = 0; i < pixmap.getWidth(); i++) {
            for (int j = 0; j < pixmap.getHeight(); j++) {
                oldColor.set(pixmap.getPixel(i, j));
                newPixmap.drawPixel(i, j, Color.rgba8888(oldColor.r * r, oldColor.g * g, oldColor.b * b, oldColor.a * a));
            }
        }
        return newPixmap;
    }

    public static TextureRegionDrawable drawable(Pixmap pixmap) {
        return new TextureRegionDrawable(new TextureRegion(new Texture(pixmap)));
    }

    @Override
    public void create() {
        // TODO Delete this wehen releasing
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
                for (Array<Actor> actors = stage.getActors(); actors.size > 0; )
                    newStage.addActor(actors.pop());
                stage.dispose();
                newStage.setDebugAll(true);
                stage = newStage;
                inputProcessor.addProcessor(stage);
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

        OperationNode.mathContext = new MathContext(5, RoundingMode.HALF_UP);
        resizeToDo.add(new Runnable() {
            @Override
            public void run() {
                recache();
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
        Pixmap buttonAddDown = filter(buttonAddUp, 0.233F, 0.233F, 0.666F, 1);
        buttonAdd = new Button(drawable(buttonAddUp), drawable(buttonAddDown));
        buttonAdd.addListener(new EventListener() {
            @Override
            public boolean handle(Event event) {
                if (event instanceof InputEvent && ((InputEvent) event).getType().equals(InputEvent.Type.touchDown))
                    dialogAdd.show();
                return false;
            }
        });
        stage.addActor(buttonAdd);
        resizeToDo.add(new Runnable() {
            @Override
            public void run() {
                int size = Math.min(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()) / 10;
                buttonAdd.setBounds(Gdx.graphics.getWidth() - size, Gdx.graphics.getHeight() - size, size, size);
            }
        });
        Pixmap buttonRefreshUp = new Pixmap(Gdx.files.internal("reload.png"));
        Pixmap buttonRefreshDown = filter(buttonRefreshUp, 0.233F, 0.233F, 0.666F, 1);
        buttonRefresh = new Button(drawable(buttonRefreshUp), drawable(buttonRefreshDown));
        buttonRefresh.addListener(new EventListener() {
            @Override
            public boolean handle(Event event) {
                if (event instanceof InputEvent && ((InputEvent) event).getType().equals(InputEvent.Type.touchDown))
                    recache();
                return false;
            }
        });
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
                            if (0 < delta && delta < 233) {
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
        dialogAdd = GDXDialogsSystem.getDialogManager().newDialog(GDXTextPrompt.class);
        dialogAdd.setTitle("Add point(s) or a function");
        dialogAdd.setConfirmButtonLabel("OK");
        dialogAdd.setCancelButtonLabel("Cancel");
        dialogAdd.setTextPromptListener(new TextPromptListener() {
            @Override
            public void cancel() {
            }

            @Override
            public void confirm(String text) {
                funcs.put(text, cacheValue(text));
            }
        });
        dialogAdd.build();
        funcs.put("1000/x", cacheValue("1000/x"));

        Gdx.graphics.setContinuousRendering(false);

    }

    @Override
    public void resize(int width, int height) {
        // Gdx.app.debug("Resize", "Width: " + width + ", Height: " + height + ".");
        for (Runnable runnable : resizeToDo) {
            runnable.run();
        }
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        Camera innerCamera = innerStage.getViewport().getCamera();
        if (touchpad.isTouched()) {
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
        if (touchpad.isTouched())
            Gdx.graphics.requestRendering();
        if (drawFuncWhenTouched || !touchpad.isTouched()) {
            shapeRenderer.setColor(lineColor);
            for (Map.Entry<String, Set<Polyfit.Point2>> entry : funcs.entrySet()) {
                for (Polyfit.Point2 point : entry.getValue()) {
                    shapeRenderer.circle((float) point.x.doubleValue(), (float) point.y.doubleValue(), 1);
                }
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

    public Set<Polyfit.Point2> cacheValue(final String func) {
        dialogCaching = GDXDialogsSystem.getDialogManager().newDialog(GDXProgressDialog.class);
        dialogCaching.setTitle("Generating cache");
        dialogCaching.setMessage("Please wait for a while,\nmaybe you can look up to the sky...");
        dialogCaching.build();
        dialogCaching.show();
        int n = Math.min(Gdx.graphics.getWidth(), nCachedPointMax);
        if (n > 0) {
            final Set<Polyfit.Point2> values = new Synchronized.SynchronizedSet<Polyfit.Point2>(new LinkedHashSet<Polyfit.Point2>(n));
            double deltaX = Gdx.graphics.getWidth() / n;
            Vector3 position = innerStage.getViewport().getCamera().position;
            final double left = position.x - Gdx.graphics.getWidth() / 2;
            final double right = position.x + Gdx.graphics.getWidth() / 2;
            final double down = position.y - Gdx.graphics.getHeight() / 2;
            final double up = position.y + Gdx.graphics.getHeight() / 2;
            // Gdx.app.debug("CacheValue", "left=" + left + ", right=" + right + ", down=" + down + ", up=" + up);
            final Object lock = new Object();
            for (int i = -n / 2; i <= n / 2; i++) {
                double x = position.x + deltaX * i;
                toCacheList.add(new OrdinaryEntry<Double, String>(x, func));
            }
            Runnable cacheRunnable = new Runnable() {
                @Override
                public void run() {
                    try {
                        while (true) {
                            OrdinaryEntry<Double, String> entry;
                            synchronized (lock) {
                                if (!toCacheList.isEmpty()) {
                                    entry = toCacheList.pop();
                                } else return;
                            }
                            double x = entry.key;
                            String expression = entry.value.replace("x", "" + x);
                            try {
                                double y = Polyfit.parseSpecialFuncs(expression).floatValue();
                                if (y > down && y < up) {
                                    values.add(new Polyfit.Point2(new BigDecimal(x, OperationNode.mathContext), new BigDecimal(y, OperationNode.mathContext)));
                                }
                            } catch (Throwable re) {
                                Gdx.app.debug("CacheValue", "Failed to cache " + func + ", x=" + x + "\n", re);
                            }

                        }
                    } catch (Throwable t) {
                        Gdx.app.debug("CacheValue", "Failed", t);
                    }
                }
            };
            for (int j = 0; j < runnablePoster.properRunnableNumber(); j++)
                runnablePoster.post(cacheRunnable);
            while (!toCacheList.isEmpty()) ; // Wait until finished caching.
            dialogCaching.dismiss();
            return values;
        }
        Gdx.app.error("Caching points", "Invalid amount " + n);
        dialogCaching.dismiss();
        System.gc(); // In order that heap will not be too large.
        return new LinkedHashSet<Polyfit.Point2>(0);
    }

    public void recache() {
        for (String func : funcs.keySet()) {
            funcs.remove(func).clear(); // Avoid memory leak ???
            funcs.put(func, cacheValue(func));
        }
    }
}
