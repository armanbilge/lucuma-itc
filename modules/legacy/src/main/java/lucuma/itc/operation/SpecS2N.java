package lucuma.itc.operation;

import lucuma.itc.base.VisitableSampledSpectrum;
import java.util.stream.IntStream;
import java.util.logging.Logger;

/**
 * A set of common values that are accessed by service users through the SpectroscopyResult object.
 */
public interface SpecS2N {

    Logger Log = Logger.getLogger( SpecS2N.class.getName() );
    VisitableSampledSpectrum getSignalSpectrum();
    VisitableSampledSpectrum getBackgroundSpectrum();
    VisitableSampledSpectrum getExpS2NSpectrum();
    VisitableSampledSpectrum getFinalS2NSpectrum();

    default double getPeakPixelCount() {
        final double[] sig = getSignalSpectrum().getValues();
        final double[] bck = getBackgroundSpectrum().getValues();

        // This is a set of conditions that need to hold true for the peak pixel calculation.
        // I am adding these assertions to avoid problems with future refactorings.
        if (getSignalSpectrum().getStart() != getBackgroundSpectrum().getStart()) throw new Error();
        if (getSignalSpectrum().getEnd()   != getBackgroundSpectrum().getEnd())   throw new Error();
        if (sig.length != bck.length)                                             throw new Error();

        // Calculate the peak pixel
        double peak = IntStream.range(0, sig.length).mapToDouble(i -> bck[i]*bck[i] + sig[i]).max().getAsDouble();
        Log.fine("Peak = " + peak);

        return peak;
    }

}
