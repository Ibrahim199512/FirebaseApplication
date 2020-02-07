package test.firebase.application.real_time_database;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import test.firebase.application.Modules.Category;
import test.firebase.application.R;

public class FirebaseRecyclerAdapterActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private FirebaseRecyclerAdapter adapter;
    Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firebase_recycler_adapter);

        recyclerView = findViewById(R.id.recyclerview);

        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        fetch();


    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView categoryTitle, categoryId;
        LinearLayout linearLayout;

        public MyViewHolder(View view) {
            super(view);
            categoryTitle = view.findViewById(R.id.category_title);
            categoryId = view.findViewById(R.id.category_id);
            linearLayout = view.findViewById(R.id.linearLayout);
        }

        public void setCategoryTitle(String string) {
            categoryTitle.setText(string);
        }


        public void setCategoryId(String string) {
            categoryId.setText(string);
        }
    }


    private void fetch() {
        Query query = FirebaseDatabase.getInstance()
                .getReference()
                .child("category");

        FirebaseRecyclerOptions<Category> options =
                new FirebaseRecyclerOptions.Builder<Category>()
                        .setQuery(query, new SnapshotParser<Category>() {
                            @NonNull
                            @Override
                            public Category parseSnapshot(@NonNull DataSnapshot snapshot) {
                                return new Category(snapshot.child("categoryId").getValue().toString(),
                                        snapshot.child("categoryTitle").getValue().toString(),
                                        snapshot.child("categoryImageUrl").getValue().toString());
                            }
                        })
                        .build();

        adapter = new FirebaseRecyclerAdapter<Category, MyViewHolder>(options) {
            int selected = -1;

            @Override
            public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.category_item, parent, false);

                return new MyViewHolder(view);
            }


            @Override
            protected void onBindViewHolder(MyViewHolder holder, final int position, Category category) {
                holder.setCategoryTitle(category.getCategoryTitle());
                holder.setCategoryId(category.getCategoryId());

                if (selected == position) {
                    holder.linearLayout.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
                } else {
                    holder.linearLayout.setBackgroundColor(context.getResources().getColor(R.color.gray_color));
                }

                holder.linearLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        selected = position;
                        notifyDataSetChanged();
                    }
                });
            }

        };
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

}
