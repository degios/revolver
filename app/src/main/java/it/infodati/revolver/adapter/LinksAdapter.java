package it.infodati.revolver.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import it.infodati.revolver.R;
import it.infodati.revolver.listener.ListItemClickListener;
import it.infodati.revolver.model.Link;
import it.infodati.revolver.util.GlobalVar;

import java.util.List;

public class LinksAdapter extends RecyclerView.Adapter<LinksAdapter.LinkViewHolder> {

    private List<Link> list;
    private final ListItemClickListener onClickListener;
    private Context context;


    // Constructor
    public LinksAdapter(Fragment fragment) {
        this.onClickListener = (ListItemClickListener) fragment;
    }

    @NonNull
    @Override
    public LinkViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.row_links, parent, false);
        return new LinkViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LinkViewHolder holder, int position) {
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
    public class LinkViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final TextView id;
        private final TextView url;
        private final ImageView icon;
        private final TextView title;

        public LinkViewHolder(@NonNull View itemView) {
            super(itemView);

            this.id = (TextView) itemView.findViewById(R.id.id);
            this.icon = (ImageView) itemView.findViewById(R.id.icon);
            this.title = (TextView) itemView.findViewById(R.id.title);
            this.url = (TextView) itemView.findViewById(R.id.url);

            itemView.setOnClickListener(this);
        }

        public void bind(Link model) {
            this.id.setText(String.valueOf(model.getId()));
            this.title.setText(model.getTitle());
            this.url.setText(model.getUrl());

            // Set as preferred
            if (GlobalVar.getInstance().getLinkId()==model.getId()) {
                this.title.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_outline_star_border_24, 0);
            } else {
                this.title.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
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
        public TextView getUrl() { return this.url; }
        public ImageView getIcon() { return this.icon; }
        public TextView getTitle() { return this.title; }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            onClickListener.onListItemClick(position);
        }
    }
}
