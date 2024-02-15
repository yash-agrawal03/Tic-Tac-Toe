package androidsamples.java.tictactoe;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class OpenGamesAdapter extends RecyclerView.Adapter<OpenGamesAdapter.ViewHolder> {

    private LayoutInflater mInflater;
    List<InternetGameDetails> mEntries;
    public onItemClickListener listener;

    public OpenGamesAdapter(Context context, List<InternetGameDetails> mEntries ) {
        mInflater = LayoutInflater.from(context);
        this.mEntries=mEntries;
    }

  public OpenGamesAdapter() {
    // FIXME if needed
  }

  @NonNull
  @Override
  public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext())
        .inflate(R.layout.fragment_item, parent, false);
    return new ViewHolder(view);
  }

  @Override
  public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
    // TODO bind the item at the given position to the holder
    if( mEntries!= null){
      InternetGameDetails current = mEntries.get(position);
      holder.mContentView.setText(current.getPlayername());
    }
  }

  @Override
  public int getItemCount() {
     // FIXME
    if (mEntries == null) {
      return 0;
    } else {
      return mEntries.size();
    }
  }

  public class ViewHolder extends RecyclerView.ViewHolder {
    public final View mView;
   // public final TextView mIdView;
    public final TextView mContentView;

    public ViewHolder(View view) {
      super(view);
      mView = view;
    //  mIdView = view.findViewById(R.id.item_number);
      mContentView = view.findViewById(R.id.content);
      view.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          int position=getAdapterPosition();
          if(listener != null && position!=RecyclerView.NO_POSITION)
            listener.onItemClick(itemView,position);
        }
      });
    }

    @NonNull
    @Override
    public String toString() {
      return super.toString() + " '" + mContentView.getText() + "'";
    }
  }
    public interface onItemClickListener{
        void onItemClick(View itemView, int position);
    }
    public void setOnItemClickListener(onItemClickListener listener){
        this.listener=listener;
    }
}