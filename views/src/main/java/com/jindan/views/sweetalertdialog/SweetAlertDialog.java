package com.jindan.views.sweetalertdialog;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spannable;
import android.text.Spanned;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.Transformation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jindan.tools.utils.ContextHolder;
import com.jindan.views.R;
import com.pnikosis.materialishprogress.ProgressWheel;

import java.util.List;

/**
 * Author zhaolei
 * Date 2018/4/12
 * Description desc
 */

public class SweetAlertDialog extends Dialog implements View.OnClickListener {

    private View mDialogView;
    private AnimationSet mModalInAnim;
    private AnimationSet mModalOutAnim;
    private Animation mOverlayOutAnim;
    private Animation mErrorInAnim;
    private AnimationSet mErrorXInAnim;
    private AnimationSet mSuccessLayoutAnimSet;
    private Animation mSuccessBowAnim;
    private TextView mTitleTextView;
    private TextView mContentTextView;
    private FrameLayout mCustomContentFrame;
    private View mCustomContentView;
    private String mTitleText;
    private String mContentText;
    private int contextGravity;
    private Spanned mContentTextSpanned;
    private Spannable mContentTextSpannable;
    private boolean mShowCancel;
    private boolean mShowContent;
    private String mCancelText;
    private String mConfirmText;
    private SweetAlertType mAlertType;
    private FrameLayout mErrorFrame;
    private FrameLayout mSuccessFrame;
    private FrameLayout mProgressFrame;
    private LinearLayout mAlertBottom;
    private FrameLayout mEditTextFrame;
    private FrameLayout mPasswordFrame;
    private SuccessTickView mSuccessTick;
    private ImageView mErrorX;
    private View mSuccessLeftMask;
    private View mSuccessRightMask;
    private Drawable mCustomImgDrawable;
    private ImageView mCustomImage;
    private Button mConfirmButton;
    private Button mCancelButton;
    private ProgressHelper mProgressHelper;
    private FrameLayout mWarningFrame;
    private OnSweetClickListener mCancelClickListener;
    private OnSweetClickListener mConfirmClickListener;
    private View vLine;
    private EditText mEdittext;
    private PasswordInputView mPwdInputView;
    private TextView mInputTips;
    private boolean mCloseFromCancel;

    public SweetAlertDialog(Context context) {
        this(context, SweetAlertType.NORMAL_TYPE);
    }

