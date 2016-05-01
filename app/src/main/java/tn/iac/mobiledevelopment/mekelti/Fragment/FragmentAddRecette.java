package tn.iac.mobiledevelopment.mekelti.Fragment;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.kbeanie.multipicker.api.CameraImagePicker;
import com.kbeanie.multipicker.api.ImagePicker;
import com.kbeanie.multipicker.api.Picker;
import com.kbeanie.multipicker.api.callbacks.ImagePickerCallback;
import com.kbeanie.multipicker.api.entity.ChosenImage;

import java.util.List;

import tn.iac.mobiledevelopment.mekelti.R;
import tn.iac.mobiledevelopment.mekelti.Utils.ImageUtils;

/**
 * Created by S4M37 on 01/05/2016.
 */
public class FragmentAddRecette extends Fragment implements ImagePickerCallback {
    View rootView;
    private EditText inputLabel;
    private EditText inputDescription;
    private EditText inputCategory;
    private ImageView inputImage;
    private String pickerPath;
    private Bitmap bitmap;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_add_recette, container, false);
        inisializeView();
        return rootView;
    }

    private void inisializeView() {
        inputLabel = (EditText) rootView.findViewById(R.id.recette_label);
        inputDescription = (EditText) rootView.findViewById(R.id.recette_description);
        inputCategory = (EditText) rootView.findViewById(R.id.recette_category);
        inputImage = (ImageView) rootView.findViewById(R.id.recette_image);
        inputImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickUpImg();
            }
        });
    }

    private void pickUpImg() {
        AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(
                getContext());
        myAlertDialog.setMessage(getString(R.string.upload_image_option_recette));
        myAlertDialog.setPositiveButton(getString(R.string.gellerie),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        pickImageSingle();

                    }
                });

        myAlertDialog.setNegativeButton(getString(R.string.camera),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        takePicture();
                    }
                });
        myAlertDialog.show();
    }

    private ImagePicker imagePicker;

    public void pickImageSingle() {
        imagePicker = new ImagePicker(this);
        imagePicker.shouldGenerateMetadata(true);
        imagePicker.shouldGenerateThumbnails(true);
        imagePicker.setImagePickerCallback(this);
        imagePicker.pickImage();
    }

    private CameraImagePicker cameraPicker;

    public void takePicture() {
        cameraPicker = new CameraImagePicker(this);
        cameraPicker.shouldGenerateMetadata(true);
        cameraPicker.shouldGenerateThumbnails(true);
        cameraPicker.setImagePickerCallback(this);
        pickerPath = cameraPicker.pickImage();
    }

    @Override
    public void onImagesChosen(List<ChosenImage> list) {
        if (list != null) {
            bitmap = ImageUtils.getBitmapFromFile(list.get(0).getThumbnailPath(), 400, 400);
            inputImage.setImageBitmap(bitmap);
        }
    }

    @Override
    public void onError(String s) {
        Log.d("error", s);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == Picker.PICK_IMAGE_DEVICE) {
                if (imagePicker == null) {
                    imagePicker = new ImagePicker(this);
                    imagePicker.setImagePickerCallback(this);
                }
                imagePicker.submit(data);
            } else if (requestCode == Picker.PICK_IMAGE_CAMERA) {
                if (cameraPicker == null) {
                    cameraPicker = new CameraImagePicker(this);
                    cameraPicker.setImagePickerCallback(this);
                    cameraPicker.reinitialize(pickerPath);
                }
                cameraPicker.submit(data);
            }
        }
    }
}
