package com.entermoor.polyfiter;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
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

public class Polyfiter extends ApplicationAdapter {

    public SpriteBatch batch;
    public InputProcessor inputProcessor;
    //	public Texture img;

    public Stage stage;
    public Viewport viewport;
    public Touchpad touchpad;

    public Stage innerStage;
    public Viewport innerViewport;
    public OrthographicCamera innerCamera;
    public Image img;
    public ShapeRenderer shapeRenderer;

    @Override
    public void create() {
        Gdx.app.setLogLevel(Application.LOG_DEBUG);
//        Gdx.app.debug("working directory", Gdx.files.internal("").file().getAbsolutePath());

        batch = new SpriteBatch();
//		img = new Texture("badlogic.jpg");
        viewport = new ScreenViewport();
        stage = new Stage(viewport, batch);


        innerCamera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight() / 5 * 3);
        innerViewport = new ScreenViewport(innerCamera);
        innerStage = new Stage(innerViewport, batch);
        shapeRenderer = new ShapeRenderer();

        VisUI.load();

        img = new Image(new TextureRegionDrawable(new TextureRegion(new Texture("badlogic.jpg"))));
        innerStage.addActor(img);

        touchpad = new Touchpad(Gdx.graphics.getWidth() / 5, VisUI.getSkin());
//        touchpad.setBounds( /* touchpad.getDeadzone() */ Gdx.graphics.getWidth() / 5, Gdx.graphics.getWidth() / 5, touchpad.getWidth(), touchpad.getHeight());
        stage.addActor(touchpad);

        inputProcessor = new InputMultiplexer(stage, innerStage);
        Gdx.input.setInputProcessor(inputProcessor);

    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
        touchpad.setDeadzone(width / 5);
//        touchpad.setBounds(width / 5, width / 5, width / 5 * 2, width / 5 * 2);
        touchpad.setBounds(0, 0, width / 5 * 2, width / 5 * 2);
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (touchpad.isTouched()) {
//            Gdx.app.debug("Touchpad", touchpad.getKnobPercentX() + ", " + touchpad.getKnobPercentY());
            innerCamera.translate(touchpad.getKnobPercentX(), touchpad.getKnobPercentY());
        }

        batch.begin();
//		batch.draw(img, 0, 0);
        batch.end();

        innerCamera.update();
        shapeRenderer.setProjectionMatrix(innerCamera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(1, 1, 0, 1);
// TODO x and y line.
        shapeRenderer.end();
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(0, 1, 0, 1);
//TODO points?
        shapeRenderer.end();

//        innerStage.draw();
        stage.draw();
    }

    @Override
    public void dispose() {
        stage.dispose();
        innerStage.dispose();
        batch.dispose();
//        img.dispose();
        VisUI.dispose();
    }
}
