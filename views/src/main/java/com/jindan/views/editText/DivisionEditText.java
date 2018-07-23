package com.jindan.views.editText;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;

import com.jindan.views.R;

/**
 * 带分隔符的格式化输入框
 * 说明: 支持用户手动输入、粘贴操作
 *
 * 改版履历：
 * 1.1.0 周荣华 2016/5/16. 创建
 */
@SuppressLint("AppCompatCustomView")
public class DivisionEditText extends EditText {
    /**
     * 输入内容监听
     */
    private OnEditTextChangedListener editTextChangedListener;
    public static final int DEFAULT_LEN = 8;
    /* 内容数组(包含输入内容和站位空白符) */
    private char[] text;
    /** 真实输入Text内容 */
    private StringBuilder lastChars = new StringBuilder();
    /** 当前内容 */
    private StringBuilder curChars = new StringBuilder();
    /* 数组实际长度 (文字内容+分隔符) */
    private int length;
    /* 允许输入的文字长度(不包含分隔符) */
    private int totalLength;
    /* 每组的长度 */
    private int eachLength;
    /** 当前输入内容长度(文字和占位符) */
    private int curLength;
    /* 分隔符 */
    private char delimiter;
    /* 占位符(默认采用空白字符占位) */
    private char placeHolder;
    /** 是否复位操作 */
    private boolean rollback = false;
    /** 变化前光标 */
    private int beforeChangePosition = 0;
    /** 光标位置 */
    private int curPosition = 0;

    public interface OnEditTextChangedListener {
        /** 输入内容变更 */
        public void onTextChanged(String text);
        /** 焦点状态变更 */
        public void onFocusChanged(boolean isFocus);
    }


    public DivisionEditText(Context context) {
        super(context);
    }

