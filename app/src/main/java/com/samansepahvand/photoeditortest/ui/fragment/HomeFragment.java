package com.samansepahvand.photoeditortest.ui.fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


import com.samansepahvand.photoeditortest.R;
import com.samansepahvand.photoeditortest.ui.activity.MainActivity;
import com.samansepahvand.photoeditortest.ui.adapter.ImageLoaderAdapter;
import com.samansepahvand.photoeditortest.utility.FileUtility;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment implements MainActivity.OnMainDataReceivedListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    private ImageView imgLeft1, imgLeft2,imgLastEdit;


    MainActivity mainActivity = new MainActivity();


    private RecyclerView recyclerView;
    private ImageLoaderAdapter adapter;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        mainActivity = (MainActivity) getActivity();
        mainActivity.setMainDataListener(this);
    }

    private void initView(View view) {


        imgLeft1 = view.findViewById(R.id.img_left_1);
        imgLeft2 = view.findViewById(R.id.img_left_2);
        imgLastEdit = view.findViewById(R.id.img_story);


        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


        defaultImage();
        ChangeStatusBar();
        getAllImagePath();

        storyViewConfig();



    }
    private  void storyViewConfig() {




    }

    private void defaultImage() {
        imgLeft1.setImageResource(R.drawable.no_image);
        imgLeft2.setImageResource(R.drawable.no_image);

        imgLastEdit.setImageResource(R.drawable.no_image);


    }

    private void getAllImagePath() {

        File baseDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File folders = new File(baseDir, FileUtility.FOLDER_NAME);

        if (folders.exists()) {

            List<String> arrayListPath = new ArrayList<>();
            List<Bitmap> bitmaps = new ArrayList<>();

            String[] filePathImageUrl = folders.list();
            for (String path : filePathImageUrl) {
                arrayListPath.add(folders.getAbsolutePath() + "/" + path);
                bitmaps.add(BitmapFactory.decodeFile(folders.getAbsolutePath() + "/" + path));
            }

            //Collections.sort(arrayListPath, Collections.reverseOrder());
            recyclerView.setAdapter(new ImageLoaderAdapter(bitmaps, getActivity()));

            if (arrayListPath.size() != 0) {
                imgLastEdit.setImageBitmap(bitmaps.get(bitmaps.size()-1));

                for (int i = 0; i <= arrayListPath.size() - 1; i++) {
                    switch (i) {
                        case 0:
                            imgLeft1.setImageBitmap(bitmaps.get(bitmaps.size()-1));
                            break;
                        case 1:
                            imgLeft2.setImageBitmap(bitmaps.get(bitmaps.size()-2));
                            break;
                    }


                }


            }


//            File[] allFiles = folders.listFiles(new FilenameFilter() {
//                public boolean accept(File dir, String name) {
//                    return (name.endsWith(".jpg") || name.endsWith(".jpeg") || name.endsWith(".png"));
//                }
//            });


        }
    }


    private void ChangeStatusBar() {
        if (Build.VERSION.SDK_INT >= 21) {
            //getActivity().getWindow().setNavigationBarColor(getResources().getColor(R.color.newYellowDark));
            getActivity().getWindow().setStatusBarColor(getResources().getColor(R.color.colorGray));
            getActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);

        }

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        initView(view);
        return view;

    }


    @Override
    public void onDataReceived(boolean status) {


        if (status) {
            getAllImagePath();
        }
    }
}