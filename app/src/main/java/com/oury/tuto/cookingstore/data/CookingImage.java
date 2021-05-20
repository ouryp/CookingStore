package com.oury.tuto.cookingstore.data;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.CancellationSignal;
import android.util.Size;
import android.widget.ImageView;

import java.io.File;
import java.io.IOException;

public class CookingImage {

    private File image;

    private CookingImage(String absPath) {
        image = new File(absPath);
    }

    private CookingImage(File parent, String name) {
        image = new File(parent, name + ".jpeg");
    }

    public static CookingImage load(String absPath) {
        return new CookingImage(absPath);
    }

    public static CookingImage create(File parent, String name) {
        return new CookingImage(parent, name);
    }

    public String toRoom() {
        return image.getAbsolutePath();
    }

    public Bitmap createThumbnail(Size size) throws IOException {
        return ThumbnailUtils.createImageThumbnail(image, size, new CancellationSignal());
    }

    public void adjustRotation() throws IOException {
        float rotate = getAdjustRotateAngle();
        Bitmap bitmap = BitmapFactory.decodeFile(image.getAbsolutePath());
        bitmap = rotateImage(bitmap, rotate);

    }

    public Bitmap displayIn(Size size) {

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(image.getAbsolutePath(), options);

        options.inSampleSize = 1;
        if(options.outHeight > size.getHeight() || options.outWidth > size.getWidth()) {
            int halfHeight = options.outHeight / 2;
            int halfWidth = options.outHeight / 2;
            while( (halfHeight / options.inSampleSize) >= size.getHeight() && (halfWidth / options.inSampleSize) >= size.getWidth()) {
                options.inSampleSize *= 2;
            }
        }

        options.inJustDecodeBounds = false;

        return BitmapFactory.decodeFile(image.getAbsolutePath(), options);
    }

    private Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),  matrix, true);
    }

    private float getAdjustRotateAngle() throws IOException {
        float rotation = 0.0f;
        ExifInterface ei = new ExifInterface(image.getAbsoluteFile());
        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);
        switch(orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                rotation = 90.0f;
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                rotation = 180.0f;
                break;
            case ExifInterface.ORIENTATION_ROTATE_270:
                rotation = 270.0f;
                break;
            case ExifInterface.ORIENTATION_NORMAL:
            default:
                break;
        }

        return rotation;
    }

    public File getFile() {
        return image;
    }

    public boolean delete() {
        return image.delete();
    }
}
