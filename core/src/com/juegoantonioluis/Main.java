package com.juegoantonioluis;

import com.badlogic.gdx.Game;
import com.juegoantonioluis.extraclases.AssetsManager;
import com.juegoantonioluis.screens.ScreenGOUser;
import com.juegoantonioluis.screens.ScreenGetReady;
import com.juegoantonioluis.screens.ScreenJuego;
import com.juegoantonioluis.screens.ScreenYouWin;
//Esta clase sera la encargada de organizar las pantallas
public class Main extends Game {
    //Nos creamos un objeto de cada pantalla que hemos creado anteriormente
    public ScreenJuego screenJuego;

    public ScreenGOUser screenGOUser;

    public ScreenYouWin screenYouWin;

    public ScreenGetReady screenGetReady;

    public AssetsManager assetsManager;
    //Con el metodo create inicializamos las pantallas
    @Override
    public void create() {
        this.assetsManager = new AssetsManager();

        this.screenJuego = new ScreenJuego(this);

        this.screenGOUser = new ScreenGOUser(this);

        this.screenYouWin = new ScreenYouWin(this);

        this.screenGetReady = new ScreenGetReady(this);
        //Y le indicamos cual queremos que se ejecute primero
        setScreen(screenGetReady);
    }
}
