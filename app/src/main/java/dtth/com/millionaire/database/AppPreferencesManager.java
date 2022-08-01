package dtth.com.millionaire.database;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import dtth.com.millionaire.models.Player;
import dtth.com.millionaire.models.Question;

public class AppPreferencesManager {
    Context context;
    int PRIVATE_MODE = 0;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    private static String MY_APP = "who_is_the_millionaire";
    private static String NAME = "name";
    private static String SCORE = "score";
    private static String ALL_PLAYERS = "all_players";

    public AppPreferencesManager(Context context) {
        this.context = context;
        pref = context.getSharedPreferences(MY_APP, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void addPlayer(Player player) {
        ArrayList<Player> players = getAllPlayers();
        players.add(player);
        storeAllPlayers(players);
    }

    public void storeName(String name) {
        editor.putString(NAME, name).commit();
    }

    public String getName() {
        return pref.getString(NAME, "Ẩn danh");
    }

    public void storeAllPlayers(ArrayList<Player> players) {
        Gson gson = new Gson();
        String str = gson.toJson(players);
        editor.putString(ALL_PLAYERS, str).commit();
    }

    public ArrayList<Player> getAllPlayers() {
        String str = pref.getString(ALL_PLAYERS, null);
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<Player>>(){}.getType();
        ArrayList<Player> players = gson.fromJson(str, type);
        if(str == null) return new ArrayList<>();
        return players;
    }
}