    public SweetAlertDialog(Context context, SweetAlertType alertType) {
        super(context, R.style.alert_dialog);
        this.contextGravity = -1;
        this.mShowCancel = true;
        this.mShowContent = true;
        this.setCancelable(true);
        this.setCanceledOnTouchOutside(true);
        this.mProgressHelper = new ProgressHelper(context);
        this.mAlertType = alertType;
        this.mErrorInAnim = OptAnimationLoader.loadAnimation(this.getContext(), R.anim.error_frame_in);
        this.mErrorXInAnim = (AnimationSet) OptAnimationLoader.loadAnimation(this.getContext(), R.anim.error_x_in);
        if (Build.VERSION.SDK_INT <= 10) {
            List<Animation> childAnims = this.mErrorXInAnim.getAnimations();

            int idx;
            for (idx = 0; idx < childAnims.size() && !(childAnims.get(idx) instanceof AlphaAnimation); ++idx) {
                ;
            }

            if (idx < childAnims.size()) {
                childAnims.remove(idx);
            }
        }

        this.mSuccessBowAnim = OptAnimationLoader.loadAnimation(this.getContext(), R.anim.success_bow_roate);
        this.mSuccessLayoutAnimSet = (AnimationSet) OptAnimationLoader.loadAnimation(this.getContext(), R.anim.success_mask_layout);
        this.mModalInAnim = (AnimationSet) OptAnimationLoader.loadAnimation(this.getContext(), R.anim.modal_in);
        this.mModalOutAnim = (AnimationSet) OptAnimationLoader.loadAnimation(this.getContext(), R.anim.modal_out);
        this.mModalOutAnim.setAnimationListener(new Animation.AnimationListener() {
            public void onAnimationStart(Animation animation) {
            }

            public void onAnimationEnd(Animation animation) {
                SweetAlertDialog.this.mDialogView.setVisibility(View.GONE);
                SweetAlertDialog.this.mDialogView.post(new Runnable() {
                    public void run() {
                        if (SweetAlertDialog.this.mCloseFromCancel) {
                            SweetAlertDialog.super.cancel();
                        } else {
                            SweetAlertDialog.super.dismiss();
                        }

                    }
                });
            }

            public void onAnimationRepeat(Animation animation) {
            }
        });
        this.mOverlayOutAnim = new Animation() {
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                WindowManager.LayoutParams wlp = SweetAlertDialog.this.getWindow().getAttributes();
                wlp.alpha = 1.0F - interpolatedTime;
                SweetAlertDialog.this.getWindow().setAttributes(wlp);
            }
        };
        this.mOverlayOutAnim.setDuration(120L);
    }

    @SuppressLint("ResourceType")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.alert_dialog);
        this.mDialogView = this.getWindow().getDecorView().findViewById(16908290);
        this.mTitleTextView = (TextView) this.findViewById(R.id.title_text);
        this.mContentTextView = (TextView) this.findViewById(R.id.content_text);
        if (this.mContentTextView != null) {
            this.mContentTextView.setVisibility(this.mShowContent ? View.VISIBLE : View.GONE);
        }

        this.mCustomContentFrame = (FrameLayout) this.findViewById(R.id.custom_content_fl);
        this.mErrorFrame = (FrameLayout) this.findViewById(R.id.error_frame);
        this.mErrorX = (ImageView) this.mErrorFrame.findViewById(R.id.error_x);
        this.mSuccessFrame = (FrameLayout) this.findViewById(R.id.success_frame);
        this.mProgressFrame = (FrameLayout) this.findViewById(R.id.progress_dialog);
        this.mSuccessTick = (SuccessTickView) this.mSuccessFrame.findViewById(R.id.success_tick);
        this.mSuccessLeftMask = this.mSuccessFrame.findViewById(R.id.mask_left);
        this.mSuccessRightMask = this.mSuccessFrame.findViewById(R.id.mask_right);
        this.mCustomImage = (ImageView) this.findViewById(R.id.custom_image);
        this.mWarningFrame = (FrameLayout) this.findViewById(R.id.warning_frame);
        this.mConfirmButton = (Button) this.findViewById(R.id.confirm_button);
        this.mEdittext = (EditText) this.findViewById(R.id.edit_text1);
        this.mEditTextFrame = (FrameLayout) this.findViewById(R.id.edittext_dialog);
        this.mPwdInputView = (PasswordInputView) this.findViewById(R.id.pwd_edit);
        this.mInputTips = (TextView) this.findViewById(R.id.input_tips);
        this.mPasswordFrame = (FrameLayout) this.findViewById(R.id.pwd_dialog);
        this.vLine = this.findViewById(R.id.v_line);
        this.mCancelButton = (Button) this.findViewById(R.id.cancel_button);
        if (this.mCancelButton != null) {
            this.mCancelButton.setVisibility(this.mShowCancel ? View.VISIBLE : View.GONE);
        }

        if (this.vLine != null) {
            this.vLine.setVisibility(this.mShowCancel ? View.VISIBLE : View.GONE);
        }

        this.mProgressHelper.setProgressWheel((ProgressWheel) this.findViewById(R.id.progressWheel));
        this.mAlertBottom = (LinearLayout) this.findViewById(R.id.alter_bottom);
        this.mConfirmButton.setOnClickListener(this);
        this.mCancelButton.setOnClickListener(this);
        this.setTitleText(this.mTitleText);
        this.setCustomContentView(this.mCustomContentView);
        if (!"".equals(this.mContentText) && this.mContentText != null) {
            this.setContentText(this.mContentText);
        } else if (this.mContentTextSpannable != null) {
            this.setContentText(this.mContentTextSpannable);
        } else if (this.mContentTextSpanned != null) {
            this.setContentText(this.mContentTextSpanned);
        }

        if (this.contextGravity != -1) {
            this.setContentTextGray(this.contextGravity);
        }

        this.setCancelText(this.mCancelText);
        this.setConfirmText(this.mConfirmText);
        this.changeAlertType(this.mAlertType, true);
    }

    private void restore() {
        this.mCustomContentFrame.setVisibility(View.GONE);
        this.mCustomImage.setVisibility(View.GONE);
        this.mErrorFrame.setVisibility(View.GONE);
        this.mSuccessFrame.setVisibility(View.GONE);
        this.mWarningFrame.setVisibility(View.GONE);
        this.mProgressFrame.setVisibility(View.GONE);
        this.mConfirmButton.setVisibility(View.GONE);
        this.mConfirmButton.setBackgroundResource(R.drawable.blue_button_background);
        this.mErrorFrame.clearAnimation();
        this.mErrorX.clearAnimation();
        this.mSuccessTick.clearAnimation();
        this.mSuccessLeftMask.clearAnimation();
        this.mSuccessRightMask.clearAnimation();
    }

    private void playAnimation() {
        if (this.mAlertType == SweetAlertType.ERROR_TYPE) {
            this.mErrorFrame.startAnimation(this.mErrorInAnim);
            this.mErrorX.startAnimation(this.mErrorXInAnim);
        } else if (this.mAlertType == SweetAlertType.SUCCESS_TYPE) {
            this.mSuccessTick.startTickAnim();
            this.mSuccessRightMask.startAnimation(this.mSuccessBowAnim);
        }

    }

    private void changeAlertType(SweetAlertType alertType, boolean fromCreate) {
        this.mAlertType = alertType;
        if (this.mDialogView != null) {
            if (!fromCreate) {
                this.restore();
            }

            switch (this.mAlertType.ordinal()) {
                case 1:
                    this.mErrorFrame.setVisibility(View.VISIBLE);
                    break;
                case 2:
                    this.mSuccessFrame.setVisibility(View.VISIBLE);
                    this.mSuccessLeftMask.startAnimation((Animation) this.mSuccessLayoutAnimSet.getAnimations().get(0));
                    this.mSuccessRightMask.startAnimation((Animation) this.mSuccessLayoutAnimSet.getAnimations().get(1));
                    break;
                case 3:
                    this.mWarningFrame.setVisibility(View.VISIBLE);
                    break;
                case 4:
                    this.setCustomImage(this.mCustomImgDrawable);
                    break;
                case 5:
                    this.setCustomContentView(this.mCustomContentView);
                    break;
                case 6:
                    this.mProgressFrame.setVisibility(View.VISIBLE);
                    this.mConfirmButton.setVisibility(View.GONE);
                    break;
                case 7:
                    this.mEditTextFrame.setVisibility(View.VISIBLE);
                    break;
                case 8:
                    this.mPasswordFrame.setVisibility(View.VISIBLE);
                    this.mPwdInputView.setPasswordLength(6);
            }

            if (!fromCreate) {
                this.playAnimation();
            }
        }

    }

    public SweetAlertType getAlerType() {
        return this.mAlertType;
    }

    public void changeAlertType(SweetAlertType alertType) {
        this.changeAlertType(alertType, false);
    }

    public String getTitleText() {
        return this.mTitleText;
    }

    public SweetAlertDialog setTitleText(String text) {
        this.mTitleText = text;
        if (this.mTitleTextView != null && this.mTitleText != null) {
            this.mTitleTextView.setText(this.mTitleText);
            this.mTitleTextView.setVisibility(View.VISIBLE);
            updateTitleContentMargin();
        }

        return this;
    }

    public SweetAlertDialog setTitleTextColor(int color) {
        if (this.mTitleTextView != null && color != 0) {
            this.mTitleTextView.setTextColor(this.getContext().getResources().getColor(color));
        }

        return this;
    }

    public SweetAlertDialog setCustomImage(Drawable drawable) {
        this.mCustomImgDrawable = drawable;
        if (this.mCustomImage != null && this.mCustomImgDrawable != null) {
            this.mCustomImage.setVisibility(View.VISIBLE);
            this.mCustomImage.setImageDrawable(this.mCustomImgDrawable);
        }

        return this;
    }

    public SweetAlertDialog setCustomImage(int resourceId) {
        return this.setCustomImage(this.getContext().getResources().getDrawable(resourceId));
    }

    public String getContentText() {
        return this.mContentText;
    }

    public SweetAlertDialog setContentText(String text) {
        this.mContentText = text;
        if (this.mContentTextView != null && this.mContentText != null) {
            this.showContentText(true);
            this.mContentTextView.setText(this.mContentText);
            updateTitleContentMargin();
        }

        return this;
    }

    public SweetAlertDialog setContentText(Spannable text) {
        this.mContentTextSpannable = text;
        if (this.mContentTextView != null && this.mContentTextSpannable != null) {
            this.showContentText(true);
            this.mContentTextView.setText(this.mContentTextSpannable);
            updateTitleContentMargin();
        }

        return this;
    }

    public SweetAlertDialog setContentText(Spanned text) {
        this.mContentTextSpanned = text;
        if (this.mContentTextView != null && this.mContentTextSpanned != null) {
            this.showContentText(true);
            this.mContentTextView.setText(this.mContentTextSpanned);
            updateTitleContentMargin();
        }

        return this;
    }

    public SweetAlertDialog setCustomContentView(View customView) {
        this.mCustomContentView = customView;
        if (null != this.mCustomContentFrame && null != mCustomContentView) {
            this.mCustomContentFrame.removeAllViews();
            this.mCustomContentFrame.addView(this.mCustomContentView);
            this.mCustomContentFrame.setVisibility(View.VISIBLE);
            updateTitleContentMargin();
        }

        return this;
    }

    @TargetApi(17)
    public SweetAlertDialog setContentTextGray(int gravity) {
        this.contextGravity = gravity;
        System.out.println("contextGravity111" + this.contextGravity);
        if (this.mContentTextView != null && gravity != -1) {
            System.out.println("contextGravity222" + this.contextGravity);
            this.mContentTextView.setGravity(this.contextGravity);
            if (this.contextGravity == 3) {
                this.mContentTextView.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);
            } else if (this.contextGravity == 5) {
                this.mContentTextView.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_END);
            }
        }

        return this;
    }

    public SweetAlertDialog setContentTextColor(int color) {
        if (this.mContentTextView != null && color != 0) {
            this.showContentText(true);
            this.mContentTextView.setTextColor(this.getContext().getResources().getColor(color));
        }

        return this;
    }

    public boolean isShowCancelButton() {
        return this.mShowCancel;
    }

    public SweetAlertDialog showCancelButton(boolean isShow) {
        this.mShowCancel = isShow;
        if (this.mCancelButton != null) {
            this.mCancelButton.setVisibility(this.mShowCancel ? View.VISIBLE : View.GONE);
        }

        if (this.vLine != null) {
            this.vLine.setVisibility(this.mShowCancel ? View.VISIBLE : View.GONE);
        }

        updateConfirmBtnBackground();
        return this;
    }

    public boolean isShowContentText() {
        return this.mShowContent;
    }

    public SweetAlertDialog showContentText(boolean isShow) {
        this.mShowContent = isShow;
        if (this.mContentTextView != null) {
            this.mContentTextView.setVisibility(this.mShowContent ? View.VISIBLE : View.GONE);
        }

        return this;
    }

    public String getCancelText() {
        return this.mCancelText;
    }

    public SweetAlertDialog setCancelText(String text) {
        this.mCancelText = text;
        if (this.mCancelButton != null && this.mCancelText != null) {
            this.showCancelButton(true);
            this.mCancelButton.setText(this.mCancelText);
        }
        updateConfirmBtnBackground();
        return this;
    }

    public SweetAlertDialog setCancelTextColor(int color) {
        if (this.mCancelButton != null && color != 0) {
            this.mCancelButton.setTextColor(this.getContext().getResources().getColor(color));
        }

        return this;
    }

    public String getConfirmText() {
        return this.mConfirmText;
    }

    public SweetAlertDialog setConfirmText(String text) {
        this.mConfirmText = text;
        if (this.mConfirmButton != null && this.mConfirmText != null) {
            this.mConfirmButton.setText(this.mConfirmText);
        }
        updateConfirmBtnBackground();
        return this;
    }

    public SweetAlertDialog setConfirmTextColor(int color) {
        if (this.mConfirmButton != null && color != 0) {
            this.mConfirmButton.setTextColor(this.getContext().getResources().getColor(color));
        }

        return this;
    }

    public SweetAlertDialog setCancelClickListener(OnSweetClickListener listener) {
        this.mCancelClickListener = listener;
        return this;
    }

    public SweetAlertDialog setConfirmClickListener(OnSweetClickListener listener) {
        this.mConfirmClickListener = listener;
        return this;
    }

    protected void onStart() {
        this.mDialogView.startAnimation(this.mModalInAnim);
        this.playAnimation();
    }

    public void cancel() {
        this.dismissWithAnimation(true);
    }

    public void dismissWithAnimation() {
        this.dismissWithAnimation(false);
    }

    private void dismissWithAnimation(boolean fromCancel) {
        this.mCloseFromCancel = fromCancel;
        this.mConfirmButton.startAnimation(this.mOverlayOutAnim);
        this.mDialogView.startAnimation(this.mModalOutAnim);
    }

    public void onClick(View view) {
        if (view.getId() == R.id.cancel_button) {
            if (this.mCancelClickListener != null) {
                this.mCancelClickListener.onClick(this);
            } else {
                this.dismissWithAnimation();
            }
        } else if (view.getId() == R.id.confirm_button) {
            if (this.mConfirmClickListener != null) {
                this.mConfirmClickListener.onClick(this);
            } else {
                this.dismissWithAnimation();
            }
        }

    }

    public ProgressHelper getProgressHelper() {
        return this.mProgressHelper;
    }

    public void setPasswordLength(int length) {
        if (this.mPwdInputView != null) {
            this.mPwdInputView.setPasswordLength(length);
            this.mPwdInputView.setFilters(new InputFilter[]{new InputFilter.LengthFilter(length)});
        }

    }

    public void setPasswordColor(int color) {
        if (this.mPwdInputView != null) {
            this.mPwdInputView.setPasswordColor(color);
        }

    }

    public String getPwdStr() {
        return this.mPwdInputView != null ? this.mPwdInputView.getEditableText().toString() : "";
    }

    public void setInputTips(String tips) {
        if (this.mInputTips != null) {
            if (tips != null && !"".equals(tips)) {
                this.mInputTips.setVisibility(View.VISIBLE);
                this.mInputTips.setText(tips);
            } else {
                this.mInputTips.setVisibility(View.GONE);
            }
        }

    }

    public void setInputTipsColor(int color) {
        if (this.mInputTips != null) {
            this.mInputTips.setTextColor(color);
        }

    }

    public String getEditStr() {
        return this.mEdittext != null ? this.mEdittext.getEditableText().toString() : "";
    }

    public void setBottomButton(View view) {
        this.mAlertBottom.removeAllViews();
        this.mConfirmButton = (Button) view.findViewById(R.id.confirm_button);
        this.mCancelButton = (Button) view.findViewById(R.id.cancel_button);
        if (null != mConfirmButton) {
            this.mConfirmButton.setOnClickListener(this);
        }
        if (null != mCancelButton) {
            this.mCancelButton.setOnClickListener(this);
        }
        this.mAlertBottom.addView(view);
    }

    /**
     * 修改单标题或者单内容边距
     */
    private void updateTitleContentMargin() {
        LinearLayout.LayoutParams lp;
        if (null != this.mTitleText
                && null == this.mContentText
                && null == this.mCustomContentView) {
            lp = (LinearLayout.LayoutParams) this.mTitleTextView.getLayoutParams();
            lp.topMargin = ContextHolder.getDimenPixel(R.dimen.y64);
            lp.bottomMargin = ContextHolder.getDimenPixel(R.dimen.y52);
            this.mTitleTextView.setLayoutParams(lp);
        } else if (null == this.mTitleText
                && null != this.mContentText
                && null == this.mCustomContentView) {
            lp = (LinearLayout.LayoutParams) this.mContentTextView.getLayoutParams();
            lp.topMargin = ContextHolder.getDimenPixel(R.dimen.y68);
            lp.bottomMargin = ContextHolder.getDimenPixel(R.dimen.y54);
            this.mContentTextView.setLayoutParams(lp);
            this.mContentTextView.setTextColor(ContextHolder.getColor(R.color.text_black));
        }
    }

    public void updateConfirmBtnBackground() {
        if (null != this.mConfirmButton) {
            if (!this.mShowCancel) {
                this.mConfirmButton.setBackgroundResource(R.drawable.confirm_button_only_background);
            } else {
                this.mConfirmButton.setBackgroundResource(R.drawable.confirm_button_background);
            }
        }
    }
}
