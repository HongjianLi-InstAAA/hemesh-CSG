package main;

import com.google.gson.Gson;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

/**
 * @author JianZ
 * @version 1.0
 * @date 2020/11/16 14:19
 */
public class Show {
    public static Socket socket;
    static public Controls controls;

    public void setup() {

        socket.connect();

        socket.on("queryCanvasSize", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                System.out.println("Sent canvas size: ");
                String canvasSize = "{\"width\":" + 800 + ",\"height\":" + 800 + "}";
                System.out.println(canvasSize);
                socket.emit("changeCanvas", canvasSize);
            }
        });

        socket.on("modulateReceiveParameters", new Emitter.Listener() {

            @Override
            public void call(Object... args) {
                System.out.println(args.length);
                System.out.println(args[0]);
                Gson gson = new Gson();
                controls = gson.fromJson(args[0].toString(), Controls.class);
                Geometry geom = parseGeometry(controls);
                String sg = gson.toJson(geom);
                System.out.println(sg);

                socket.emit("geometryExchange", sg);
            }
        });
    }

    static public Geometry parseGeometry(Controls controls) {


        return null;
    }
}
