package com.jindan.views.editText;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.DigitsKeyListener;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jindan.tools.log.LogUtils;
import com.jindan.views.R;


/**
 * 组合输入框
 *
 * @author Joe
 * @version 1.0 2017/12/24
 */
public class ComboEditView extends RelativeLayout {
    final static String ANDROIDXML = "http://schemas.android.com/apk/res/android";
    /** 页面上下文 */
    private Context context;
    /** 视图Inflater */
    private LayoutInflater inflater;
    /** 标题部分 */
    private TextView txTitle;
    /** 输入内容Tips */
    private TextView txTips;
    /** 输入内容Header */
    private TextView txHeader;
    /** 输入框部分 */
    private EditText etComboEdit;
    /** 删除按钮 */
    private ImageView btnDelete;
    /** 底部分割线 */
    private View splitLine;
    /** 输入框结果变更结果监听 */
    private OnEditTextChangedListener textChangedListener;
    /** 标题部分内容 */
    private String title;
    /** 小数位数 */
    private int decimalDigits = 0;
    private boolean isError = false;
    private boolean focusable = true;
    private boolean focusableInTouchMode = false;

    /**
     * 设置输入键值
     *
     * @param instance
     */
    public void setKeyListener(DigitsKeyListener instance) {
        etComboEdit.setKeyListener(instance);
    }

    /**
     * 手机号监听接口
     */
    public interface OnEditTextChangedListener {
        /** 输入内容变更 */
        public void onTextChanged(String text);
        /** 焦点状态变更 */
        public void onFocusChanged(boolean isFocus);
    }

    public ComboEditView(Context context) {
        this(context, null);
    }

