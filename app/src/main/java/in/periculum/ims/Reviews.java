package in.periculum.ims;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import in.periculum.ims.adapter.GridAdapter;
import in.periculum.ims.database.TableHelper;
import in.periculum.ims.ds.Sites;
import in.periculum.ims.handler.CommonHandler;
import in.periculum.ims.listener.HttpRequestCallback;
import in.periculum.ims.utility.CommonUtility;
import in.periculum.ims.utility.ConnectionDetector;
import in.periculum.ims.utility.ImsUtility;

public class Reviews extends AppCompatActivity {

    private List<Bitmap> photos;
    private String ba1;
    private Uri fileUri;
    private String site;
    private ArrayList<String> imgpath;
    private JSONObject jobject;
    private GridAdapter photosView;
    private int j;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reviews);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        CommonUtility.hideKeyboard(this);
        photos = new ArrayList<>();
        imgpath=new ArrayList<>();
        j = 0;

        photosView = new GridAdapter(Reviews.this, photos);

        ((GridView) findViewById(R.id.photos)).setAdapter(photosView);

        findViewById(R.id.sites).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                TableHelper sitesHelper = new TableHelper(getApplicationContext());
                final List<Sites> sites = sitesHelper.getAllSites();
                sitesHelper.close();
                if (sites != null) {
                    final AlertDialog levelDialog;
                    final CharSequence[] filterBy;
                    filterBy = new CharSequence[sites.size()];
                    for (int i = 0; i < sites.size(); i++) {
                        filterBy[i] = sites.get(i).getWHDesc();
                    }

                    AlertDialog.Builder builder = new AlertDialog.Builder(Reviews.this);
                    builder.setTitle("Choose");

                    builder.setSingleChoiceItems(filterBy, -1,
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int item) {
                                    site = sites.get(item).getWHNo();
                                    ((EditText) v.findViewById(R.id.sites)).setText(sites.get(item).getWHDesc());
                                    dialog.dismiss();
                                }
                            });

                    levelDialog = builder.create();
                    levelDialog.show();
                } else {
                    Snackbar.make(findViewById(R.id.review_activity), "Sites Not Available!!!", Snackbar.LENGTH_SHORT).show();
                }
            }
        });

        findViewById(R.id.take_photo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickpic();
            }
        });
        findViewById(R.id.upload_photo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create intent to Open Image applications like Gallery, Google Photos
                Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                // Start the Intent
                startActivityForResult(galleryIntent, 101);
            }
        });
        findViewById(R.id.submit_visit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate() && ConnectionDetector.getInstance(getApplicationContext()).isConnectingToInternet()) {

                    if (photos.size() > 0) {

                        recursiveCall();

                    } else {
                        submitData();
                    }


                }
            }
        });
    }

    private void submitData() {

        String purpose = ((EditText) findViewById(R.id.visit_purpose)).getText().toString();
        String comment = ((EditText) findViewById(R.id.comment)).getText().toString();


        jobject = new JSONObject();
        try {
            jobject.put("tag", "siteVisitInsert");
            jobject.put("empId", ImsUtility.getUser(getApplicationContext()).getEmployeeId());
            jobject.put("siteCode", site);
            jobject.put("visitPurpose", purpose);
            jobject.put("siteRemarks", comment);
            jobject.put("datetimeEntered", CommonUtility.getDate());
            jobject.put("imagepath", TextUtils.join(",", imgpath));

        } catch (JSONException e) {
        }
        CommonUtility.showProgressDialog(Reviews.this, "", "Site Visit Submit", true, false);
        CommonHandler.loadTheData("sitevisit", ImsUtility.BASE_URL, new HttpRequestCallback<JSONObject>() {
            @Override
            public void response(String errro, JSONObject returnType) {
                try {
                    if (Integer.parseInt(returnType.getString("success")) != 0) {

                        CommonUtility.showProgressDialog(Reviews.this, "", "Site Visit Submit", false, false);
                        Snackbar.make(findViewById(R.id.review_activity), "Subbmited Successfully", Snackbar.LENGTH_SHORT).show();
                        jobject = null;
                        ((EditText) findViewById(R.id.visit_purpose)).setText("");
                        ((EditText) findViewById(R.id.comment)).setText("");
                        ((EditText) findViewById(R.id.sites)).setText("");
                        photos.clear();
                         photosView.notifyDataSetChanged();
                    } else {
                        CommonUtility.showProgressDialog(Reviews.this, "", "Site Visit Submit", false, false);
                        Snackbar.make(findViewById(R.id.review_activity), "Server Error", Snackbar.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    CommonUtility.showProgressDialog(Reviews.this, "", "Site Visit Submit", false, false);
                    Snackbar.make(findViewById(R.id.review_activity), "Error in data", Snackbar.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String errorMessage) {
                CommonUtility.showProgressDialog(Reviews.this, "", "Site Visit Submit", false, false);
                Snackbar.make(findViewById(R.id.review_activity), "Error in Network", Snackbar.LENGTH_SHORT).show();
            }
        }, jobject);
    }

    private boolean validate() {
        if (TextUtils.isEmpty(((EditText) findViewById(R.id.visit_purpose)).getText().toString())) {
            Snackbar.make(findViewById(R.id.review_activity), "Enter Visit Purpose", Snackbar.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(((EditText) findViewById(R.id.comment)).getText().toString())) {
            Snackbar.make(findViewById(R.id.review_activity), "Enter Comment", Snackbar.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(((EditText) findViewById(R.id.sites)).getText().toString())) {
            Snackbar.make(findViewById(R.id.review_activity), "Sites Not Selected!!!", Snackbar.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void recursiveCall() {
        if (j < photos.size()) {
            upload(photos.get(j), j + 1);
        } else {
            submitData();
        }
    }

    private void clickpic() {
        // Check Camera
        if (getApplicationContext().getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_CAMERA)) {
            // Open default camera
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            fileUri = getOutputMediaFileUri();

            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
            // start the image capture Intent
            startActivityForResult(intent, 100);

        } else {
            Toast.makeText(getApplication(), "Camera not supported", Toast.LENGTH_LONG).show();
        }
    }

    public Uri getOutputMediaFileUri() {
        // External sdcard location
        File mediaStorageDir = new File(
                Environment
                        .getExternalStorageDirectory(),
                "IMS");
        boolean success = true;
        if (!mediaStorageDir.exists()) {
            success = mediaStorageDir.mkdir();
        }
        if (success) {
            // Do something on success
            // Create a media file name
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                    Locale.getDefault()).format(new Date());

            File mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "IMS_" + timeStamp + ".jpg");

            return Uri.fromFile(mediaFile);
        } else {
            // Do something else on failure
            return null;
        }

    }

    private void upload(final Bitmap photo, int no) {
        CommonUtility.showProgressDialog(this, "Uploading", "Uploading Site Image No " + (no + 1), true, false);
        ByteArrayOutputStream bao = new ByteArrayOutputStream();
        photo.compress(Bitmap.CompressFormat.JPEG, 90, bao);
        byte[] ba = bao.toByteArray();
        ba1 = Base64.encodeToString(ba, Base64.DEFAULT);

        JSONObject jobj = new JSONObject();
        try {

            jobj.put("tag", "uploadsiteimg");
            jobj.put("empid", ImsUtility.getUser(getApplicationContext()).getEmployeeId());
            jobj.put("imgname", System.currentTimeMillis() + ".jpg");
            jobj.put("base64", ba1);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        CommonHandler.loadTheData("uploadimg", ImsUtility.BASE_URL, new HttpRequestCallback<JSONObject>() {
            @Override
            public void response(String errro, JSONObject returnType) {

                try {
                    if (returnType.getInt("success") != 0) {

                        imgpath.add(returnType.getString("imgpath"));
                        CommonUtility.showProgressDialog(Reviews.this, "Uploading", "Uploading Site Image", false, false);
                        Snackbar.make(findViewById(R.id.review_form_layout), "Photo Uploaded!!!", Snackbar.LENGTH_LONG).show();

                        j++;
                        recursiveCall();


                    } else {
                        CommonUtility.showProgressDialog(Reviews.this, "Uploading", "Uploading Site Image", false, false);
                        Snackbar.make(findViewById(R.id.review_form_layout), "Upload Failed Try Again!!!", Snackbar.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    CommonUtility.showProgressDialog(Reviews.this, "Uploading", "Uploading Site Image", false, false);
                    Snackbar.make(findViewById(R.id.review_form_layout), "Error in request", Snackbar.LENGTH_LONG).show();

                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String errorMessage) {
                CommonUtility.showProgressDialog(Reviews.this, "Uploading", "Uploading Site Image", false, false);
                Snackbar.make(findViewById(R.id.review_form_layout), "Error In request!!!", Snackbar.LENGTH_LONG).show();

            }
        }, jobj);

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Bitmap photo;
        if (requestCode == 100 && resultCode == RESULT_OK) {
            // photo = (Bitmap) data.getExtras().get("data");

            photo = BitmapFactory.decodeFile(fileUri.getPath());
            photo = Bitmap.createScaledBitmap(photo, 500, 300, false);
            ImageView imageView = new ImageView(this);
            imageView.setImageBitmap(photo);
            imageView.setPadding(3, 3, 3, 3);

            photos.add(photo);

            photosView.notifyDataSetChanged();

        } else if (requestCode == 101 && resultCode == RESULT_OK && null != data) {
            // Get the Image from data

            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            // Get the cursor
            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            // Move to first row
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String imgDecodableString = cursor.getString(columnIndex);
            cursor.close();
            photo = BitmapFactory
                    .decodeFile(imgDecodableString);
            photo = Bitmap.createScaledBitmap(photo, 500, 300, false);
            ImageView imgView = new ImageView(this);
            imgView.setPadding(3, 3, 3, 3);
            // Set the Image in ImageView after decoding the String
            imgView.setImageBitmap(photo);

            photos.add(photo);
            photosView.notifyDataSetChanged();
        } else {
            Toast.makeText(this, "Error To Take/Select Image",
                    Toast.LENGTH_LONG).show();
        }


    }

    /**
     * Here we store the file url as it will be null after returning from camera
     * app
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // save file url in bundle as it will be null on screen orientation
        // changes
        outState.putParcelable("file_uri", fileUri);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        // get the file url
        fileUri = savedInstanceState.getParcelable("file_uri");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
