package es.codeurjc.ais.tictactoe;
import static org.mockito.Mockito.*;


import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.CoreMatchers.not;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.hamcrest.MockitoHamcrest;

import static org.junit.Assert.*;

import es.codeurjc.ais.tictactoe.TicTacToeGame.EventType;
import es.codeurjc.ais.tictactoe.TicTacToeGame.WinnerValue;

class TicTacToeGameTest{

	TicTacToeGame juego; 
	Connection doblecon1, doblecon2;
	Player jugador1, jugador2; 
	
	@BeforeEach
	public void setUp() {
		//paso 1
		juego = new TicTacToeGame();
		//paso 2
		doblecon1 = mock(Connection.class); 
		doblecon2 = mock(Connection.class);
		
		//paso 3
		juego.addConnection(doblecon1);
		juego.addConnection(doblecon2);
		
		//paso 4
		jugador1 = new Player(01, "X","Calisto");
		jugador2 = new Player(02, "O","Melibea");
		
		//paso 5
		juego.addPlayer(jugador1);
		reset(doblecon1);
		reset(doblecon2);
		juego.addPlayer(jugador2);
	}
		
	@Test
	void testJugador1() {
		
		verify(doblecon1).sendEvent(eq(EventType.JOIN_GAME),MockitoHamcrest.argThat(hasItems(jugador1,jugador2)));
		verify(doblecon2).sendEvent(eq(EventType.JOIN_GAME),MockitoHamcrest.argThat(hasItems(jugador1,jugador2)));
		
		
		verify(doblecon1).sendEvent(eq(EventType.SET_TURN),eq(jugador1));
		verify(doblecon2).sendEvent(eq(EventType.SET_TURN),eq(jugador1));
		juego.mark(0);	
		verify(doblecon2).sendEvent(eq(EventType.SET_TURN),eq(jugador2));
		verify(doblecon1).sendEvent(eq(EventType.SET_TURN),eq(jugador2));
		
		reset(doblecon1);
		reset(doblecon2);
		
		juego.mark(1);
		verify(doblecon1).sendEvent(eq(EventType.SET_TURN),eq(jugador1));
		verify(doblecon2).sendEvent(eq(EventType.SET_TURN),eq(jugador1));
		
		reset(doblecon1);
		reset(doblecon2);
		
		juego.mark(3);
		verify(doblecon2).sendEvent(eq(EventType.SET_TURN),eq(jugador2));
		verify(doblecon1).sendEvent(eq(EventType.SET_TURN),eq(jugador2));
		
		reset(doblecon1);
		reset(doblecon2);
		
		juego.mark(7);
		verify(doblecon1).sendEvent(eq(EventType.SET_TURN),eq(jugador1));
		verify(doblecon2).sendEvent(eq(EventType.SET_TURN),eq(jugador1));
		
		reset(doblecon1);
		reset(doblecon2);
		
		juego.mark(6); 
		
		//JUGADOR 1 GANA: FIN DEL JUEGO		
		ArgumentCaptor<WinnerValue> argument = ArgumentCaptor.forClass(WinnerValue.class);
		verify(doblecon1).sendEvent(eq(EventType.GAME_OVER), argument.capture());
		verify(doblecon2).sendEvent(eq(EventType.GAME_OVER), argument.capture());
		
		//comprobamos que el ganador es el jugador1
		assertThat(argument.getValue().player, is(jugador1));
		
		//comprobamos que el ganador no es el jugador2
		assertThat(argument.getValue().player, is(not(jugador2)));
		
		//comprobamos que no hay empate
		assertNotNull(argument.getValue().pos);
	}
	
