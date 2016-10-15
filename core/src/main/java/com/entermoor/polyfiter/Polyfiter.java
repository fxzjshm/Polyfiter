package com.entermoor.polyfiter;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.entermoor.polyfiter.action.MoveCameraToAction;
import com.kotcrab.vis.ui.VisUI;

import net.hakugyokurou.fds.parser.MathExpressionParser;

import java.io.StringReader;
import java.util.LinkedHashSet;
import java.util.Set;

import de.tomgrill.gdxdialogs.core.GDXDialogsSystem;

public class Polyfiter extends ApplicationAdapter {
    public Set<Vector2> points = new LinkedHashSet<Vector2>();
    public Set<String> funcs = new LinkedHashSet<String>(1);
    public Set<Set<Vector2>> values = new LinkedHashSet<Set<Vector2>>();

    public SpriteBatch batch;
    public InputProcessor inputProcessor;

    public Stage stage;
    public Viewport viewport;
    public Touchpad touchpad;

    public Stage innerStage;
    public Viewport innerViewport;
    public OrthographicCamera innerCamera;
    public Image img;
    public ShapeRenderer shapeRenderer;

    public Color pointColor, xColor, yColor, lineColor;
    public float scaleDelta = 1;
//    public float dScaleDelta = 0.15F;

    public long padLastTouchTime = -1;

    public static float dScaleDelta(float scaleDelta) {
        return (float) 23.3 / scaleDelta;
    }

    /**
     * /        sigma(i = 1 -> n)(xi*yi) - n*avgX*avgY
     * | k = -------------------------------------------- ;
     * \          sigma(i = 1 -> n)(xi^2) - n*avgX^2
     * b = avgY - k * avgX;
     * ==> f(x) = kx+b;
     *
     * @param points Given points
     * @return "k*x+b"
     */
    public static String polyfit1(Set<Vector2> points) {
        int n = points.size();
        float sigmaX = 0;
        float sigmaY = 0;
        float sigmaXY = 0;
        float sigmaX2 = 0;

        for (Vector2 point : points) {
            sigmaX += point.x;
            sigmaY += point.y;
            sigmaXY += point.x * point.y;
            sigmaX2 += point.x * point.x;
        }

        float avgX = sigmaX / n;
        float avgY = sigmaY / n;

        float k = (sigmaXY - n * avgX * avgY) / (sigmaX2 - n * avgX * avgX);
        float b = avgY - k * avgX;

        return "(" + k + ")*(x)+(" + b + ")";
    }

    @Override
    public void create() {
        Gdx.app.setLogLevel(Application.LOG_DEBUG);
//        Gdx.app.debug("Working Directory", Gdx.files.internal("").file().getAbsolutePath());

        Gdx.graphics.setWindowedMode(Gdx.graphics.getDisplayMode().width, Gdx.graphics.getDisplayMode().height);

        batch = new SpriteBatch();
//		img = new Texture("badlogic.jpg");
        viewport = new ScreenViewport();
        stage = new Stage(viewport, batch);
        stage.setDebugAll(true);

        innerCamera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight() / 5 * 3);
        innerViewport = new ScreenViewport(innerCamera);
        innerStage = new Stage(innerViewport, batch);
        innerStage.setDebugAll(true);
        shapeRenderer = new ShapeRenderer();

        //Avoid exception on hot swap.
        if (VisUI.isLoaded())
            VisUI.dispose();
        VisUI.load();

        try {
            GDXDialogsSystem.getDialogManager();
        } catch (NullPointerException e) {
            GDXDialogsSystem.install();
        }

        img = new Image(new TextureRegionDrawable(new TextureRegion(new Texture("badlogic.jpg"))));
        innerStage.addActor(img);

        touchpad = new Touchpad(Math.min(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()) / 5, VisUI.getSkin());
        touchpad.getColor().a *= 0.233;
        stage.addActor(touchpad);

        inputProcessor = new InputMultiplexer(stage, innerStage);
        Gdx.input.setInputProcessor(inputProcessor);

