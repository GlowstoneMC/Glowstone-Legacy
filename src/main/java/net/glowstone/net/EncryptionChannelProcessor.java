package net.glowstone.net;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.ShortBufferException;
import javax.crypto.spec.IvParameterSpec;

import com.flowpowered.networking.processor.simple.SimpleMessageProcessor;

public class EncryptionChannelProcessor extends SimpleMessageProcessor {

    private CryptBuf encodeBuf;
    private CryptBuf decodeBuf;

    public EncryptionChannelProcessor(SecretKey sharedSecret, int capacity) {
        super(capacity);

        Cipher encode;
        try {
            encode = Cipher.getInstance("AES/CFB8/NoPadding");
            Cipher decode = Cipher.getInstance("AES/CFB8/NoPadding");
            encode.init(Cipher.ENCRYPT_MODE, sharedSecret,  new IvParameterSpec(sharedSecret.getEncoded()));
            decode.init(Cipher.DECRYPT_MODE, sharedSecret, new IvParameterSpec(sharedSecret.getEncoded()));

            this.encodeBuf = new CryptBuf(encode, capacity * 2);
            this.decodeBuf = new CryptBuf(decode, capacity * 2);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | InvalidAlgorithmParameterException e) {
            e.printStackTrace(); //should never happen
            throw new AssertionError(e);
        }
    }

    @Override
    protected void writeEncode(byte[] buf, int length) {
        encodeBuf.write(buf, length);
    }

    @Override
    protected int readEncode(byte[] buf) {
        return encodeBuf.read(buf);
    }

    @Override
    protected void writeDecode(byte[] buf, int length) {
        decodeBuf.write(buf, length);
    }

    @Override
    protected int readDecode(byte[] buf) {
        return decodeBuf.read(buf);
    }

    private static class CryptBuf {
        private final Cipher cipher;
        private final byte[] buffer;
        private int writePosition;
        private int readPosition;

        private CryptBuf(Cipher cipher, int bufSize) {
            this.cipher = cipher;
            this.buffer = new byte[bufSize];
        }

        private int read(byte[] dest) {
            if (readPosition >= writePosition) {
                return 0;
            } else {
                int amount = Math.min(dest.length, writePosition - readPosition);
                System.arraycopy(buffer, readPosition, dest, 0, amount);
                readPosition += amount;
                return amount;
            }
        }

        private void write(byte[] src, int length) {
            if (readPosition < writePosition) {
                throw new IllegalStateException("Stored data must be completely read before writing more data");
            }
            try {
                writePosition = cipher.update(src, 0, length, buffer, 0);
            } catch (ShortBufferException e) {
                e.printStackTrace(); //Should never happen
            }
            readPosition = 0;
        }
    }
}
