package protocolsupport.injector.pe;

import io.prometheus.client.Counter;
import io.prometheus.client.Histogram;
import io.prometheus.client.exporter.HTTPServer;
import io.prometheus.client.hotspot.DefaultExports;

import protocolsupport.utils.Utils;
import raknetserver.RakNetServer;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class PERakNetMetrics implements RakNetServer.Metrics {
    private static double nsInS = 1.0 / TimeUnit.NANOSECONDS.convert(1, TimeUnit.SECONDS);
    final static RakNetServer.Metrics INSTANCE = new PERakNetMetrics();

    static {
        final int port = Utils.getJavaPropertyValue("metrics-port", 1271, Integer::parseInt);
        DefaultExports.initialize();
        try {
            new HTTPServer(port);
        } catch (IOException e) {
            System.err.println("Failed to start HTTP Prometheus exporter!");
            e.printStackTrace();
        }
    }

    final Counter outPacket = Counter.build().name("out_packet").help("Game packets sent").register();
    final Counter inPacket = Counter.build().name("in_packet").help("Game packets received").register();
    final Counter join = Counter.build().name("join").help("Outbound packet joins").register();
    final Counter send = Counter.build().name("send").help("Outbound packets sent").register();
    final Counter recv = Counter.build().name("recv").help("Outbound packets received").register();
    final Counter resend = Counter.build().name("resend").help("Outbound packet resends").register();
    final Counter ackSend = Counter.build().name("ack_send").help("ACKs sent").register();
    final Counter nackSend = Counter.build().name("nack_send").help("NACKs sent").register();
    final Counter ackRecv = Counter.build().name("ack_recv").help("ACKs received").register();
    final Counter nackRecv = Counter.build().name("nack_recv").help("NACKs received").register();
    final Histogram sendAttempts = Histogram.build().name("send_attempts").help("Number of send attempts before ACK")
            .buckets(0.99, 1.99, 2.99, 3.99, 4.99, 5.99, 6.99, 7.99, 8.99, 9.99, 10.99, 15, 20).register();
    final Histogram rtt = Histogram.build().name("rtt").help("RTT in seconds").register();

    private PERakNetMetrics() {

    }

    @Override
    public void incrOutPacket(int i) {
        outPacket.inc(i);
    }

    @Override
    public void incrInPacket(int i) {
        inPacket.inc(i);
    }

    @Override
    public void incrJoin(int i) {
        join.inc(i);
    }

    @Override
    public void incrSend(int i) {
        send.inc(i);
    }

    @Override
    public void incrRecv(int i) {
        recv.inc(i);
    }

    @Override
    public void incrResend(int i) {
        resend.inc(i);
    }

    @Override
    public void incrAckSend(int i) {
        ackSend.inc(i);
    }

    @Override
    public void incrNackSend(int i) {
        nackSend.inc(i);
    }

    @Override
    public void incrAckRecv(int i) {
        ackRecv.inc(i);
    }

    @Override
    public void incrNackRecv(int i) {
        nackRecv.inc(i);
    }

    @Override
    public void measureSendAttempts(int i) {
        sendAttempts.observe(i);
    }

    @Override
    public void measureRTTns(long l) {
        rtt.observe(l * nsInS);
    }
}
