/*
 * Copyright (C) 2015 Square, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.jindan.network.converter;

import com.jindan.network.entity.HttpResult;
import com.jindan.network.entity.Params;
import com.jindan.network.exception.ApiException;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.jindan.tools.log.Logger;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.StringReader;

import okhttp3.ResponseBody;
import retrofit2.Converter;

/**
 * Author: TinhoXu
 * E-mail: xth@erongdu.com
 * Date: 2016/4/6 9:35
 * <p/>
 * Description:  JSON response 解析
 *
 * 2.0 数据解析模块功能
 */
@SuppressWarnings("unused")
final class JDResponseBodyConverter<T> implements Converter<ResponseBody, T> {
    private final Gson           gson;
    private final TypeAdapter<T> adapter;

    JDResponseBodyConverter(Gson gson, TypeAdapter<T> adapter) {
        this.gson = gson;
        this.adapter = adapter;
    }

    @Override
    public T convert(ResponseBody value) throws IOException {
        String response = value.string().trim();
        value.close();

        try {
            Logger.d("way", "sendRequest-应答-bodydata=" + response);
            //解析应答结果
            JSONObject object = new JSONObject(response);
            int code = object.getInt(Params.RES_CODE);
            if (Params.RES_SUCCEED == code || Params.RES_OK == code) {
                //正常情况直接进行解析
                StringReader reader = new StringReader(response);
                JsonReader jsonReader = gson.newJsonReader(reader);
                return adapter.read(jsonReader);
            } else {
                //失败的情况 解析 resCode ,对不成功的返回做统一处理
                //步骤一: 尝试采用标准模式解析
                HttpResult result = new HttpResult();
                try {
                    StringReader reader = new StringReader(response);
                    JsonReader jsonReader = gson.newJsonReader(reader);
                    T t = adapter.read(jsonReader);
                    if(null != t && t instanceof HttpResult) {
                        throw new ApiException((HttpResult) t);
                    }
                } catch (Exception e) {
                }
                //步骤二：如果解析失败采用兼容模式解析(data区不进行解析)
                result.setCode(code);
                result.setMsg(object.getString(Params.RES_MSG));
                throw new ApiException(result);
            }

        } catch (JSONException e) {
            e.printStackTrace();
            //JSON数据异常
            HttpResult result = new HttpResult();
            result.setCode(Params.JSON_PARSE_ERROR);
            result.setMsg(e.getMessage());
            throw new ApiException(result);
        } catch (ApiException e) {
            //已经截获的ApiException异常
            throw e;
        } catch (Exception ex) {
            ex.printStackTrace();
            HttpResult result = new HttpResult();
            result.setCode(Params.Error);
            result.setMsg(ex.getMessage());
            throw new ApiException(result);
        }
    }
}
