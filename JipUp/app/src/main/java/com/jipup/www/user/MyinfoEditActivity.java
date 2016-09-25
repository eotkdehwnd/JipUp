package com.jipup.www.user;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mcjun.mkjar.QuadcoreView;
import com.jipup.www.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

/**
 * 회원정보 수정 화면
 * 회원의 이름, 프로필 사진을 수정할 수 있다.
 * 비밀번호 수정 클릭 시 비밀번호 수정 화면으로 넘어가서 비밀번호를 수정 할 수 있다.
 */
public class MyinfoEditActivity extends Activity {

    //프로필 사진 설정을 위한 상수
    static final int EDIT_PASSWORD = 0;
    static final int PICK_FROM_CAMERA = 1;
    static final int PICK_FROM_GALLERY = 2;
    static final int CROP_FROM_CAMERA = 3;

    private Button btnCamera, btnGallery; //프로필 사진 카메라, 앨범 버튼
    private QuadcoreView imgEditPhoto; //프로필 사진
    private Bitmap bitmapEditPhoto; //프로필 사진(비트맵)
    private EditText edtEditName, edtEditPw; //변경된 이름 및 비밀번호
    private String strEditName, strEditPw, strNum, strImg; //이름, 비밀번호, 유저 번호 객체

    private Button btnPwChange, btnMyinfoEditCancle, btnMyinfoEditComplete; //비밀번호 변경 버튼 및 수정완료, 취소 버튼
    private Intent myinfoEditIntent, mainIntent, editPwIntent; //화면 인텐트
    private Uri mImageCaptureUri; //임시 파일 경로
    private File photo;
    private boolean isEditPhoto = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myinfoedit);
        setTitle("내 정보 수정");

        //Xml 변수들에 대입
        edtEditName = (EditText) findViewById(R.id.myinfoedit_edtEditName);
        edtEditPw = (EditText) findViewById(R.id.myinfoedit_edtEditPw);
        btnPwChange = (Button) findViewById(R.id.myinfoedit_btnPwChange);
        btnMyinfoEditCancle = (Button) findViewById(R.id.myinfoedit_btnMyinfoEditCancle);
        btnMyinfoEditComplete = (Button) findViewById(R.id.myinfoedit_btnMyinfoEditComplete);
        btnCamera = (Button) findViewById(R.id.myinfoedit_btnCamera);
        btnGallery = (Button) findViewById(R.id.myinfoedit_btnGallery);
        imgEditPhoto = (QuadcoreView) findViewById(R.id.myinfoedit_imgEditPhoto);


        //메인액티비티가 보낸 데이터 받음
        myinfoEditIntent = getIntent();
        strEditName = myinfoEditIntent.getStringExtra("UserName");
        strEditPw = myinfoEditIntent.getStringExtra("UserPassWord");
        strNum = myinfoEditIntent.getStringExtra("UserNum");
        strImg = myinfoEditIntent.getStringExtra("UserImg");


        //받아온 데이터를 에디트텍스트에 출력
        edtEditName.setText(String.valueOf(strEditName));
        edtEditPw.setText(String.valueOf(strEditPw));
        photoLoad();

        //수정된 데이터를 메인액티비티에 보내기위해 인텐트 선언
        mainIntent = new Intent(getApplicationContext(), JoinActivity.class);

        //수정완료버튼 클릭
        btnMyinfoEditComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mainIntent.putExtra("EditName", edtEditName.getText().toString());
                mainIntent.putExtra("EditPassWord", edtEditPw.getText().toString());
                if(isEditPhoto) {
                    UploadImageToServer uploadImageToServer = new UploadImageToServer(photo.getPath(), Integer.parseInt(strNum));
                    uploadImageToServer.execute();
                    File file = new File(mImageCaptureUri.getPath());
                    if (file.exists()) {
                        file.delete(); //임시 파일 삭제
                    }
                }
                infoEdit(edtEditName.getText().toString(), edtEditPw.getText().toString(), strNum);
                setResult(RESULT_OK, mainIntent);
                Toast.makeText(getApplicationContext(), "내 정보 수정완료!", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        //취소버튼
        btnMyinfoEditCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //비밀번호 수정 액티비티에 현재비밀번호를 보내기위해 인텐트선언
        editPwIntent = new Intent(getApplicationContext(), EditPwActivity.class);

        //비밀번호수정 버튼 클릭 시
        btnPwChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //비번수정 액티비티에 현재 비번 보냄
                editPwIntent.putExtra("password", edtEditPw.getText().toString());
                startActivityForResult(editPwIntent, EDIT_PASSWORD);

            }
        });

        //카메라에서 사진 촬영 버튼 클릭 시
        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent CameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                String url = "tmp_" + String.valueOf(System.currentTimeMillis()) + ".jpg";
                mImageCaptureUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), url));
                CameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, mImageCaptureUri);
                startActivityForResult(CameraIntent, PICK_FROM_CAMERA);
            }
        });

        //앨범에서 사진 가져오기 버튼 클릭 시
        btnGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent GalleryIntent = new Intent(Intent.ACTION_PICK);
                GalleryIntent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                startActivityForResult(GalleryIntent, PICK_FROM_GALLERY);
            }
        });

    }

    @Override //양방향 액티비티
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != RESULT_OK) {
            return;
        }

        switch (requestCode) {
            case EDIT_PASSWORD: {
                //새 비밀번호 받아오기
                strEditPw = data.getStringExtra("newpassword");
                //에디트텍스트에 출력
                edtEditPw.setText(String.valueOf(strEditPw));
                break;
            }

            //앨범 가져오기
            case PICK_FROM_GALLERY: {
                mImageCaptureUri = data.getData();
                Log.i("NR", mImageCaptureUri.getPath().toString());

                // 이후의 처리가 카메라 부분과 같아 break 없이 진행
            }

            //카메라에서 사진 가져오기
            case PICK_FROM_CAMERA: {
                photo = getImageFile(mImageCaptureUri);
                Intent intent = new Intent("com.android.camera.action.CROP");
                intent.setDataAndType(mImageCaptureUri, "image/*");

                //photo 경로추가 *수정

                //crop한 이미지를 저장할 때 크기 및 비율 조정
                intent.putExtra("outputX", 120);
                intent.putExtra("outputY", 120);
                intent.putExtra("aspectX", 0);
                intent.putExtra("aspectY", 0);

                intent.putExtra("scale", true);
                intent.putExtra("return-data", true);
                startActivityForResult(intent, CROP_FROM_CAMERA);
                break;
            }

            //crop완료 후 프로필 사진 설정
            case CROP_FROM_CAMERA: {
                final Bundle extras = data.getExtras();
                if (extras != null) {
                    bitmapEditPhoto = extras.getParcelable("data");
                    saveBitmapToFileCache(bitmapEditPhoto, Environment.getExternalStorageDirectory().getAbsolutePath(), "temp.jpg");
                    imgEditPhoto.setImageBitmap(bitmapEditPhoto);
                    isEditPhoto = true;
                }
            }

        }
    }


    /**
     * 변경된 사용자의 정보를 서버에게 송신
     *
     * @param user_name 수정된 유저 이름
     * @param user_pass 수정된 유저 비밀번호
     * @param user_num  유저 번호
     */
    private void infoEdit(String user_name, String user_pass, String user_num) {
	
    }

    /**
     * 사용자 이미지가 설정 된 것이 없으면 기본
     * 있을 시 이미지를 가져온다.
     */
    private void photoLoad() {

        if (strImg.equals("null")) {
            Bitmap bitmapPhoto = BitmapFactory.decodeResource(getResources(), R.drawable.profile_default);
            imgEditPhoto.setImageBitmap(bitmapPhoto);
        } else {
            imgEditPhoto.show(url);
        }
    }

    // Bitmap to File
    public  void saveBitmapToFileCache(Bitmap bitmap, String strFilePath,
                                       String filename) {

        File file = new File(strFilePath);

        // If no folders
        if (!file.exists()) {
            file.mkdirs();
        }

        File fileCacheItem = new File(strFilePath + filename);
        OutputStream out = null;

        try {
            fileCacheItem.createNewFile();
            out = new FileOutputStream(fileCacheItem);

            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //try {
            //    out.close();
            //} catch (IOException e) {
            //    e.printStackTrace();
            //}
        }
    }

    /**
     * 선택된 uri의 사진 Path를 가져온다.
     * uri 가 null 경우 마지막에 저장된 사진을 가져온다.
     *
     * @param uri
     * @return
     */
    private File getImageFile(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        if (uri == null) {
            uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        }

        Cursor mCursor = getContentResolver().query(uri, projection, null, null,
                MediaStore.Images.Media.DATE_MODIFIED + " desc");
        if (mCursor == null || mCursor.getCount() < 1) {
            return null; // no cursor or no record
        }
        int column_index = mCursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        mCursor.moveToFirst();

        String path = mCursor.getString(column_index);

        if (mCursor != null) {
            mCursor.close();
        }

        return new File(path);
    }

    @Override
    protected void onDestroy() {
        if(isEditPhoto) {
            File file = new File(mImageCaptureUri.getPath());
            if (file.exists()) {
                file.delete(); //임시 파일 삭제
            }
        }
        super.onDestroy();
    }
}