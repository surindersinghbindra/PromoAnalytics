package com.promoanalytics.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.promoanalytics.AppConstants;
import com.promoanalytics.R;
import com.promoanalytics.activity.login.User;
import com.promoanalytics.databinding.EditProfileBinding;
import com.promoanalytics.modules.MyApplication;
import com.promoanalytics.utils.PromoAnalyticsServices;
import com.promoanalytics.utils.RootFragment;
import com.vansuita.pickimage.bean.PickResult;
import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.dialog.PickImageDialog;
import com.vansuita.pickimage.listeners.IPickResult;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by think360user on 17/3/17.
 */

public class EditProfile extends RootFragment implements IPickResult {


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private EditProfileBinding editProfileBinding;
    private String imagePath;
    private View view;

    private String mParam1;
    private String mParam2;


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RegisterFragment.
     */

    public static EditProfile newInstance(String param1, String param2) {
        EditProfile fragment = new EditProfile();
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
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.edit_profile, container, false);

        view.findViewById(R.id.btnSaveProfile).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                File file = new File(imagePath);
                RequestBody fbody = RequestBody.create(MediaType.parse("image*//*"), file);
                RequestBody name = RequestBody.create(MediaType.parse("text/plain"), editProfileBinding.editName.getText().toString());
                RequestBody id = RequestBody.create(MediaType.parse("text/plain"), MyApplication.sharedPreferencesCompat.getString(AppConstants.USER_ID, ""));

                PromoAnalyticsServices promoAnalyticsServices = PromoAnalyticsServices.retrofit.create(PromoAnalyticsServices.class);

                Call<User> call = promoAnalyticsServices.editUser(fbody, name, id, fbody);
                call.enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {

                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {

                    }
                });


            }
        });


        view.findViewById(R.id.ivUser).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PickSetup setup = new PickSetup().setTitle("Choose")
                        .setTitleColor(R.color.black)
                        .setSystemDialog(true);

                PickImageDialog.build(setup, new IPickResult() {
                    @Override
                    public void onPickResult(PickResult r) {
                        r.getBitmap();
                        r.getError();
                        r.getUri();
                    }
                }).show(getActivity().getSupportFragmentManager());
            }
        });

        return view;

    }


    private void onImageViewClick() {
        PickSetup setup = new PickSetup().setTitle("Choose")
                .setTitleColor(R.color.black)
                .setSystemDialog(true);


        //  PickImageDialog.build(setup).show(getActivity());

        //If you don't have an Activity, you can set the FragmentManager
        PickImageDialog.build(setup, new IPickResult() {
            @Override
            public void onPickResult(PickResult r) {
                r.getBitmap();
                r.getError();
                r.getUri();
            }
        }).show(getActivity().getSupportFragmentManager());

        //For overriding the click events you can do this
        /*PickImageDialog.build(setup).setOnClick(new IPickClick() {
            @Override
            public void onGalleryClick() {

            }

            @Override
            public void onCameraClick() {

            }
        }).show(this);*/
    }

    @Override
    public void onPickResult(final PickResult r) {
        Log.e("RESULT", r.getPath());
        if (r.getError() == null) {
            //If you want the Uri.
            //Mandatory to refresh image from Uri.
            //getImageView().setImageURI(null);

            //Setting the real returned image.
            //getImageView().setImageURI(r.getUri());

            Log.e("RESULT", r.getPath());
            //If you want the Bitmap.
            ((ImageView) view.findViewById(R.id.ivUser)).setImageBitmap(r.getBitmap());

            imagePath = r.getPath();
        } else {
            //Handle possible errors

            Toast.makeText(getActivity(), r.getError().getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}
