package ru.ifmo.md.colloquium2;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MyAdapter extends ArrayAdapter<Candidates> {
    Context context;
    ArrayList<Candidates> items;
    boolean browser;
    int sumvotes = 0;
    int size = 0;

    public MyAdapter(Context context, ArrayList<Candidates> items) {
        super(context, R.layout.list_element, items);
        this.context = context;
        this.size = items.size();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        final View elementView = inflater.inflate(R.layout.list_element, parent, false);
        TextView surname = (TextView) elementView.findViewById(R.id.surname);
        TextView votes = (TextView) elementView.findViewById(R.id.votes);
        TextView percent = (TextView) elementView.findViewById(R.id.percent);
        Button button = (Button) elementView.findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Candidates candidate = getItem(position);
                sumvotes++;
                int vote = candidate.getVotes();
                candidate.setVotes(vote+1);
                for (int i = 0; i<size; i++) {
                    candidate = getItem(i);
                    vote = candidate.getVotes();
                    int perc = (int) (((double)vote/sumvotes)*100);
                    candidate.setPercent(perc);
                }
            }
        });


        surname.setText(getItem(position).getSurname());
        votes.setText(getItem(position).getVotes() + " votes");
        percent.setText(getItem(position).getPercent() + "%");
        return elementView;
    }
}
