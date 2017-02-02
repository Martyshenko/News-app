package savern.newsapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.R.attr.description;

public class NewsAdapter extends ArrayAdapter<News> {


    public NewsAdapter(Context context, List<News> newses) {
        super(context, 0, newses);
    }

    static class ViewHolder {
        @BindView(R.id.date)
        TextView dateText;
        @BindView(R.id.title)
        TextView titleText;
        @BindView(R.id.section)
        TextView sectionText;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        // Check if there is an existing list item view (called convertView) that we can reuse,
        // otherwise, if convertView is null, then inflate a new list item layout.
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, parent, false);
            holder = new ViewHolder(listItemView);
            listItemView.setTag(holder);
        } else {
            //if view already exists, get the holder instance from the view
            holder = (ViewHolder) listItemView.getTag();
        }
        final News currentNews = getItem(position);

        String author = currentNews.getDate();
        // Display the date of the current news in the TextView
        holder.dateText.setText(author);

        String title = currentNews.getTitle();
        // Display the tile of the current news in the TextView
        holder.titleText.setText(title);

        String section = currentNews.getDescription();
        // Display the section of the current news in the TextView
        holder.sectionText.setText(section);

        return listItemView;
    }
}