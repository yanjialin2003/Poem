package com.example.redtoursystem.activity;


import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.example.redtoursystem.R;
import com.example.redtoursystem.event.AgeEvent;
import com.example.redtoursystem.event.DescriptionEvent;
import com.example.redtoursystem.event.GenderEvent;
import com.example.redtoursystem.event.HeadImageEvent;
import com.example.redtoursystem.event.IntroEvent;
import com.example.redtoursystem.manager.BmobManager;
import com.example.redtoursystem.manager.UserManager;
import com.example.redtoursystem.util.DialogUtil;

import de.greenrobot.event.EventBus;

public class EditInfoActivity extends SwipeBackBaseActivity {

    private static final int REQUEST_IMAGE = 100;

    private ImageView mHeadImageView;

    private TextView mAgeText;
    private ImageView mGenderImage;
    private TextView mGenderText;
    private TextView mIntroText;
    private TextView mDescriptionText;

    private ProgressDialog mProgressDialog;
    private UserManager mUserManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_info);
        EventBus.getDefault().register(this);
        mUserManager = UserManager.getInstance(this);
        mProgressDialog = DialogUtil.createProgressDialog(this);

        initViews();
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    /**
     * 初始化View
     */
    private void initViews() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.modify_info);
        setSupportActionBar(toolbar);

        ImageView phoneImage = (ImageView) findViewById(R.id.iv_phone);
        phoneImage.setImageDrawable(new IconicsDrawable(this,
                GoogleMaterial.Icon.gmd_phone_android).colorRes(R.color.text_color));
        ImageView emailImage = (ImageView) findViewById(R.id.iv_email);
        emailImage.setImageDrawable(new IconicsDrawable(this,
                GoogleMaterial.Icon.gmd_email).paddingDp(1).colorRes(R.color.text_color));

        ImageView cityImage = (ImageView) findViewById(R.id.iv_city);
        cityImage.setImageDrawable(new IconicsDrawable(this,
                GoogleMaterial.Icon.gmd_location_city).colorRes(R.color.text_color));

        ImageView passwordImage = (ImageView) findViewById(R.id.iv_password);
        passwordImage.setImageDrawable(new IconicsDrawable(this,
                GoogleMaterial.Icon.gmd_lock).colorRes(R.color.text_color));

        //设置头像
        mHeadImageView = (ImageView) findViewById(R.id.iv_head_image);
        mUserManager.setHeadImage(mHeadImageView);

        //设置用户名
        TextView usernameText = (TextView) findViewById(R.id.tv_username);
        usernameText.setText(mUserManager.getUsername());

        //设置年龄
        mAgeText = (TextView) findViewById(R.id.tv_age);
        mAgeText.setText(mUserManager.getAge());

        //设置性别
        mGenderText = (TextView) findViewById(R.id.tv_gender);
        mGenderImage = (ImageView) findViewById(R.id.iv_gender);
        setGender();

        //设置一句话介绍
        mIntroText = (TextView) findViewById(R.id.tv_intro);
        setIntro();

        //设置个人介绍
        mDescriptionText = (TextView) findViewById(R.id.tv_description);
        mDescriptionText.setText(mUserManager.getDescription());

        initEvents();
    }

    /**
     * 设置事件
     */
    private void initEvents() {

        mHeadImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, null);
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intent, REQUEST_IMAGE);
            }
        });

        View ageLayout = findViewById(R.id.ll_age);
        ageLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditInfoActivity.this, SelectBirthYearActivity.class);
                startActivity(intent);
            }
        });

        View genderLayout = findViewById(R.id.ll_gender);
        genderLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditInfoActivity.this, ModifyGenderActivity.class);
                startActivity(intent);
            }
        });

        mIntroText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditInfoActivity.this, ModifyIntroActivity.class);
                startActivity(intent);
            }
        });

        mDescriptionText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditInfoActivity.this, ModifyDescriptionActivity.class);
                startActivity(intent);
            }
        });

        View passwordLayout = findViewById(R.id.rl_password);
        passwordLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditInfoActivity.this, ModifyPasswordActivity.class);
                startActivity(intent);
            }
        });

        View phoneLayout = findViewById(R.id.rl_phone);
        phoneLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditInfoActivity.this, ModifyPhoneActivity.class);
                startActivity(intent);
            }
        });

        View emailLayout = findViewById(R.id.rl_email);
        emailLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditInfoActivity.this, ModifyEmailActivity.class);
                startActivity(intent);
            }
        });

        View cityLayout = findViewById(R.id.rl_city);
        cityLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditInfoActivity.this, SelectCityActivity.class);
                startActivity(intent);
            }
        });

    }

    public void onEventMainThread(AgeEvent event) {
        mAgeText.setText(event.getAge());
    }

    public void onEventMainThread(GenderEvent event) {
        setGender();
    }

    public void onEventMainThread(IntroEvent event) {
        setIntro();
    }

    public void onEventMainThread(DescriptionEvent event) {
        mDescriptionText.setText(mUserManager.getDescription());
    }

    /**
     * 设置性别
     */
    private void setGender() {
        if (mUserManager.getGender()) {
            mGenderText.setText("女");
            mGenderImage.setImageResource(R.drawable.ic_gender_f_g);
        } else {
            mGenderText.setText("男");
            mGenderImage.setImageResource(R.drawable.ic_gender_m_g);
        }
    }

    /**
     * 设置一句话介绍
     */
    private void setIntro() {
        String intro = mUserManager.getIntro();
        if (intro.equals("(添加一句话介绍自己)")) {
            mIntroText.setText("");
        } else {
            mIntroText.setText(intro);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_IMAGE) {
                mProgressDialog.show();
                Cursor cursor = getContentResolver().query(
                        data.getData(), null, null, null, null);
                cursor.moveToFirst();
                final String imagePath = cursor.getString(cursor.getColumnIndex("_data"));
                cursor.close();
                BmobManager.getInstance(this).upload(imagePath, new BmobManager.IBmobResponse() {
                    @Override
                    public void onSuccess(String data) {
                        mUserManager.setImageName(data, imagePath, new UserManager.OnUpdateListener() {
                            @Override
                            public void onSuccess() {
                                EventBus.getDefault().post(new HeadImageEvent());
                                mProgressDialog.dismiss();
                            }

                            @Override
                            public void onError() {
                                mProgressDialog.dismiss();
                            }
                        });
                        Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
                        mHeadImageView.setImageBitmap(bitmap);
                    }

                    @Override
                    public void onError(String error) {
                        mProgressDialog.dismiss();
                    }
                });

            }
        }
    }
}