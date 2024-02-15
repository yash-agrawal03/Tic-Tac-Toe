package androidsamples.java.tictactoe;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.util.Random;

public class GameFragment extends Fragment {
  private static final String TAG = "GameFragment";
  private static final int GRID_SIZE = 16;

  private final Button[] mButtons = new Button[GRID_SIZE];
  private boolean mIsDialogShowing = false;
  private String mDialogMessage = "";
  private NavController mNavController;
  private boolean mIsGameInProgress;

  private String gameStatus;
  private ValueEventListener mValueEventListener;

  private TextView result;

  private FirebaseAuth mAuth;

  private DatabaseReference rootReference,games;

  private String Player1,Player2;
  int mode =-1;
  // 0 depicts null state
  // 1 depicts move from player one : "O"
  // 2 depicts move from player two (or computer for 1 player game)  : "X"


  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setHasOptionsMenu(true); // Needed to display the action menu for this fragment

    // Extract the argument passed with the action in a type-safe way
    GameFragmentArgs args = GameFragmentArgs.fromBundle(getArguments());
    Log.d(TAG, "New game type = " + args.getGameType());

    mIsGameInProgress = false;
    gameStatus = "0000000000000000";
    mAuth = FirebaseAuth.getInstance();

    // Handle the back press by adding a confirmation dialog
    OnBackPressedCallback callback = new OnBackPressedCallback(true) {
      @Override
      public void handleOnBackPressed() {
        Log.d(TAG, "Back pressed");

        // TODO show dialog only when the game is still in progress
        if(!mIsGameInProgress) {
          AlertDialog dialog = new AlertDialog.Builder(requireActivity())
                  .setTitle(R.string.confirm)
                  .setMessage(R.string.forfeit_game_dialog_message)
                  .setPositiveButton(R.string.yes, (d, which) -> {
                    // TODO update loss count
                    mNavController.popBackStack();
                    rootReference.child("users").child(mAuth.getCurrentUser().getUid()).child("losses").setValue(ServerValue.increment(1));
                  })
                  .setNegativeButton(R.string.cancel, (d, which) -> d.dismiss())
                  .create();
            dialog.show();
        }
        else {
          mNavController.popBackStack();
        }

      }
    };
    requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
    mValueEventListener = new ValueEventListener() {
      @Override
      public void onDataChange(@NonNull DataSnapshot snapshot) {
        // Update your game state here...
        gameStatus = snapshot.child("gameStatus").getValue().toString();
        Log.d(TAG, "yahape update hota toh hai");
        // Call a separate method to update your UI
        if (getView() != null) {
          updateUI();
        }
      }

      @Override
      public void onCancelled(@NonNull DatabaseError error) {
      }
    };
  }

  @Override
  public View onCreateView(LayoutInflater inflater,
                           ViewGroup container,
                           Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_game, container, false);
  }
  @Override
  public void onDestroyView() {
    super.onDestroyView();
    // Remove the Firebase listeners here
    if (mValueEventListener != null && mode == 2 && !mIsGameInProgress) {
      //games.child(Player1).removeEventListener(mValueEventListener);
    }
  }
  @Override
  public void onSaveInstanceState(@NonNull Bundle outState) {
    super.onSaveInstanceState(outState);
    outState.putString("gameStatus", gameStatus);
    outState.putBoolean("mIsGameInProgress", mIsGameInProgress);
    outState.putBoolean("mIsDialogShowing", mIsDialogShowing);
    outState.putString("mDialogMessage", mDialogMessage);
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    rootReference = FirebaseDatabase.getInstance().getReference();
    games = FirebaseDatabase.getInstance().getReference("games");
    mNavController = Navigation.findNavController(view);
    result = view.findViewById(R.id.txt_result);

    mButtons[0] = view.findViewById(R.id.button0);
    mButtons[1] = view.findViewById(R.id.button1);
    mButtons[2] = view.findViewById(R.id.button2);
    mButtons[3] = view.findViewById(R.id.button2e);  // Additional button for the fourth column

    mButtons[4] = view.findViewById(R.id.button3);
    mButtons[5] = view.findViewById(R.id.button4);
    mButtons[6] = view.findViewById(R.id.button5);
    mButtons[7] = view.findViewById(R.id.button5e);  // Additional button for the fourth column

    mButtons[8] = view.findViewById(R.id.button6);
    mButtons[9] = view.findViewById(R.id.button7);  // Additional button for the fourth column
    mButtons[10] = view.findViewById(R.id.button8);
    mButtons[11] = view.findViewById(R.id.button8e);  // Additional button for the fourth column

    mButtons[12] = view.findViewById(R.id.button6e);
    mButtons[13] = view.findViewById(R.id.button7e);  // Additional button for the fourth column
    mButtons[14] = view.findViewById(R.id.button8ed); // Additional button for the fourth column
    mButtons[15] = view.findViewById(R.id.button8ee); // Additional button for the fourth column

    GameFragmentArgs args = GameFragmentArgs.fromBundle(getArguments());
    if (savedInstanceState != null) {
      gameStatus = savedInstanceState.getString("gameStatus");
      mIsGameInProgress = savedInstanceState.getBoolean("mIsGameInProgress");
      mIsDialogShowing = savedInstanceState.getBoolean("mIsDialogShowing");
      mDialogMessage = savedInstanceState.getString("mDialogMessage");
      if (mIsDialogShowing) {
        showDialogAndNavigate(mDialogMessage);
      }
      updateUI();
    }
    // Reattach the ValueEventListener here


    if(args.getGameType().equals("One-Player"))
    {
      for(int i=0;i<mButtons.length;i++)
      {
        int finalI= i;
        mButtons[i].setOnClickListener(v->{
          Log.d(TAG, "Button " + finalI + " clicked");
          if(gameStatus.charAt(finalI)!='0')
          {
            Toast.makeText(getContext(), "Cell already filled", Toast.LENGTH_SHORT).show();
          }
          else if(!mIsGameInProgress)
          {
            mButtons[finalI].setText("X");
            //updating the string with a 1
            gameStatus = gameStatus.substring(0,finalI)+"1"+gameStatus.substring(finalI+1);
            if(checkGameResult(gameStatus)==0)
            {
              Random rand = new Random();
              int rand_int1 = rand.nextInt(16); // Random number between 0 and 15
              while(gameStatus.charAt(rand_int1)!='0')
              {
                rand_int1++;
                if(rand_int1==16) // If reached the end, start from 0
                {
                  rand_int1=0;
                }
              }
              gameStatus = gameStatus.substring(0,rand_int1)+"2"+gameStatus.substring(rand_int1+1);
              mButtons[rand_int1].setText("O");
            }
            if (checkGameResult(gameStatus) == 1) {
              showDialogAndNavigate("You Win. Press OK to go back to main menu");
              rootReference.child("users").child(mAuth.getCurrentUser().getUid()).child("wins").setValue(ServerValue.increment(1));
              mIsGameInProgress = true;
            } else if (checkGameResult(gameStatus) == 2) {
              showDialogAndNavigate("You Lose. Press OK to go back to main menu");
              rootReference.child("users").child(mAuth.getCurrentUser().getUid()).child("losses").setValue(ServerValue.increment(1));
              mIsGameInProgress = true;
            } else if (checkGameResult(gameStatus) == 3) {
              showDialogAndNavigate("Game is Draw. Press OK to go back to main menu");
              mIsGameInProgress = true;
            }
          }
        } );
      }
    }
    else if(args.getGameType().equals("Two-Player"))
    {
      Log.d(TAG, "Two-Player Initialized");
      mode =2;
      Player1 = mAuth.getCurrentUser().getUid();
      TwoPlayerGame twoPlayerGame = new TwoPlayerGame(Player1);
      games.child(Player1).setValue(twoPlayerGame);
      mValueEventListener = new ValueEventListener()
      {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot)
        {
          gameStatus = snapshot.child("gameStatus").getValue().toString();
          updateUI();
          if (!snapshot.child("secondPlayer").getValue().equals("") && snapshot.child("gameStatus").getValue().equals("0000000000000000")) {
            Toast.makeText(getActivity(), "Second player joined, you can make the first move", Toast.LENGTH_SHORT).show();
          }
          if (snapshot.child("gameResult").getValue().equals("2") && !mIsGameInProgress) {
            showDialogAndNavigate("You Lose. Press OK to go back to main menu");
            rootReference.child("users").child(mAuth.getCurrentUser().getUid()).child("losses").setValue(ServerValue.increment(1));
            mIsGameInProgress=true;
          }

          for(int i=0;i<mButtons.length;i++)
          {
            int finalI= i;
            mButtons[i].setOnClickListener(v->{
              Log.d(TAG, "Button " + finalI + " clicked");
              if(gameStatus.charAt(finalI)!='0')
              {
                Toast.makeText(getContext(), "Cell already filled", Toast.LENGTH_SHORT).show();
              }
              else if(!mIsGameInProgress && Integer.parseInt(snapshot.child("turn").getValue().toString())==1 && !snapshot.child("secondPlayer").getValue().equals(""))
              {
                mButtons[finalI].setText("X");
                //updating the string with a 1
                gameStatus = gameStatus.substring(0,finalI)+"1"+gameStatus.substring(finalI+1);
                games.child(Player1).child("gameStatus").setValue(gameStatus);

                if(checkGameResult(gameStatus)==1)
                {
                  showDialogAndNavigate("You Win. Press OK to go back to main menu");
                  rootReference.child("users").child(mAuth.getCurrentUser().getUid()).child("wins").setValue(ServerValue.increment(1));
                  mIsGameInProgress = true;
                  games.child(Player1).child("gameResult").setValue("1");
                }
                else if(checkGameResult(gameStatus)==3)
                {
                  showDialogAndNavigate("Game is Draw. Press OK to go back to main menu");
                  mIsGameInProgress = true;
                  games.child(Player1).child("gameResult").setValue("3");
                }
                games.child(Player1).child("turn").setValue(2);
              }
            } );
          }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {
        }
      };games.child(Player1).addValueEventListener(mValueEventListener);
    }
    else{
      Log.d(TAG, "I am the new player");
      Player1=args.getUid();
      mode=2;
      Player2=mAuth.getCurrentUser().getUid();
      games.child(Player1).child("secondPlayer").setValue(Player2);
      mValueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
          gameStatus = snapshot.child("gameStatus").getValue().toString();
          updateUI();
          if (snapshot.child("gameResult").getValue().equals("1") && !mIsGameInProgress) {
            showDialogAndNavigate("You Lose. Press OK to go back to main menu");
            rootReference.child("users").child(mAuth.getCurrentUser().getUid()).child("losses").setValue(ServerValue.increment(1));
            mIsGameInProgress = true;
          }
          if (snapshot.child("gameResult").getValue().equals("3") && !mIsGameInProgress) {
            showDialogAndNavigate("Game is Draw. Press OK to go back to main menu");
            mIsGameInProgress = true;
          }

          for (int i = 0; i < mButtons.length; i++) {
            int finalI = i;
            mButtons[i].setOnClickListener(v -> {
              if (gameStatus.charAt(finalI) != '0') {
                Toast.makeText(getContext(), "Cell already filled", Toast.LENGTH_SHORT).show();
              }
              else if(!mIsGameInProgress && Integer.parseInt(snapshot.child("turn").getValue().toString())==2){
                mButtons[finalI].setText("O");
                gameStatus = gameStatus.substring(0, finalI) + '2' + gameStatus.substring(finalI + 1);
                games.child(Player1).child("gameStatus").setValue(gameStatus);
                if (checkGameResult(gameStatus) == 2) {
                  showDialogAndNavigate("You Win. Press OK to go back to main menu");
                  rootReference.child("users").child(mAuth.getCurrentUser().getUid()).child("wins").setValue(ServerValue.increment(1));
                  mIsGameInProgress = true;
                  games.child(Player1).child("gameResult").setValue("2");
                }
                games.child(Player1).child("turn").setValue(1);
              }
            });
          }
        }
        @Override
        public void onCancelled(@NonNull DatabaseError error) {
        }
      };games.child(Player1).addValueEventListener(mValueEventListener);
    }
    updateUI();
  }

  @Override
  public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
    super.onCreateOptionsMenu(menu, inflater);
    inflater.inflate(R.menu.menu_logout, menu);
    // this action menu is handled in MainActivity
  }
  private void updateUI()
  {
    for(int i=0;i<mButtons.length;i++)
    {
      if(gameStatus.charAt(i)=='1')
      {
        mButtons[i].setText("X");
      }
      else if(gameStatus.charAt(i)=='2')
      {
        mButtons[i].setText("O");
      }
    }
  }
  private void showDialogAndNavigate(String message) {
    mIsDialogShowing = true;
    mDialogMessage = message;
    new AlertDialog.Builder(requireActivity())
            .setTitle("Game Result")
            .setMessage(message)
            .setPositiveButton("OK", (dialog, which) -> {
              mNavController.popBackStack();
              mIsDialogShowing = false;
            })
            .setCancelable(false)
            .show();
  }
  int checkGameResult(String a) {
    int[][] win = {
            {0, 1, 2, 3}, {4, 5, 6, 7}, {8, 9, 10, 11}, {12, 13, 14, 15}, // rows
            {0, 4, 8, 12}, {1, 5, 9, 13}, {2, 6, 10, 14}, {3, 7, 11, 15}, // columns
            {0, 5, 10, 15}, {3, 6, 9, 12} // diagonals
    };

    for (int i = 0; i < 10; i++) {
      if (a.charAt(win[i][0]) != '0' &&
              a.charAt(win[i][0]) == a.charAt(win[i][1]) &&
              a.charAt(win[i][1]) == a.charAt(win[i][2]) &&
              a.charAt(win[i][2]) == a.charAt(win[i][3])) {
        if (a.charAt(win[i][0]) == '1')
          return 1;
        else
          return 2;
      }
    }

    for(int i=0; i<16; i++) {
      if(a.charAt(i)=='0')
        return 0;
    }

    return 3;
  }
}
// 0 1 2
// 3 4 5
// 6 7 8

