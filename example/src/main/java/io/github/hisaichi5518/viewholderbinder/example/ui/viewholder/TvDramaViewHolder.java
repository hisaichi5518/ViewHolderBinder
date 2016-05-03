package io.github.hisaichi5518.viewholderbinder.example.ui.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import io.github.hisaichi5518.viewholderbinder.example.entity.Filmography;

public class TvDramaViewHolder extends RecyclerView.ViewHolder {
    public TvDramaViewHolder(View itemView) {
        super(itemView);
    }

    public void render(Filmography filmography) {
        ((TextView) itemView).setText("TV DRAMA: " + filmography.title);
    }
}
