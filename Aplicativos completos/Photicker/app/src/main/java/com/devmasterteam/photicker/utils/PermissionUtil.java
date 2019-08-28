package com.devmasterteam.photicker.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;

import com.devmasterteam.photicker.R;

public final class PermissionUtil {

    public static final int CAMERA_PERMISSION = 0;

    /**
     * Verifica se é necessário requisitar a permissão ao usuário de acordo com a versão do Android
     */
    //PERMISSAO_
    private static boolean needToAskPermission() {
        return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
    }

    //PERMISSAO_
    //verifica se possui permissão para acessar a camera
    public static boolean hasCameraPermission(Context context) {
        if (needToAskPermission())
            //verifica se o usuario já permitiu acessar a camera e sdcard return um boolean true caso tenha false caso contrário
            return ActivityCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;

        //caso nao necessite pedir a permissão, entao o usuario possui uma versão menor que 6 no celular, ja aceitou no momento da instalação
        // as permissões que incluímos no AndroidManifest
        return true;
    }

    /**
     * Faz a requisição ao usuário para acessar a câmera
     */
    //PERMISSAO_
    public static void asksCameraPermission(final Activity activity) {

        //caso a permissão já tenha sido solicitada ao usuario, porém ele tenha negado, este método retornará true e aí mostraremos uma msg
        //personalizada com o motivo pelo qual precisamos da permissão
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.CAMERA)) {
            // Mostra uma dialog com a explicação e um botão para permitir a requisição
            new AlertDialog.Builder(activity)
                    .setMessage(activity.getString(R.string.permission_camera_explanation))
                    .setPositiveButton(R.string.btn_ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            ActivityCompat.requestPermissions(activity,
                                    new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                    PermissionUtil.CAMERA_PERMISSION);
                        }
                    }).show();
        } else {
            //caso seja a primeira vez que o usuário está sendo informado que o app precisa da permissão, apenas solicita as permissões necessárias
            //sem nenhuma mensagem específica
            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    PermissionUtil.CAMERA_PERMISSION);
        }
    }
}
