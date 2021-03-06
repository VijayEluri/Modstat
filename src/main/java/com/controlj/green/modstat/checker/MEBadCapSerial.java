package com.controlj.green.modstat.checker;

import com.controlj.green.addonsupport.access.Location;
import com.controlj.green.addonsupport.access.SystemAccess;
import com.controlj.green.modstat.FirmwareVersion;
import com.controlj.green.modstat.HardwareInfo;
import com.controlj.green.modstat.Modstat;
import com.controlj.green.modstat.checks.ReportRow;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Created by sappling on 2/27/2016.
 */
public class MEBadCapSerial extends BaseChecker {
    // Exclusive limits of bad serial number ranges
    private static final String[][] limits = {
            {"IOU560000P", "IOU5B9999P"},
            {"ME8560000P", "ME85B9999P"},
            {"M8L560000P", "M8L5B9999P"},
            {"M8U560000P", "M8U5B9999P"},
            {"O8E560000P", "O8E5B9999P"},
            {"PSI560000P", "PSI5B9999P"},
            {"PSO560000P", "PSO5B9999P"},
            {"SIO560000P", "SIO5B9999P"}
    };

    public MEBadCapSerial(String id) {
        super(id);
        name = "Power Supply Serial Number";
        description = "Lists modules made in 2015 with a serial number in the range that may have a faulty power supply.";
        setEnabled(false);  // default to disabled
    }

    @Override
    public List<ReportRow> check(Modstat modstat, SystemAccess access, Location location) {
        List<ReportRow> result = null;

        if (modstat.hasBaseHardwareInfo()) {
            HardwareInfo info = modstat.getBaseHardwareInfo();
            String serialNumber = info.getSerialNumber();

            if (isInRange(serialNumber)) {
                result = new ArrayList<ReportRow>();
                result.add(ReportRow.error("Serial number '"+serialNumber+"' is in the range that may fail when exposed to elevated temperature.  Please contact tech support."));
            }
        }
        return result;
    }

    private static boolean isInRange(String serialNumber) {
        for (String[] models : limits) {
            if (isBetween(serialNumber, models[0], models[1])) {
                return true;
            }
        }

        return false;
    }

    private static boolean isBetween(String test, String lower, String upper) {
        int bottomResult = test.compareTo(lower);
        int topResult = test.compareTo(upper);
        return ((bottomResult > 0) && (topResult < 0));
    }

}
