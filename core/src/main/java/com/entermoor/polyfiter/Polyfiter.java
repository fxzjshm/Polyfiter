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
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.kotcrab.vis.ui.VisUI;

import java.util.List;

import net.hakugyokurou.fds.parser.MathExpressionParser;

public class Polyfiter extends ApplicationAdapter {
    public static List<Point> points;

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

    public Color pointColor, xColor, yColor;
    public float scaleDeltaXY = 1;

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

        VisUI.load();

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

        innerCamera.position.x = 0;
        innerCamera.position.y = 0;
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
        touchpad.setDeadzone(width / 5);
        touchpad.setBounds(Math.min(width, height) / 21, Math.min(width, height) / 21, Math.min(width, height) / 7 * 2, Math.min(width, height) / 7 * 2);
        Gdx.app.debug("Resize", "Width: " + width + ", Height: " + height + ".");
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (touchpad.isTouched()) {
            // Gdx.app.debug("Touchpad", touchpad.getKnobPercentX() + ", " + touchpad.getKnobPercentY());
            float deltaX = touchpad.getKnobPercentX();
            float deltaY = touchpad.getKnobPercentY();
            scaleDeltaXY += Math.abs((deltaX + deltaY)) / 20;
            innerCamera.translate(deltaX * scaleDeltaXY, deltaY * scaleDeltaXY);
        } else if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            scaleDeltaXY += 0.1;
            innerCamera.translate(-1 * scaleDeltaXY, 0);
        } else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            scaleDeltaXY += 0.1;
            innerCamera.translate(1 * scaleDeltaXY, 0);
        } else if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            scaleDeltaXY += 0.1;
            innerCamera.translate(0, 1 * scaleDeltaXY);
        } else if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            scaleDeltaXY += 0.1;
            innerCamera.translate(0, -1 * scaleDeltaXY);
        } else {
            scaleDeltaXY = 1;
        }

        batch.begin();
//		batch.draw(img, 0, 0);
        batch.end();

        innerCamera.update();
        shapeRenderer.setProjectionMatrix(innerCamera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(xColor);
        if (innerCamera.position.x - Gdx.graphics.getWidth() / 2 < 0 && innerCamera.position.x + Gdx.graphics.getWidth() / 2 > 0) {
            shapeRenderer.line(0, innerCamera.position.y - Gdx.graphics.getHeight() / 2, 0, innerCamera.position.y + Gdx.graphics.getHeight() / 2);
        }
        shapeRenderer.setColor(yColor);
        if (innerCamera.position.y - Gdx.graphics.getHeight() / 2 < 0 && innerCamera.position.y + Gdx.graphics.getHeight() / 2 > 0) {
            shapeRenderer.line(innerCamera.position.x - Gdx.graphics.getWidth() / 2, 0, innerCamera.position.x + Gdx.graphics.getWidth() / 2, 0);
        }
        shapeRenderer.end();
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(pointColor);
        shapeRenderer.circle(0, 0, 2.33F);
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

    public static class Point {
        public double x, y = 0;

        public Point(double x, double y) {
            this.x = x;
            this.y = y;
        }
    }
}
