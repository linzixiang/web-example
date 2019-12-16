package com.linzx.framework.config.support;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.ByteBufOutputStream;
import org.redisson.client.codec.BaseCodec;
import org.redisson.client.handler.State;
import org.redisson.client.protocol.Decoder;
import org.redisson.client.protocol.Encoder;

import java.io.IOException;

/**
 * 自定义FastjsonCodec序列化,替换默认的JsonJacksonCodec序列化
 */
public class FastjsonCodec extends BaseCodec {

    public static final FastjsonCodec instance = new FastjsonCodec();

    static {
        ParserConfig.getGlobalInstance().setAutoTypeSupport(true);
        //如果遇到反序列化autoType is not support错误，请添加并修改一下包名到bean文件路径
        ParserConfig.getGlobalInstance().addAccept("org.apache.shiro.authz.SimpleAuthorizationInfo");
    }

    private FastjsonCodec() {
    }

    private final Encoder encoder = new Encoder() {

        @Override
        public ByteBuf encode(Object in) throws IOException {
            ByteBuf out = ByteBufAllocator.DEFAULT.buffer();
            ByteBufOutputStream os = new ByteBufOutputStream(out);
            try {
                JSON.writeJSONString(os, in, SerializerFeature.WriteClassName);
            } catch (IOException e) {
                out.release();
                throw e;
            } catch (Exception e) {
                out.release();
                throw new IOException(e);
            }
            return os.buffer();
        }

    };

    private final Decoder<Object> decoder = new Decoder<Object>() {

        @Override
        public Object decode(ByteBuf byteBuf, State state) throws IOException {
            return JSON.parseObject(new ByteBufInputStream(byteBuf), Object.class);
        }

    };


    @Override
    public Decoder<Object> getValueDecoder() {
        return decoder;
    }

    @Override
    public Encoder getValueEncoder() {
        return encoder;
    }

}
