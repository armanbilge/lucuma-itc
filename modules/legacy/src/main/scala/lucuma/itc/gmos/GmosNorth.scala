package lucuma.itc.gmos

import cats.syntax.all._
import lucuma.itc.shared.{GmosNorthParameters, ObservationDetails}
import lucuma.itc.base.Instrument
import lucuma.itc.base.Filter
import lucuma.itc.base.Detector
import lucuma.core.enum.GmosNorthDetector
import lucuma.core.enum.GmosAmpGain
// import edu.gemini.spModel.gemini.gmos.GmosCommonType
import lucuma.core.enum.GmosAmpReadMode
import lucuma.core.enum.GmosNorthFpu

/**
  * Gmos specification class
  */
object GmosNorth { // value taken from instrument's web documentation
  val WellDepth: Double = 105000
  // Average full well depth of 12 amplifiers for GMOS-N Hamamatsu CCD
  val HAMAMATSU_WELL_DEPTH: Double = 125000
  val HAMAMATSU_DETECTOR_PIXELS: Int = 6278

  /**
    * Related files will start with this prefix
    */
  val INSTR_PREFIX = "gmos_n_"
  // Instrument reads its configuration from here.
  val FILENAME = "gmos_n" + Instrument.getSuffix
  //
  // Detector data files (see REL-478)
  val DETECTOR_CCD_FILES = Array("ccd_hamamatsu_bb", "ccd_hamamatsu_hsc", "ccd_hamamatsu_bb")
  // Detector display names corresponding to the detectorCcdIndex
  val DETECTOR_CCD_NAMES = Array("BB(B)", "HSC", "BB(R)");
}

final class GmosNorth(gnp: GmosNorthParameters, odp1: ObservationDetails, override val detectorCcdIndex: Int) extends Gmos[GmosNorthDetector](gnp, odp1, GmosNorth.FILENAME, detectorCcdIndex) {
  //Choose correct CCD QE curve
  val (_detector, _instruments) = gnp.ccdType match { // E2V, site dependent
    case GmosNorthDetector.E2V =>
      val _detector = new Detector(getDirectory + "/", getPrefix, "E2V4290DDmulti3", "EEV DD array")
      _detector.setDetectorPixels(detectorPixels)
      (_detector, if (detectorCcdIndex == 0) Array[Gmos[GmosNorthDetector]](this) else Array())

    // Hamamatsu, both sites: gmos_n_CCD-{R,G,B}.dat        =>  Hamamatsu (R,G,B)
    case GmosNorthDetector.Hamamatsu =>
      val fileName = getCcdFiles(detectorCcdIndex)
      val name = getCcdNames(detectorCcdIndex)
      val _detector = new Detector(getDirectory + "/", getPrefix, fileName, "Hamamatsu array", name)
      _detector.setDetectorPixels(detectorPixels)
      (_detector, if (detectorCcdIndex == 0) createCcdArray else Array())
  }


  val (_Filter, _gratingOptics) = gnp.filterGrating.fold( filter => {
      val _Filter = Filter.fromWLFile(getPrefix, filter.longName, getDirectory + "/")
      addFilter(_Filter)
      (_Filter.some, none)
    },grating => {
      val _gratingOptics = new GmosGratingOptics(getDirectory + "/" + getPrefix, grating.longName, _detector, gp.centralWavelength.nanometer.value.toDouble, _detector.getDetectorPixels, gp.spectralBinning)
      val _sampling = _gratingOptics.dispersion
      addDisperser(_gratingOptics)
      (none, _gratingOptics.some)
    },(filter, grating) => {(none, none)})

  override def isIfu2 = gnp.fpMask === GmosNorthFpu.Ifu2Slits

  override protected def createCcdArray = Array[Gmos[GmosNorthDetector]](this, new GmosNorth(gnp, odp, 1), new GmosNorth(gnp, odp, 2))

  override protected def getPrefix = GmosNorth.INSTR_PREFIX

  override protected def getCcdFiles = GmosNorth.DETECTOR_CCD_FILES

  override protected def getCcdNames = GmosNorth.DETECTOR_CCD_NAMES

  override def wellDepth: Double = gnp.ccdType match {
    case GmosNorthDetector.E2V =>
      GmosNorth.WellDepth
    case GmosNorthDetector.Hamamatsu =>
      GmosNorth.HAMAMATSU_WELL_DEPTH
  }

  override def detectorPixels: Int = gnp.ccdType match {
    case GmosNorthDetector.E2V =>
      Gmos.E2V_DETECTOR_PIXELS
    case GmosNorthDetector.Hamamatsu=>
      GmosNorth.HAMAMATSU_DETECTOR_PIXELS
  }

  override def gain = getMeanGain(gp.ampGain, gp.ampReadMode, gnp.ccdType)

  // FROM InstGmosNorth.getMeanGain
  /**
    * Calculates the mean gain for the given parameters for GMOS North.
    */
  def getMeanGain(gain: GmosAmpGain, readMode: GmosAmpReadMode, detectorManufacturer: GmosNorthDetector): Double = {
    (detectorManufacturer, readMode, gain) match {
      case (GmosNorthDetector.E2V, GmosAmpReadMode.Fast, GmosAmpGain.High) => 5.0
      case (GmosNorthDetector.E2V, GmosAmpReadMode.Fast, GmosAmpGain.Low) => 2.5
      case (GmosNorthDetector.E2V, GmosAmpReadMode.Slow, GmosAmpGain.High) => 4.4
      case (GmosNorthDetector.E2V, GmosAmpReadMode.Slow, GmosAmpGain.Low) => 2.2
      case (GmosNorthDetector.Hamamatsu, GmosAmpReadMode.Fast, GmosAmpGain.High) => 5.11
      case (GmosNorthDetector.Hamamatsu, GmosAmpReadMode.Fast, GmosAmpGain.Low) => 1.96
      case (GmosNorthDetector.Hamamatsu, GmosAmpReadMode.Slow, GmosAmpGain.High) => 4.4
      case (GmosNorthDetector.Hamamatsu, GmosAmpReadMode.Slow, GmosAmpGain.Low) => 1.63
    }
  }

}