package com.devmasterteam.photicker.utils;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.devmasterteam.photicker.R;
import com.facebook.share.model.ShareHashtag;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareDialog;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class SocialUtil {

    //hashtag para compartilhamento junta a foto
    private static final String HASHTAG = "#photickerapp";

    /**
     * Handle Whatsapp
     */
    public static void shareImageOnWhats(Activity activity, RelativeLayout photoContent, View v) {

        //utilizamos para verificar se determinado app está ou não instalado no dispositivo
        PackageManager pkManager = activity.getPackageManager();
        try {
            //vamos tentar obter informações sobre o whatsapp, caso não encontre, ocorre erro
            pkManager.getPackageInfo("com.whatsapp", 0);

            // Cria arquivo único utilizando o próprio horário do sistema em milis segundos
            String fileName = "temp_file" + System.currentTimeMillis() + ".jpg";
            try {
                //habilitamos nosso RelativeLayout para ser criado um bitmap a partir dele
                photoContent.setDrawingCacheEnabled(true);
                //cria o cache do RelativeLayout (utilizado para se criar um bitmap a partir de todo conteúdo da View)
                photoContent.buildDrawingCache(true);
                //criamos um arquivo com o nome que definimos anteriormente
                File imageFile = new File(Environment.getExternalStorageDirectory(), fileName);
                //criamos um output para colocarmos a imagem que estamos obtendo a partir da nossa View
                FileOutputStream fileOutputStream = new FileOutputStream(imageFile);
                //criamos um bitmap a partir do metodo getDrawingCache da nossa View que estava em cache
                //com o bitmap criado, chamamos os método compress passando o formato que desejamos gerar, a qualidade (0 é o mínimo e 100 é o máximo possível - pouca perda, e nosso output que é onde a imagem será armazenada)
                photoContent.getDrawingCache(true).compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
                //fecha nosso output, não deve mais ser utilizada para escrita
                fileOutputStream.close();
                //desabilita o cache
                photoContent.setDrawingCacheEnabled(false);
                //libera os recursos usados pelo cache para desenhar a View
                photoContent.destroyDrawingCache();

                try {
                    //entao criamos a Intent responsável por chamar o whatsapp
                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    //colocamos a hashtag do app no rodape da imagem
                    sendIntent.putExtra(Intent.EXTRA_TEXT, HASHTAG);
                    //colocamos a imagem na mensagem
                    sendIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse("file:///sdcard/" + fileName));
                    //setamos o tipo do conteudo enviado
                    sendIntent.setType("image/jpeg");
                    //definimos qual é o app que deverá tratar nossa intent
                    sendIntent.setPackage("com.whatsapp");
                    //fazemos a chamada que iniciará o whatsapp
                    v.getContext().startActivity(Intent.createChooser(sendIntent, activity.getString(R.string.share_image)));
                } catch (Exception e) {
                    //exibimos erro caso algo de errado no envio
                    Toast.makeText(activity, R.string.unexpected_error, Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                //exibimos erro caso algo de errado no tratamento da imagem
                Toast.makeText(activity, R.string.unexpected_error, Toast.LENGTH_SHORT).show();
            }
        } catch (PackageManager.NameNotFoundException e) {
            //exibimos um erro para quando o app nao está instalado
            Toast.makeText(activity, R.string.whatsapp_not_installed, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Handle Twitter
     */
    public static void shareImageOnTwitter(Activity activity, RelativeLayout photoContent, View v) {
        //mesmo caso acima, checamos se o app está instalado
        PackageManager pkManager = activity.getPackageManager();
        try {
            // Da erro caso não encontre
            pkManager.getPackageInfo("com.twitter.android", 0);

            try {
                Intent tweetIntent = new Intent(Intent.ACTION_SEND);

                //gera um bitmap a partir do nosso relativelayout (mesmo objetivo acima mas feito de uma outra forma)
                Bitmap image = ImageUtil.drawBitmap(photoContent);
                //cria um output para guardarmos a imagem gerada
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                //guardamos a imagem no output
                image.compress(Bitmap.CompressFormat.JPEG, 100, bytes);

                //tb temos que criar o arquivo para guardar a imagem
                File f = new File(Environment.getExternalStorageDirectory() + File.separator + "temp_file.jpg");
                f.createNewFile();
                FileOutputStream fo = new FileOutputStream(f);
                fo.write(bytes.toByteArray());

                //colocamos os dados para chamada da nossa intent
                tweetIntent.putExtra(Intent.EXTRA_TEXT, HASHTAG);
                tweetIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse("file:///sdcard/temp_file.jpg"));
                tweetIntent.setType("image/jpeg");

                //neste trecho, verificamos qual app está apto para resolver a nossa intencao
                //pode ser o proprio app do twitter ou outros apps que conseguem tratar ações do twitter
                PackageManager pm = activity.getPackageManager();
                //queryIntentActivities retornará todas as activities que podem resolver nossa intenção
                List<ResolveInfo> lract = pm.queryIntentActivities(tweetIntent, PackageManager.MATCH_DEFAULT_ONLY);
                boolean resolved = false;
                for (ResolveInfo ri : lract) {
                    if (ri.activityInfo.name.contains("twitter")) {
                        //defini qual é a activity que resolverá nossa intent
                        tweetIntent.setClassName(ri.activityInfo.packageName, ri.activityInfo.name);
                        resolved = true;
                        break;
                    }
                }

                //fazemos a chamada que iniciará o twitter passando a intent que o android escolhei para tratar ou dando a opção do usuário escolher o app que deseja usar
                v.getContext().startActivity(resolved ? tweetIntent : Intent.createChooser(tweetIntent, "Choose one"));
            } catch (final ActivityNotFoundException e) {
                //caso nenhum app para tratar nossa intent
                Toast.makeText(activity, R.string.twitter_not_installed, Toast.LENGTH_SHORT).show();
            } catch (FileNotFoundException e) {
                //erro ao tratar imagem
                Toast.makeText(activity, R.string.unexpected_error, Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                //erro ao manipular arquivos
                Toast.makeText(activity, R.string.unexpected_error, Toast.LENGTH_SHORT).show();
            }

        } catch (PackageManager.NameNotFoundException e) {
            //twitter nao instalado
            Toast.makeText(activity, R.string.twitter_not_installed, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Handle Whatsapp
     */
    public static void shareImageOnInsta(Activity activity, RelativeLayout photoContent, View v) {

        PackageManager pkManager = activity.getPackageManager();
        try {
            // Da erro caso não encontre
            pkManager.getPackageInfo("com.instagram.android", 0);

            try {
                //convertemos nosso RelativeLayout para bitmap
                Bitmap image = ImageUtil.drawBitmap(photoContent);
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                image.compress(Bitmap.CompressFormat.JPEG, 100, bytes);

                //criamos um novo arquivo para armazenar a imagem
                File f = new File(Environment.getExternalStorageDirectory() + File.separator + "temp_file.jpg");

                try {
                    //escrevemos a imagem no nosso arquivo
                    f.createNewFile();
                    FileOutputStream fo = new FileOutputStream(f);
                    fo.write(bytes.toByteArray());

                    //setamos os dados extras para chamada da nossa intent
                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse("file:///sdcard/temp_file.jpg"));
                    sendIntent.setType("image/*");
                    //setamos quem irá tratar a intent
                    sendIntent.setPackage("com.instagram.android");
                    //iniciamos o app por meio do startActivity e passando a intent
                    v.getContext().startActivity(Intent.createChooser(sendIntent, activity.getString(R.string.share_image)));
                } catch (IOException e) {
                    Toast.makeText(activity, R.string.unexpected_error, Toast.LENGTH_SHORT).show();
                }

            } catch (Exception e) {
                Toast.makeText(activity, R.string.unexpected_error, Toast.LENGTH_SHORT).show();
            }

        } catch (PackageManager.NameNotFoundException e) {
            Toast.makeText(activity, R.string.instagram_not_installed, Toast.LENGTH_SHORT).show();
        }
    }


    /**
     * Handle Facebook
     */
    public static void shareImageOnFace(Activity activity, RelativeLayout photoContent, View v) {
        //cria conteudo para ser publicado no face
        SharePhoto photo = new SharePhoto.Builder().setBitmap(ImageUtil.drawBitmap(photoContent)).build();
        SharePhotoContent content = new SharePhotoContent.Builder()
                .addPhoto(photo)
                .setShareHashtag(new ShareHashtag.Builder().setHashtag(HASHTAG).build())
                .build();
        new ShareDialog(activity).show(content);
    }

}