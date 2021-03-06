package com.github.hebelala.waker;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

import java.util.Calendar;
import java.util.Date;

public class Main {

    public static void main(String[] args) throws Exception {
        if (args.length != 2) {
            throw new RuntimeException("please input -t parameter, like: -t 08:00");
        }
        if (!args[0].equals("-t")) {
            throw new RuntimeException("the parameter is not supported");
        }
        String point = args[1];

        String[] split = point.split(":");
        int hour = Integer.parseInt(split[0]);
        int minute = Integer.parseInt(split[1]);

        if (hour > 23 || hour < 0) {
            throw new RuntimeException("the hour should be 00-23");
        }

        if (minute > 59 || minute < 0) {
            throw new RuntimeException("the minute should be 00-59");
        }

        int deviation = 1;

        System.out.println("Hebe will wake you up at " + point + ", deviation is possibly " + deviation + " minutes");

        Calendar calendar = Calendar.getInstance();
        boolean played = false;
        while (true) {
            Thread.sleep(500L);
            if (!played) {
                calendar.setTimeInMillis(System.currentTimeMillis());
                int h = calendar.get(Calendar.HOUR_OF_DAY);
                int m = calendar.get(Calendar.MINUTE);
                if (h == hour) {
                    if (Math.abs(minute - m) <= deviation) {
                        play();
                        played = true;
                    }
                } else if (h > hour) {
                    if (h - hour == 1) {
                        if (60 - minute + m <= deviation) {
                            play();
                            played = true;
                        }
                    } else if (h - hour == 23) {
                        if (60 - m + minute <= deviation) {
                            play();
                            played = true;
                        }
                    }
                } else {
                    if (hour - h == 1) {
                        if (60 - m + minute <= deviation) {
                            play();
                            played = true;
                        }
                    } else if (hour - h == 23) {
                        if (60 - minute + m <= deviation) {
                            play();
                            played = true;
                        }
                    }
                }
            } else {
                play();
            }
        }
    }

    private static void play() throws JavaLayerException {
        System.out.println("--- ^-^ wake wake wake...up... now is " + new Date());
        Player player = new Player(Main.class.getResourceAsStream("/Hebe-FlowersWorld.mp3"));
        try {
            player.play();
        } finally {
            player.close();
        }
    }

}
