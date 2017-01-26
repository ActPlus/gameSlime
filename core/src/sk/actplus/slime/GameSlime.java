package sk.actplus.slime;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import sk.actplus.slime.screens.PlayScreen;

public class GameSlime extends ApplicationAdapter {
	SpriteBatch batch;
	Texture img;
  

	
	@Override
	public void create () {
		batch = new SpriteBatch();
		setScreen(new PlayScreen(batch))
	}

	@Override
	public void render () {
		clearScreen();
		super.render();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
	}

	public void clearScreen() {
		Gdx.gl.glClearColor(0,0,0,1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
	}
}