	@Test
	void testJugador2() {
		
		verify(doblecon1).sendEvent(eq(EventType.JOIN_GAME),MockitoHamcrest.argThat(hasItems(jugador1,jugador2)));
		verify(doblecon2).sendEvent(eq(EventType.JOIN_GAME),MockitoHamcrest.argThat(hasItems(jugador1,jugador2)));
		
		
		verify(doblecon1).sendEvent(eq(EventType.SET_TURN),eq(jugador1));
		verify(doblecon2).sendEvent(eq(EventType.SET_TURN),eq(jugador1));
		juego.mark(0);	
		verify(doblecon2).sendEvent(eq(EventType.SET_TURN),eq(jugador2));
		verify(doblecon1).sendEvent(eq(EventType.SET_TURN),eq(jugador2));
		
		reset(doblecon1);
		reset(doblecon2);
		
		juego.mark(1);
		verify(doblecon1).sendEvent(eq(EventType.SET_TURN),eq(jugador1));
		verify(doblecon2).sendEvent(eq(EventType.SET_TURN),eq(jugador1));
		
		reset(doblecon1);
		reset(doblecon2);
		
		juego.mark(2);
		verify(doblecon2).sendEvent(eq(EventType.SET_TURN),eq(jugador2));
		verify(doblecon1).sendEvent(eq(EventType.SET_TURN),eq(jugador2));
		
		reset(doblecon1);
		reset(doblecon2);
		
		juego.mark(4);
		verify(doblecon1).sendEvent(eq(EventType.SET_TURN),eq(jugador1));
		verify(doblecon2).sendEvent(eq(EventType.SET_TURN),eq(jugador1));
		
		reset(doblecon1);
		reset(doblecon2);
		
		juego.mark(6); 
		verify(doblecon2).sendEvent(eq(EventType.SET_TURN),eq(jugador2));
		verify(doblecon1).sendEvent(eq(EventType.SET_TURN),eq(jugador2));
		
		reset(doblecon1);
		reset(doblecon2);
		
		juego.mark(7);
		
		
		//JUGADOR 2 GANA: FIN DEL JUEGO		
		ArgumentCaptor<WinnerValue> argument = ArgumentCaptor.forClass(WinnerValue.class);
		verify(doblecon1).sendEvent(eq(EventType.GAME_OVER), argument.capture());
		verify(doblecon2).sendEvent(eq(EventType.GAME_OVER), argument.capture());
		
		//comprobamos que el ganador es el jugador2
		assertThat(argument.getValue().player, is(jugador2));
		
		//comprobamos que el ganador no es el jugador1
		assertThat(argument.getValue().player, is(not(jugador1)));
		
		//comprobamos que no hay empate
		assertNotNull(argument.getValue().pos);
	}
	
	@Test
	void testEmpate() {
		
		verify(doblecon1).sendEvent(eq(EventType.JOIN_GAME),MockitoHamcrest.argThat(hasItems(jugador1,jugador2)));
		verify(doblecon2).sendEvent(eq(EventType.JOIN_GAME),MockitoHamcrest.argThat(hasItems(jugador1,jugador2)));
		
		
		verify(doblecon1).sendEvent(eq(EventType.SET_TURN),eq(jugador1));
		verify(doblecon2).sendEvent(eq(EventType.SET_TURN),eq(jugador1));
		juego.mark(1);	
		verify(doblecon2).sendEvent(eq(EventType.SET_TURN),eq(jugador2));
		verify(doblecon1).sendEvent(eq(EventType.SET_TURN),eq(jugador2));
		
		reset(doblecon1);
		reset(doblecon2);
		
		juego.mark(0);
		verify(doblecon1).sendEvent(eq(EventType.SET_TURN),eq(jugador1));
		verify(doblecon2).sendEvent(eq(EventType.SET_TURN),eq(jugador1));
		
		reset(doblecon1);
		reset(doblecon2);
		
		juego.mark(3);
		verify(doblecon2).sendEvent(eq(EventType.SET_TURN),eq(jugador2));
		verify(doblecon1).sendEvent(eq(EventType.SET_TURN),eq(jugador2));
		
		reset(doblecon1);
		reset(doblecon2);
		
		juego.mark(2);
		verify(doblecon1).sendEvent(eq(EventType.SET_TURN),eq(jugador1));
		verify(doblecon2).sendEvent(eq(EventType.SET_TURN),eq(jugador1));
		
		reset(doblecon1);
		reset(doblecon2);
		
		juego.mark(5); 
		verify(doblecon2).sendEvent(eq(EventType.SET_TURN),eq(jugador2));
		verify(doblecon1).sendEvent(eq(EventType.SET_TURN),eq(jugador2));
		
		reset(doblecon1);
		reset(doblecon2);
		
		juego.mark(4);
		verify(doblecon1).sendEvent(eq(EventType.SET_TURN),eq(jugador1));
		verify(doblecon2).sendEvent(eq(EventType.SET_TURN),eq(jugador1));
		
		reset(doblecon1);
		reset(doblecon2);
		
		juego.mark(6); 
		verify(doblecon2).sendEvent(eq(EventType.SET_TURN),eq(jugador2));
		verify(doblecon1).sendEvent(eq(EventType.SET_TURN),eq(jugador2));
		
		reset(doblecon1);
		reset(doblecon2);
		
		juego.mark(7);
		verify(doblecon1).sendEvent(eq(EventType.SET_TURN),eq(jugador1));
		verify(doblecon2).sendEvent(eq(EventType.SET_TURN),eq(jugador1));
		
		reset(doblecon1);
		reset(doblecon2);
		
		juego.mark(8); 
		
		
		//Empate: FIN DEL JUEGO		
		ArgumentCaptor<WinnerValue> argument = ArgumentCaptor.forClass(WinnerValue.class);
		verify(doblecon1).sendEvent(eq(EventType.GAME_OVER), argument.capture());
		verify(doblecon2).sendEvent(eq(EventType.GAME_OVER), argument.capture());
		
		//comprobamos que hay empate
		assertNull(argument.getValue());
	}
	

}
