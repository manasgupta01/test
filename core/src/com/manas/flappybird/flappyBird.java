package com.manas.flappybird;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;

import java.util.Random;

public class flappyBird extends ApplicationAdapter {
	SpriteBatch batch;
	Texture background;
	Texture message;
	Texture easy;

	 Texture myTexture;
	 TextureRegion myTextureRegion;
	 TextureRegionDrawable myTexRegionDrawable;
	ImageButton button;

	Preferences prefs ;

	Texture gameover;
	//ShapeRenderer shapeRenderer;
	int score = 0;
	int highScore ;
	int scoringTube = 0;
	BitmapFont font ;
	BitmapFont hscore;



	Texture topTube;
	Texture bottomTube;
	float gap = 600;
	float maxTubeOffSet ;
	Random randomGenerator;

	float tubeVelocity = 12;

	int numberOfTubes= 4;
	float[] tubeX = new float[numberOfTubes];
	float[] tubeOffSet = new float[numberOfTubes];
	float distanceBTubes;
	Rectangle[] topTubeR ;
	Rectangle[] bottomTubeR;



	Texture[] birds;
	int flapState =0;
	float birdY = 0;
	float velocity = 0 ;
	Circle birdCircle ;

	float gravity = 2;

	int gameState = 0;


	private OrthographicCamera camera;
	private FitViewport viewport;
	private Stage stage;



	@Override
	public void resize(int width, int height) {
		stage.getViewport().update(width, height);
	}





	@Override
	public void create () {
		 myTexture = new Texture(Gdx.files.internal("download.png"));
		 myTextureRegion = new TextureRegion(myTexture);
	 myTexRegionDrawable = new TextureRegionDrawable(myTextureRegion);
		 button = new ImageButton(myTexRegionDrawable); //Set the button up


		camera = new OrthographicCamera();
		viewport = new FitViewport(200, 400, camera);
		stage = new Stage(viewport);

		stage.addActor(button);




		batch = new SpriteBatch();
		//shapeRenderer = new ShapeRenderer();
		birdCircle = new Circle();
		prefs = Gdx.app.getPreferences("highScore");
		highScore = prefs.getInteger("highScore");


		font = new BitmapFont();
		font . setColor(Color.WHITE);
		font.getData().setScale(10);

		hscore = new BitmapFont();
		hscore.setColor(Color.RED);
		hscore.getData().setScale(10);

		background=new Texture("background-day.png");

		message = new Texture("message.png");
		easy = new Texture("download.png");

		gameover = new Texture("gameover.png");
		birds= new Texture[2];

		birds[1]=new Texture("yellowbird-downflap.png");
		birds[0]=new Texture("yellowbird-upflap.png");



		topTube = new Texture("topTube.png");
		bottomTube = new Texture("bottomTube.png");
		maxTubeOffSet = Gdx.graphics.getHeight()/ 2 -gap/2 -200;
		randomGenerator = new Random();
		distanceBTubes = Gdx.graphics.getWidth() * 3/4;

		topTubeR = new Rectangle[numberOfTubes];
		bottomTubeR = new Rectangle[numberOfTubes];

		startGame();


	}


	public void startGame(){

		birdY = Gdx.graphics.getHeight()/2 - birds[flapState].getHeight()/2 - 55;

		for(int i=0;i<numberOfTubes;i++) {

			tubeOffSet[i] = (randomGenerator.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() / 2 - gap / 2 - 200);
			tubeX[i] = Gdx.graphics.getWidth() / 2 - topTube.getWidth() / 2 + Gdx.graphics.getWidth() + i * distanceBTubes;

			topTubeR[i] = new Rectangle();
			bottomTubeR[i]= new Rectangle();
		}
	}


