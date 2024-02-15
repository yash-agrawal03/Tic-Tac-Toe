package androidsamples.java.tictactoe;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DashboardFragment extends Fragment {

  private static final String TAG = "DashboardFragment";
  private NavController mNavController;

  private FirebaseAuth mAuth;

  private DatabaseReference userReference, availableReference, gameReference;

  private String uniqueID;

  private String currentUser;

    public InternetGameDetails mEntry;
    public List<InternetGameDetails> list;

    interface Callbacks {
    void onEntrySelected(String id);

    }
    private Callbacks mCallbacks = null;

  /**
   * Mandatory empty constructor for the fragment manager to instantiate the
   * fragment (e.g. upon screen orientation changes).
   */
  public DashboardFragment() {
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Log.d(TAG, "onCreate");
    mAuth = FirebaseAuth.getInstance();
    setHasOptionsMenu(true); // Needed to display the action menu for this fragment
    if(mAuth.getCurrentUser()!=null)
    {
        uniqueID=mAuth.getCurrentUser().getUid();
    }
  }

  @Override
  public View onCreateView(LayoutInflater inflater,
                           ViewGroup container,
                           Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_dashboard, container, false);
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
      super.onViewCreated(view, savedInstanceState);
      mNavController = Navigation.findNavController(view);
      userReference = FirebaseDatabase.getInstance().getReference("users");
      availableReference = FirebaseDatabase.getInstance().getReference("available");
      gameReference = FirebaseDatabase.getInstance().getReference("games");
      TextView welcome = view.findViewById(R.id.txt_score);
      if (mAuth.getCurrentUser() == null) {
          mNavController.navigate(R.id.action_need_auth);
      } else {
          list = new ArrayList<>();
          RecyclerView entriesList = view.findViewById(R.id.list);
          entriesList.setLayoutManager(new LinearLayoutManager(getActivity()));
          OpenGamesAdapter adapter = new OpenGamesAdapter(getActivity(), list);
          entriesList.setAdapter(adapter);

          availableReference.addValueEventListener(new ValueEventListener() {
              @SuppressLint("NotifyDataSetChanged")
              @Override
              public void onDataChange(@NonNull DataSnapshot snapshot) {
                  list.clear();
                  for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                      InternetGameDetails internetGameDetails = dataSnapshot.getValue(InternetGameDetails.class);
                      list.add(internetGameDetails);
                  }
                  adapter.notifyDataSetChanged();
              }

              @Override
              public void onCancelled(@NonNull DatabaseError error) {

              }
          });

          adapter.setOnItemClickListener(new OpenGamesAdapter.onItemClickListener() {
              @Override
              public void onItemClick(View view, int position) {
                  mEntry = adapter.mEntries.get(position);
                  String gameType = "SECOND";
                  NavDirections action = DashboardFragmentDirections.actionGame(gameType, mEntry.getPlayeruid());
                  mNavController.navigate(action);
                  availableReference.child(mEntry.getPlayeruid()).removeValue();
              }
          });

          userReference.addValueEventListener(new ValueEventListener() {
              @SuppressLint("SetTextI18n")
              @Override
              public void onDataChange(@NonNull DataSnapshot snapshot) {
                  PlayerDetails playerInfo = snapshot.child(uniqueID).getValue(PlayerDetails.class);
                  welcome.setText(convertString(playerInfo.getUsername(), playerInfo.getWins(), playerInfo.getLosses()));
                  currentUser = playerInfo.getUsername();
              }

              @Override
              public void onCancelled(@NonNull DatabaseError error) {
              }
          });


          // TODO if a user is not logged in, go to LoginFragment

          // Show a dialog when the user clicks the "new game" button
          view.findViewById(R.id.fab_new_game).setOnClickListener(v -> {

              // A listener for the positive and negative buttons of the dialog
              DialogInterface.OnClickListener listener = (dialog, which) -> {
                  String gameType = "No type";
                  if (which == DialogInterface.BUTTON_POSITIVE) {
                      gameType = getString(R.string.two_player);
                      uniqueID = mAuth.getCurrentUser().getUid();
                        InternetGameDetails internetGameDetails = new InternetGameDetails(currentUser, uniqueID);
                        availableReference.child(uniqueID).setValue(internetGameDetails);
                  }
                  else if (which == DialogInterface.BUTTON_NEGATIVE) {
                      gameType = getString(R.string.one_player);
                  }
                  Log.d(TAG, "New Game: " + gameType);

                  // Passing the game type as a parameter to the action
                  // extract it in GameFragment in a type safe way
                  NavDirections action = DashboardFragmentDirections.actionGame(gameType,"");
                  mNavController.navigate(action);
              };

              // create the dialog
              AlertDialog dialog = new AlertDialog.Builder(requireActivity())
                      .setTitle(R.string.new_game)
                      .setMessage(R.string.new_game_dialog_message)
                      .setPositiveButton(R.string.two_player, listener)
                      .setNegativeButton(R.string.one_player, listener)
                      .setNeutralButton(R.string.cancel, (d, which) -> d.dismiss())
                      .create();
              dialog.show();
          });
      }
  }

  @Override
  public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
    super.onCreateOptionsMenu(menu, inflater);
    inflater.inflate(R.menu.menu_logout, menu);
    // this action menu is handled in MainActivity
  }
  public void onAttach(@NonNull Context context) {
        super.onAttach(context);
          try{
              mCallbacks = (Callbacks) context;
          } catch(ClassCastException ignored){}
    }

    public String convertString(String user,int w,int l) {
        return ("Welcome "+user+"\n"+"Your Wins are:   "+w+"\n"+"Your Losses are: "+l+"\n\n"+"List of available games are:");
    }
}