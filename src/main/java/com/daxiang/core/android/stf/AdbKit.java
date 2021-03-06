package com.daxiang.core.android.stf;

import com.daxiang.core.PortProvider;
import com.daxiang.utils.Terminal;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.exec.*;

import java.io.IOException;

/**
 * Created by jiangyitao.
 */
@Slf4j
public class AdbKit {

    private static final String LOCAL_ADBKIT = "vendor/adbkit/bin/adbkit";

    private ExecuteWatchdog watchdog;
    private String deviceId;

    public AdbKit(String deviceId) {
        this.deviceId = deviceId;
    }

    /**
     * 开启远程调试功能
     *
     * @throws IOException
     */
    public int start() throws IOException {
        stop();

        int localPort = PortProvider.getAdbKitAvailablePort();
        // https://github.com/openstf/adbkit
        String cmd = String.format("node " + LOCAL_ADBKIT + " usb-device-to-tcp -p %d %s", localPort, deviceId);

        log.info("[adbkit][{}]开启远程调试功能: {}", deviceId, cmd);
        watchdog = Terminal.executeAsyncAndGetWatchdog(cmd);

        return localPort;
    }

    /**
     * 关闭远程调试功能
     */
    public void stop() {
        if (watchdog != null) {
            log.info("[adbkit][{}]关闭adbkit", deviceId);
            watchdog.destroyProcess();
        }
    }
}
