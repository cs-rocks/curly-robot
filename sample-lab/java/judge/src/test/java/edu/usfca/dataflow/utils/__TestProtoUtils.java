package edu.usfca.dataflow.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.Message;

import edu.usfca.dataflow.__TestBase;
import edu.usfca.protobuf.Bid.BidLog;
import edu.usfca.protobuf.Bid.BidLog.BidResult;
import edu.usfca.protobuf.Bid.Exchange;
import edu.usfca.protobuf.Common.DeviceId;
import edu.usfca.protobuf.Common.OsType;

// NOTE: You should not modify this file, as the grading system will use this file as-is (and additional hidden tests)
// in order to grade your submission.
public class __TestProtoUtils extends __TestBase {

  public void testEncodeAndDecodeHelper(String encodedMessage, Message protoMessage) {
    assertEquals(encodedMessage, ProtoUtils.encodeMessageBase64(protoMessage));
    try {
      assertEquals(protoMessage, ProtoUtils.decodeMessageBase64(protoMessage.getParserForType(), encodedMessage));
    } catch (InvalidProtocolBufferException e) {
      fail();
    }
  }

  // Note: This test is provided as hint.
  // If your implementation correctly uses the suggested Base64 Decoder,
  // the decoder itself should throw IllegalArgumentException (before we parse the decoded message into proto).
  @Test(expected = IllegalArgumentException.class)
  public void testDecodeForException() throws IllegalArgumentException, InvalidProtocolBufferException {
    ProtoUtils.decodeMessageBase64(BidLog.parser(), "=");
  }

  @Test
  public void testEncodeAndDecode() {
    // Test 1: Simple Device Id proto.
    DeviceId deviceId = DeviceId.newBuilder().setOs(OsType.IOS).setWebid("12345").build();
    testEncodeAndDecodeHelper("CAIaBTEyMzQ1", deviceId);

    // Test 2: A few cases for BidLog protos.
    BidLog.Builder bidLog = BidLog.newBuilder().setExchange(Exchange.UNITY);
    testEncodeAndDecodeHelper("EAM=", bidLog.setExchange(Exchange.UNITY).build());
    testEncodeAndDecodeHelper("EAMoAQ==", bidLog.setBidResult(BidResult.BID).build());
    testEncodeAndDecodeHelper("EAMY6Acg0A8oAQ==", bidLog.setRecvAt(1000).setProcessedAt(2000).build());
  }

  // ----------

  @Test(expected = InvalidProtocolBufferException.class)
  public void __shareable__testDecodeForException1() throws InvalidProtocolBufferException {
    ProtoUtils.decodeMessageBase64(BidLog.parser(), "abcdefg");
  }

  @Test(expected = IllegalArgumentException.class)
  public void __shareable__testDecodeForException2() throws IllegalArgumentException, InvalidProtocolBufferException {
    ProtoUtils.decodeMessageBase64(BidLog.parser(), "==");
  }

  @Test
  public void __shareable__testEncodeAndDecode() {
    DeviceId deviceId = DeviceId.newBuilder().setOs(OsType.ANDROID).setUuid("12345").build();
    testEncodeAndDecodeHelper("CAESBTEyMzQ1", deviceId);

    BidLog.Builder bidLog = BidLog.newBuilder();
    testEncodeAndDecodeHelper("", bidLog.build());
    testEncodeAndDecodeHelper("EAI=", bidLog.setExchange(Exchange.MOPUB).build());
    testEncodeAndDecodeHelper("EAIoAw==", bidLog.setBidResult(BidResult.INVALID_REQUEST).build());
    testEncodeAndDecodeHelper("EAIY8sPL38sDIMDK+9agRCgD",
        bidLog.setRecvAt(123412341234L).setProcessedAt(2345234523456L).build());
  }

  // ---------
  @Test(expected = InvalidProtocolBufferException.class)
  public void __hidden__testDecodeForException1() throws InvalidProtocolBufferException {
    ProtoUtils.decodeMessageBase64(BidLog.parser(), "awfiwefoqwhfwofxhiwqeofhbxc=");
  }

  @Test(expected = IllegalArgumentException.class)
  public void __hidden__testDecodeForException2() throws IllegalArgumentException, InvalidProtocolBufferException {
    ProtoUtils.decodeMessageBase64(BidLog.parser(), "abc==");
  }

  @Test
  public void __hidden__testEncodeAndDecode() {
    DeviceId deviceId = DeviceId.newBuilder().setOs(OsType.ANDROID).setUuid("12345").build();
    testEncodeAndDecodeHelper("CAESBTEyMzQ1", deviceId);

    BidLog.Builder bidLog = BidLog.newBuilder();
    testEncodeAndDecodeHelper("", bidLog.build());
    testEncodeAndDecodeHelper("EAI=", bidLog.setExchange(Exchange.MOPUB).build());
    testEncodeAndDecodeHelper("EAIoAw==", bidLog.setBidResult(BidResult.INVALID_REQUEST).build());
    testEncodeAndDecodeHelper("EAIY8sPL38sDIMDK+9agRCgD",
        bidLog.setRecvAt(123412341234L).setProcessedAt(2345234523456L).build());
    testEncodeAndDecodeHelper("EAIoAw==", bidLog.setRecvAt(0).setProcessedAt(0).build());
    testEncodeAndDecodeHelper("EAIoAzIJCAESBTEyMzQ1", bidLog.setDeviceId(deviceId).build());
    testEncodeAndDecodeHelper("EAIY8sPL38sDIMDK+9agRCgDMgkIARIFMTIzNDU=",
        bidLog.setRecvAt(123412341234L).setProcessedAt(2345234523456L).setDeviceId(deviceId).build());
  }
}
