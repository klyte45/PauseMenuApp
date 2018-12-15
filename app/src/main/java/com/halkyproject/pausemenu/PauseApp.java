package com.halkyproject.pausemenu;

import android.app.Application;
import android.graphics.Typeface;
import com.halkyproject.pausemenu.singletons.MusicServiceSingleton;
import com.halkyproject.pausemenu.util.TypeFactory;

public class PauseApp extends Application {

    private static PauseApp mInstance;
    private TypeFactory mFontFactory;


    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
    }


    @Override
    public void onTerminate() {
        super.onTerminate();
        MusicServiceSingleton.INSTANCE.finalize();
    }

    public static synchronized PauseApp getApp() {
        return mInstance;
    }

    public Typeface getTypeFace(int type) {
        if (mFontFactory == null)
            mFontFactory = new TypeFactory(this);

        switch (type) {
            case FontsEnum.P2P:
            default:
                return mFontFactory.getPress2p();

            case FontsEnum.SVAECTOR:
                return mFontFactory.getSvaector();

        }
    }

    public interface FontsEnum {
        int P2P = 1, SVAECTOR = 2;
    }

}
