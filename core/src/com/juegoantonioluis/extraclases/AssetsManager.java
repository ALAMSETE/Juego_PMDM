package com.juegoantonioluis.extraclases;

import static com.juegoantonioluis.extraclases.Utilidades.*;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

//ESTA CLASE SE ENCARGA DE CARGAR TODOS LOS RECURSOS NECESARIOS PARA EL JUEGO

public class AssetsManager extends AssetManager {

    private AssetManager assetManager;
    private TextureAtlas textureAtlas;

    //El constructor se encargara de leer tanto el atlas, como la musica o sonidos
    public AssetsManager() {
        this.assetManager = new AssetManager();
        //Cargamos el atlas
        assetManager.load(MAPEADO_ATLAS, TextureAtlas.class);
        //Cargamos todas las canciones
        assetManager.load(MUSICA_BG, Music.class);
        assetManager.load(WIN_SONG, Music.class);
        assetManager.load(LOOSE_SONG, Music.class);
        // Una vez cargado to, le decimos que deje de cargar
        assetManager.finishLoading();
        //Al textureAtlas le pasamos el atlas que hemos cargado anteriormente
        textureAtlas = assetManager.get(MAPEADO_ATLAS);
    }
    //Creamos un metodo que nos devolvera un TextureRegion de la imagen de fondo
    public TextureRegion getBackground() {
        return this.textureAtlas.findRegion(BACKGROUND_IMAGE);
    }
    // Metodo que se encarga de devolver la animacion del coche(actor)
    public Animation<TextureRegion> getCarAnimation() {
        return new Animation<TextureRegion>(0.20f, //Tiempo que acumula el delta
                textureAtlas.findRegion("coche1"),
                textureAtlas.findRegion("coche2"),
                textureAtlas.findRegion("coche3"),
                textureAtlas.findRegion("coche4")
                //tantos findRegion por foto que tengas
        );
    }
    //Metodo que se encarga de devolver la animacion del coche(NPC)
    public Animation<TextureRegion> getNPCAnimation() {
        return new Animation<TextureRegion>(0.20f, //Tiempo que acumula el delta
                textureAtlas.findRegion("cocher1"),
                textureAtlas.findRegion("cocher2"),
                textureAtlas.findRegion("cocher3"),
                textureAtlas.findRegion("cocher4")
                //tantos findRegion por foto que tengas
        );
    }
    //para calcular la tasa de refresco es cogiendo el num de fotos que tienes dividiendo 1 --> tu tienes 4 fotos = 1/4 y te sale la duracion
    // que tiene que durar cada foto(0.25), es decir que no le puedes pasar delta
    //Metodo que se encarga de devolver la cancion de fondo del ScreenJuego
    public Music getCancionFondo(){
        return this.assetManager.get(MUSICA_BG);
    }
    //Metodo que se encarga de devolver la cancion de fondo del ScreenYouWin
    public Music getCancionWin(){
        return this.assetManager.get(WIN_SONG);
    }
    //Metodo que se encarga de devolver la cancion de fondo del ScreenGOUser
    public Music getCancionLoose(){
        return this.assetManager.get(LOOSE_SONG);
    }
    // Este metodo se encargará de devolver la fuente personalizada
    public BitmapFont getFuente(){
        return new BitmapFont(Gdx.files.internal(FNT_FUENTE), Gdx.files.internal(PNG_FUENTE), false);
    }
    //Creamos un metodo que nos devolvera un TextureRegion de la imagen de fondo del Game Over
    public TextureRegion getGOBackground(){
        return this.textureAtlas.findRegion(GAMEOVER_BG);
    }
    //Creamos un metodo que nos devolvera un TextureRegion de la imagen del coche verde roto
    public TextureRegion getGreenCar(){
        return this.textureAtlas.findRegion(GO_GREEN);
    }
    //Creamos un metodo que nos devolvera un TextureRegion de la imagen del coche rojo roto
    public TextureRegion getRedCar(){
        return this.textureAtlas.findRegion(GO_RED);
    }
    //Creamos un metodo que nos devolvera un TextureRegion de la imagen del Game Over
    public TextureRegion getGameOver(){
        return this.textureAtlas.findRegion(GAMEOVER);
    }
    //Creamos un metodo que nos devolvera un TextureRegion de la imagen del You Win
    public TextureRegion getYouWin(){
        return this.textureAtlas.findRegion(YOUWIN);
    }
    //Creamos un metodo que nos devolvera un TextureRegion de la imagen del podio
    public TextureRegion getPodium(){
        return this.textureAtlas.findRegion(PODIO);
    }
    //Creamos un metodo que nos devolvera un TextureRegion de la imagen de la mano
    public TextureRegion getTouch(){
        return this.textureAtlas.findRegion(TOUCH);
    }

}
