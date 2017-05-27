package com.entermoor.polyfiter;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
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
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.entermoor.polyfiter.plugin.FunctionCacherPlugin;
import com.entermoor.polyfiter.plugin.IPlugin;
import com.entermoor.polyfiter.plugin.KeyboardStageMoverPlugin;
import com.entermoor.polyfiter.plugin.TouchpadStageMoverPlugin;
import com.entermoor.polyfiter.utils.GwtRunnablePoster;
import com.entermoor.polyfiter.utils.IRunnablePoster;
import com.entermoor.polyfiter.utils.Polyfit;
import com.entermoor.polyfiter.utils.Synchronized;

import net.hakugyokurou.fds.node.OperationNode;

import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ConcurrentModificationException;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import de.tomgrill.gdxdialogs.core.GDXDialogsSystem;
import de.tomgrill.gdxdialogs.core.dialogs.GDXButtonDialog;
import de.tomgrill.gdxdialogs.core.dialogs.GDXTextPrompt;
import de.tomgrill.gdxdialogs.core.listener.TextPromptListener;
import io.github.fxzjshm.gdx.svg2pixmap.Svg2Pixmap;

public class Polyfiter extends ApplicationAdapter {

    public static final String VERSION = "0.0.1"; //TODO check this before release.
    public static Polyfiter instance;

    public Set<Polyfit.Point2> points = new Synchronized.SynchronizedSet<Polyfit.Point2>(new LinkedHashSet<Polyfit.Point2>());
    public Set<Runnable> resizeToDo = new LinkedHashSet<Runnable>();
    public Set<IPlugin> plugins = new HashSet<IPlugin>(4);

    public IRunnablePoster runnablePoster;

    public SpriteBatch batch;
    public InputMultiplexer inputProcessor;
    public Skin skin;

    public Stage stage;

    public Stage innerStage;
    public Image img;
    public ShapeRenderer shapeRenderer;
    public Button buttonAdd;
    public Button buttonRefresh;
    public GDXTextPrompt dialogAdd;

    public Color pointColor, xColor, yColor, lineColor;

    public Polyfiter() {
        this(new GwtRunnablePoster());
    }

