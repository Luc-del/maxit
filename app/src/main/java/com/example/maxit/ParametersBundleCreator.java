package com.example.maxit;

import android.os.Bundle;

public abstract class ParametersBundleCreator {

    static Bundle CreateBundle(int Nx, int Ny, boolean bot_selected) {
        Bundle b = new Bundle();

        b.putInt("Nx", Nx);
        b.putInt("Ny", Ny);

        b.putBoolean("bot",bot_selected);
        b.putBoolean("rotate_text",false);

        b.putInt("hidden_cells",0);


        return b;
    }

    static Bundle CreateBundle(int Nx, int Ny, boolean bot_selected, boolean switch_text_rotation) {
        Bundle b = new Bundle();

        b.putInt("Nx", Nx);
        b.putInt("Ny", Ny);

        b.putBoolean("bot",bot_selected);
        b.putBoolean("rotate_text",switch_text_rotation);

        b.putInt("hidden_cells",0);

        return b;
    }

    static Bundle CreateBundle(int Nx, int Ny, boolean bot_selected, boolean switch_text_rotation, int hidden_cells) {
        Bundle b = new Bundle();

        b.putInt("Nx", Nx);
        b.putInt("Ny", Ny);

        b.putBoolean("bot",bot_selected);
        b.putBoolean("rotate_text",switch_text_rotation);

        b.putInt("hidden_cells",hidden_cells);

        return b;
    }
}
