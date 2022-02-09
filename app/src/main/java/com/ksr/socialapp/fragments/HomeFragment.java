package com.ksr.socialapp.fragments;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.cooltechworks.views.shimmer.ShimmerRecyclerView;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.ksr.socialapp.R;
import com.ksr.socialapp.adapter.PostAdapter;
import com.ksr.socialapp.adapter.StoryAdapter;
import com.ksr.socialapp.model.Post;
import com.ksr.socialapp.model.Story;
import com.ksr.socialapp.model.User;
import com.ksr.socialapp.model.UserStories;
import com.ksr.socialapp.tools.FragmentManager;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;
import java.util.Date;

public class HomeFragment extends Fragment {

    private FirebaseDatabase firebaseDatabase;
    private FirebaseStorage firebaseStorage;
    private FirebaseAuth firebaseAuth;

    private BottomNavigationView bottomNavigationView;
    private FragmentManager fragmentManager;

    private ShimmerRecyclerView dashboardRecyclerView,storyRecyclerView;
    private ArrayList<Story> storyList;
    private ArrayList<Post> postArrayList;

    private RoundedImageView addStory, topProfileImage;
    private ActivityResultLauncher<String> galleryLauncher;
    private ProgressDialog progressDialog;


    public HomeFragment() {
        //Required Public Constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();

        progressDialog = new ProgressDialog(getContext());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);


        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setTitle("Story Uploading");
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);

        topProfileImage = view.findViewById(R.id.topProfileImage);
        addStory = view.findViewById(R.id.addStory);
        dashboardRecyclerView = view.findViewById(R.id.dashboardRecyclerView);
        storyRecyclerView = view.findViewById(R.id.storiesRecyclerView);
        bottomNavigationView = getActivity().findViewById(R.id.bottomNavigation);



        topProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomNavigationView.setSelectedItemId(R.id.nav_profile);
            }
        });


        //getting current loged in users data and setting its profile in top of the home screen
        firebaseDatabase.getReference().child("Users").child(firebaseAuth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    User user = snapshot.getValue(User.class);
                    Glide.with(getContext()).load(user.getProfile()).placeholder(R.drawable.placeholder).into(addStory);
                    Glide.with(getContext()).load(user.getProfile()).placeholder(R.drawable.placeholder).into(topProfileImage);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //showing shimmer until data is load
        ShimmerRecyclerView shimmerStoriesRecycler = (ShimmerRecyclerView) view.findViewById(R.id.storiesRecyclerView);
        shimmerStoriesRecycler.showShimmerAdapter();
        storyList = new ArrayList<>();

        StoryAdapter storyAdapter = new StoryAdapter(storyList, getContext());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        storyRecyclerView.setLayoutManager(linearLayoutManager);
        storyRecyclerView.setNestedScrollingEnabled(false);


        /*getting stories data and Storing it in Story model and
         *one user can have multiple story thats why the nested loop for userstories which will be stored in UserStories model
         *
         */

        firebaseDatabase.getReference()
                .child("stories")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            storyList.clear();
                            for (DataSnapshot storySnapshot : snapshot.getChildren()) {
                                Story story = new Story();
                                story.setStoryBy(storySnapshot.getKey());
                                story.setStoryAt(storySnapshot.child("postedBy").getValue(Long.class));

                                ArrayList<UserStories> stories = new ArrayList<>();
                                for (DataSnapshot snapshot1 : storySnapshot.child("userStories").getChildren()) {
                                    UserStories userStories = snapshot1.getValue(UserStories.class);
                                    stories.add(userStories);
                                }

                                story.setUserStories(stories);
                                storyList.add(story);
                            }

                            storyAdapter.notifyDataSetChanged();
                            //hiding shimmer adapter when data is load
                            shimmerStoriesRecycler.hideShimmerAdapter();
                            storyRecyclerView.setAdapter(storyAdapter);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


        //showing shimmer effect until data is load
        ShimmerRecyclerView shimmerDashboardRecycler = (ShimmerRecyclerView) view.findViewById(R.id.dashboardRecyclerView);
        shimmerDashboardRecycler.showShimmerAdapter();
        postArrayList = new ArrayList<>();

        PostAdapter postAdapter = new PostAdapter(postArrayList, getContext());
        LinearLayoutManager dasboardLlm = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
        dashboardRecyclerView.setLayoutManager(dasboardLlm);
        dashboardRecyclerView.setNestedScrollingEnabled(false);


        /*getting all posts from the app and storing them to Post model
         */
        firebaseDatabase.getReference().child("posts").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                postArrayList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Post post = dataSnapshot.getValue(Post.class);
                    post.setPostID(dataSnapshot.getKey());
                    postArrayList.add(post);
                }
                postAdapter.notifyDataSetChanged();

                //hiding shimmer adapter when data is load
                shimmerDashboardRecycler.hideShimmerAdapter();
                dashboardRecyclerView.setAdapter(postAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        galleryLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
            @Override
            public void onActivityResult(Uri result) {
                if (result != null) {
                    progressDialog.show();
                    /*making new directory in storage of stories inside that creating another directory of the user name
                     * inside that storing all the stories of the user according to the time
                     */
                    final StorageReference reference = firebaseStorage.getReference()
                            .child("stories")
                            .child(FirebaseAuth.getInstance().getUid())
                            .child(new Date().getTime() + "");

                    //when story is successfully uploaded creating its url and storing stories data in Story model
                    reference.putFile(result).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    Story story = new Story();
                                    story.setStoryAt(new Date().getTime());

                                    /*
                                     *creating a new node stories in database and inside that creating another node of the user's id and then postedby
                                     *and setting value as storyAt
                                     * when its succeed, making another node in it as user stories which will contains all the stories of that user
                                     * and when it will succeed progressDialog will be dismiss
                                     */
                                    firebaseDatabase.getReference()
                                            .child("stories")
                                            .child(FirebaseAuth.getInstance().getUid())
                                            .child("postedBy")
                                            .setValue(story.getStoryAt()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            UserStories userStories = new UserStories(uri.toString(), story.getStoryAt());
                                            firebaseDatabase.getReference()
                                                    .child("stories")
                                                    .child(FirebaseAuth.getInstance().getUid())
                                                    .child("userStories")
                                                    .push()
                                                    .setValue(userStories).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    progressDialog.dismiss();
                                                    Toast.makeText(getContext(), "Story Uploaded", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                        }
                                    });
                                }
                            });
                        }
                    });
                }
            }
        });


        //opening gallery launcher to select images
        addStory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                galleryLauncher.launch("image/*");
            }
        });


        return view;

    }

}
