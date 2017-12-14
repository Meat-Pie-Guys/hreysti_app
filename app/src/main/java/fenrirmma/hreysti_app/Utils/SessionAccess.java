package fenrirmma.hreysti_app.Utils;

import android.content.Context;
import android.content.SharedPreferences;


public class SessionAccess {
    private static final String PREF_NAME = "TOKEN";
    private static final String TOKEN_ACCESS_KEY = "STORED_TOKEN";
    private static final String USER_ROLE = "STORED_USER_ROLE";

    private static SessionAccess INSTANCE = null;
    public static SessionAccess getInstance(Context context) {
        if(INSTANCE == null){
            synchronized (SessionAccess.class){
                if(INSTANCE == null){
                    INSTANCE = new SessionAccess(context);
                }
            }
        }
        return INSTANCE;
    }

    private SharedPreferences prefrences;
    private SessionAccess(Context context){
        this.prefrences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public void setToken(String token){
        SharedPreferences.Editor editor = prefrences.edit();
        editor.putString(TOKEN_ACCESS_KEY, token);
        editor.apply();
    }

    public void setRole(String role){
        SharedPreferences.Editor editor = prefrences.edit();
        editor.putString(USER_ROLE, role);
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

    public String getRole(){
        return prefrences.getString(USER_ROLE, null);
    }
}