    public ComboEditView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        //视图初始化
        inflater = LayoutInflater.from(getContext());
        inflater.inflate(R.layout.view_combo_edit, this);
        txTitle = findViewById(R.id.combo_title);
        txTips = findViewById(R.id.combo_tips);
        txHeader = findViewById(R.id.combo_edit_header);
        etComboEdit = findViewById(R.id.combo_edit);
        btnDelete = findViewById(R.id.delete_all);
        btnDelete.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //删除用户输入信息
                etComboEdit.setText("");
            }
        });
        splitLine = findViewById(R.id.line_split);
        //初始化视图
        try {
            // 初始化属性
            TypedArray typedArray = context.obtainStyledAttributes(attrs,
                    R.styleable.ComboEditView);
            //标题内容
            title = typedArray
                    .getString(R.styleable.ComboEditView_comboTitle);

            String editHint = typedArray
                    .getString(R.styleable.ComboEditView_comboEditHint);

            String editTips = typedArray
                    .getString(R.styleable.ComboEditView_comboEditTips);
            String editHeader = typedArray
                    .getString(R.styleable.ComboEditView_comboEditHeader);

            int titleVisibility = typedArray
                    .getInt(R.styleable.ComboEditView_comboTitleVisibility, VISIBLE);
            int splitVisibility = typedArray
                    .getInt(R.styleable.ComboEditView_comboSplitVisibility, VISIBLE);
            int headVisibility = typedArray
                    .getInt(R.styleable.ComboEditView_comboHeaderVisibility, GONE);

            //输入小数位数(大于0表示输入小数)
            this.decimalDigits = typedArray
                    .getInt(R.styleable.ComboEditView_comboDecimalDigits, 0);

            typedArray.recycle();

            txTitle.setText(title);
            txTitle.setVisibility(titleVisibility);
            splitLine.setVisibility(splitVisibility);
            txHeader.setVisibility(headVisibility);
            if(!TextUtils.isEmpty(editHeader)) {
                txHeader.setText(editHeader);
            }
            //保证能够设置输入的内容长度
            etComboEdit.setHint(editHint);
            if(!TextUtils.isEmpty(editTips)) {
                //Tips提示语
                txTips.setText(editTips);
            }
            //系统属性
            int maxLength = attrs.getAttributeIntValue(ANDROIDXML, "maxLength", 0);
            if(maxLength > 0) {
                //设置最大的内容长度
                etComboEdit.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLength)});
            }
            focusable = attrs.getAttributeBooleanValue(ANDROIDXML, "focusable", true);
            etComboEdit.setFocusable(focusable);
            focusableInTouchMode = attrs.getAttributeBooleanValue(ANDROIDXML, "focusableInTouchMode", true);
            etComboEdit.setFocusableInTouchMode(focusableInTouchMode);
            etComboEdit.setOnTouchListener(new OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        //输入框点击事件
                        obtainFocus();
                    }
                    return false;
                }
            });
            //如果是输入小数
            if(this.decimalDigits > 0) {
                //设置样式为输入小数
                etComboEdit.setInputType(InputType.TYPE_CLASS_NUMBER
                        | InputType.TYPE_NUMBER_FLAG_DECIMAL
                        | InputType.TYPE_NUMBER_VARIATION_NORMAL);
            }

            String str = attrs.getAttributeValue(ANDROIDXML, "text");
            if(!TextUtils.isEmpty(str)) {
                etComboEdit.setText(str);
            }
            //设置输入框变化事件
            setEditTextChange(etComboEdit);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取焦点
     */
    public void obtainFocus() {
        if (focusable && focusableInTouchMode) {
            etComboEdit.setFocusable(true);
            etComboEdit.setFocusableInTouchMode(true);
            etComboEdit.requestFocus();
        }
    }

    /**
     * 清理焦点
     */
    public void clearFocus() {
        etComboEdit.clearFocus();
        etComboEdit.setFocusable(false);
        etComboEdit.setFocusableInTouchMode(false);
    }

    /**
     * 设置输入框点击事件
     * 说明: 这是为了解决ComboEdit点击输入框部分无响应的问题增加的补丁方案
     */
    public void setEditClickListener(OnClickListener l) {
        etComboEdit.setOnClickListener(l);
    }

    /** 设置输入框变化事件 */
    private void setEditTextChange(final EditText edittext) {
        edittext.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                //焦点变化
                inputFocusChanged(v, hasFocus);
            }
        });
        edittext.addTextChangedListener(new TextWatcher() {

            /**
             * 在EditText里的内容即将发生变化之前触发，EditText里的内容变化有3种方式:
             * 1.新增加字符；2.删除字符；3.替换输入框中的若干个字符。
             *
             * @param s:     之前的文字内容
             * @param start: 添加文字的位置(从0开始)
             * @param count: 不知道 一直是0
             * @param after: 添加的文字总数
             */
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//                LogUtils.d("LoginView", "beforeTextChanged");
            }

            /**
             * Text变化事件
             *
             * @param s:      之后的文字内容
             * @param start:  start:添加文字的位置(从0开始)
             * @param before: 不知道 一直是0
             * @param count:  添加的文字总数
             */
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                LogUtils.d("LoginView", "onTextChanged");
            }

            /**
             * Text变化之后事件
             *
             * @param s: 表示最终内容
             */
            @Override
            public void afterTextChanged(Editable s) {
//                LogUtils.d("LoginView", "afterTextChanged");
                inputTextChanged(edittext);
            }
        });
    }

    /**
     * 　设置事件监听
     *
     * @param v
     * @param hasFocus
     */
    private void inputFocusChanged(View v, boolean hasFocus) {
        //刷新输入状态
        refreshTextStatus(etComboEdit.getText().toString());
        //发送回调处理
        if(null != textChangedListener) {
            textChangedListener.onFocusChanged(hasFocus);
        }
    }

    /**
     * 刷新输入状态
     *
     * @param str : 输入框内容
     */
    private String refreshTextStatus(String str) {
        //如果有内容显示删除按钮
        if (focusable && focusableInTouchMode
                && !TextUtils.isEmpty(str) && etComboEdit.hasFocus()) {
            //可编辑状态并且获取焦点状态
            btnDelete.setVisibility(VISIBLE);
        } else {
            btnDelete.setVisibility(GONE);
        }
        return str;
    }

    /**
     * 输入Text变化事件
     */
    protected void inputTextChanged(Object obj) {
        if (null == obj || !(obj instanceof EditText)) {
            return;
        }
        EditText edit = (EditText) obj;
        String str = edit.getText().toString();
        if(this.decimalDigits > 0 && !TextUtils.isEmpty(str)) {
            //如果是输入小数进行小数位数检测
            //当前小数点位置
            int index = str.indexOf(".");
            int selectStart = edit.getSelectionStart();
            int selectStop = edit.getSelectionEnd();
            LogUtils.d("ComboEditView", "index: " + index + " str: " + str + " decimalDigits：" + decimalDigits);

            if (index > 0 && (str.length() - index - 1) > decimalDigits){
                LogUtils.d("ComboEditView", "delete index: " + index + " str end: " + str.length());
                //删除多余输入的字（不会显示出来）
                edit.getText().delete(index + decimalDigits + 1, str.length());
                edit.setText(edit.getText());
                //设置光标到末尾
                LogUtils.d("ComboEditView", "selection before start: " + selectStart + " selectStop: " + selectStop);
                selectStart = Math.max(0, Math.min(selectStart, edit.getText().length()));
                selectStop = Math.max(selectStart, Math.min(selectStop, edit.getText().length()));
                LogUtils.d("ComboEditView", "selection stop start: " + selectStart + " selectStop: " + selectStop);
                edit.setSelection(selectStart, selectStop);
                return;
            }
        }

        String result = refreshTextStatus(str);
        //发送输入框内容变更的监听
        if(null != textChangedListener) {
            textChangedListener.onTextChanged(result);
        }
    }

    /**
     * 设置输入框输入类型
     *
     * @param type : InputType
     */
    public void setInputType(int type) {
        etComboEdit.setInputType(type);
    }

    /**
     * 设置输入内容变更监听
     * 说明: 用于实时监控输入内容
     *
     * @param textChangedListener
     */
    public void setTextChangedListener(OnEditTextChangedListener textChangedListener) {
        this.textChangedListener = textChangedListener;
    }

    /**
     * 设置标题栏是否可见属性
     *
     * @param visibility : 可见属性
     */
    public void setTitleVisibility(int visibility) {
        txTitle.setVisibility(visibility);
    }

    /**
     * 设置底部分割线是否可见属性
     *
     * @param visibility : 可见属性
     */
    public void setSplitLineVisibility(int visibility) {
        splitLine.setVisibility(visibility);
    }

    /**
     * 显示错误状态
     */
    public void showErrorStatus() {
        isError = true;
        this.txTitle.setText(title + " !");
        this.txTitle.setTextColor(context.getResources().getColor(R.color.color_warning));
    }

    /**
     * 显示正常状态
     */
    public void showNormalStatus() {
        isError = false;
        this.txTitle.setText(title);
        this.txTitle.setTextColor(context.getResources().getColor(R.color.text_black_second));
    }

    private void updateStatus() {
        if(isError) {
            showErrorStatus();
        } else {
            showNormalStatus();
        }
    }

    /**
     * 设置标题
     *
     * @param title : 标题
     */
    public void setTitle(String title) {
        this.title = !TextUtils.isEmpty(title) ? title : "";
        //更新标题信息
        updateStatus();
    }

    /**
     * 设置输入框提示
     *
     * @param hint : 输入框提示内容
     */
    public void setEditHint(String hint) {
        if(!TextUtils.isEmpty(hint)) {
            etComboEdit.setHint(hint);
        }
    }

    /**
     * 设置EditText内容
     *
     * @param text
     */
    public void setText(String text) {
        etComboEdit.setText(text);
    }

    /**
     * 获取输入内容
     */
    public String getText() {
        return etComboEdit.getText().toString();
    }
}
