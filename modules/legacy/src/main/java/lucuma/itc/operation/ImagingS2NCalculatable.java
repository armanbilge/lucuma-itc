package lucuma.itc.operation;

public interface ImagingS2NCalculatable extends Calculatable {

    void setSecondaryIntegral(double secondary_integral);

    void setSecondarySourceFraction(double secondary_source_fraction);

    double getVarSource();
    double getVarBackground();
    double getVarDark();
    double getVarReadout();

    double numberSourceExposures();

    double singleSNRatio();
    double totalSNRatio();

}
