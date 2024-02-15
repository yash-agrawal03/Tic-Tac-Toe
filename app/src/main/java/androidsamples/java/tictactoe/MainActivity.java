package androidsamples.java.tictactoe;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.Navigation;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

  private static final String TAG = "MainActivity";
  private FirebaseAuth auth;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    //FirebaseApp.initializeApp(this);
    Toolbar toolbar = findViewById(R.id.toolbar);
      auth =  FirebaseAuth.getInstance();
    setSupportActionBar(toolbar);


  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    if (item.getItemId() == R.id.menu_logout) {
      Log.d(TAG, "logout clicked");
      auth.signOut();
      Navigation.findNavController(this, R.id.nav_host_fragment).navigate(R.id.loginFragment);
      // TODO handle log out

      return true;
    }
    return super.onOptionsItemSelected(item);
  }
}