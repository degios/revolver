package it.infodati.revolver.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import it.infodati.revolver.R;
import it.infodati.revolver.listener.ListItemClickListener;
import it.infodati.revolver.listener.ListItemLongClickListener;
import it.infodati.revolver.model.Link;
import it.infodati.revolver.util.GlobalVar;

public class ActionsAdapter extends RecyclerView.Adapter<ActionsAdapter.ActionViewHolder> {

    private List<Link> list;
    private final ListItemClickListener onClickListener;
    private final ListItemLongClickListener onLongClickListener;
    private Context context;


    // Constructor
    public ActionsAdapter(Fragment fragment) {
        this.onClickListener = (ListItemClickListener) fragment;
        this.onLongClickListener = (ListItemLongClickListener) fragment;
    }

    @NonNull
    @Override
    public ActionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.row_actions, parent, false);
        return new ActionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ActionViewHolder holder, int position) {
        holder.bind(list.get(position));
    }

    @Override
    public int getItemCount() {
        int count;
        if (list == null) {
            count = 0;
        } else {
            count = list.size();
        }
        return count;
    }

    public void setAdapterList(List<Link> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public Link getItem(int position) {
        return list.get(position);
    }

    // Inner ViewHolder
    public class ActionViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        private final TextView id;
        private final ImageView icon;
        private final TextView description;

        public ActionViewHolder(@NonNull View itemView) {
            super(itemView);

            this.id = (TextView) itemView.findViewById(R.id.id);
            this.icon = (ImageView) itemView.findViewById(R.id.icon);
            this.description = (TextView) itemView.findViewById(R.id.description);

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        public void bind(Link model) {
            this.id.setText(String.valueOf(model.getId()));
            this.description.setText(model.getDescription());

            // Set as preferred
            if (GlobalVar.getInstance().getLinkId()==model.getId()) {
                this.description.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_outline_star_border_24, 0);
            } else {
                this.description.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            }

            String iconName = context.getResources().getResourceEntryName(android.R.drawable.ic_menu_myplaces);
            int iconId = context.getResources().getIdentifier(iconName, "drawable", "android");
            if (model.getIcon()!=null && model.getIcon().trim().toUpperCase().startsWith("HTTP")) {
                Glide.with(this.icon.getContext()).load(model.getIcon()).error(iconId).into(this.icon);
            } else {
                this.icon.setImageResource(iconId);
            }
        }

        private void bindOrHideTextView(TextView textView, String data) {
            if (data == null) {
                textView.setVisibility(View.GONE);
            } else  {
                textView.setText(data);
                textView.setVisibility(View.VISIBLE);
            }
        }

        // getter
        public TextView getId() { return this.id; }
        public ImageView getIcon() { return this.icon; }
        public TextView getDescription() { return this.description; }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            onClickListener.onListItemClick(position);
        }

        @Override
        public boolean onLongClick(View v) {
            int position = getAdapterPosition();
            return onLongClickListener.onListItemLongClick(position);
        }
    }
}
