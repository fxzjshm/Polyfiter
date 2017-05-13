package com.entermoor.polyfiter.dragome.launcher;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.dragome.DragomeApplication;
import com.badlogic.gdx.backends.dragome.DragomeApplicationConfiguration;
import com.badlogic.gdx.backends.dragome.DragomeWindow;
import com.badlogic.gdx.backends.dragome.preloader.AssetDownloader.AssetLoaderListener;
import com.badlogic.gdx.backends.dragome.preloader.AssetType;
import com.dragome.web.annotations.PageAlias;
import com.entermoor.polyfiter.Polyfiter;


@PageAlias(alias= "Polyfiter")
public class DragomeLauncher extends DragomeApplication
{

	@Override
	public ApplicationListener createApplicationListener()
	{
		AssetLoaderListener listener = new AssetLoaderListener<Object>();

		getPreloader().loadAsset("visui/default.fnt", AssetType.Text, null, listener);
		getPreloader().loadAsset("visui/font-small.fnt", AssetType.Text, null, listener);
		getPreloader().loadAsset("visui/uiskin.atlas", AssetType.Text, null, listener);
		getPreloader().loadAsset("visui/uiskin.json", AssetType.Text, null, listener);
		
		getPreloader().loadAsset("visui/uiskin.png", AssetType.Image, null, listener);
		
		return new Polyfiter();
	}

	@Override
	public DragomeApplicationConfiguration getConfig()
	{
		return null;
	}

	@Override
	protected void onResize()
	{
		int clientWidth= DragomeWindow.getInnerWidth();
		int clientHeight= DragomeWindow.getInnerHeight();
		getCanvas().setWidth(clientWidth);
		getCanvas().setHeight(clientHeight);
		getCanvas().setCoordinateSpaceWidth(clientWidth);
		getCanvas().setCoordinateSpaceHeight(clientHeight);
		super.onResize();
	}
}
