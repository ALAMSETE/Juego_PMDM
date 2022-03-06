package com.juegoantonioluis.screens;

import static com.juegoantonioluis.extraclases.Utilidades.ALTO_MUNDO;
import static com.juegoantonioluis.extraclases.Utilidades.ANCHO_MUNDO;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.juegoantonioluis.Main;

public class ScreenYouWin extends ScreenBase{
    // Creamos el objeto que actuara como imagen de fondo
    private Image background;
    // Creamos el objeto sera una imagen del podio
    private Image podio;
    // Creamos el objeto sera la imagen de You Win
    private Image youWin;
    //Creamos el mundo, encargado de controlar el apartado grafico
    private World world;
    // Declaramos la camara del mundo
    private OrthographicCamera camaraDeMundo;
    // Creamos el "stage", que se encargara de el apartado fisico
    private Stage stage;
    // Creamos ademas un objeto music para añadir una cancion para que suene de fondo
    private Music winSong;

    public ScreenYouWin(Main main) {
        super(main);
        //Nos creamos el mundo indicando donde queremos posicionarlo, y el dosleep a true para deshabilitar las fisicas cuando no se esten usando
        this.world = new World(new Vector2(0,-10),true);
        // Nos creamos el fitViewport que sera el encargado de ajustar el mundo al ancho y alto de la pantalla
        FitViewport fitViewport = new FitViewport(ANCHO_MUNDO, ALTO_MUNDO);
        // Le pasamos al stage el fitViewPort
        this.stage = new Stage(fitViewport);
        // Inicializamos la camara del mundo
        this.camaraDeMundo = (OrthographicCamera) this.stage.getCamera();
        // Inicializamos tambien la cancion de fondo
        this.winSong = main.assetsManager.getCancionWin();
    }
    //Con el metodo show indicamos que queremos que nos muestre por pantalla
    @Override
    public void show() {
        super.show();
        //En este caso le decimos que nos muestre los siguientes metodos que nos hemos creado
        addBackground();
        addPodio();
        addYouWin();
        //Ajustamos la cancion de fondo para que no suene en bucle
        this.winSong.setLooping(false);
        //Reproducimos la cancion de fondo
        this.winSong.play();
    }
    //Metodo que creamos para añadir el fondo
    public void addBackground(){
        //Iniciamos la variable background con la Imagen que recogemos desde el metodo de AssetsManager
        this.background =new Image(main.assetsManager.getBackground());
        // Indicamos la posicion de la imagen del fondo
        this.background.setPosition(0f,0f);
        // Indicamos tambien el tamaño que queremos que adopte la imagen, en este caso, el ancho y alto de la pantalla
        this.background.setSize(ANCHO_MUNDO, ALTO_MUNDO);
        // Añadimos al stage el actor(la variable background)
        this.stage.addActor(this.background);
    }
    //Metodo para añadir el podio
    public void addPodio(){
        //Le pasamos a la variable podio, la imagen que cogemos desde AssetsManager
        this.podio =new Image(main.assetsManager.getPodium());
        // Declaramos la posicion donde queremos mostrarlo
        this.podio.setPosition(4f,0f);
        // ademas de declarar el tamaño
        this.podio.setSize(2.3f, 2f);
        // y se lo añadimos al stage como actor
        this.stage.addActor(this.podio);
    }
    //Metodo que creamos para añadir el Game Over
    public void addYouWin(){
        //Le pasamos a la variable gameOver, la imagen que cogemos desde AssetsManager
        this.youWin =new Image(main.assetsManager.getYouWin());
        // Declaramos la posicion donde queremos mostrarlo
        this.youWin.setPosition(4f,2f);
        // ademas de declarar el tamaño
        this.youWin.setSize(2f, 1.8f);
        // y se lo añadimos al stage como actor
        this.stage.addActor(this.youWin);
    }
    // Este metodo se ejecutará cuando la aplicacion pase a segundo plano
    @Override
    public void hide() {
        super.hide();
        //le decimos que la musica pare
        this.winSong.stop();
    }
    //Render sera el encargado de dibujar todo.
    @Override
    public void render(float delta) {
        super.render(delta);
        // Limpiamos el buffer de bits para evitar que se haya errores a la hora de mostrar las imagenes
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        //Mandamos a la grafica la proyeccion de la camara del mundo
        this.stage.getBatch().setProjectionMatrix(camaraDeMundo.combined);
        // Le decimos al stage que todos sus actores actuen
        this.stage.act();
        // Declaramos el world.step con los parametros 6 y 2 porque lo dice la documentacion
        this.world.step(delta, 6, 2);
        // Con el metodo .draw() dibujamos el stage, es decir, todos los actores
        this.stage.draw();
        // Le decimos a la camaraDeMundo que actualice(para poder ver las animaciones)
        this.camaraDeMundo.update();
        // Si la pantalla es tocada, se ejecutara la pantalla de GetReady
        if (Gdx.input.justTouched()){
            main.setScreen(main.screenGetReady);
        }
    }
    // Este metodo se ejecutara cuando se cierre la aplicacion
    @Override
    public void dispose() {
        super.dispose();
    }
}
