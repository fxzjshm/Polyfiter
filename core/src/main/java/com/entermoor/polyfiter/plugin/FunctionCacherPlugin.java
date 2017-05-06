package com.entermoor.polyfiter.plugin;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector3;
import com.entermoor.polyfiter.Polyfiter;
import com.entermoor.polyfiter.utils.OrdinaryEntry;
import com.entermoor.polyfiter.utils.Polyfit;
import com.entermoor.polyfiter.utils.Synchronized;

import net.hakugyokurou.fds.node.OperationNode;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import de.tomgrill.gdxdialogs.core.GDXDialogsSystem;
import de.tomgrill.gdxdialogs.core.dialogs.GDXProgressDialog;

import static com.entermoor.polyfiter.Polyfiter.instance;

public class FunctionCacherPlugin implements IPlugin {

    public static FunctionCacherPlugin inst = new FunctionCacherPlugin();

    public Map<String, Set<Polyfit.Point2>> funcs = new Synchronized.SynchronizedMap<String, Set<Polyfit.Point2>>(new LinkedHashMap<String, Set<Polyfit.Point2>>());
    public Stack<OrdinaryEntry<Double, String>> toCacheList = new Stack<OrdinaryEntry<Double, String>>();
    public GDXProgressDialog dialogCaching;

    public int nCachedPointMax = 10000;

    @Override
    public String getName() {
        return "FunctionCacherPlugin";
    }

    @Override
    public String getDescription() {
        return "Generate cache for exist function.";
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
        recache();
    }

    @Override
    public void render() {

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

    public Set<Polyfit.Point2> cacheValue(final String func) {
        dialogCaching = GDXDialogsSystem.getDialogManager().newDialog(GDXProgressDialog.class);
        dialogCaching.setTitle("Generating cache");
        dialogCaching.setMessage("Please wait for a while...");
        dialogCaching.build();
        dialogCaching.show();
        int n = Math.min(Gdx.graphics.getWidth(), nCachedPointMax);
        if (n > 0) {
            final Set<Polyfit.Point2> values = new Synchronized.SynchronizedSet<Polyfit.Point2>(new LinkedHashSet<Polyfit.Point2>(n));
            double deltaX = Gdx.graphics.getWidth() / n;
            Vector3 position = instance.innerStage.getViewport().getCamera().position;
            // final double left = position.x - Gdx.graphics.getWidth() / 2;
            // final double right = position.x + Gdx.graphics.getWidth() / 2;
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
            for (int j = 0; j < instance.runnablePoster.properRunnableNumber(); j++)
                instance.runnablePoster.post(cacheRunnable);
            // while (!toCacheList.isEmpty()) ; // Wait until finished caching.
            dialogCaching.dismiss();
            return values;
        }
        Gdx.app.error("Caching points", "Invalid amount " + n);
        dialogCaching.dismiss();
        System.gc(); // In order that heap will not be too large.
        return new LinkedHashSet<Polyfit.Point2>(0);
    }

    public void recache() {
        String[] ss = new String[funcs.keySet().size()];
        ss = funcs.keySet().toArray(ss);
        for (String func : ss) {
            funcs.remove(func).clear(); // Avoid memory leak ???
            funcs.put(func, cacheValue(func));
        }
    }
}
