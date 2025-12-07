package com.warmclouds.app.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.warmclouds.app.AddEditNurseryActivity;
import com.warmclouds.app.R;
import com.warmclouds.app.adapters.AdminNurseryAdapter;
import com.warmclouds.app.models.Nursery;

import java.util.ArrayList;
import java.util.List;

public class NurseriesFragment extends Fragment {

    private RecyclerView nurseriesRecyclerView;
    private FloatingActionButton addNurseryFab;
    private ProgressBar progressBar;
    private ConstraintLayout emptyStateLayout;

    private AdminNurseryAdapter adapter;
    private List<Nursery> nurseries;
    private FirebaseFirestore db;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_nurseries, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        db = FirebaseFirestore.getInstance();
        nurseries = new ArrayList<>();

        initializeViews(view);
        setupRecyclerView();
        loadNurseries();
    }

    private void initializeViews(View view) {
        nurseriesRecyclerView = view.findViewById(R.id.nurseriesRecyclerView);
        addNurseryFab = view.findViewById(R.id.addNurseryFab);
        progressBar = view.findViewById(R.id.progressBar);
        emptyStateLayout = view.findViewById(R.id.emptyStateLayout);

        addNurseryFab.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), AddEditNurseryActivity.class);
            startActivity(intent);
        });
    }

    private void setupRecyclerView() {
        nurseriesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new AdminNurseryAdapter(nurseries, new AdminNurseryAdapter.OnNurseryActionListener() {
            @Override
            public void onEditClick(Nursery nursery) {
                Intent intent = new Intent(getContext(), AddEditNurseryActivity.class);
                intent.putExtra("nursery_id", nursery.getId());
                startActivity(intent);
            }

            @Override
            public void onDeleteClick(Nursery nursery) {
                deleteNursery(nursery);
            }
        });
        nurseriesRecyclerView.setAdapter(adapter);
    }

    private void loadNurseries() {
        if (progressBar != null) {
            progressBar.setVisibility(View.VISIBLE);
        }
        if (nurseriesRecyclerView != null) {
            nurseriesRecyclerView.setVisibility(View.GONE);
        }
        if (emptyStateLayout != null) {
            emptyStateLayout.setVisibility(View.GONE);
        }

        db.collection("nurseries").get()
                .addOnCompleteListener(new OnCompleteListener<com.google.firebase.firestore.QuerySnapshot>() {
                    @Override
                    public void onComplete(Task<com.google.firebase.firestore.QuerySnapshot> task) {
                        if (progressBar != null) {
                            progressBar.setVisibility(View.GONE);
                        }

                        if (task.isSuccessful()) {
                            nurseries.clear();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Nursery nursery = document.toObject(Nursery.class);
                                nursery.setId(document.getId());
                                nurseries.add(nursery);
                            }
                            adapter.notifyDataSetChanged();

                            if (nurseries.isEmpty()) {
                                showEmptyState();
                            } else {
                                showNurseriesList();
                            }
                        } else {
                            Toast.makeText(getContext(), "Error loading nurseries: " +
                                    (task.getException() != null ? task.getException().getMessage() : "Unknown error"),
                                    Toast.LENGTH_SHORT).show();
                            showEmptyState();
                        }
                    }
                });
    }

    private void showEmptyState() {
        if (nurseriesRecyclerView != null) {
            nurseriesRecyclerView.setVisibility(View.GONE);
        }
        if (emptyStateLayout != null) {
            emptyStateLayout.setVisibility(View.VISIBLE);
        }
    }

    private void showNurseriesList() {
        if (nurseriesRecyclerView != null) {
            nurseriesRecyclerView.setVisibility(View.VISIBLE);
        }
        if (emptyStateLayout != null) {
            emptyStateLayout.setVisibility(View.GONE);
        }
    }

    private void deleteNursery(Nursery nursery) {
        new AlertDialog.Builder(getContext())
                .setTitle(R.string.delete_confirmation_title)
                .setMessage(R.string.delete_confirmation_message)
                .setPositiveButton(R.string.delete_confirm, (dialog, which) -> {
                    performDelete(nursery);
                })
                .setNegativeButton(R.string.delete_cancel, (dialog, which) -> {
                    dialog.dismiss();
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    private void performDelete(Nursery nursery) {
        if (progressBar != null) {
            progressBar.setVisibility(View.VISIBLE);
        }

        db.collection("nurseries").document(nursery.getId())
                .delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(Task<Void> task) {
                        if (progressBar != null) {
                            progressBar.setVisibility(View.GONE);
                        }

                        if (task.isSuccessful()) {
                            Toast.makeText(getContext(), R.string.delete_success, Toast.LENGTH_SHORT).show();
                            nurseries.remove(nursery);
                            adapter.notifyDataSetChanged();

                            if (nurseries.isEmpty()) {
                                showEmptyState();
                            } else {
                                showNurseriesList();
                            }
                        } else {
                            Toast.makeText(getContext(), R.string.delete_error + ": " +
                                    (task.getException() != null ? task.getException().getMessage() : "Unknown error"),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    public void onResume() {
        super.onResume();
        loadNurseries();
    }
}


