package test.firebase.application.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import test.firebase.application.Modules.Category;
import test.firebase.application.R;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.MyViewHolder> {
    List<Category> categoryList;
    Context context;
    private int selected = -1;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView categoryTitle, categoryId;
        LinearLayout linearLayout;

        public MyViewHolder(View view) {
            super(view);
            categoryTitle = view.findViewById(R.id.category_title);
            categoryId = view.findViewById(R.id.category_id);
            linearLayout = view.findViewById(R.id.linearLayout);
        }
    }


    public CategoryAdapter(List<Category> categoryList, Context context) {
        this.categoryList = categoryList;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.category_item, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        holder.categoryTitle.setText(categoryList.get(position).getCategoryTitle());
        holder.categoryId.setText(categoryList.get(position).getCategoryId());

        if (selected == position) {
            holder.linearLayout.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
        } else {
            holder.linearLayout.setBackgroundColor(context.getResources().getColor(R.color.gray_color));
        }

        setUpActions(holder, position);
    }

    private void setUpActions(final MyViewHolder holder, final int position) {

        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selected = position;
                notifyDataSetChanged();
            }
        });

    }

    public Category getSelectedCategory() {
        if (selected != -1) {
            return categoryList.get(selected);
        } else
            return null;
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }


}
