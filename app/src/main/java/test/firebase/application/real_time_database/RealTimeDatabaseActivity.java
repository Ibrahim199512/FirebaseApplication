package test.firebase.application.real_time_database;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import test.firebase.application.modules.Category;
import test.firebase.application.R;
import test.firebase.application.adapter.CategoryAdapter;

public class RealTimeDatabaseActivity extends AppCompatActivity {

    String TAG = "RealTimeDatabaseActivity";
    private DatabaseReference mDatabase;

    Button addNewCategory, editCategory, deleteCategory;
    EditText title, categoryId;
    RecyclerView recyclerView;
    CategoryAdapter categoryAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_real_time_database);

        title = findViewById(R.id.title);
        categoryId = findViewById(R.id.category_id);
        addNewCategory = findViewById(R.id.add_new_category);
        editCategory = findViewById(R.id.edit_new_category);
        deleteCategory = findViewById(R.id.delete_new_category);
        recyclerView = findViewById(R.id.recyclerview);


        mDatabase = FirebaseDatabase.getInstance().getReference();


        addNewCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (categoryId.getText().toString().isEmpty()) {
                    createFailSnackbar(findViewById(R.id.layout), "Enter Id For Category").show();
                } else if (title.getText().toString().isEmpty()) {
                    createFailSnackbar(findViewById(R.id.layout), "Enter Title For Category").show();
                } else {
                    writeNewCategory(categoryId.getText().toString()
                            , title.getText().toString()
                            , "");
                }
            }
        });

        editCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (categoryAdapter.getSelectedCategory() != null) {
                    editCategory();
                } else {
                    createFailSnackbar(findViewById(R.id.layout), "Select Item First").show();
                }
            }
        });

        deleteCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (categoryAdapter.getSelectedCategory() != null) {
                    deleteCategory();
                } else {
                    createFailSnackbar(findViewById(R.id.layout), "Select Item First").show();
                }
            }
        });


        readAllCategory(true);
    }

    private void deleteCategory() {
        mDatabase.child("category").child(categoryAdapter.getSelectedCategory().getCategoryId())
                .removeValue()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        createSuccessSnackbar(findViewById(R.id.layout), "The Item Deleted successfully").show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Write failed
                        // ...
                        createFailSnackbar(findViewById(R.id.layout), "Something Went Wrong").show();
                    }
                });

    }

    private void editCategory() {
        Category temp = categoryAdapter.getSelectedCategory();
        temp.setCategoryTitle(title.getText().toString());

        Map<String, Object> categoryValues = temp.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/category/" + temp.getCategoryId(), categoryValues);
        mDatabase.updateChildren(childUpdates)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Write was successful!
                        // ...
                        createSuccessSnackbar(findViewById(R.id.layout), "The Item Updated successfully").show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Write failed
                        // ...
                        createFailSnackbar(findViewById(R.id.layout), "Something Went Wrong").show();
                    }
                });
    }

    private void setUpRecyclerView(List<Category> categoryList) {
        categoryAdapter = new CategoryAdapter(categoryList, RealTimeDatabaseActivity.this);
        RecyclerView.LayoutManager mLayoutManager1 = new LinearLayoutManager(RealTimeDatabaseActivity.this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(mLayoutManager1);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(categoryAdapter);
        categoryAdapter.notifyDataSetChanged();
        recyclerView.setHasFixedSize(true);
    }

    private void readAllCategory(boolean realTime) {
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                List<Category> temp = new ArrayList<>();
                for (DataSnapshot adSnapshot : dataSnapshot.getChildren()) {
                    Category category = adSnapshot.getValue(Category.class);
                    temp.add(category);
                    if (category != null)
                        Log.d("categoryId", category.getCategoryId() + "");
                }
                setUpRecyclerView(temp);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        };
        if (realTime)
            mDatabase.child("category").addValueEventListener(postListener);
        else
            mDatabase.child("category").addListenerForSingleValueEvent(postListener);

    }


    private void writeNewCategory(String categoryId, String categoryTitle, String categoryImageUrl) {
        Category category = new Category(categoryId, categoryTitle, categoryImageUrl);
        mDatabase.child("category").child(categoryId).setValue(category)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Write was successful!
                        // ...
                        createSuccessSnackbar(findViewById(R.id.layout), "The Item Added successfully").show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Write failed
                        // ...
                        createFailSnackbar(findViewById(R.id.layout), "Something Went Wrong").show();
                    }
                });
    }

    public Snackbar createSuccessSnackbar(@NonNull View view, @NonNull String message) {
        Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG);
        snackbar.getView().setBackgroundColor(view.getContext().getResources().getColor(R.color.colorPrimary));

        return snackbar;
    }

    public Snackbar createFailSnackbar(@NonNull View view, @NonNull String message) {
        Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG);
        snackbar.getView().setBackgroundColor(view.getContext().getResources().getColor(R.color.fail_color));

        return snackbar;
    }
}
