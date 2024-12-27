package org.example.mapagrafos.util;

import javafx.scene.image.Image;

import java.util.Random;

public class GifBattle {

    private Image image;

    public GifBattle() {
        Random random = new Random();
        switch (random.nextBoolean() ? 2 : 1) {
            case 1:
                image = new Image(getClass().getResourceAsStream("/iconos/gifScenes/gifBoss2.gif"));
                break;
            case 2:
                image = new Image(getClass().getResourceAsStream("/iconos/gifScenes/gifBoss2.gif"));
                break;
            default:
                image = new Image(getClass().getResourceAsStream("/iconos/gifScenes/gifBoss1.gif"));
                break;
        }
    }

    public Image getImage() {
        return image;
    }
}
