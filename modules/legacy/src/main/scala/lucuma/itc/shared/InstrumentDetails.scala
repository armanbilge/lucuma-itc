package lucuma.itc.shared

import edu.gemini.spModel.core.Site
// import edu.gemini.spModel.core.{Wavelength, Site}
// import edu.gemini.spModel.data.YesNoType
// import edu.gemini.spModel.gemini.acqcam.AcqCamParams
// import edu.gemini.spModel.gemini.altair.AltairParams
// import edu.gemini.spModel.gemini.flamingos2.Flamingos2
// import edu.gemini.spModel.gemini.gmos.{GmosSouthType, GmosNorthType, GmosCommonType}
// import edu.gemini.spModel.gemini.gnirs.GNIRSParams
// import edu.gemini.spModel.gemini.gsaoi.Gsaoi
// import edu.gemini.spModel.gemini.michelle.MichelleParams
// import edu.gemini.spModel.gemini.nifs.NIFSParams
// import edu.gemini.spModel.gemini.niri.Niri
// import edu.gemini.spModel.gemini.trecs.TReCSParams
import lucuma.core.enum
import lucuma.core.math.Wavelength

/*
 * A collection of objects that define subsets of instrument configuration parameters
 * which are needed for ITC calculations of the corresponding instruments.
 */

sealed trait InstrumentDetails

// final case class AcquisitionCamParameters(
//                      colorFilter:       AcqCamParams.ColorFilter,
//                      ndFilter:          AcqCamParams.NDFilter) extends InstrumentDetails
//
// final case class Flamingos2Parameters(
//                      filter:            Flamingos2.Filter,
//                      grism:             Flamingos2.Disperser,
//                      mask:              Flamingos2.FPUnit,
//                      customSlitWidth:   Option[Flamingos2.CustomSlitWidth],
//                      readMode:          Flamingos2.ReadMode) extends InstrumentDetails
//
// // TODO-GHOSTITC
// final case class GhostParameters() extends InstrumentDetails
//
final case class GmosParameters(
                     filter:            enum.GmosNorthFilter,
                     grating:           enum.GmosNorthDisperser,
                     centralWavelength: Wavelength,
                     fpMask:            enum.GmosNorthFpu,
                     ampGain:           enum.GmosAmpGain,
                     ampReadMode:       enum.GmosAmpReadMode,
                     customSlitWidth:   Option[enum.GmosCustomSlitWidth],
                     spatialBinning:    Int,
                     spectralBinning:   Int,
                     ccdType:           enum.GmosDetector,
                     builtinROI:        enum.GmosRoi,
                     site:              enum.Site) extends InstrumentDetails {
    import edu.gemini.spModel.gemini.gmos.GmosCommonType
    import edu.gemini.spModel.gemini.gmos.GmosNorthType

    val legacyCcdType = ccdType match {
      case enum.GmosDetector.E2V => GmosCommonType.DetectorManufacturer.E2V
      case enum.GmosDetector.HAMAMATSU => GmosCommonType.DetectorManufacturer.HAMAMATSU
    }

    val legacySite = site match {
      case enum.Site.GN => Site.GN
      case enum.Site.GS => Site.GS
    }

    val legacyFpMask = fpMask match {
      case enum.GmosNorthFpu.Ifu1 => GmosNorthType.FPUnitNorth.IFU_1
      case enum.GmosNorthFpu.Ifu2 => GmosNorthType.FPUnitNorth.IFU_2
    }

    val legacyGrating = grating match {
      case enum.GmosNorthDisperser.B480_G5309 => GmosNorthType.DisperserNorth.B480_G5309
    }
  }

//
// final case class GnirsParameters(
//                      pixelScale:        GNIRSParams.PixelScale,
//                      filter:            Option[GNIRSParams.Filter],
//                      grating:           Option[GNIRSParams.Disperser],
//                      readMode:          GNIRSParams.ReadMode,
//                      crossDispersed:    GNIRSParams.CrossDispersed,
//                      centralWavelength: Wavelength,
//                      slitWidth:         GNIRSParams.SlitWidth,
//                      camera:            Option[GNIRSParams.Camera],
//                      wellDepth:         GNIRSParams.WellDepth,
//                      altair:            Option[AltairParameters]) extends InstrumentDetails
//
// final case class GsaoiParameters(
//                      filter:            Gsaoi.Filter,
//                      readMode:          Gsaoi.ReadMode,
//                      largeSkyOffset:    Int,
//                      gems:              GemsParameters) extends InstrumentDetails
//
// final case class MichelleParameters(
//                      filter:            MichelleParams.Filter,
//                      grating:           MichelleParams.Disperser,
//                      centralWavelength: Wavelength,
//                      mask:              MichelleParams.Mask,
//                      polarimetry:       YesNoType) extends InstrumentDetails
//
// final case class NifsParameters(
//                      filter:            NIFSParams.Filter,
//                      grating:           NIFSParams.Disperser,
//                      readMode:          NIFSParams.ReadMode,
//                      centralWavelength: Wavelength,
//                      altair:            Option[AltairParameters]) extends InstrumentDetails
//
// final case class NiriParameters(
//                      filter:            Niri.Filter,
//                      grism:             Niri.Disperser,
//                      camera:            Niri.Camera,
//                      readMode:          Niri.ReadMode,
//                      wellDepth:         Niri.WellDepth,
//                      mask:              Niri.Mask,
//                      builtinROI:        Niri.BuiltinROI,
//                      altair:            Option[AltairParameters]) extends InstrumentDetails
//
// final case class TRecsParameters(
//                      filter:            TReCSParams.Filter,
//                      instrumentWindow:  TReCSParams.WindowWheel,
//                      grating:           TReCSParams.Disperser,
//                      centralWavelength: Wavelength,
//                      mask:              TReCSParams.Mask) extends InstrumentDetails
//

// == AO

// final case class AltairParameters(
//                      guideStarSeparation: Double,
//                      guideStarMagnitude:  Double,
//                      fieldLens:           AltairParams.FieldLens,
//                      wfsMode:             AltairParams.GuideStarType)
//
// final case class GemsParameters(
//                      avgStrehl:           Double,
//                      strehlBand:          String)
//

object InstrumentDetails {

  // NOTE: This is similar to the code in ItcUniqueConfig which decides on imaging or spectroscopy setup
  // on Config elements. There should be a way to share this?

  // figure out if the instrument is configured for imaging by checking if a disperser element is present
  def isImaging(i: InstrumentDetails): Boolean = i match {
    // case _: AcquisitionCamParameters  => true                                       // Acq cam is imaging only
    // case i: Flamingos2Parameters      => i.grism.equals(Flamingos2.Disperser.NONE)
    // case i: GnirsParameters           => i.grating.isEmpty
    // case _: GsaoiParameters           => true                                       // Gsaoi is imaging only
    // case i: MichelleParameters        => i.grating.equals(MichelleParams.Disperser.MIRROR)
    // case _: NifsParameters            => false                                      // NIFS is spectroscopy only
    // case _: GhostParameters           => true // TBD is this true?
    // case i: NiriParameters            => i.grism.equals(Niri.Disperser.NONE)
    // case i: TRecsParameters           => i.grating.equals(TReCSParams.Disperser.MIRROR)
    case _: GmosParameters            =>
      true
      // i.grating.equals(GmosNorthType.DisperserNorth.MIRROR) ||
      // i.grating.equals(GmosSouthType.DisperserSouth.MIRROR)
  }

  // figure out if the instrument is configured for spectroscopy (ie. not imaging)
  def isSpectroscopy(i: InstrumentDetails): Boolean = !isImaging(i)

}