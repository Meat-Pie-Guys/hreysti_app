package fenrirmma.hreysti_app.login;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Notandi on 7.12.2017.
 */

public class TokenAccess {
    private static final String PREF_NAME = "TOKEN";
    private static final String TOKEN_ACCESS_KEY = "STORED_TOKEN";


    private static TokenAccess INSTANCE = null;
    public static TokenAccess getInstance(Context context) {
        if(INSTANCE == null){
            synchronized (TokenAccess.class){
                if(INSTANCE == null){
                    INSTANCE = new TokenAccess(context);
                }
            }
        }
        return INSTANCE;
    }

    private SharedPreferences prefrences;
    private TokenAccess(Context context){
        this.prefrences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public void setToken(String token){
        SharedPreferences.Editor editor = prefrences.edit();
        editor.putString(TOKEN_ACCESS_KEY, token);
        editor.apply();
    }

    public void clearToken(){
        SharedPreferences.Editor editor = prefrences.edit();
        editor.remove(TOKEN_ACCESS_KEY);
        editor.apply();
    }

    public boolean hasToken(){
        return prefrences.contains(TOKEN_ACCESS_KEY);
    }

    public String getToken(){
        return prefrences.getString(TOKEN_ACCESS_KEY, null);
    }
}
