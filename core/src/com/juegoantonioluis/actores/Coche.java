package com.juegoantonioluis.actores;

import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.juegoantonioluis.extraclases.*;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class Coche extends Actor {
    //Indicamos el estado de nuestro coche(cuando no ha perdido)
    public static final int ESTADO_NORMAL = 0;
    //Indicamos una variable para guardar el estado del coche
    public int state;
    // Nos creamos un objeto animacion donde guardaremos la animacion del coche
    private Animation<TextureRegion> carAnimation;
    //Declaramos el ancho y el alto del coche
    private float CAR_WIDTH = 2.2f;
    private float CAR_HEIGHT = 0.9f;
    //Declaramos un vector para saber la posicion de inicio del coche
    private Vector2 posicion;
    //Creamos el mundo, encargado de controlar el apartado grafico
    private World world;
    // Creamos la variable tiempoDelta para guardar el tiempo de delta
    private float tiempoDelta;
    //Nos creamos la variable body para nuestro coche
    private Body body;
    //Creamos nuestra variable Hitbox, donde guardaremos el fixture
    private Fixture hitbox;

    //Creamos el constructor con los siguientes parametros
    public Coche(World world,Animation<TextureRegion> animation, Vector2 posicion) {
        this.carAnimation = animation;
        this.posicion = posicion;
        this.world         = world;
        tiempoDelta = 0f;
        state = ESTADO_NORMAL;
        //Llamamos a los metodos que hemos creado mas abajo
        createBody();
        createFixture();

    }

    //Con este metodo crearemos el cuerpo de nuestro coche
    public void createBody(){
        //Nos creamos una definicion del cuerpo con BodyDef
        BodyDef bodyDef = new BodyDef();
        // Indicamos la posicion
        bodyDef.position.set(posicion);
        // Definimos el tipo del body como dinamico porque se va a mover y le va a afectar la gravedad
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        // A la variable body le pasamos la definicion que acabamos de crear
        this.body = this.world.createBody(bodyDef);
    }

    //Con este metodo crearemos el fixture de nuestro coche
    public void createFixture(){
        // En este caso, el fixture tendra forma rectangular
        PolygonShape rectangulo = new PolygonShape();
        // Le indicamos las dimensiones
        rectangulo.setAsBox(CAR_WIDTH/2,CAR_HEIGHT/2);
        // Al fixture hitbox, le pasamos el que acabamos de crear
        this.hitbox = this.body.createFixture(rectangulo,3);
        // Al fixture que acabamos de crear lo "enlazamos" con el nombre que le hemos dado en Utilidades
        this.hitbox.setUserData(Utilidades.COCHE_USUARIO);
        // y lo eliminamos de la memoria
        rectangulo.dispose();
    }
    //Metodo que nos devuelve stateTime
    public float getTiempoDelta() {
        return tiempoDelta;
    }
    //Metodo donde se definirá la funcion de nuestro coche
    @Override
    public void act(float delta) {
        //Creamos una variable booleana para saber si se pulsa la pantalla o no
        boolean run = Gdx.input.justTouched();
        //Si es pulsada, y el estado del coche es normal...
        if(run && this.state == ESTADO_NORMAL){
            //Definimos la velocidad que queremos que el coche avance
            this.body.setLinearVelocity(0.4f, 0);
        }
    }
    //Dibujamos la animacion del coche
    @Override
    public void draw(Batch batch, float parentAlpha) {
        //Definimos la posicion de nuestro coche
        setPosition(body.getPosition().x-CAR_WIDTH/2, body.getPosition().y-CAR_HEIGHT/2);
        //Le decimos a la grafica que dibuje la animacion y la muestre por pantalla
        batch.draw(this.carAnimation.getKeyFrame(tiempoDelta,true),getX(),getY(), CAR_WIDTH,CAR_HEIGHT);
        // Incrementamos a tiempoDelta el valor de delta
        tiempoDelta += Gdx.graphics.getDeltaTime();
    }

    //Metodo para destruir el body y el mundo
    public void detach(){
        this.body.destroyFixture(this.hitbox);
        this.world.destroyBody(this.body);

    }

}
