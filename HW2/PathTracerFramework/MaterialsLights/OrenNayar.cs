using System;

namespace PathTracer
{
    public class OrenNayar : BxDF
    {
        private Spectrum kd;
        private double sigma;
        private double A;
        private double B;

        public OrenNayar(Spectrum r, double sigma_)
        {
            kd = r;
            sigma = (Math.PI / 180) * sigma_;
            double sigmaSquared = sigma * sigma;

            A = 1 - (sigmaSquared / (2 * (sigmaSquared + 0.33)));
            B = 0.45 * (sigmaSquared / (sigmaSquared + 0.09));
        }

        public override Spectrum f(Vector3 wo, Vector3 wi)
        {
            if (!Utils.SameHemisphere(wo, wi))
                return Spectrum.ZeroSpectrum;

            if (sigma == 0) { return kd * Utils.PiInv; }


            double sinThetaI = Utils.SinTheta(wi);
            double sinThetaO = Utils.SinTheta(wo);

            double maxCos = 0;
            if (sinThetaI <= 1e-4 && sinThetaO <= 1e-4)
                return Spectrum.ZeroSpectrum;

            Double sinPhiI = Utils.SinPhi(wi);
            Double cosPhiI = Utils.CosPhi(wi);
            Double sinPhiO = Utils.SinPhi(wo);
            Double cosPhiO = Utils.CosPhi(wo);
            Double dCos = cosPhiI * cosPhiO + sinPhiI * sinPhiO;
            maxCos = Math.Max((Double)0, dCos);

            Double sinAlpha;
            Double tanBeta;

            if (Utils.AbsCosTheta(wi) > Utils.AbsCosTheta(wo))
            {
                sinAlpha = sinThetaO;
                tanBeta = sinThetaI / Utils.AbsCosTheta(wi);
            }
            else
            {
                sinAlpha = sinThetaI;
                tanBeta = sinThetaO;
            }
            return kd * Utils.PiInv * (A + B * maxCos * sinAlpha * tanBeta);
        }


        public override (Spectrum, Vector3, double) Sample_f(Vector3 wo)
        {
            var wi = Samplers.CosineSampleHemisphere();
            if (wo.z < 0)
                wi.z *= -1;
            double pdf = Pdf(wo, wi);
            return (f(wo, wi), wi, pdf);
        }

        public override double Pdf(Vector3 wo, Vector3 wi)
        {
            if (!Utils.SameHemisphere(wo, wi))
                return 0;

            return Math.Abs(wi.z) * Utils.PiInv; // wi.z == cosTheta
        }
    }
}