    public DivisionEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        try {
            // 初始化属性
            TypedArray typedArray = context.obtainStyledAttributes(attrs,
                    R.styleable.DivisionEditText);
            this.totalLength = typedArray.getInteger(
                    R.styleable.DivisionEditText_totalLength, 0);
            this.eachLength = typedArray.getInteger(
                    R.styleable.DivisionEditText_eachLength, 0);
            String sDelimiter = typedArray
                    .getString(R.styleable.DivisionEditText_delimiter);
            if (TextUtils.isEmpty(sDelimiter) || sDelimiter.length() > 1) {
                this.delimiter = '-';
            } else {
                this.delimiter = sDelimiter.charAt(0);
            }
            String sPlaceHolder = typedArray
                    .getString(R.styleable.DivisionEditText_placeHolder);
            if (TextUtils.isEmpty(sPlaceHolder) || sPlaceHolder.length() > 1) {
                this.placeHolder = ' ';
            } else {
                this.placeHolder = sPlaceHolder.charAt(0);
            }
            typedArray.recycle();

            // 初始化
            init();

            // 内容变化监听
            this.addTextChangedListener(new DivisionTextWatcher());
            // 获取焦点监听
            this.setOnFocusChangeListener(new DivisionFocusChangeListener());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public DivisionEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    /**
     * 初始化
     */
    public void init() {
        // 总共分几组
        length = 0;
        int groupNum = 0;
        // 如果每组长度(除数)不为0,计算
        if (this.eachLength > 0) {
            //计算分组个数
            groupNum = this.totalLength / this.eachLength;
            if(0 == this.totalLength % this.eachLength) {
                //恰好是分组结尾不需要增加分隔符
                groupNum--;
            }
        }
        // 实际长度
        length = this.totalLength + this.eachLength > 0 ? this.totalLength
                + groupNum : DEFAULT_LEN;
        //设置最大的内容长度
        setFilters(new InputFilter[]{new InputFilter.LengthFilter(length)});
        //设置单行显示
        setSingleLine();
        setInputType( InputType.TYPE_CLASS_NUMBER | InputType.TYPE_CLASS_TEXT );
        // 初始化数组
        text = new char[this.length];
        // 如果数组大小大于0,初始化里面内容
        // 空格占位,分隔符占位
        for (int i = 0; i < length; i++) {
            text[i] = placeHolder;
        }
        // 设置文本
        mySetText();
        // 设置焦点
        mySetSelection();
    }

    /**
     * 获取文字内容结果(不包含占位符)
     * 说明: 真正的字符串内容。必须全遍历，防止删除时丢失光标以后的文字
     *
     * @return 实际文字内容
     */
    public String getRealResult() {
        StringBuffer buffer = new StringBuffer();
        for(int i = 0; i < text.length; i++) {
            char ch = text[i];
            if (placeHolder != ch && delimiter != ch) {
                //有效字符
                buffer.append(ch);
                continue;
            }
        }
        return buffer.toString();
    }

    /**
     * 设置输入内容监听
     *
     * @param listener : EditText内容监听器
     */
    public void setEditTextChangedListener(OnEditTextChangedListener listener) {
        this.editTextChangedListener = listener;
    }

    /**
     * 获取文字内容结果(不包含占位符)
     * 说明: 真正的字符串内容。必须全遍历，防止删除时丢失光标以后的文字
     *
     * @param strText: 需要过滤的字符串
     */
    private String getRealText(String strText) {
        StringBuffer buffer = new StringBuffer();
        if(!TextUtils.isEmpty(strText)) {
            if (this.eachLength > 0) {
                //需要分段格式化
                for (int i = 0; i < strText.length(); i++) {
                    char ch = strText.charAt(i);
                    if (placeHolder != ch && delimiter != ch) {
                        //有效字符
                        buffer.append(ch);
                        continue;
                    }
                }
            } else {
                //不需要格式化
                buffer.append(strText);
            }
        }
        return buffer.toString();
    }

    /**
     * 获取格式化字符串内容
     *
     */
    public String getFormatText() {
        //当前有效的Text内容
        String realStr = curChars.toString();
        StringBuffer buffer = new StringBuffer();
        if(realStr.length() > 0) {
            if(this.eachLength > 0) {
                //需要格式化
                for(int i = 0; i < realStr.length(); i++) {
                    if(i > 0 && 0 == i % this.eachLength) {
                        //分组位置多增加分隔符
                        buffer.append(delimiter);
                    }
                    buffer.append(realStr.charAt(i));
                }
            } else {
                //不需要格式化
                buffer.append(realStr);
            }
        }
        return buffer.toString();
    }

    /**
     * 将Text内容缓存到StringBuilder
     *
     */
    private void saveRealText(StringBuilder builder, String realText) {
        if(null != builder) {
            builder.delete(0, builder.length());
            if(!TextUtils.isEmpty(realText)) {
                builder.append(realText);
            }
        }
    }

    /**
     * 文本监听
     *
     */
    private class DivisionTextWatcher implements TextWatcher {

        @Override
        public void afterTextChanged(Editable s) {

        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
            //缓存变化前的真实内容
            beforeChangePosition = DivisionEditText.this.getSelectionStart();
            saveRealText(lastChars, getRealText(s.toString()));
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {
            String tmpText = getRealText(s.toString());
            //过滤字符长度
            if(!rollback && tmpText.length() > totalLength) {
                //如果大于总长度限制的话过滤输入字符重新复位
                rollback = true;
                saveRealText(curChars, getRealResult());
                curPosition = beforeChangePosition;
                // 设置文本
                mySetText();
                // 设置焦点
                mySetSelection(curPosition);
                return;
            }
            //如果有效则缓存文字内容
            saveRealText(curChars, getRealText(s.toString()));
            //检查内容是否有变化
            if(rollback) {
                //复位处理完成内容已经恢复为之前状态
                rollback = false;
                return;
            }

            //如果实际文字内容没有变化
            if(lastChars.toString().equals(curChars.toString())) {
                //内容无变化
                if(!isFormatText(s.toString())){
                    //如果删除的分隔符需要重新格式化内容
                    curPosition = DivisionEditText.this.getSelectionStart();
                    // 设置文本
                    mySetText();
                    // 设置焦点
                    mySetSelection(curPosition);
                }
                return;
            }

            // 如果当前长度小于数组长度,认为使用退格
            if (lastChars.length() > curChars.length() && count == 0) {
                // 删除内容光标所在位置
                curPosition = DivisionEditText.this.getSelectionStart();
                //如果删除位置的前一个为分隔符需要继续往前跳过分隔符
                if(curPosition > 0 && 0 == curPosition % (eachLength + 1)) {
                    //跳过分隔符
                    curPosition--;
                }
                if (0 == curPosition && 1 == curChars.length()) {
                    //如果只有一个字符并且向前删除时删除仅有的内容
                    curChars.delete(0, curChars.length());
                }
                // 设置文本
                mySetText();
                // 设置焦点
                mySetSelection(curPosition);
            } else if (count == 1) {
                // 单个字符输入
                // 获取光标位置
                curPosition = DivisionEditText.this.getSelectionStart();
                if(curPosition > 0 && 0 == curPosition % (eachLength + 1)) {
                    if(curChars.length() < totalLength) {
                        //如果新增字符恰好位于分隔符位置并且还能新增加字符跳过分隔符
                        curPosition++;
                    }
                }
                // 设置文本
                mySetText();
                // 设置焦点
                mySetSelection(curPosition);
            } else if (lastChars.length() != curChars.length() && count > 0) {
                //粘贴(包含新增和替换)
                //文字内容已变化并且有新增字符内容
                curPosition = DivisionEditText.this.getSelectionStart();
                //因为替换后并不能确定用户是删除还是添加故不需要进行分隔符跳转
                //计算需要增加的分割符个数
                //格式化后的字符长度
                String fmtText = getFormatText();
                int maxCharsTotal = fmtText.length();
                //粘贴前的字符个数
                int lastCharsTotal = s.length();
                if (curChars.length() <= lastChars.length()) {
                    //如果有效字符减少或者不变光标不移动
                    //字符减少光标位置为当前位置
                } else {
                    //格式化后字符增加(计算新增的分隔符个数)
                    int totalNewChar = maxCharsTotal - lastCharsTotal;
                    if (totalNewChar > 0) {
                        //增加偏移的空格位置
                        curPosition += totalNewChar;
                    }
                }
                // 设置文本
                mySetText();
                // 设置焦点
                mySetSelection(curPosition);
            }
            //输入内容变化监听
            if(null != editTextChangedListener) {
                //返回当前结果
                editTextChangedListener.onTextChanged(getRealResult());
            }
        }
    }

    /** 是否是格式化的Text文本 */
    private boolean isFormatText(String s) {
        boolean isFormat = true;
        for (int i = 0; i < s.length(); i++) {
            if(i > 0 && 0 == (i + 1) % (this.eachLength + 1)) {
                isFormat = s.charAt(i) == delimiter;
            } else {
                isFormat = s.charAt(i) != delimiter;
            }
            if(!isFormat) {
                break;
            }
        }
        return isFormat;
    }

    /**
     * 获取焦点监听
     *
     * @author Administrator
     */
    private class DivisionFocusChangeListener implements OnFocusChangeListener {

        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (hasFocus) {
                // 设置焦点
                mySetSelection(0);
//                //增加事件统计
//                if(null != statistics) {
//                    SREPORTER.reportAction(statistics.model,
//                            statistics.action);
//                }
            }
            if(null != editTextChangedListener) {
                //焦点变化回调
                editTextChangedListener.onFocusChanged(hasFocus);
            }
        }
    }

    /**
     * 设置文本
     *
     */
    private void mySetText() {
        //获取格式化文本
        String fmtText = getFormatText();
        //实际长度为格式化后的字符内容长度
        curLength = fmtText.length();
        //保存格式化内容
        formatTextContent(fmtText);
        // 设置文本
        setText(fmtText);
    }

    /** 重新格式化Text内容缓存 */
    private void formatTextContent(String fmtText) {
        int fmtLen = TextUtils.isEmpty(fmtText) ? 0 : fmtText.length();
        //赋值新的内容
        if(!TextUtils.isEmpty(fmtText)) {
            fmtLen = fmtText.length();
            for(int i = 0; i < fmtLen; i++) {
                text[i] = fmtText.charAt(i);
            }
        }
        //后续部分添加占位符
        for(int i = fmtLen; i < text.length; i++) {
            text[i] = placeHolder;
        }
    }


    /**
     * 设置焦点
     *
     */
    private void mySetSelection() {
        mySetSelection(fullSelection());
    }

    /**
     * 设置焦点
     *
     * @param index
     */
    private void mySetSelection(int index) {
        int pos = index;
        if(index < 0) {
            pos = 0;
        } else if(index > curLength) {
            pos = curLength;
        }
        try {
            DivisionEditText.this.setSelection(pos);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 从光标位置起始,检查后面是否还有空的占位符
     *
     * @param selection
     * @return
     */
    private int isBlank(int selection) {
        int index = -1;
        for (int i = selection - 1; i < length; i++) {
            if (placeHolder == text[i]) {
                index = i;
                break;
            }
        }
        return index;
    }

    /**
     * 最后一个不空的字符后的光标位置
     *
     * @return
     */
    private int fullSelection() {
        int index = 0;
        for (int i = 0; i < text.length; i++) {
            if(placeHolder != text[i] && delimiter != text[i]) {
                index = i + 1;
                continue;
            }
            //分隔符或占位符
            if(text.length > 1 && i < text.length - 1) {
                char next = text[i + 1];
                //后续一个字符也是特殊字符
                if (placeHolder == next || delimiter == next) {
                    //两个特殊符号相连文字结束
                    break;
                }
            }
        }
        return index;
    }

    public Integer getTotalLength() {
        return totalLength;
    }

    public void setTotalLength(Integer totalLength) {
        this.totalLength = totalLength;
    }

    public Integer getEachLength() {
        return eachLength;
    }

    public void setEachLength(Integer eachLength) {
        this.eachLength = eachLength;
    }

    public char getDelimiter() {
        return delimiter;
    }

    public void setDelimiter(char delimiter) {
        if(0 == delimiter) {
            delimiter = '-';
        }
        this.delimiter = delimiter;
    }

    public char getPlaceHolder() {
        return placeHolder;
    }

    public void setPlaceHolder(char placeHolder) {
        if(0 == placeHolder) {
            placeHolder = ' ';
        }
        this.placeHolder = placeHolder;
    }
}