	@Override
	public void render () {

		batch.begin();
		batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

		if(gameState== 1) {
			if(tubeX[scoringTube] < Gdx.graphics.getWidth()/2){
				score ++;
				if(score > highScore){
					highScore=score;
					prefs.putInteger("highScore", highScore);
					prefs.flush();
				}


				Gdx.app.log("score","your score is " + score);
				if(scoringTube<numberOfTubes-1){
					scoringTube++;
				}
				else {
					scoringTube=0;
				}
			}
			if(Gdx.input.justTouched()){

				velocity = -30;

			}



			for(int i=0;i<numberOfTubes;i++) {

				if(tubeX[i]< - topTube.getHeight()){
					tubeX[i]+=numberOfTubes*distanceBTubes;
					tubeOffSet[i] = (randomGenerator.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() / 2 - gap / 2 - 100);
				}
				else {
					tubeX[i] = tubeX[i]-tubeVelocity;

				}

				batch.draw(topTube, tubeX[i], Gdx.graphics.getHeight() / 2 + gap / 2 - tubeOffSet[i], topTube.getWidth() + 180, topTube.getHeight() + 800);
				batch.draw(bottomTube, tubeX[i], 0, bottomTube.getWidth() + 180, bottomTube.getHeight() + 800 - gap / 2 - tubeOffSet[i]);

				topTubeR[i] = new Rectangle(tubeX[i], Gdx.graphics.getHeight() / 2 + gap / 2 - tubeOffSet[i],topTube.getWidth() + 180, topTube.getHeight() + 800);
				bottomTubeR[i] = new Rectangle(tubeX[i], 0, bottomTube.getWidth() + 180, bottomTube.getHeight() + 800 - gap / 2 - tubeOffSet[i]);
			}





				if(birdY > 0) {
				velocity = velocity + gravity;
				birdY -= velocity;
			}
				else {
					gameState = 2;
				}
		}
		else if(gameState==0){

			batch.draw(message,Gdx.graphics.getWidth()/2 - message.getWidth()/2 - 300, Gdx.graphics.getHeight()/2 - message.getHeight()/2 - 140,message.getWidth()+600,message.getHeight()+600);
		//	batch.draw(easy,Gdx.graphics.getWidth()/2 - easy.getWidth()/2 - 300, Gdx.graphics.getHeight()/2 - easy.getHeight()/2  -600,easy.getWidth()+100,easy.getHeight()+100);








			if(Gdx.input.justTouched()){

				gameState = 1;
			}
		}
		else if(gameState==2){
			batch.draw(gameover,Gdx.graphics.getWidth() / 2 - gameover.getWidth()/2 - 250,Gdx.graphics.getHeight()/2-gameover.getHeight()/2 - 100,gameover.getWidth()+500,gameover.getHeight()+200);
			if(Gdx.input.justTouched()){

				gameState = 0;
				startGame();
				score = 0;
				scoringTube = 0;
				velocity = 0;
			}
		}

		if (flapState == 0) {
			flapState = 1;
		} else {
			flapState = 0;
		}


		batch.draw(birds[flapState], Gdx.graphics.getWidth() / 2 - birds[0].getWidth() / 2 - 55, birdY, birds[flapState].getWidth() + 110, birds[flapState].getHeight() + 110);

		font.draw(batch,String.valueOf(score),100,200);
		hscore.draw(batch,String.valueOf(highScore),900,200);
		batch.end();
		birdCircle.set(Gdx.graphics.getWidth()/2, birdY+ birds[flapState].getHeight()/2 + 55,birds[flapState].getWidth()/2 + 55);

		/*shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
		shapeRenderer.setColor(Color.RED);
		shapeRenderer.circle(birdCircle.x,birdCircle.y,birdCircle.radius);*/

		for(int i=0;i<numberOfTubes;i++){



			//shapeRenderer.rect(tubeX[i], Gdx.graphics.getHeight() / 2 + gap / 2 - tubeOffSet[i],topTube.getWidth() + 180, topTube.getHeight() + 800);
			//shapeRenderer.rect(tubeX[i], 0, bottomTube.getWidth() + 180, bottomTube.getHeight() + 800 - gap / 2 - tubeOffSet[i]);


			if(Intersector.overlaps(birdCircle,topTubeR[i]) || Intersector.overlaps(birdCircle,bottomTubeR[i])){
				gameState = 2;
			}
		}



		//shapeRenderer.end();
	}

	
	/*@Override
	public void dispose () {
		batch.dispose();
		img.dispose();
	}*/
}
