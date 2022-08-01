package dtth.com.millionaire.activities;

import android.content.Context;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import dtth.com.millionaire.R;
import dtth.com.millionaire.database.AppPreferencesManager;
import dtth.com.millionaire.models.Player;

public class RankActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    RankAdapter adapter;
    ArrayList<Player> players;
    AppPreferencesManager manager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rank);

        manager = new AppPreferencesManager(this);
        players = manager.getAllPlayers();
        sortByScore(players);
        recyclerView = findViewById(R.id.recycler);
        adapter = new RankAdapter(players, this);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        recyclerView.setAdapter(adapter);
    }

    public class RankAdapter extends RecyclerView.Adapter {
        private Context context;
        private List<Player> playerList;

        public RankAdapter(List<Player> list, Context context) {
            this.playerList = list;
            this.context = context;
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
            View listPlayers = inflater.inflate(R.layout.item_player_rank, viewGroup, false);
            return new ViewHolder(listPlayers);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
            Player player = playerList.get(i);
            ViewHolder v = (ViewHolder) viewHolder;

            v.name.setText(player.name);
            v.score.setText(String.valueOf(player.score)+" VNĐ");
            v.rank.setText(String.valueOf(i+1));
            if(i == 0) v.rank.setTextAppearance(RankActivity.this, R.style.gold);
            else if(i == 1) v.rank.setTextAppearance(RankActivity.this, R.style.silver);
            else if(i == 2) v.rank.setTextAppearance(RankActivity.this, R.style.cu);
        }

        @Override
        public int getItemCount() {
            if(playerList == null) return 0;
            return playerList.size();
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, score, rank;
        public ViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.name_rank);
            score = view.findViewById(R.id.score_rank);
            rank = view.findViewById(R.id.rank);
        }
    }

    void sortByScore(ArrayList<Player> list) {
        Collections.sort(list, (o1, o2) -> {
            if (o1.score == o2.score) {
                return o1.name.compareTo(o2.name);
            } else {
                return o2.score - o1.score;
            }
        });

    }
}
