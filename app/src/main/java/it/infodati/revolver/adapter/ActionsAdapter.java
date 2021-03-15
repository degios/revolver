package it.infodati.revolver.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.core.graphics.drawable.WrappedDrawable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import it.infodati.revolver.R;
import it.infodati.revolver.listener.ListItemClickListener;
import it.infodati.revolver.listener.ListItemLongClickListener;
import it.infodati.revolver.model.Link;
import it.infodati.revolver.util.GlobalVar;

public class ActionsAdapter extends RecyclerView.Adapter<ActionsAdapter.ActionViewHolder> {

    public static int LISTVIEW = 0;
    public static int GRIDVIEW = 1;

    private List<Link> list;
    private final ListItemClickListener onClickListener;
    private final ListItemLongClickListener onLongClickListener;
    private Context context;
    private int itemViewType;

    // Constructor
    public ActionsAdapter(Fragment fragment, int itemViewType) {
        this.onClickListener = (ListItemClickListener) fragment;
        this.onLongClickListener = (ListItemLongClickListener) fragment;
        this.itemViewType = itemViewType;
    }

    @NonNull
    @Override
    public ActionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(
                (viewType==GRIDVIEW ? R.layout.item_actions : R.layout.row_actions), parent, false);
        return new ActionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ActionViewHolder holder, int position) {
        holder.bind(list.get(position));
    }

    @Override
    public int getItemViewType(int position) {
        return this.itemViewType;
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
        private final TextView title;

        public ActionViewHolder(@NonNull View itemView) {
            super(itemView);

            this.id = (TextView) itemView.findViewById(R.id.id);
            this.icon = (ImageView) itemView.findViewById(R.id.icon);
            this.title = (TextView) itemView.findViewById(R.id.title);

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        public void bind(Link model) {
            this.id.setText(String.valueOf(model.getId()));
            this.title.setText(model.getTitle());

            // Set as preferred
            if (model.hasAutorun()) {
                if (itemViewType==GRIDVIEW) {
                    this.title.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, R.drawable.ic_outline_run_circle_24);
                } else {
                    this.title.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_outline_run_circle_24, 0);
                }
            } else if (model.hasBookmark()) {
                if (itemViewType==GRIDVIEW) {
                    this.title.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, R.drawable.ic_outline_star_border_24);
                } else {
                    this.title.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_outline_star_border_24, 0);
                }
            } else {
                if (itemViewType==GRIDVIEW) {
                    Drawable transparent = context.getResources().getDrawable(android.R.drawable.ic_menu_camera);
                    DrawableCompat.setTint(transparent,Color.TRANSPARENT);
                    this.title.setCompoundDrawablesWithIntrinsicBounds(null, null, null, transparent);
                } else {
                    this.title.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                }
            }

            String iconName = context.getResources().getResourceEntryName(android.R.drawable.ic_menu_myplaces);
            int iconId = context.getResources().getIdentifier(iconName, "drawable", "android");
            byte[] arIcon = model.getIcon();
            if (arIcon!=null && arIcon.length>0) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(arIcon, 0, arIcon.length);
                this.icon.setImageBitmap(bitmap);
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
        public TextView getTitle() { return this.title; }

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
