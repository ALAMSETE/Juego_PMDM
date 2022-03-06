package com.juegoantonioluis.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.juegoantonioluis.Main;
import com.juegoantonioluis.actores.Coche;
import com.juegoantonioluis.actores.NPC;

import static com.juegoantonioluis.extraclases.Utilidades.*;

//Heredamos esta clase de ScreenBase y le implementamos la clase ContactListener
public class ScreenJuego extends ScreenBase implements ContactListener {
    // Creamos el "stage", que se encargara de el apartado fisico
    private Stage stage;
    // Creamos el objeto que actuara como imagen de fondo
    private Image background;
    //Creamos el mundo, encargado de controlar el apartado grafico
    private World world;
    //Creamos los dos "actores" del juego
    private Coche car;
    private NPC npc;
    //Nos creamos este fixture para que sea el sensor de la linea de meta
    private Fixture meta;
    private Fixture suelo;
    // Creamos ademas un objeto music para añadir una musica de fondo
    private Music cancionFondo;
    // Declaramos la camara del mundo
    private OrthographicCamera camaraDeMundo;
    // Camara que nos permite ver los fixtures de los actores
    private Box2DDebugRenderer camaraDep;
    //Nos creamos una camara y un objeto BitMapFont para trabajar con la fuente que tenemos en la carpeta assets
    private OrthographicCamera camaraFuente;
    private BitmapFont tiempo;

    public ScreenJuego(Main main){
        super(main);

        //Nos creamos el mundo indicando donde queremos posicionarlo, y el dosleep a true para deshabilitar las fisicas cuando no se esten usando
        this.world = new World(new Vector2(0,-10),true);
        this.world.setContactListener(this);
        // Nos creamos el fitViewport que sera el encargado de ajustar el mundo al ancho y alto de la pantalla
        FitViewport fitViewport = new FitViewport(ANCHO_MUNDO, ALTO_MUNDO);
        // Le pasamos al stage el fitViewPort
        this.stage = new Stage(fitViewport);
        // Inicializamos la camara del mundo
        this.camaraDeMundo = (OrthographicCamera) this.stage.getCamera();
        //this.camaraDep = new Box2DDebugRenderer();
        // Inicializamos tambien la cancion de fondo
        this.cancionFondo = main.assetsManager.getCancionFondo();
        // Llamamos al metodo prepararTimer que hemos creado mas abajo para preparar el temporizador
        prepararTimer();
    }

