package gesport.xpertise.com.gesportapk;

import android.graphics.Bitmap;

public class obj_attributes {
    protected int id;
    protected int w;
    protected int h;
    protected Bitmap image;

    public obj_attributes(int id, int w, int h, Bitmap image) {
        this.id = id;
        this.w = w;
        this.h = h;
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getW() {
        return w;
    }

    public void setW(int w) {
        this.w = w;
    }

    public int getH() {
        return h;
    }

    public void setH(int h) {
        this.h = h;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }
}
