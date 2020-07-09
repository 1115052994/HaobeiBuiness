package com.netmi.baselibrary.data.base;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.nio.charset.Charset;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okio.Buffer;
import retrofit2.Converter;
import retrofit2.Retrofit;

/**
 * 类描述：
 * 创建人：Simple
 * 创建时间：2019/4/28
 * 修改备注：
 */
public class AesGsonConverterFactory extends Converter.Factory {

    //Aes加密对应的请求方式
    public static final String CONTENT_TYPE_AES = "text/aes";

    public static final String CONTENT_TYPE_JSON = "application/json;charset=UTF-8";

    //是否开启AES加密
    public static final boolean ENCRYPTION = false;

    private final Gson gson;

    public static AesGsonConverterFactory create(Gson gson) {
        if (gson == null) gson = new Gson();
        return new AesGsonConverterFactory(gson);
    }

    private AesGsonConverterFactory(Gson gson) {
        this.gson = gson;
    }

    @Override
    public Converter<?, RequestBody> requestBodyConverter(Type type,
                                                          Annotation[] parameterAnnotations, Annotation[] methodAnnotations, Retrofit retrofit) {
        TypeAdapter<?> adapter = gson.getAdapter(TypeToken.get(type));
        return new GsonRequestBodyConverter<>(gson, adapter);
    }

    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
        return new ResponseBodyConverter<>(gson, type);
    }


    /**
     * 自定义请求RequestBody
     */
    final class GsonRequestBodyConverter<T> implements Converter<T, RequestBody> {
        private final MediaType MEDIA_TYPE = MediaType.parse("application/json; charset=UTF-8");
        private final Charset UTF_8 = Charset.forName("UTF-8");

        private final Gson gson;
        private final TypeAdapter<T> adapter;

        GsonRequestBodyConverter(Gson gson, TypeAdapter<T> adapter) {
            this.gson = gson;
            this.adapter = adapter;
        }

        @Override
        public RequestBody convert(T value) throws IOException {
            Buffer buffer = new Buffer();
            Writer writer = new OutputStreamWriter(buffer.outputStream(), UTF_8);
            JsonWriter jsonWriter = gson.newJsonWriter(writer);
            adapter.write(jsonWriter, value);
            jsonWriter.close();
            return RequestBody.create(MEDIA_TYPE, buffer.readByteString());
        }

    }

    /**
     * 自定义响应ResponseBody
     */
    public class ResponseBodyConverter<T> implements Converter<ResponseBody, T> {
        private final Gson gson;
        private final Type type;

        ResponseBodyConverter(Gson gson, Type type) {
            this.gson = gson;
            this.type = type;
        }

        /**
         * @param value
         * @return T
         * @throws IOException
         */
        @Override
        public T convert(ResponseBody value) throws IOException {
            try {
                //这里加上你自己的解密方法
                if (ENCRYPTION) {
                    return gson.fromJson(Aes128CdcUtils.getInstance().decrypt(value.string()), type);
                } else {
                    return gson.fromJson(value.string(), type);
                }
            } finally {
                value.close();
            }
        }

    }
}