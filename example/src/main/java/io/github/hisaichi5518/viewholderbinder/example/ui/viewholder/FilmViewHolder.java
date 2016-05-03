package io.github.hisaichi5518.viewholderbinder.example.ui.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import io.github.hisaichi5518.viewholderbinder.example.entity.Filmography;

public class FilmViewHolder extends RecyclerView.ViewHolder {
    public FilmViewHolder(View itemView) {
        super(itemView);
    }

    public void render(Filmography filmography) {
        ((TextView) itemView).setText("FILM: " + filmography.title);
    }
}
