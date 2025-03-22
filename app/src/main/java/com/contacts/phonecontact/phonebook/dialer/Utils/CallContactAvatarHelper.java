package com.contacts.phonecontact.phonebook.dialer.Utils;

import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Size;

import com.contacts.phonecontact.phonebook.dialer.R;
import com.contacts.phonecontact.phonebook.dialer.AllModels.CallContact;

public class CallContactAvatarHelper {
    private final Context context;

    public CallContactAvatarHelper(Context context2) {
        this.context = context2;
    }

    public Bitmap getCallContactAvatar(final CallContact callContact) {
        final int n = 1;
        int n2 = 0;
        Label_0049:
        {
            if (callContact != null) {
                final String photoUri = callContact.getPhotoUri();
                if (photoUri != null && photoUri.length() > 0) {
                    n2 = n;
                    break Label_0049;
                }
            }
            n2 = 0;
        }
        Bitmap circularBitmap = null;
        if (n2 == 0) {
            return circularBitmap;
        }
        final Uri parse = Uri.parse(callContact.getPhotoUri());
        try {
            final ContentResolver contentResolver = this.context.getContentResolver();
            Bitmap bitmap;
            if (ContaxtExtUtils.isQPlus()) {
                final int n3 = (int) this.context.getResources().getDimension(R.dimen.list_avatar_size);
                bitmap = contentResolver.loadThumbnail(parse, new Size(n3, n3), null);
            } else {
                bitmap = MediaStore.Images.Media.getBitmap(contentResolver, parse);
            }
            circularBitmap = this.getCircularBitmap(bitmap);
            return circularBitmap;
        } catch (final Exception ex) {
            circularBitmap = circularBitmap;
            return circularBitmap;
        }
    }

    public Bitmap getCircularBitmap(Bitmap bitmap) {
        Bitmap createBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getWidth(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(createBitmap);
        Paint paint = new Paint();
        Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        float width = ((float) bitmap.getWidth()) / 2.0f;
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        canvas.drawCircle(width, width, width, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return createBitmap;
    }

}
