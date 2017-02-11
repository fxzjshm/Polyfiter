package com.entermoor.polyfiter;

import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.entermoor.polyfiter.Polyfiter;
import com.entermoor.polyfiter.utils.PooledRunnablePoster;

public class AndroidLauncher extends AndroidApplication {
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		initialize(new Polyfiter(new PooledRunnablePoster()), config);
	}
}
