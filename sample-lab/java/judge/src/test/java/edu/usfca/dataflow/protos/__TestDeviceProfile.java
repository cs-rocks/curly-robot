package edu.usfca.dataflow.protos;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import edu.usfca.dataflow.__TestBase;
import edu.usfca.protobuf.Bid.Exchange;
import edu.usfca.protobuf.Common.DeviceId;
import edu.usfca.protobuf.Common.OsType;
import edu.usfca.protobuf.Profile.DeviceProfile;
import edu.usfca.protobuf.Profile.DeviceProfile.AppActivity;

// You should NOT modify any of these unit tests.
// If you want to add your own unit tests, do so in java/dataflow project.
public class __TestDeviceProfile extends __TestBase {
  @Test
  public void testDeviceProfileMessage() {
    DeviceId deviceId = DeviceId.newBuilder().setOs(OsType.IOS).setUuid("some_uuid").setWebid("some_webid").build();

    DeviceProfile.Builder dp = DeviceProfile.newBuilder();
    dp.setDeviceId(deviceId);

    // Add an AppActivity proto.
    AppActivity.Builder app = AppActivity.newBuilder();
    final long firstAt = 1579674040585L;
    final long lastAt = 15796740801324L;
    app.setBundle("bundle!").setFirstAt(firstAt).setLastAt(lastAt);
    dp.addActivity(app);

    assertEquals(42, dp.build().getSerializedSize());
  }

  // ---------
  @Test
  public void __shareable__testDeviceProfileMessage() {
    DeviceId deviceId = DeviceId.newBuilder().setOs(OsType.IOS).setWebid("web_abcde").build();

    DeviceProfile.Builder dp = DeviceProfile.newBuilder();
    dp.setDeviceId(deviceId);

    // Add an AppActivity proto.
    AppActivity.Builder app = AppActivity.newBuilder();
    final long firstAt = 1579674040585L;
    final long countToday = 123123123L;
    final long countLifetime = countToday + 111111111L;
    app.setBundle("bundle@").setFirstAt(firstAt).setLastAt(firstAt + 1234567L).setCountToday(countToday)
        .setCountLifetime(countLifetime);
    app.putCountPerExchange(Exchange.ADX.getNumber(), 200200200);
    app.putCountPerExchange(Exchange.MOPUB.getNumber(), 30030030);
    app.putCountPerExchange(Exchange.UNITY_VALUE, 4004004);
    app.putCountPerExchange(Exchange.UNKNOWN_EXCHANGE_VALUE, 0);

    int sum = app.getCountPerExchangeMap().values().stream().mapToInt(i -> i).sum();
    assertEquals(sum, app.getCountLifetime());

    dp.addActivity(app.build());

    assertEquals(83, dp.build().getSerializedSize());
  }

  // ---------
  @Test
  public void __hidden__testDeviceProfileMessage() {
    DeviceId deviceId = DeviceId.newBuilder().setOs(OsType.IOS).setWebid("web_abcde").build();

    DeviceProfile.Builder dp = DeviceProfile.newBuilder();
    dp.setDeviceId(deviceId);

    // Add an AppActivity proto.
    AppActivity.Builder app = AppActivity.newBuilder();
    final long firstAt = 1579674040585L;
    final long countToday = 123123123L;
    final long countLifetime = countToday + 111111111L;
    app.setBundle("bundle@").setFirstAt(firstAt).setLastAt(firstAt + 1234567L).setCountToday(countToday)
        .setCountLifetime(countLifetime);
    app.putCountPerExchange(Exchange.ADX.getNumber(), 200200200);
    app.putCountPerExchange(Exchange.MOPUB.getNumber(), 30030030);
    app.putCountPerExchange(Exchange.UNITY_VALUE, 4004004);
    app.putCountPerExchange(Exchange.UNKNOWN_EXCHANGE_VALUE, 0);

    dp.addActivity(app.build());

    // Add one more.
    app.setBundle("@bundle").setFirstAt(firstAt + 1234L).setLastAt(firstAt + 234567L).setCountToday(countToday * 2L)
        .setCountLifetime(countLifetime * 2L);
    app.putCountPerExchange(Exchange.ADX.getNumber(), 200200200 * 2);
    app.putCountPerExchange(Exchange.MOPUB.getNumber(), 30030030 * 2);
    app.putCountPerExchange(Exchange.UNITY_VALUE, 4004004 * 2);
    app.putCountPerExchange(Exchange.UNKNOWN_EXCHANGE_VALUE, 0);

    dp.addActivity(app.build());

    assertEquals(153, dp.build().getSerializedSize());
  }
}
