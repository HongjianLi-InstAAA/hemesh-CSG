package main;

import io.socket.client.IO;

import java.net.URISyntaxException;

/**
 * @classname: simpleProcessing
 * @description:
 * @author: amomorning
 * @date: 2020/06/16
 */
public class Main {

    public static void main(String[] args) {

        try {
            if (args.length > 0) {
                Show.socket = IO.socket(args[0]);
            } else {
                Show.socket = IO.socket("http://127.0.0.1:23810");
            }
            Show show = new Show();
            show.setup();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }
}
