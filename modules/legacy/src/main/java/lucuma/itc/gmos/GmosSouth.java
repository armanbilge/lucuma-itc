package lucuma.itc.gmos;

import lucuma.itc.shared.GmosParameters;
import lucuma.itc.shared.ObservationDetails;
import edu.gemini.spModel.gemini.gmos.InstGmosSouth;
import edu.gemini.spModel.gemini.gmos.GmosSouthType;

/**
 * Gmos specification class
 */
public final class GmosSouth extends Gmos {

    // value taken from instrument's web documentation
    private static final double WellDepth = 106000;

    // GMOS-S Hamamatsu (value supplied by the instrument scientist)
    private static final double HAMAMATSU_WELL_DEPTH = 150000;

    private static final int HAMAMATSU_DETECTOR_PIXELS = 6266;

    /**
     * /** Related files will start with this prefix
     */
    public static final String INSTR_PREFIX = "gmos_s_";

    // Instrument reads its configuration from here.
    private static final String FILENAME = "gmos_s" + getSuffix();

    // Detector data files (see REL-478)
    private static final String[] DETECTOR_CCD_FILES = {"ccd_hamamatsu_bb", "ccd_hamamatsu_hsc", "ccd_hamamatsu_sc"};

    // Detector display names corresponding to the detectorCcdIndex
    private static final String[] DETECTOR_CCD_NAMES = {"BB", "HSC", "SC"};


    public GmosSouth(final GmosParameters gp, final ObservationDetails odp, final int detectorCcdIndex) {
        super(gp, odp, FILENAME, detectorCcdIndex);
    }

    public boolean isIfu2() {
        return getFpMask() == GmosSouthType.FPUnitSouth.IFU_1;
    }

    protected Gmos[] createCcdArray() {
        return new Gmos[]{this, new GmosSouth(gp, odp, 1), new GmosSouth(gp, odp, 2)};
    }

    protected String getPrefix() {
        return INSTR_PREFIX;
    }

    protected String[] getCcdFiles() {
        return DETECTOR_CCD_FILES;
    }

    protected String[] getCcdNames() {
        return DETECTOR_CCD_NAMES;
    }

    @Override
    public double wellDepth() {
        switch (gp.ccdType()) {
            case E2V:
                return WellDepth;
            case HAMAMATSU:
                return HAMAMATSU_WELL_DEPTH;
            default:
                throw new Error("invalid ccd type");
        }
    }

    public int detectorPixels() {
        switch (gp.ccdType()) {
            case E2V:
                return E2V_DETECTOR_PIXELS;
            case HAMAMATSU:
                return HAMAMATSU_DETECTOR_PIXELS;
            default:
                throw new Error("invalid ccd type");
        }
    }

    @Override public double gain() {
        return InstGmosSouth.getMeanGain(gp.ampGain(), gp.ampReadMode(), gp.ccdType());
    }
}
