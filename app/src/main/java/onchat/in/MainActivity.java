package onchat.in;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Adapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

import onchat.in.Adapters.FragmentAdapter;
import onchat.in.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    FirebaseAuth auth;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth=FirebaseAuth.getInstance();
        binding.viewPager.setAdapter(new FragmentAdapter(getSupportFragmentManager()));
        binding.tablayout.setupWithViewPager(binding.viewPager);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search,menu);
        MenuItem menuItem=menu.findItem(R.id.searchIcon);
        inflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.Setting:
              Intent i=new Intent(MainActivity.this,SettingsActivity.class);
              startActivity(i);
                break;
            case R.id.Logout:
                auth.signOut();
                Intent intent= new Intent(MainActivity.this,SignIn_Activity.class);
                startActivity(intent);
                break;
            case R.id.groupChat:
                Intent intentt= new Intent(MainActivity.this,GroupChatActivity.class);
                startActivity(intentt);
                break;


        }
        return true;
    }
}