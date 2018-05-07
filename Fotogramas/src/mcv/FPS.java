package mcv;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Dimension;

import javax.swing.JFrame;

public class FPS extends Canvas implements Runnable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static JFrame ventana;
	private static Thread thread;
	private static volatile boolean running = false;// no puede utilizarlo de forma simultanea los dos threads
	private static int aps = 0;
	private static int fps = 0;

	private FPS() {
		setPreferredSize(new Dimension(720, 480));
		ventana = new JFrame();
		ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		ventana.setVisible(true);
		ventana.setLocationRelativeTo(null);
		ventana.setResizable(false);
		ventana.setLayout(new BorderLayout());// No se que es
		ventana.add(this, BorderLayout.CENTER);// BorderLayout añade el canvas en el centro de la pantalla
		ventana.pack();// para que vaya todo como lo hemos puesto como por ejemplo el tamaño de ventana
						// etc...

	}

	public void update() {
		aps++;
	}

	public void draw() {
		fps++;
	}

	@Override
	public synchronized void run() { // hace que no pueda manipular el boolean los dos metodos a la vez
		final int NS_POR_SEGUNDO = 1000000000;// Cuantos nanosegundos hay en un segundo
		final byte APS_OBJETIVO = 60;// cuantas actualizaciones queremos tener por segundo
		final double NS_POR_ACTUALIZACION = NS_POR_SEGUNDO / APS_OBJETIVO;// Cuantos nanosegundos transcurren por
																			// actualizacion

		long referenciaActualizacion = System.nanoTime();
		long referenciaContador = System.nanoTime();
		double tiempoTranscurrido;
		double delta = 0;
		// TODO Auto-generated method stub
		System.out.println(thread.isAlive());
		while (running) {
			final long inicioBucle = System.nanoTime();

			tiempoTranscurrido = inicioBucle - referenciaActualizacion;
			referenciaActualizacion = inicioBucle;
			delta += tiempoTranscurrido / NS_POR_ACTUALIZACION;
			while (delta >= 1) {
				update();
				delta--;
			}
			draw();
			if (System.nanoTime() - referenciaContador > NS_POR_SEGUNDO) {
				ventana.setTitle("Nombre " + "|| APS " + aps + " || FPS " + fps);
				aps = 0;
				fps = 0;
				referenciaContador = System.nanoTime();
			}
		}

	}

	public synchronized void start() {
		running = true;
		thread = new Thread(this, "Graficos");// El nombre de este hilo es Graficos lo unico que sirve es
												// paramonitorizarlo
		thread.start();

	}

	public void stop() {
		try {
			running = false;
			thread.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		new FPS().start();
	}
}