        pointColor = new Color(0, 1, 0, 1);
        xColor = new Color(1, 1, 0, 1);
        yColor = new Color(0, 1, 1, 1);
        lineColor = new Color(1, 1, 1, 1);

        innerCamera.position.x = 0;
        innerCamera.position.y = 0;

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
                                MoveCameraToAction action = new MoveCameraToAction((innerCamera), 0.666666F);
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
        points.add(new Vector2(0, 0));
        points.add(new Vector2(1, 1));
        points.add(new Vector2(5, 5));
        String func = polyfit1(points);
        funcs.add(func);
        cacheValue(func);
    }

    @Override
    public void resize(int width, int height) {
        Gdx.app.debug("Resize", "Width: " + width + ", Height: " + height + ".");
        viewport.update(width, height);
//        touchpad.setDeadzone(0);
        touchpad.setBounds(Math.min(width, height) / 11, Math.min(width, height) / 11, Math.min(width, height) / 7 * 2, Math.min(width, height) / 7 * 2);
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (touchpad.isTouched()) {
            // Gdx.app.debug("Touchpad", touchpad.getKnobPercentX() + ", " + touchpad.getKnobPercentY());
            float deltaX = touchpad.getKnobPercentX();
            float deltaY = touchpad.getKnobPercentY();
            scaleDelta += Math.abs((deltaX + deltaY)) / 20;
            innerCamera.translate(deltaX * scaleDelta, deltaY * scaleDelta);
        } else if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            scaleDelta += dScaleDelta(scaleDelta);
            innerCamera.translate(-1 * scaleDelta, 0);
        } else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            scaleDelta += dScaleDelta(scaleDelta);
            innerCamera.translate(1 * scaleDelta, 0);
        } else if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            scaleDelta += dScaleDelta(scaleDelta);
            innerCamera.translate(0, 1 * scaleDelta);
        } else if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            scaleDelta += dScaleDelta(scaleDelta);
            innerCamera.translate(0, -1 * scaleDelta);
        } else {
            scaleDelta = 1;
        }

        stage.act();

        batch.begin();
//		batch.draw(img, 0, 0);
        batch.end();

        innerCamera.update();
        shapeRenderer.setProjectionMatrix(innerCamera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(yColor);
        if (innerCamera.position.x - Gdx.graphics.getWidth() / 2 < 0 && innerCamera.position.x + Gdx.graphics.getWidth() / 2 > 0) {
            shapeRenderer.line(0, innerCamera.position.y - Gdx.graphics.getHeight() / 2, 0, innerCamera.position.y + Gdx.graphics.getHeight() / 2);
        }
        shapeRenderer.setColor(xColor);
        if (innerCamera.position.y - Gdx.graphics.getHeight() / 2 < 0 && innerCamera.position.y + Gdx.graphics.getHeight() / 2 > 0) {
            shapeRenderer.line(innerCamera.position.x - Gdx.graphics.getWidth() / 2, 0, innerCamera.position.x + Gdx.graphics.getWidth() / 2, 0);
        }
        shapeRenderer.end();
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(pointColor);
        shapeRenderer.circle(0, 0, 6.66F);
        for (Vector2 point : points) {
            shapeRenderer.circle(point.x, point.y, 2.33F);
        }
        shapeRenderer.setColor(lineColor);
        for (Set<Vector2> set : values) {
            for (Vector2 point : set) {
                shapeRenderer.circle(point.x, point.y, 2.33F);
            }
        }
//        shapeRenderer.circle(innerCamera.position.x - Gdx.graphics.getWidth() / 2, 0, 10);
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

    public void cacheValue(String func) {
        int n = Math.min(viewport.getScreenWidth(), 1000);
        Set<Vector2> values = new LinkedHashSet<Vector2>(n);
        double deltaX = Gdx.graphics.getWidth() / n;
        for (int i = 0; i <= n; i++) {
            float x = (float) (innerCamera.position.x - Gdx.graphics.getWidth() / 2 + deltaX * i * 2);
            String expression = func.replace("x", "" + x);
            float y = (float) MathExpressionParser.parseLine(new StringReader(expression)).eval();
            values.add(new Vector2(x, y));
        }
        this.values.add(values);
    }

}
