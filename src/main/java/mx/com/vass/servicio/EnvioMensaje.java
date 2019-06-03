package mx.com.vass.servicio;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EnvioMensaje {
	private static final Logger logger = LoggerFactory.getLogger(EnvioMensaje.class);
	private static final String QUEUE_NAME = "Envio mensaje a RabbitMQ";

	public static void main(String[] argv) throws Exception {
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("localhost");
		logger.info("Log4j : Comienza envio de mensaje !!");
		Connection conexion = factory.newConnection();
		Channel canal = conexion.createChannel();

		try {
			canal.queueDeclare(QUEUE_NAME, false, false, false, null);

			for (int i = 0; i < 15; ++i) {
				String message = String.format("Hola, enviando mensaje RabbitMQ",
						LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));
				canal.basicPublish("", QUEUE_NAME, null, message.getBytes());
				System.out.println(String.format("Envio de mensaje RabbitMQ", message));

				Thread.sleep(1500);
			}
		} finally {
			canal.close();
			conexion.close();
		}
	}
}
