package com.halkyproject.pausemenu.util;

import android.content.Context;
import android.graphics.Typeface;

public class TypeFactory {
    private final String SVAECTOR = "fonts/svaector.ttf";
    private final String P_2P = "fonts/pressstart2p.ttf";

    private Typeface svaector;
    private Typeface press2p;

    public TypeFactory(Context context) {
        svaector = Typeface.createFromAsset(context.getAssets(), SVAECTOR);
        press2p = Typeface.createFromAsset(context.getAssets(), P_2P);
    }


    public Typeface getSvaector() {
        return svaector;
    }

    public Typeface getPress2p() {
        return press2p;
    }
}
