package com.example.foto;

import android.content.Intent;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.PluginRegistry;
import io.flutter.plugin.common.PluginRegistry.Registrar;
import top.kikt.gallerypicker.GalleryOption;
import top.kikt.gallerypicker.GalleryPicker;
import top.kikt.gallerypicker.entity.ImageEntity;

/**
 * FotoPlugin
 */
public class FotoPlugin implements MethodCallHandler {
    private final GalleryPicker picker;

    private FotoPlugin(Registrar registrar) {
        picker = new GalleryPicker(registrar.activity());
        registrar.addRequestPermissionsResultListener(
                new PluginRegistry.RequestPermissionsResultListener() {
                    @Override
                    public boolean onRequestPermissionsResult(int i, String[] strings, int[] ints) {
                        picker.getPermissionsUtils().dealResult(i, strings, ints);
                        return false;
                    }
                }
        );
        registrar.addActivityResultListener(
                new PluginRegistry.ActivityResultListener() {
                    @Override
                    public boolean onActivityResult(int i, int i1, Intent intent) {
                        if (i == REQUEST_CODE && intent != null) {
                            try {
                                if (handleIntentResult(intent)) return false;
                            } catch (Exception e) {
                                handleResult(null);
                            }
                        }
                        return false;
                    }

                }
        );
    }

    /**
     * Plugin registration.
     */
    public static void registerWith(Registrar registrar) {
        final MethodChannel channel = new MethodChannel(registrar.messenger(), "foto");
        channel.setMethodCallHandler(new FotoPlugin(registrar));
    }

    @Override
    public void onMethodCall(MethodCall call, Result result) {
        if (call.method.equals("pickImage")) {
            pickImage(call, result);
        } else {
            result.notImplemented();
        }
    }

    private static final int REQUEST_CODE = 30303;

    private Result result;

    private void pickImage(MethodCall call, Result result) {
        this.result = result;
        String optionJson = (String) call.arguments;
        try {
            JSONObject jsonObject = new JSONObject(optionJson);
            int themeColor = jsonObject.optInt("themeColor");
            int textColor = jsonObject.optInt("textColor");
            int dividerColor = jsonObject.optInt("dividerColor");
            int disableColor = jsonObject.optInt("disableColor");
            int rowCount = jsonObject.optInt("rowCount");
            int padding = jsonObject.optInt("padding");
            int maxSelected = jsonObject.optInt("maxSelected");
            float radio = ((float) jsonObject.optDouble("itemRadio"));

            GalleryOption option = new GalleryOption();
            option.setThemeColor(themeColor);
            option.setTextColor(textColor);
            option.setDividerColor(dividerColor);
            option.setDisableColor(disableColor);
            option.setRowCount(rowCount);
            option.setPadding(padding);
            option.setMaxSelected(maxSelected);
            option.setItemRadio(radio);

            picker.openWithOption(option, REQUEST_CODE, false);
        } catch (JSONException e) {
            handleResult(null);
        }
    }

    private void handleResult(Object obj) {
        if (result != null) {
            Result r = result;
            result = null;
            r.success(obj);
        }
    }

    private boolean handleIntentResult(Intent intent) {
        ArrayList<ImageEntity> resultFromIntent = picker.getResultFromIntent(intent);
        if (resultFromIntent == null) {
            handleResult(null);
            return true;
        }
        List<String> list = new ArrayList<>();
        for (ImageEntity imageEntity : resultFromIntent) {
            list.add(imageEntity.getPath());
        }
        handleResult(list);
        return false;
    }
}