    //Con el metodo show indicamos que queremos que nos muestre por pantalla
    @Override
    public void show() {
        super.show();
        //En este caso le decimos que nos muestre los siguientes metodos que nos hemos creado
        addBackground();
        addMeta();
        addNPC();
        addCoche();
        addSuelo();
        addSecondSuelo();
        //Ajustamos la cancion de fondo para que suene en bucle
        this.cancionFondo.setLooping(true);
        //Reproducimos la cancion de fondo
        this.cancionFondo.play();
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

    //Metodo para añadir el coche principal
    public void addCoche(){
        // En este caso, spriteCoche sera una animacion, que recogemos desde AssetsManager
        Animation<TextureRegion> spriteCoche = main.assetsManager.getCarAnimation();
        // Creamos el coche pasandole como parametros: el mundo, la animacion, y la posicion inicial
        this.car = new Coche(this.world,spriteCoche,new Vector2(1.2f, 0.5f));
        // y lo añadimos como actor
        this.stage.addActor(this.car);
    }
    //Metodo para añadir el coche oponente (el NPC, es decir, la CPU)
    public void addNPC(){
        // En este caso, spriteCoche sera una animacion, que recogemos desde AssetsManager
        Animation<TextureRegion> spriteCoche = main.assetsManager.getNPCAnimation();
        // Creamos el coche pasandole como parametros: el mundo, la animacion, y la posicion inicial
        this.npc = new NPC(this.world,spriteCoche,new Vector2(1.2f, 1.5f));
        // y lo añadimos como actor
        this.stage.addActor(this.npc);
    }
    //Metodo para añadir el suelo
    public void addSuelo(){
        //Nos creamos una definicion del cuerpo con BodyDef
        BodyDef bodyDef = new BodyDef();
        // Definimos el tipo del body como estatico porque no se va a mover ni le va a afectar la gravedad
        bodyDef.type = BodyDef.BodyType.StaticBody;
        // Le indicamos al world que guarde una entidad para el suelo
        Body body = world.createBody(bodyDef);
        // Indicamos la forma que tendrá el fixture del suelo, en este caso, un EdgeShape(una linea)
        EdgeShape edgeShape = new EdgeShape();
        // Indicamos la posicion de la linea con 2 coordenadas
        edgeShape.set(new Vector2(0,0), new Vector2(ANCHO_MUNDO, 0));
        // Al fixture suelo, la que acabamos de crear
        this.suelo=body.createFixture(edgeShape, 1);
        // Le añadimos un "identidicador"
        this.suelo.setUserData(SUELO);
        // La eliminamos de la memoria
        edgeShape.dispose();
    }
    //Creamos un segundo suelo, que se encargará que el coche de arriba no choque con el de abajo
    public void addSecondSuelo(){
        //Nos creamos una definicion del cuerpo con BodyDef
        BodyDef bodyDef = new BodyDef();
        // Definimos el tipo del body como estatico porque no se va a mover ni le va a afectar la gravedad
        bodyDef.type = BodyDef.BodyType.StaticBody;
        // Le indicamos al world que guarde una entidad para el suelo
        Body body = world.createBody(bodyDef);
        // Indicamos la forma que tendrá el fixture del suelo, en este caso, un EdgeShape(una linea)
        EdgeShape edgeShape = new EdgeShape();
        // Indicamos la posicion de la linea con 2 coordenadas
        edgeShape.set(new Vector2(0,1), new Vector2(ANCHO_MUNDO, 1));
        // Al fixture suelo, la que acabamos de crear
        this.suelo=body.createFixture(edgeShape, 1);
        // Le añadimos un "identidicador"
        this.suelo.setUserData(SUELO);
        // La eliminamos de la memoria
        edgeShape.dispose();
    }
    // Creamos el siguiente metodo para crear el sensor que decidirá quien ha ganado
    public void addMeta(){
        BodyDef bodyDef = new BodyDef();
        // Definimos el tipo del body como estatico porque no se va a mover ni le va a afectar la gravedad
        bodyDef.type = BodyDef.BodyType.StaticBody;
        // Indicamos la posicion
        bodyDef.position.set(new Vector2(9.7f, ALTO_MUNDO/2));
        // Al cuerpo le pasamos la definicion que acabamos de crear
        Body body = world.createBody(bodyDef);
        // En este caso, el fixture tendra forma rectangular
        PolygonShape shape = new PolygonShape();
        // Le indicamos las dimensiones
        shape.setAsBox(0.2f, ALTO_MUNDO/2);
        // Al fixture meta, le pasamos el que acabamos de crear
        this.meta = body.createFixture(shape, 1);
        // Y le indicamos que este va a ser un sensor
        this.meta.setSensor(true);
        // Al sensor que acabamos de crear lo "enlazamos" con el nombre que le hemos dado en Utilidades
        this.meta.setUserData(META);
        // y lo eliminamos de la memoria
        shape.dispose();
    }
    // Nos creamos un metodo para preparara el temporizador
    public void prepararTimer(){
        //Le decimos que tipo de fuente queremos usar, en este caso, la cogemos desde AssetsManager
        this.tiempo = this.main.assetsManager.getFuente();
        //Declaramos el escalado de la fuente
        this.tiempo.getData().scale(0.1f);

        // Creamos otro tipo de camara ortografica
        this.camaraFuente = new OrthographicCamera();
        // Indicamos cuanto va a ocupar esta pantalla, con las medidas en pixeles de la nuestra
        this.camaraFuente.setToOrtho(false, ANCHO_PANTALLA,ALTO_PANTALLA);
        // Actualizamos la camara para actualiza la informacion
        this.camaraFuente.update();
    }

    //Render sera el encargado de dibujar todo.
    @Override
    public void render(float delta) {
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
        // Con el metodo render mostramos por la pantalla cada frame
        //this.camaraDep.render(this.world, this.camaraDeMundo.combined);

        //Ahora necesitamos decirle a la tarjeta grafica que habrá otra camara para la fuente
        this.stage.getBatch().setProjectionMatrix(this.camaraFuente.combined);
        // Iniciamos el batch
        this.stage.getBatch().begin();
        // Le decimos en que posicion queremos que se dibuje la fuente:
        this.tiempo.draw(this.stage.getBatch(), "Tiempo: "+Math.round(this.car.getTiempoDelta()),ANCHO_PANTALLA/13f, ALTO_PANTALLA-ALTO_PANTALLA/3f);
        // Y una vez terminadas las acciones le decimos al batch que ya puede terminar
        this.stage.getBatch().end();
    }
    // Este metodo se ejecutará cuando la aplicacion pase a segundo plano
    @Override
    public void hide() {
        // Eliminamos de la memoria los coches y paramos la musica
        this.car.detach();
        this.npc.detach();
        this.cancionFondo.stop();
    }
    // Este metodo se ejecutara cuando se cierre la aplicacion
    @Override
    public void dispose() {
        // Eliminamos de la memoria el stage y el world
        this.stage.dispose();
        this.world.dispose();
    }

    //la funcion de este metodo es informarnos de aquellos objetos que han tenido colision
    public boolean detectorColisiones(Contact contacto, Object objeto1, Object objeto2){
        return (contacto.getFixtureA().getUserData().equals(objeto1) && contacto.getFixtureB().getUserData().equals(objeto2)) ||
                (contacto.getFixtureA().getUserData().equals(objeto2) && contacto.getFixtureB().getUserData().equals(objeto1));
    }

    //Este metodo se ejecutará cada vez que haya una colision
    @Override
    public void beginContact(Contact contact) {
        //Si el coche verde ha colisionado con la meta...
        if (detectorColisiones(contact, COCHE_USUARIO, META)){
            //Paramos a los coches
            this.world = new World(new Vector2(0,0),true);
            //Le pasamos las siguientes acciones que queremos que realicen cuando tenga lugar la colision
            this.stage.addAction(Actions.sequence(
                        // Con el delay le decimos cuantos segundos se desea que pasen desde que sucede la colision para llevar a cabo la otra actividad
                        Actions.delay(1f),
                        // Le decimos que ejecute la pantalla de victoria
                        Actions.run(new Runnable() {
                            @Override
                            public void run() {
                                main.setScreen(main.screenYouWin);
                            }
                        })
                    )
            );
        }
        // Si el coche rojo colisiona con la meta...
        else if(detectorColisiones(contact, NPC_CAR, META)){
            //Paramos a los coches
            this.world = new World(new Vector2(0,0),true);
            //Le pasamos las siguientes acciones que queremos que realicen cuando tenga lugar la colision
            this.stage.addAction(Actions.sequence(
                    // Con el delay le decimos cuantos segundos se desea que pasen desde que sucede la colision para llevar a cabo la otra actividad
                    Actions.delay(1f),
                    // Le decimos que ejecute la pantalla de derrota
                    Actions.run(new Runnable() {
                    @Override
                        public void run() {
                            main.setScreen(main.screenGOUser);
                        }
                    })
                 )
            );
        }

    }
    //Metodo requerido por la implementacion ContactListener, que en este programa no se usa
    @Override
    public void endContact(Contact contact) {

    }
    //Metodo requerido por la implementacion ContactListener, que en este programa no se usa
    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }
    //Metodo requerido por la implementacion ContactListener, que en este programa no se usa
    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
