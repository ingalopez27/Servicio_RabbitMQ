package mx.com.vass.servicio;

import com.rabbitmq.client.*;

import java.io.IOException;

public class RecepcionMensaje {

    private final static String QUEUE_NAME = "Mensaje de llegada a RabbitMQ";

    public static void main(String[] argv) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection cone = factory.newConnection();
        Channel can = cone.createChannel();

        try {
            can.queueDeclare(QUEUE_NAME, false, false, false, null);
            System.out.println(" [*] Esperando los mensajes. La salida es CTRL+C");

            Consumer consumer = new DefaultConsumer(can) {
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                    String message = new String(body, "UTF-8");
                    System.out.println(String.format("Recepcion de mensaje", message));
                }
            };

            can.basicConsume(QUEUE_NAME, true, consumer);

            Thread.sleep(20000);
        } finally {
            can.close();
            cone.close();
        }
    }
}