    public Polyfiter(IRunnablePoster poster) {
        runnablePoster = poster;
        instance = this;
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
        // TODO Delete this when releasing
        Gdx.app.setLogLevel(Application.LOG_DEBUG);
        Gdx.graphics.setWindowedMode((int) (Gdx.graphics.getDisplayMode().width * 0.9), (int) (Gdx.graphics.getDisplayMode().height * 0.8));

        plugins.add(TouchpadStageMoverPlugin.inst);
        plugins.add(KeyboardStageMoverPlugin.inst);
        plugins.add(FunctionCacherPlugin.inst);

        batch = new SpriteBatch();
        stage = new Stage(new ScreenViewport(), batch);
        // TODO Delete this when releasing
        stage.setDebugAll(true);
        resizeToDo.add(new Runnable() {
            @Override
            public void run() {
                inputProcessor.removeProcessor(stage);
                Stage newStage = new Stage(stage.getViewport(), batch);
                for (Array<Actor> actors = stage.getActors(); actors.size > 0; )
                    newStage.addActor(actors.pop());
                stage.dispose();
                // TODO Delete this when releasing
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

        skin = new Skin(Gdx.files.internal("visui/uiskin.json"));
        //Avoid exception on hot swap.
        /*
        if (VisUI.isLoaded())
            VisUI.dispose();
        VisUI.load(Gdx.files.internal("visui/uiskin.json"));
        */
        GDXDialogsSystem.install();

        // img = new Image(new TextureRegionDrawable(new TextureRegion(new Texture("badlogic.jpg"))));
        // innerStage.addActor(img);

        final int size = Math.min(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()) / 10;
        Pixmap buttonAddUp = Svg2Pixmap.path2Pixmap(32, 32, "M16 2 L16 30 M2 16 L30 16", null, Color.WHITE, 2, new Pixmap(size, size, Pixmap.Format.RGBA4444)); //new Pixmap(Gdx.files.internal("plus.png"));
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
                buttonAdd.setBounds(Gdx.graphics.getWidth() - size, Gdx.graphics.getHeight() - size, size, size);
            }
        });
        Pixmap buttonRefreshUp = Svg2Pixmap.path2Pixmap(32, 32, "M29 16 C29 22 24 29 16 29 8 29 3 22 3 16 3 10 8 3 16 3 21 3 25 6 27 9 M20 10 L27 9 28 2", null, Color.WHITE, 2, new Pixmap(size, size, Pixmap.Format.RGBA4444));// new Pixmap(Gdx.files.internal("reload.png"));
        Pixmap buttonRefreshDown = filter(buttonRefreshUp, 0.233F, 0.233F, 0.666F, 1);
        buttonRefresh = new Button(drawable(buttonRefreshUp), drawable(buttonRefreshDown));
        buttonRefresh.addListener(new EventListener() {
            @Override
            public boolean handle(Event event) {
                if (event instanceof InputEvent && ((InputEvent) event).getType().equals(InputEvent.Type.touchDown))
                    FunctionCacherPlugin.inst.recache();
                return false;
            }
        });
        stage.addActor(buttonRefresh);
        resizeToDo.add(new Runnable() {
            @Override
            public void run() {
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

        dialogAdd = GDXDialogsSystem.getDialogManager().newDialog(GDXTextPrompt.class);
        dialogAdd.setTitle("Add a point (x,y) or a function");
        dialogAdd.setConfirmButtonLabel("OK");
        dialogAdd.setCancelButtonLabel("Cancel");
        dialogAdd.setTextPromptListener(new TextPromptListener() {
            String lastFunc;

            @Override
            public void cancel() {
            }

            @Override
            public void confirm(String text) {
                if (text.contains(",") || text.contains("，")) {
                    try {
                        points.add(new Polyfit.Point2(text));
                        if (lastFunc != null) FunctionCacherPlugin.inst.funcs.remove(lastFunc);
                        lastFunc = Polyfit.polyfit1(points);
                        FunctionCacherPlugin.inst.funcs.put(lastFunc, FunctionCacherPlugin.inst.cacheValue(lastFunc));
                    } catch (IllegalArgumentException iae) {
                        dialogAdd.dismiss();
                        GDXDialogsSystem.getDialogManager().newDialog(GDXButtonDialog.class).setTitle("Error").setMessage("Unable to understand: " + text).addButton("OK").build().show();
                    } catch (ArithmeticException ignored) {
                    }
                } else {
                    FunctionCacherPlugin.inst.funcs.put(text, FunctionCacherPlugin.inst.cacheValue(text));
                }
            }
        });
        dialogAdd.build();
        // funcs.put("1000/x", cacheValue("1000/x"));

        Gdx.graphics.setContinuousRendering(false);

        for (IPlugin plugin : plugins)
            plugin.create();
    }

    @Override
    public void resize(int width, int height) {
        // Gdx.app.debug("Resize", "Width: " + width + ", Height: " + height + ".");
        for (Runnable runnable : resizeToDo) {
            runnable.run();
        }
        for (IPlugin plugin : plugins)
            plugin.resize(width, height);
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        Camera innerCamera = innerStage.getViewport().getCamera();

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
        if (TouchpadStageMoverPlugin.inst.drawFuncWhenTouched || !TouchpadStageMoverPlugin.inst.touchpad.isTouched()) {
            shapeRenderer.setColor(lineColor);
            try {
                for (Map.Entry<String, Set<Polyfit.Point2>> entry : FunctionCacherPlugin.inst.funcs.entrySet()) {
                    for (Polyfit.Point2 point : entry.getValue()) {
                        shapeRenderer.circle((float) point.x.doubleValue(), (float) point.y.doubleValue(), 1);
                    }
                }
            } catch (ConcurrentModificationException ignored) {
            }
        }
        shapeRenderer.end();
        for (IPlugin plugin : plugins)
            plugin.render();
        stage.draw();
    }

    @Override
    public void dispose() {
        for (IPlugin plugin : plugins)
            plugin.dispose();
        shapeRenderer.dispose();
        stage.dispose();
        innerStage.dispose();
        batch.dispose();
//        img.dispose();
        // VisUI.dispose();
        skin.dispose();
    }

    @Override
    public void pause() {
        for (IPlugin plugin : plugins)
            plugin.pause();
    }

    @Override
    public void resume() {
        for (IPlugin plugin : plugins)
            plugin.resume();
    }
}
