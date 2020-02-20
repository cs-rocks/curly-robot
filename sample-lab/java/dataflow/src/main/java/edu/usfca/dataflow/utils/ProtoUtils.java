package edu.usfca.dataflow.utils;

import java.util.Base64;

import javax.annotation.Nonnull;

import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.Message;
import com.google.protobuf.Parser;

/**
 * This class contains various utility methods that deal with Proto messages.
 */
public class ProtoUtils {
  /**
   * Returns a String that encodes a given proto message's byte array in Base64, according to RFC4648.
   *
   * See Javadoc of java.util.Base64 for more details.
   *
   * @throws IllegalArgumentException if {@code msg} is null.
   */
  static String encodeMessageBase64(Message msg) {
    if (msg == null) {
      throw new IllegalArgumentException("msg is null");
    }
    Base64.Encoder encoder = Base64.getEncoder(); // TODO: Use this to encode.
    return new String(encoder.encode(msg.toByteArray()));
  }

  /**
   * Returns a proto message by decoding a given encoded message in Base64 (RFC4648).
   *
   * This method should never return null (but it may return a default instance of a proto message).
   *
   * @throws InvalidProtocolBufferException if {@code encodedMsg} cannot be parsed using the provided parser.
   */
  @Nonnull
  static <T> T decodeMessageBase64(Parser<T> parser, String encodedMsg) throws InvalidProtocolBufferException {
    Base64.Decoder decoder = Base64.getDecoder(); // TODO: Use this to decode.
    return parser.parseFrom(decoder.decode(encodedMsg));
  }
}
