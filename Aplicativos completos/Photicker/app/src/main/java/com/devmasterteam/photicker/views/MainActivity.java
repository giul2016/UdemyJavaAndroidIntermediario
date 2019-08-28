package com.devmasterteam.photicker.views;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.devmasterteam.photicker.R;
import com.devmasterteam.photicker.utils.ImageUtil;
import com.devmasterteam.photicker.utils.PermissionUtil;
import com.devmasterteam.photicker.utils.SocialUtil;
import com.facebook.FacebookSdk;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnTouchListener, View.OnLongClickListener {

    static final int REQUEST_TAKE_PHOTO = 2;
    private final ViewHolder mViewHolder = new ViewHolder();

    // Permite a troca de informações com a thread, envio de msgs
    private Handler mRepeatUpdateHandler = new Handler();
    private boolean mAutoIncrement = false;
    private LongEventType mLongEventType;
    private ImageView mImageSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);

        // Faz a criação da lista de imagens
        List<Integer> mListmages = ImageUtil.getImagesList();

        final RelativeLayout relativeLayout = this.findViewById(R.id.relative_photo_content_draw);
        final LinearLayout content = this.findViewById(R.id.linear_horizontal_scroll_content);

        // Itera o número de imagens
        for (Integer imageId : mListmages) {

            // Obtém elemento de imagem
            ImageView image = new ImageView(this);

            // Substitui a imagem com a imagem sendo iterada
            image.setImageBitmap(ImageUtil.decodeSampledBitmapFromResource(getResources(), imageId, 70, 70));
            image.setPadding(20, 10, 20, 10);

            BitmapFactory.Options dimensions = new BitmapFactory.Options();
            dimensions.inJustDecodeBounds = true;
            BitmapFactory.decodeResource(getResources(), imageId, dimensions);

            final int width = dimensions.outWidth;
            final int height = dimensions.outHeight;

            image.setOnClickListener(onClickImageOption(relativeLayout, imageId, width, height));

            // Adiciona nova imagem
            content.addView(image);
        }

        // Adiciona os elementos
        this.mViewHolder.mImageTakePhoto = this.findViewById(R.id.image_take_photo);
        this.mViewHolder.mImagePhoto = this.findViewById(R.id.image_photo);

        //AULA_SOCIAL_2
        this.mViewHolder.mImageInstagram = this.findViewById(R.id.image_instagram);
        this.mViewHolder.mImageTwitter = this.findViewById(R.id.image_twitter);
        this.mViewHolder.mImageFacebook = this.findViewById(R.id.image_facebook);
        this.mViewHolder.mImageWahtsApp = this.findViewById(R.id.image_whatsapp);

        this.mViewHolder.mButtonZoomIn = this.findViewById(R.id.image_zoom_in);
        this.mViewHolder.mButtonZoomOut = this.findViewById(R.id.image_zoom_out);
        this.mViewHolder.mButtonRotateLeft = this.findViewById(R.id.image_rotate_left);
        this.mViewHolder.mButtonRotateRight = this.findViewById(R.id.image_rotate_right);
        this.mViewHolder.mImageFinish = this.findViewById(R.id.image_finish);
        this.mViewHolder.mImageRemove = this.findViewById(R.id.image_remove);

        this.mViewHolder.mLinearSharePanel = this.findViewById(R.id.linear_share_panel);
        this.mViewHolder.mLinearControlPanel = this.findViewById(R.id.linear_control_panel);
        this.mViewHolder.mRelativePhotoContent = this.findViewById(R.id.relative_photo_content_draw);

        // Adicona evento aos elementos
        this.setListeners();

        // Incializa o SDK do facebook
        FacebookSdk.sdkInitialize(getApplicationContext());
    }

    /**
     * Habilita o painel para compartilhamento de imagem ou manipulação de imagem
     */
    private void toogleControlPanel(boolean value) {
        if (value) {
            this.mViewHolder.mLinearControlPanel.setVisibility(View.VISIBLE);
            this.mViewHolder.mLinearSharePanel.setVisibility(View.GONE);
        } else {
            this.mViewHolder.mLinearControlPanel.setVisibility(View.GONE);
            this.mViewHolder.mLinearSharePanel.setVisibility(View.VISIBLE);
        }
    }

    private View.OnClickListener onClickImageOption(final RelativeLayout relativeLayout, final int position, final int width, final int height) {
        return new View.OnClickListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public void onClick(View view) {
                // Atualizar a imagem e colocar imagem para sobrepor
                final ImageView image = new ImageView(MainActivity.this);
                image.setBackgroundResource(position);
                relativeLayout.addView(image);

                // Centraliza imagem
                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) image.getLayoutParams();
                layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
                layoutParams.addRule(RelativeLayout.CENTER_VERTICAL);

                // Adiciona os parametros a imagem
                image.setLayoutParams(layoutParams);
                image.setAdjustViewBounds(true);

                // DEixa selecionado último item
                mImageSelected = image;

                // Painel de controle de imagem ativo
                toogleControlPanel(true);
//
//                ViewGroup.LayoutParams params = image.getLayoutParams();
//                params.width = (int) (width * 0.5);
//                params.height = (int) (height * 0.5);
//                image.setLayoutParams(params);

                // Evento de Drag
                image.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent motionEvent) {
                        /**
                         MotionEvent - utilizado para pegar as ações do usuário na tela, se ele está clicado no elemento,
                         se está movendo, qual a posição atual, se parou de tocar no elemento.
                         *
                         */

                        float x, y;
                        switch (motionEvent.getAction()) {
                            case MotionEvent.ACTION_DOWN:
                                mImageSelected = image;
                                toogleControlPanel(true);
                                break;
                            case MotionEvent.ACTION_MOVE:

                                int coords[] = {0, 0};
                                /**
                                 * pega a posição exata do nosso relative layout na tela
                                 */
                                relativeLayout.getLocationOnScreen(coords);

                                x = (motionEvent.getRawX() - (image.getWidth() / 2));
                                //faz o calculo do y utilizando a posição exata do clique
                                //menos
                                //a posicao inicial da altura do relative layout + 100 (para que a imagem fique um pouco acima do nosso dedo qdo arrastarmos
                                //mais
                                //a metade da altura da nosso imagem, para que o centro seja considerado
                                y = motionEvent.getRawY() - ((coords[1] + 100) + (image.getHeight() / 2));
                                image.setX(x);
                                image.setY(y);

                                break;
                            case MotionEvent.ACTION_UP:
                                break;
                        }
                        return true;
                    }
                });
            }
        };
    }


    @Override
    //AULA12_1
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.image_take_photo:
                //PERMISSAO_
                //As permissões do sistema são dividas em categorias, as mais importantes são as normais e perigosas:

                //cada permissão deve ser solicitada apenas no momento em que o app precisa utilizar tal recurso

                //primeiro verifica se o app possui permissão para chamar a camera
                if (!PermissionUtil.hasCameraPermission(this)) {
                    //caso seja android 6 ou superior e ainda nao tenha a permissão, então precisa pedir pro usuário aceitar
                    PermissionUtil.asksCameraPermission(this);
                } else {
                    dispatchTakePictureIntent();
                }
                break;

            case R.id.image_zoom_in:
                ImageUtil.handleZoomIn(this.mImageSelected);
                break;

            case R.id.image_zoom_out:
                ImageUtil.handleZoomOut(this.mImageSelected);
                break;

            case R.id.image_rotate_left:
                ImageUtil.handleRotateLeft(this.mImageSelected);
                break;

            case R.id.image_rotate_right:
                ImageUtil.handleRotateRight(this.mImageSelected);
                break;

            case R.id.image_finish:
                toogleControlPanel(false);
                break;

            case R.id.image_remove:
                this.mViewHolder.mRelativePhotoContent.removeView(this.mImageSelected);
                toogleControlPanel(false);
                break;

            //AULA_SOCIAL_4
            case R.id.image_whatsapp:
                SocialUtil.shareImageOnWhats(this, this.mViewHolder.mRelativePhotoContent, v);
                break;

            case R.id.image_facebook:
                Toast.makeText(this, R.string.openning_share, Toast.LENGTH_LONG).show();
                SocialUtil.shareImageOnFace(this, this.mViewHolder.mRelativePhotoContent, v);
                break;

            case R.id.image_twitter:
                SocialUtil.shareImageOnTwitter(this, this.mViewHolder.mRelativePhotoContent, v);
                break;

            case R.id.image_instagram:
                SocialUtil.shareImageOnInsta(this, this.mViewHolder.mRelativePhotoContent, v);
                break;
        }
    }

    //PERMISSAO_
    //O resultado da decisão do usuário será entregue via o método onRequestPermissionsResult
    // que recebe como parâmetro o código da chamada (requestCode), a lista de permissões e a lista de respostas do usuário.
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //verificamos o retorno da permissão de uso da camera
        if (requestCode == PermissionUtil.CAMERA_PERMISSION) {
            //caso tenha permitido o uso, chamamos o método que chama a camera
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                dispatchTakePictureIntent();
            } else {
                //caso contrario, exibimos uma Dialog com uma msg dizendo que nao temos permissão para chamar a camera
                new AlertDialog.Builder(this)
                        .setMessage(getString(R.string.without_permission_camera_explanation))
                        .setPositiveButton(R.string.btn_ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }
                        }).show();
            }
        }
    }

    @Override
    //trata o retorno da activity que foi chamada, no nosso caso o retorno da câmera
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //por meio do requestCode podemos validar se foi mesmo o retorno da camera (por meio do código que nós mesmos definimos na chamada)
        //e com o resultCode verificamos se a operação foi realizada com sucesso, caso tenha falhada o retorno será um RESULT_CANCELED
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
            //chamamos um método para definir a imagem em to do nosso RelativeLayout
            this.setPhotoAsBackground();
        }
    }

    @Override
    //AULA_11_4
    public boolean onLongClick(View view) {

        // Verifica qual a ação do click
        if (view.getId() == R.id.image_zoom_in) {
            mAutoIncrement = true;
            this.mLongEventType = LongEventType.ZoomIn;
            new RptUpdater().run();
        } else if (view.getId() == R.id.image_zoom_out) {
            mAutoIncrement = true;
            this.mLongEventType = LongEventType.ZoomOut;
            new RptUpdater().run();
        } else if (view.getId() == R.id.image_rotate_left) {
            mAutoIncrement = true;
            this.mLongEventType = LongEventType.RotateLeft;
            new RptUpdater().run();
        } else if (view.getId() == R.id.image_rotate_right) {
            mAutoIncrement = true;
            this.mLongEventType = LongEventType.RotateRight;
            new RptUpdater().run();
        }

        return false;
    }

    //AULA_11_3
    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        int id = view.getId();
        if (id == R.id.image_zoom_in || id == R.id.image_zoom_out || id == R.id.image_rotate_left || id == R.id.image_rotate_right) {

            // Verifica se usuário está liberando o botão
            if (motionEvent.getAction() == MotionEvent.ACTION_UP && mAutoIncrement) {
                mAutoIncrement = false;
                this.mLongEventType = null;
            }
        }
        return false;
    }

    /**
     * Adicona evento aos elementos
     */
    private void setListeners() {
        //PERMISSAO_
        this.findViewById(R.id.image_take_photo).setOnClickListener(this);

        //AULA_SOCIAL_3
        this.findViewById(R.id.image_instagram).setOnClickListener(this);
        this.findViewById(R.id.image_twitter).setOnClickListener(this);
        this.findViewById(R.id.image_facebook).setOnClickListener(this);
        this.findViewById(R.id.image_whatsapp).setOnClickListener(this);

        this.findViewById(R.id.image_zoom_in).setOnClickListener(this);
        this.findViewById(R.id.image_zoom_out).setOnClickListener(this);
        this.findViewById(R.id.image_rotate_left).setOnClickListener(this);
        this.findViewById(R.id.image_rotate_right).setOnClickListener(this);
        this.findViewById(R.id.image_finish).setOnClickListener(this);
        this.findViewById(R.id.image_remove).setOnClickListener(this);

        //AULA_11_1
        this.findViewById(R.id.image_zoom_in).setOnTouchListener(this);
        this.findViewById(R.id.image_zoom_out).setOnTouchListener(this);
        this.findViewById(R.id.image_rotate_left).setOnTouchListener(this);
        this.findViewById(R.id.image_rotate_right).setOnTouchListener(this);

        this.findViewById(R.id.image_zoom_in).setOnLongClickListener(this);
        this.findViewById(R.id.image_zoom_out).setOnLongClickListener(this);
        this.findViewById(R.id.image_rotate_left).setOnLongClickListener(this);
        this.findViewById(R.id.image_rotate_right).setOnLongClickListener(this);
    }

    /**
     * Evento disparado para tirar foto
     */
    //AULA12_2
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        // Certifica que a Activity da camera existe e consegue responder
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {

            // Cria o arquivo onde a foto será salva
            File photoFile = null;
            try {
                photoFile = ImageUtil.createImageFile(this);
                // Save a file: path for use with ACTION_VIEW intents
                this.mViewHolder.mUriPhotoPath = Uri.fromFile(photoFile);
            } catch (IOException ex) {
            }

            // Continua somente se teve sucesso na criação do arquivo
            if (photoFile != null) {

                StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
                StrictMode.setVmPolicy(builder.build());

                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    /**
     * Usa a imagem recém criada como plano de fundo
     */
    //AULA12_4
    private void setPhotoAsBackground() {

        // Obtém as dimensões da View onde a imagem será colocada
        int targetW = this.mViewHolder.mImagePhoto.getWidth();
        int targetH = this.mViewHolder.mImagePhoto.getHeight();

        // Obtém as dimensões do bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        //permite pegarmos dados da imagem sem alocala na memória
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(this.mViewHolder.mUriPhotoPath.getPath(), bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determina o quanto dimensionar a imagem
        //Math.min pega o menor valor calculado dentre os parametros passados
        int scaleFactor = Math.min(photoW / targetW, photoH / targetH);

        // Decodifica a imagem em um arquivo de imagem para preencher a View
        //agora queremos a imagem, então temos que setar para false
        bmOptions.inJustDecodeBounds = false;

        //seta o numero que deverá redimensionar a imagem com base no menor valor da divisão da altura e largura
        // exemplo: 2 calcula a imagem fica com 1/2 da altura e largura originais
        bmOptions.inSampleSize = scaleFactor; //divide a imagem com base no valor recebido,

        //decodificamos a imagem para um arquivo Bitmap
        Bitmap bitmap = BitmapFactory.decodeFile(this.mViewHolder.mUriPhotoPath.getPath(), bmOptions);

        //AULA_13
        Bitmap bitmapRotated = ImageUtil.rotateImageIfRequired(bitmap, this.mViewHolder.mUriPhotoPath);

        //setamos o bitmap no nosso imagePhoto
        this.mViewHolder.mImagePhoto.setImageBitmap(bitmapRotated);
    }

    /**
     * Enumeration para tipos de eventos
     */
    //AULA_11_5
    private enum LongEventType {
        ZoomIn, ZoomOut, RotateLeft, RotateRight
    }

    /**
     * ViewHolder
     */
    private static class ViewHolder {
        ImageView mImageTakePhoto;
        ImageView mImagePhoto;
        //AULA_SOCIAL_1
        ImageView mImageInstagram;
        ImageView mImageTwitter;
        ImageView mImageFacebook;
        ImageView mImageWahtsApp;

        Uri mUriPhotoPath;

        ImageView mButtonZoomIn;
        ImageView mButtonZoomOut;
        ImageView mButtonRotateLeft;
        ImageView mButtonRotateRight;
        ImageView mImageFinish;
        ImageView mImageRemove;

        LinearLayout mLinearSharePanel;
        LinearLayout mLinearControlPanel;
        RelativeLayout mRelativePhotoContent;
    }

    /**
     * Thread responsável por acionar um botão diversas vezes
     */
    //AULA_11_6
    private class RptUpdater implements Runnable {
        public void run() {

            // Se o usuário ainda estiver pressionando o botão
            if (mAutoIncrement)
                mRepeatUpdateHandler.postDelayed(new RptUpdater(), 50);
            //faz com que a thread seja adicionada a uma fila para que após 50 milis segundos execute novamente

            // Verifica o tipo de evento e toma a ação
            if (mLongEventType != null) {
                switch (mLongEventType) {
                    case ZoomIn:
                        ImageUtil.handleZoomIn(mImageSelected);
                        break;
                    case ZoomOut:
                        ImageUtil.handleZoomOut(mImageSelected);
                        break;
                    case RotateLeft:
                        ImageUtil.handleRotateLeft(mImageSelected);
                        break;
                    case RotateRight:
                        ImageUtil.handleRotateRight(mImageSelected);
                        break;
                }
            }
        }
    }
}