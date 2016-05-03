package io.github.hisaichi5518.viewholderbinder.example.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.github.hisaichi5518.viewholderbinder.annotation.ViewHolder;
import io.github.hisaichi5518.viewholderbinder.example.R;
import io.github.hisaichi5518.viewholderbinder.example.entity.Filmography;
import io.github.hisaichi5518.viewholderbinder.example.ui.viewholder.FilmViewHolder;
import io.github.hisaichi5518.viewholderbinder.example.ui.viewholder.TvDramaViewHolder;

public class FilmographyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final List<Filmography> items = new ArrayList<>();
    private final FilmographyAdapterViewHolderBinder binder;

    public FilmographyAdapter(Context context) {
        items.addAll(getTvDramaList(context));
        items.addAll(getFilmList(context));

        this.binder = new FilmographyAdapterViewHolderBinder(context, this);
    }

    private List<Filmography> getTvDramaList(Context context) {
        List<String> stringList = Arrays.asList(context.getResources().getStringArray(R.array.tv_drama));

        List<Filmography> filmographies = new ArrayList<>();
        for (String title : stringList) {
            Filmography filmography = new Filmography();
            filmography.title = title;
            filmography.type = Filmography.TYPE_TV_DRAMA;
            filmographies.add(filmography);
        }

        return filmographies;
    }

    private List<Filmography> getFilmList(Context context) {
        List<String> stringList = Arrays.asList(context.getResources().getStringArray(R.array.film));

        List<Filmography> filmographies = new ArrayList<>();
        for (String title : stringList) {
            Filmography filmography = new Filmography();
            filmography.title = title;
            filmography.type = Filmography.TYPE_FILM;
            filmographies.add(filmography);
        }

        return filmographies;
    }

    void onBindViewHolder(@ViewHolder(viewType = Filmography.TYPE_TV_DRAMA, layout = R.layout.row_filmography) TvDramaViewHolder holder, int position) {
        holder.render(items.get(position));
    }

    void onBindViewHolder(@ViewHolder(viewType = Filmography.TYPE_FILM, layout = R.layout.row_filmography) FilmViewHolder holder, int position) {
        holder.render(items.get(position));
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return binder.create(parent, viewType);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        binder.bind(holder, position);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public int getItemViewType(int position) {
        return items.get(position).type;
    }
}
